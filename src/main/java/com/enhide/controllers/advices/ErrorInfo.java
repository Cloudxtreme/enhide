package com.enhide.controllers.advices;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author edwin.palathinkal
 */
public class ErrorInfo {

  public final String url;
  public final String error_description;
  public final String error;
  public final String[] stackTrace;

  public ErrorInfo(String url, Exception ex) {
    this.url = url;
    this.error_description = ex.getMessage();
    this.error = ex.getClass().getSimpleName();

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    this.stackTrace = sw.toString().split("\r\n\t"); // stack trace as a string
  }
}
