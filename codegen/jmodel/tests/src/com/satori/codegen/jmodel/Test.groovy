package com.satori.codegen.jmodel

import com.satori.codegen.jmodel.codemodel.*
import com.sun.codemodel.*

class Test extends JModelScript {

  @Override
  Object run() {
    PACKAGE("com.mz.gtfs") {
      //IMPORT("com.fasterxml.jackson.annotation.*")
      CLASS("Z") {

      }
      COMMENT("sadsadsa")
      CLASS("TranslatedStringAAA") {
        CLASS("Foo") {

        }
        COMMENT("sadsadsa")
        FIELD("x") {
          ANNOTATION("com.mz.JsonProperty") {
            PARAM("s")
          }
          COMMENT("  sadsad\n  sa")
          TYPE("int")
        }
        FIELD("y") {
          ANNOTATION("com.mz.JsonProperty") {
            PARAM("s")
          }
          COMMENT("  sadsad\n  sa")
          TYPE("int")
        }
        METHOD("toString") {
          ANNOTATION("Override")
        }
        def ename = "Bar"
        ENUM(ename) {
          OPTION("mo") {
            ARG(01)
          }
          OPTION("tu", 1)
          OPTION("we", 2)
          FIELD("value") {
            TYPE("int")
            ANNOTATION("com.fasterxml.jackson.annotation.JsonValue") {}
          }
          CTOR() {
            PARAM("value") {
              TYPE("int")
            }
            CODE("""log.info("creating ${ename} object....");""")
            BODY() {
              CODE("this.value = value;")
              RETURN("value")
            }
          }
          METHOD("fromInt") {
            TYPE(ename)
            def v = PARAM("value", int)
            SWITCH(v) {
              for (int i = 0; i < 3; i += 1) {
                CASE(i) {
                  RETURN(2 * i)
                }
              }
              DEFAULT {
                RETURN {
                  //CODE("asdsada");
                }
              }
            }
            RETURN(v)
          }
        }

      }
    }
  }

  static void main(String... args) {
    def jmodel = new Test();
    jmodel.run()
    jmodel.model.build(new CodeWriter() {
      @Override
      OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        return System.out
      }

      @Override
      void close() throws IOException {

      }
    })

  }

}

