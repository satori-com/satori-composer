package com.satori.libs.gradle.transform

import org.gradle.api.plugins.*
import org.gradle.testfixtures.*
import org.gradle.testkit.runner.*
import org.junit.*
import org.junit.rules.*
import java.io.*

class TransformPluginTests : Assert() {
  
  @Rule
  @JvmField
  val testDir = TemporaryFolder()
  
  fun TemporaryFolder.newFile(name:String, content: String): File {
    val file = testDir.newFile(name)
    file.writeText(content);
    return file
  }
  
  //@Test
  public fun `sanity checks`() {
  
    println("CLASS PATH: *************************************************************************************")
    System.getProperty("java.class.path").split(";").forEach {
      println(it)
    }
    println("*************************************************************************************************")
  
  
    testDir.newFile("template.txt", """
      hello world!
    """)
    testDir.newFile("build.gradle", """
      apply plugin: "com.satori.transform"
      //apply plugin: "com.satori.libs.gradle.transform.TransformPlugin"
      
      task test {
        transform() {
          template = file("template.txt")
          out = file("out.txt")
        }
      }
    """)
  
   /* val pluginClasspath = Thread.currentThread().contextClassLoader.getResourceAsStream("plugin-classpath.txt")!!.use {inputStream ->
      BufferedReader(InputStreamReader(inputStream)).readLines().map { File(it) }
    }*/
    
    //pluginClasspathResource.readLines().collect { new File(it) }
  
    val result = GradleRunner.create()
      .withProjectDir(testDir.root)
      //.withPluginClasspath(System.getProperty("java.class.path").split(";").map { File(it) })
      //.withPluginClasspath(pluginClasspath)
      .withPluginClasspath()
      .withArguments("test")
      .build()
  }
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.transform")
    val ext = project.properties["ext"] as ExtraPropertiesExtension
  
    /*val t = project.tasks.create("sadsa", TransformTask::class.java)
    t.execute()*/
    
    assertSame(
      TransformTask::class.java,
      project.extensions.getByName("TransformTask")
    )
  }
}