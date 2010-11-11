package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.filter.GraphicAxises;
import org.filter.viewelements.GraphicPanel;

public class AxisJRBActionListener implements ActionListener {

	private GraphicPanel panel;
	private GraphicAxises ga;
	private int axis;
	
	public AxisJRBActionListener(GraphicPanel panel, GraphicAxises ga, int axis){
		this.panel = panel;
		this.ga = ga;
		this.axis = axis;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (axis) {
		case GraphicPanel.X_AXIS:			
			panel.setxAxis(ga);
			break;
		case GraphicPanel.Y_AXIS:
			panel.setyAxis(ga);
			break;
	
		}
	}

}
