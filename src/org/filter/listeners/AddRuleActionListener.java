package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.filter.Filter;
import org.filter.dto.IPRule;
import org.filter.viewelements.CheckBoxPanel;

public class AddRuleActionListener implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    ArrayList<IPRule> rules = new ArrayList<IPRule>();
    IPRule rule = new IPRule();
    rules.add(rule);
    ((CheckBoxPanel)Filter.inputPanel.getComponent(0)).setRules(rules);
    
  }

}
