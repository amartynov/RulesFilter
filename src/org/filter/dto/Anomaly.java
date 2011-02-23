package org.filter.dto;

public enum Anomaly {
  SHADOWING("затенение"),
  CORRELATION("корреляция"),
  GENERALIZATION("исключение"),
  REDUNDANCY("избыточность");
  
  private String label;
  
  private Anomaly(String label) {
    this.label = label;
  }
  
  public String getMessage(int rule1, int rule2) {
    return "Аномалия мужду правилами " + rule1 + " и " + rule2 + " : " + label + "\n";
  }
}
