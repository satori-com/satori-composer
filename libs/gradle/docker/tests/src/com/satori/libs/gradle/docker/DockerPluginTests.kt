package com.satori.libs.gradle.docker

import org.codehaus.groovy.runtime.*
import org.gradle.internal.installation.*
import org.gradle.testfixtures.*
import org.gradle.tooling.*
import org.junit.*
import org.junit.rules.*
import java.io.*

class DockerPluginTests : Assert() {
  
  @Rule
  @JvmField
  val testDir = TemporaryFolder()
  
  fun TemporaryFolder.newFile(name: String, content: String): File {
    val file = testDir.newFile(name)
    file.writeText(content);
    return file
  }
 
  @Test
  public fun `sanity checks`() {
    
    val buildGradle = testDir.newFile("build.gradle", """
      buildscript {
        repositories {
          mavenLocal()
          mavenCentral()
        }
        dependencies {
          classpath  "com.satori:satori-libs-gradle-docker:${MetaInfo.version}"
        }
      }
      apply plugin: "com.satori.docker"
      
      task runContainer(type: DockerRunContainer){
        cmd = ["print"]
        host "open-data.api.mz.com:3375"
        tlsVerify true
        tlsKey new File("key.pem")
        tlsCert new File("cert.pem")
        tlsCaCert new File("ca.pem")
        containerName="my-container"
        imageName="my-image"
        runArgs += ["--network", "my-net"]
        cmdArgs += ["--port", "2000"]
      }
      
      task dockerTest(type: DockerBase){
        //dependsOn runContainer

        host "open-data.api.mz.com:3375"
        tlsVerify true
        tlsKey new File("key.pem")
        tlsCert new File("cert.pem")
        tlsCaCert new File("ca.pem")
        doFirst{
          println buildDockerCommandAsString("images")
        }
      }
      
    """)
    
    /*
    // Gradle TestKit hides exceptions, so we are not using it, as
    // it really hard sometimes to figure out why build was failed
    val result = GradleRunner.create()
      .withProjectDir(testDir.root)
      .withArguments("--stacktrace")
      .withArguments("dockerTest")
      .build()
    println(result.output)
    */
    
    val connector = GradleConnector.newConnector()
    connector.useInstallation(CurrentGradleInstallation.get()!!.gradleHome)
    connector.forProjectDirectory(testDir.root)
  
    var ostream = ByteArrayOutputStream()
    
  
    val buildLauncher = connector.connect().newBuild().apply {
      withArguments("dockerTest")
      /*addProgressListener( ProgressListener{ ev->
        println("```${ev.description}```")
      })*/
      setStandardOutput(object :OutputStream(){
        override fun write(b: Int) {
          System.out.write(b)
          ostream.write(b)
        }
  
        override fun flush() {
          System.out.flush()
          ostream.flush()
        }
  
        override fun close() {
          ostream.close()
        }
      })
      setStandardError(System.err)
    }
    buildLauncher.run()

    assertTrue(
      String(ostream.toByteArray()).lines().contains(
        "docker -H open-data.api.mz.com:3375 --tlsverify --tlscert cert.pem --tlskey key.pem --tlscacert ca.pem images"
      )

    )
    
  }
  
  @Test
  public fun `validate project extentions`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("com.satori.docker")
    
    assertSame(
      DockerBaseTask::class.java,
      InvokerHelper.getProperty(project, "DockerBaseTask")
    )
    
    assertSame(
      DockerBuildImageTask::class.java,
      InvokerHelper.getProperty(project, "DockerBuildImageTask")
    )
    
    assertSame(
      DockerRunContainerTask::class.java,
      InvokerHelper.getProperty(project, "DockerRunContainerTask")
    )
    
    assertSame(
      DockerStopContainerTask::class.java,
      InvokerHelper.getProperty(project, "DockerStopContainerTask")
    )
  
    assertSame(
      DockerBaseTask::class.java,
      InvokerHelper.getProperty(project, "DockerBase")
    )
  
    assertSame(
      DockerBuildImageTask::class.java,
      InvokerHelper.getProperty(project, "DockerBuildImage")
    )
  
    assertSame(
      DockerRunContainerTask::class.java,
      InvokerHelper.getProperty(project, "DockerRunContainer")
    )
  
    assertSame(
      DockerStopContainerTask::class.java,
      InvokerHelper.getProperty(project, "DockerStopContainer")
    )
  }
}