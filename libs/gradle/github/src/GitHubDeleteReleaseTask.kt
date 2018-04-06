import org.gradle.api.tasks.*

open class GitHubDeleteReleaseTask : GitHubTask() {
  
  var tag: String? = "v${project.version}"
  
  @TaskAction
  fun process() {
    
    val release = get("releases/tags/$tag").run {
      if (notFound()) {
        return
      }
      if (!isSucceeded()) {
        throw Exception("failed to delete release")
      }
      content!!
    }
    val apiResult = request("DELETE", "releases/${release["id"].asText()}")
    if (!apiResult.isSucceeded()) {
      throw Exception("failed to delete release")
    }
  }
}