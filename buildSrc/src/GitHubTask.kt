import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jdk8.*
import com.fasterxml.jackson.module.afterburner.*
import org.gradle.api.*
import java.io.*
import java.net.*
import java.nio.charset.*


open class GitHubTask : DefaultTask() {

  init {
    group = "github"
  }

  var url: String? = null
  var authToken: String? = null

  data class ApiResult(val status: Int, val message: String?, val content: JsonNode?) {
    fun isSucceeded() = status in 200..299
    fun notFound() = status == HttpURLConnection.HTTP_NOT_FOUND
    fun get(): JsonNode? {
      if (!isSucceeded()) {
        throw Exception("request failed with $status '$message'")
      }
      return content
    }

    inline fun <reified T> getAs(): T? = jsonMap<T>(get())
  }

  fun get(path: String) = request("GET", path)
  fun head(path: String) = request("HEAD", path)
  fun post(path: String, content: JsonNode?) = request("POST", path, content)
  fun delete(path: String) = request("DELETE", path)

  fun upload(path: String, contentType: String? = null, content: InputStream): ApiResult {
    val uri = URI(url).resolve(path)
    val con = uri.toURL().openConnection() as HttpURLConnection
    con.requestMethod = "POST"

    if (!authToken.isNullOrBlank()) {
      con.setRequestProperty("Authorization", "token $authToken")
    }
    //con.setChunkedStreamingMode(-1);
    con.setRequestProperty("Accept", "application/json")

    if (contentType !== null) {
      con.setRequestProperty("Content-Type", contentType)
    }
    con.doOutput = true

    println("${con.requestMethod} '$uri' ...")
    con.connect()
    try {
      con.outputStream.use { out ->

        val buf = ByteArray(1024)
        while (true) {
          val read = content.read(buf)
          if (read <= 0) {
            break
          }
          out.write(buf, 0, read)
        }
        out.flush()
      }

      (if (con.responseCode >= 400) con.errorStream else con.inputStream).use { inputStream ->
        val apiResult = ApiResult(
          status = con.responseCode,
          message = con.responseMessage,
          content = inputStream?.run { mapper.readTree(bufferedReader()) }
        )
        println("${apiResult.status} '${apiResult.message}': ${apiResult.content?.let { x -> jsonFormat(x) } ?: ""}")
        return apiResult
      }
    } finally {
      con.disconnect()
    }
  }

  fun request(method: String, path: String, content: JsonNode? = null): ApiResult {

    val uri = URI(url).resolve(path)
    val con = uri.toURL().openConnection() as HttpURLConnection
    con.requestMethod = method

    if (!authToken.isNullOrBlank()) {
      con.setRequestProperty("Authorization", "token $authToken")
    }
    con.setRequestProperty("Accept", "application/json")

    if (content !== null && !content.isMissingNode) {
      con.setRequestProperty("Content-Type", "application/json")
      con.doOutput = true
    }

    println("$method '$uri' ${jsonFormat(content)}...")
    con.connect()
    try {
      if (content !== null && !content.isMissingNode) {
        con.outputStream.writer().use { w ->
          mapper.writeValue(w, content)
          w.flush()
        }
      }

      (if (con.responseCode >= 400) con.errorStream else con.inputStream).use { inputStream ->
        val apiResult = ApiResult(
          status = con.responseCode,
          message = con.responseMessage,
          content = inputStream?.run { mapper.readTree(bufferedReader()) }
        )
        println("${apiResult.status} '${apiResult.message}': ${apiResult.content?.let { x -> jsonFormat(x) } ?: ""}")
        return apiResult
      }
    } finally {
      con.disconnect()
    }
  }


  companion object {
    val mapper = ObjectMapper()
      .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
      .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
      .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
      .configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
      .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(AfterburnerModule())
      .registerModule(Jdk8Module())

    fun jsonTree(value: Any?): JsonNode? {
      if (value == null) {
        return null
      } else {
        return mapper.valueToTree(value)
      }
    }

    fun jsonParse(json: String) = mapper.readTree(json)

    inline fun <reified T> jsonMap(json: JsonNode?): T? {
      if (json === null) {
        return null
      }
      return mapper.readValue(mapper.treeAsTokens(json), object : TypeReference<T>() {})
    }

    fun jsonFormat(json: String): String = jsonFormat(
      jsonParse(json)
    )

    fun jsonFormat(json: JsonNode?): String {
      if (json === null || json.isMissingNode) {
        return ""
      }
      return StringWriter().use { w ->
        mapper.writerWithDefaultPrettyPrinter().writeValue(
          w, json
        )
        w.toString()
      }
    }

    fun urlEncode(value: String) = URLEncoder.encode(value, StandardCharsets.UTF_8.name())
  }
}