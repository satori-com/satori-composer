package com.satori.codegen.mustache.builder

import com.github.mustachejava.*
import java.io.*

fun TemplateParser.parse(code: Code, name: String) = TemplateVisitor.new(name).also {
  parse(code, it)
}

fun TemplateParser.parse(codes: Array<Code>?, name: String) = TemplateVisitor.new(name).also {
  parse(codes, it)
}

fun TemplateParser.parse(code: Code, visitor: ITemplateVisitor) {
  processCode(code, visitor)
}

fun TemplateParser.parse(codes: Array<Code>?, ctx: ITemplateVisitor) {
  processCodes(codes, ctx)
}

fun TemplateParser.parse(tempalte: String, name: String) = TemplateVisitor.new(name).also {
  parse(tempalte, it)
}

fun TemplateParser.parse(tempalte: String, visitor: TemplateVisitor) = tempalte.reader().use {
  parse(it, visitor)
}

fun TemplateParser.parse(tempalte: Reader, name: String) = TemplateVisitor.new(name).also {
  parse(tempalte, it)
}

class T : DefaultMustacheFactory() {
  override fun getReader(resourceName: String?): Reader {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun encode(value: String?, writer: Writer?) {
    super.encode(value, writer)
  }
  
  override fun getObjectHandler(): ObjectHandler {
    return super.getObjectHandler()
  }
  
  override fun compile(name: String?): Mustache {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun compile(reader: Reader?, name: String?): Mustache {
    return mc.compile(reader, name)
  }
  
  override fun createMustacheVisitor(): MustacheVisitor {
    return super.createMustacheVisitor()
  }
  
  override fun translate(from: String?): String {
    return super.translate(from)
  }
}

fun TemplateParser.parse(tempalte: Reader, visitor: TemplateVisitor) {
  val code = T().compile(tempalte, visitor.model.name)
  processCodes(code.codes, visitor)
}