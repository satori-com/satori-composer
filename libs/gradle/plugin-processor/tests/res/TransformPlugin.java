package com.satori.libs.gradle.plugin.processor;

import com.satori.libs.gradle.plugin.annotations.GradlePlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@GradlePlugin("com.satori.transform")
public class TransformPlugin implements Plugin<Project> {
  
  @Override
  public void apply(Project project) {
  }
}