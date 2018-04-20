// auto generated
// don't modify
package com.satori.gradle.composer.plugin

import org.gradle.api.*

open class ${model.className}(val project: Project) {
  <% rootProject.publishingProjects.forEach { p->%>
  fun ${codeFormatter.methodName(p.name)}() = "${p.group}:satori-${p.name}:${p.version}"
  <% } %>
}