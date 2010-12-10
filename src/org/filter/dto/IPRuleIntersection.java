package org.filter.dto;

public class IPRuleIntersection extends IPRule {
  
  private IPRule rule1;
  private IPRule rule2;
  
  public IPRule getRule1() {
    return rule1;
  }
  public void setRule1(IPRule rule1) {
    this.rule1 = rule1;
  }
  public IPRule getRule2() {
    return rule2;
  }
  public void setRule2(IPRule rule2) {
    this.rule2 = rule2;
  }
  
  public String toString() {
    return super.toString();
  }
  
}
