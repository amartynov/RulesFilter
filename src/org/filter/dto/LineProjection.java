package org.filter.dto;

public class LineProjection {

	private double start;
	private double length;
	
	public LineProjection(double start, double length) {
		this.start = start;
		this.length = length;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
}
