package org.filter.viewelements;

import javax.swing.JCheckBox;

import org.filter.dto.IPRule;

public class IPRuleJCheckBox extends JCheckBox {
	
	private static final long serialVersionUID = -1360190107574796036L;
	
	private IPRule rule;
	
	public IPRuleJCheckBox(IPRule rule, String text) {
		super(text);
		this.rule = rule;
		super.addMouseListener(null);
	}

	public IPRule getRule() {
		return rule;
	}

	public void setRule(IPRule rule) {
		this.rule = rule;
	}
	
}
