package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import org.filter.common.Activity;
import org.filter.viewelements.GraphicPanel;
import org.filter.viewelements.IPRuleJCheckBox;

public class SelectAllListener implements ActionListener {
	
	private ArrayList<IPRuleJCheckBox> list;
	private GraphicPanel panel;
	
	public SelectAllListener(ArrayList<IPRuleJCheckBox> list, GraphicPanel panel){
		this.list = list;
		this.panel = panel;
	}
	
	public SelectAllListener(GraphicPanel panel, ArrayList<IPRuleJCheckBox> list){
//	  this.list = new ArrayList<IPRuleJCheckBox>();
	  this.list = list;
    this.panel = panel;
  }
	
	public void addIPRuleCB(IPRuleJCheckBox rule) {
	  list.add(rule);
	}
	
	public void removeIPRuleCB(IPRuleJCheckBox rule) {
	  list.remove(rule);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean val = ((JCheckBox)e.getSource()).isSelected();
		for(IPRuleJCheckBox cb : list) {
		  if(cb.isEnabled()) {
		    cb.setSelected(val);
		    cb.getRule().setActivity(val ? Activity.active : Activity.inactive);		    
		  }
		}
		panel.clearRects();
		panel.repaint();
	}

	public ArrayList<IPRuleJCheckBox> getList() {
		return list;
	}

	public void setList(ArrayList<IPRuleJCheckBox> list) {
		this.list = list;
	}
	
}
