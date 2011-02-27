package org.filter.exeption;

public class FilterException extends Throwable {

  private static final long serialVersionUID = 3501542078992982832L;
  
  private String mes;
  
  public FilterException(String str) {
    mes = str;
  }

  public String getMes() {
    return mes;
  }

  public void setMes(String mes) {
    this.mes = mes;
  }
  
}
