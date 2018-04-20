// auto generated
// don't modify
package com.satori.gradle.composer.plugin

import org.gradle.api.*

open class ${model.className}(val project: Project) {
  val satoriComposerVersion = "${version}"
  <% rootProject.publishingProjects.forEach { p->%>
  fun satori${codeFormatter.pascal(p.name)}() = "${p.group}:satori-${p.name}:${p.version}"
  <% } %>
}