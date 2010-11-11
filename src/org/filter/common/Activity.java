package org.filter.common;

public enum Activity {
	active("active"),
	inactive("inactive ");
	
	private String label;
	
	Activity(String label){
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public static Activity getByLabel(String label){
		for(Activity a : values()){
			if(a.getLabel().equals(label)) return a;
		}
		return null;
	}
	
}
