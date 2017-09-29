import com.fasterxml.jackson.annotation.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.tasks.*
import java.io.*
import java.net.*
import java.nio.charset.*
import java.nio.file.*

open class GitHubPublishReleaseTask : GitHubTask() {

  var releaseTag: String? = "v${project.version}"
  var releaseName: String? = "composer $releaseTag"
  var releaseBranch: String? = null//"dev"
  var releaseDescription: String? = "experimental version, API may change"
  var releaseDraft: Boolean? = false
  var releasePrerelease: Boolean? = false

  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  class Request{
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

  var createReleaseRequest:Request = Request().apply {

  }

  val assets = HashSet<File>()

  fun asset(task: Task){
    dependsOn(task)
    task.outputs.files.forEach {
      assets.add(it)
    }
  }

  @TaskAction
  fun process() {

    run checkExists@{
      val release = get("releases/tags/$releaseTag").run{
        if(notFound()){
          return@checkExists
        }
        get()!!
      }
      request("DELETE","releases/${release["id"].asText()}").get()
    }

    val body = jsonTree(Request().apply {
      tagName = releaseTag
      targetCommitish = releaseBranch
      name = releaseName
      body = releaseDescription
      draft = releaseDraft
      prerelease = releasePrerelease
    })

    println("publishing release to github... ")

    val createdRelease = post("releases", body).get()!!

    //val uploadUrl = createdRelease!!["assets_url"].asText()
    val uploadUrl ="https://uploads.github.com/repos/satori-com/satori-composer/releases/${createdRelease["id"].asText()}/assets"

    assets.forEach {
      println("uploading asset '${it.name}'")
      Files.newInputStream(it.toPath()).use { content->
        upload(
          "$uploadUrl?name=${urlEncode(it.name)}",
          "application/zip",
          content
        )
      }
      //println("${URL("https://uploads.github.com/repos/satori-com/satori-composer/releases/7936971/assets{?name,label}")}")
      /*parameters.put("name", "Arnold")

      val builder = UriBuilder.fromPath(template)
      val output = builder.build(parameters)
      parameters.put("name", "Arnold")

      val builder = UriBuilder.fromPath(template)
      val output = builder.build(parameters)*/
      //POST https://<upload_url>/repos/:owner/:repo/releases/:id/assets?name=foo.zip
    }
  }
}