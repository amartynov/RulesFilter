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
  private ArrayList<IPRule> inputRulesList;
  private JPanel outputPanel;
  private JMenuItem export;
  
  public StartMenuItemListener(ArrayList<IPRule> outputRulesList, ArrayList<IPRule> inputList,JPanel outputPanel, JMenuItem export) {
    this.outputRulesList = outputRulesList;
    this.outputPanel = outputPanel;
    this.inputRulesList = inputList;
    this.export = export;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    outputRulesList.clear();
    ArrayList<IPRule> buf = new ArrayList<IPRule>();
    ArrayList<IPRule> toDelete = new ArrayList<IPRule>();
    ArrayList<IPRule> toInsert = new ArrayList<IPRule>();
    for(IPRule irule : inputRulesList){
      if(outputRulesList.isEmpty()) {
        outputRulesList.add(irule);
        continue;
      }
      buf.add(irule);
      for(IPRule orule : outputRulesList) {
        for(IPRule bufRule : buf) {
          IPRuleIntersection inter = orule.intersection(bufRule);
          if(!inter.isEmpty()) {
            toDelete.add(bufRule);
            ArrayList<IPRule> ibuf = bufRule.decompositRule(inter);
            for(IPRule rule: ibuf) {
              if(!rule.equals((IPRule)inter)) {
                toInsert.add(rule);
              }
            }
          }
        }
        if(!toDelete.isEmpty()) buf.removeAll(toDelete);
        toDelete.clear();
        if(!toInsert.isEmpty()) buf.addAll(toInsert); 
        /*{
          int kol = toInsert.size();
          iRule:
          for(int i = 0; i < kol - 1; i++) {
            for(int j = i + 1; j < kol; j++) {
              if(toInsert.get(i).addRule(toInsert.get(j))) {
                toInsert.remove(toInsert.get(j));
                kol--;
                i--;
                continue iRule;
              }
            }
          }
          buf.addAll(toInsert);
        }*/
        toInsert.clear();
      }
      outputRulesList.addAll(buf);
      buf.clear();
      toInsert.clear();
    }
    
    if(IPRule.globalIpRule != null) {
      for(IPRule r : outputRulesList) {
        if(r.getRuleAction() == IPRule.globalIpRule.getRuleAction()) {
          toDelete.add(r);
        }
      }
      outputRulesList.removeAll(toDelete);
    }
    
    int kol = outputRulesList.size();
    iRule:
    for(int i = 0; i < kol - 1; i++) {
      for(int j = i + 1; j < kol; j++) {
        if(outputRulesList.get(i).getRuleAction() == outputRulesList.get(j).getRuleAction()) {
          if(outputRulesList.get(i).addRule(outputRulesList.get(j))) {
            outputRulesList.remove(outputRulesList.get(j));
            kol--;
            i--;
            continue iRule;
          }          
        }
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
  /*
  private ArrayList<IPRule> decompositRuleByIntersection(IPRuleIntersection inter){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    
    IPRule rule1 = inter.getRule1();
    IPRule rule2 = inter.getRule2();
    
    if(rule1.getRuleAction() == rule2.getRuleAction()) {
      if(rule1.equals(inter)){
        res.add(rule2);
        return res;
      } else if(rule2.equals(inter)) {
        //res.add(rule1);
        return res;
      }
    }
    res.add(rule1);
    res.addAll(rule2.subRule(inter));
    
    return res;
  }*/
}
