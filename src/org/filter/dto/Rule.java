package org.filter.dto;

import org.filter.RuleAction;

public class Rule {
	
	private String label = "ip";
	private int number;
	private RuleAction ruleAction;
	private String ruleLog = "log";
	
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getMessage(){
		return "Rule number " + getNumber();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public RuleAction getRuleAction() {
		return ruleAction;
	}

	public void setRuleAction(RuleAction ruleAction) {
		this.ruleAction = ruleAction;
	}

  public String getRuleLog() {
    return ruleLog;
  }

  public void setRuleLog(String ruleLog) {
    this.ruleLog = ruleLog;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(label).append(":").append(number).append(":").append(ruleAction.getLabel()).append(":").append(ruleLog);
    return sb.toString();
  }

}
