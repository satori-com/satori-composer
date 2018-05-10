// auto generated
// don't modify
package ${project.pckg}

object ${model.className} {
  const val version = "${project.version}"
  const val project = "${project.archivesBaseName}"
  const val group = "${project.group}"
  const val pckg = "${project.pckg}"
  const val alias = "${codeFormatter.packageName(project.projectDir.name)}"
  <% if(model.sha != null) { %>
  val sha:String? = "${model.sha}"
  <% } else { %>
  val sha:String? = null
  <% } %>
  <% if(model.tags != null && model.tags.size() > 0) { %>
  val tags:Array<String>? = arrayOf(
    <%  for (def t in model.tags){ %>"${t}" <% if(t != model.tags.last()) { %>, <% }} %>
  )
  <% } else {%>
  val tags:Array<String>? = null
  <% } %>

  override fun toString(): String {
    val sb = StringBuilder()
    sb.append(group)
    sb.append(":")
    sb.append(project)
    sb.append(":")
    sb.append(version)
    <% if(model.sha != null) { %>
    sb.append("(")
    sb.append(sha)
    sb.append(")")
    <% } %>
    return sb.toString()
  }
}
