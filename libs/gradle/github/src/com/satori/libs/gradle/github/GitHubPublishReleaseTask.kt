package com.satori.libs.gradle.github

import com.damnhandy.uri.template.*
import com.fasterxml.jackson.annotation.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import java.io.*
import java.nio.file.*

open class GitHubPublishReleaseTask : GitHubTask() {
  
  @Input
  var releaseTag: String? = "v${project.version}"
  
  @Input
  var releaseName: String? = "composer $releaseTag"
  
  @Input
  var releaseBranch: String? = null//"dev"
  
  @Input
  var releaseDescription: String? = "experimental version, API may change"
  
  @Input
  var releaseDraft: Boolean? = false
  
  @Input
  var releasePrerelease: Boolean? = false
  
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  class Request {
    @JsonProperty("tag_name")
    var tagName: String? = null
    
    @JsonProperty("target_commitish")
    var targetCommitish: String? = null
    
    @JsonProperty("name")
    var name: String? = null
    
    @JsonProperty("body")
    var body: String? = null
    
    @JsonProperty("draft")
    var draft: Boolean? = null
    
    @JsonProperty("prerelease")
    var prerelease: Boolean? = null
  }
  
  @InputFiles
  val assets = HashSet<File>()
  
  fun asset(task: Task) {
    dependsOn(task)
    task.outputs.files.forEach {
      assets.add(it)
    }
  }
  
  @TaskAction
  fun process() {
    
    run checkExists@{
      val release = get("releases/tags/$releaseTag").run {
        if (notFound()) {
          return@checkExists
        }
        get()!!
      }
      request("DELETE", "releases/${release["id"].asText()}").get()
    }
    
    val body = GitHubTask.jsonTree(Request().apply {
      tagName = releaseTag
      targetCommitish = releaseBranch
      name = releaseName
      body = releaseDescription
      draft = releaseDraft
      prerelease = releasePrerelease
    })
    
    println("publishing release to github... ")
    
    val createdRelease = post("releases", body).get()!!
    val uploadUrl = createdRelease["upload_url"].asText()
    
    assets.forEach {
      println("uploading asset '${it.name}'")
      Files.newInputStream(it.toPath()).use { content ->
        val uri = UriTemplate.fromTemplate(uploadUrl)
          .set("name", it.name)
          .expand()
        upload(
          uri, "application/zip", content
        )
      }
    }
  }
}