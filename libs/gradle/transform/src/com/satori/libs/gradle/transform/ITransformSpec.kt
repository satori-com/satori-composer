package com.satori.libs.gradle.transform

import java.io.*

interface ITransformSpec {
  var template: File?
  var output: File?
}