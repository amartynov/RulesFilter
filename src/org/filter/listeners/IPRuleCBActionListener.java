package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import org.filter.common.Activity;
import org.filter.dto.IPRule;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.IPRuleJCheckBox;

public class IPRuleCBActionListener implements ActionListener{

	private CheckBoxPanel panel;
	
	public IPRuleCBActionListener(CheckBoxPanel panel) {
		this.panel = panel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.getGrPanel().clearRects();
		panel.getGrPanel().repaint();
		boolean flag = true;
		IPRule ipRule = ((IPRuleJCheckBox)e.getSource()).getRule();
		if(((JCheckBox)e.getSource()).isSelected()){
		  ipRule.setActivity(Activity.active);
			ArrayList<IPRuleJCheckBox> rules = panel.getRulesJB();
			for(IPRuleJCheckBox r : rules) {
			  if(r.isSelected() == false){
			    flag = false;
			    break;
			  }
			}
		} else {
		  ipRule.setActivity(Activity.inactive);
			flag = false;
		}
		panel.getSelectAll().setSelected(flag);
		
	}

}
