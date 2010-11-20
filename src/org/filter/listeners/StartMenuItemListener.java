package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.filter.dto.IPRule;
import org.filter.dto.IPRuleIntersection;
import org.filter.viewelements.CheckBoxPanel;

public class StartMenuItemListener implements ActionListener {
  
  private ArrayList<IPRule> outputRulesList;
  private ArrayList<IPRuleIntersection> intersection;
  private JPanel outputPanel;
  private JMenuItem export;
  
  public StartMenuItemListener(ArrayList<IPRule> outputRulesList, ArrayList<IPRuleIntersection> intersection,JPanel outputPanel, JMenuItem export) {
    this.outputRulesList = outputRulesList;
    this.outputPanel = outputPanel;
    this.intersection = intersection;
    this.export = export;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    for(IPRuleIntersection rule : intersection){
      if(!rule.isEmpty()){
        outputRulesList.addAll(decompositRuleByIntersection(rule));      
      }
    }
    ((CheckBoxPanel)outputPanel.getComponent(0)).setRules(outputRulesList);
    for(ActionListener al : export.getActionListeners()) {
      if(al instanceof ExportFileMenuItemListener){
        ((ExportFileMenuItemListener) al).setOutputRulesList(outputRulesList);
      }
    }
    export.setEnabled(true);
  }
  
  private ArrayList<IPRule> decompositRuleByIntersection(IPRuleIntersection inter){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    
    IPRule rule1 = inter.getRule1();
    IPRule rule2 = inter.getRule2();
    
    if(rule1.getRuleAction() == rule2.getRuleAction()) {
      if(rule1.equals(inter)){
        res.add(rule2);
        return res;
      } else if(rule2.equals(inter)) {
        res.add(rule1);
        return res;
      }
    }
    res.add(rule1);
    res.addAll(rule2.sub(inter));
    
    return res;
  }
}
