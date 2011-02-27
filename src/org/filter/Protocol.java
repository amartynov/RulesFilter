package org.filter;

import org.filter.dto.LineProjection;

public enum Protocol {
	UDP("udp"),
	TCP("tcp");
	//ICMP("icmp");
	
	private String label;
	
	Protocol(String label){
		this.label =label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public static Protocol getByLabel(String label){
		for(Protocol p : values()){
			if(p.getLabel().equals(label)){
				return p;
			}
		}
		return null;
	}
	
	public LineProjection getLineProjection(){
	  double start, length;
	  length = 1. / values().length;
	  start = ordinal() * length;
	  return new LineProjection(start, length);
	}
}
