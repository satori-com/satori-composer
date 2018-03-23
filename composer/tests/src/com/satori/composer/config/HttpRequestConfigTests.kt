package com.satori.composer.config

import org.junit.*

class HttpRequestConfigTests : Assert() {
  
  @Test
  fun `getUrl() sanity test`() {
    
    assertEquals(
      "http://localhost",
      HttpRequestConfig().apply {
        host = "localhost"
        validate()
      }.getUrl("http")
    )
  
    assertEquals(
      "http://localhost",
      HttpRequestConfig().apply {
        host = "localhost"
        port = 80
        validate()
      }.getUrl("http")
    )

    assertEquals(
      "https://satori.com",
      HttpRequestConfig().apply {
        host = "satori.com"
        ssl = true
        validate()
      }.getUrl("http")
    )
  
    assertEquals(
      "https://satori.com",
      HttpRequestConfig().apply {
        host = "satori.com"
        ssl = true
        port = 443
        validate()
      }.getUrl("http")
    )
    
    assertEquals(
      "ws://127.0.0.1:443",
      HttpRequestConfig().apply {
        host = "127.0.0.1"
        port = 443
        validate()
      }.getUrl("ws")
    )
  
    assertEquals(
      "wss://127.0.0.1:80",
      HttpRequestConfig().apply {
        host = "127.0.0.1"
        ssl = true
        port = 80
        validate()
      }.getUrl("ws")
    )
  
    assertEquals(
      "http://x.com/a?x",
      HttpRequestConfig().apply {
        host = "x.com"
        path = "a"
        args = HashMap()
        args.put("x", null)
        validate()
      }.getUrl("http")
    )
  
    assertEquals(
      "http://search.com?q=heaven",
      HttpRequestConfig().apply {
        host = "search.com"
        args = HashMap()
        args.put("q", "heaven")
        validate()
      }.getUrl("http")
    )
  }
  
}
