import org.gradle.api.tasks.*

open class GitHubListReleasesTask : GitHubTask() {
  
  @TaskAction
  fun process() {
    println("retrieving github releases...")
    get("releases").run {
      if (!isSucceeded()) {
        throw  Exception("failed to retrieve releases")
      }
    }
  }
}