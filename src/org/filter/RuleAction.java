package org.filter;

public enum RuleAction {
	Accept("accept"),
	Drop("drop"),
	Pass("pass");
	
	private String label;
	
	RuleAction(String label){
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public static RuleAction getByLabel(String label) {
		for(RuleAction ra : values()){
			if(ra.getLabel().equals(label)){
				return ra;
			}
		}
		return null;
	}
}
