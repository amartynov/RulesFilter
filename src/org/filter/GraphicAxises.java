package org.filter;

public enum GraphicAxises {
	srcIP("Source IP", "srcIP"),
	destIP("Destination IP", "destIP"),
	srcPort("Source Port", "srcPort"),
	destPort("Destination Port", "destPOrt"),
	protocol("Protocol", "protocol");
	
	private String value;
	private String label;
	
	GraphicAxises(String value, String label){
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
