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
            /*
            //тут надо расскладывать оба правила
            ArrayList<IPRule> obuf = orule.decompositRule(inter);
            ArrayList<IPRule> ibuf = bufRule.decompositRule(inter);
            toDelete.add(bufRule);
            rRule:
            for(IPRule r : obuf) {
              for(IPRule rb : ibuf) {
                if(rb.equals(r)) {
                  ibuf.remove(rb);
                  continue rRule;
                }
                  //if(!toInsert.contains(rb)) toInsert.add(rb);                
              }
            }
            toInsert.addAll(ibuf);
            /*
            if(bufRule.getRuleAction() == orule.getRuleAction()) {
              //if rules are intersected and actions are equals
              if(!orule.decideAnomaly(bufRule, inter)) {
                toInsert.addAll(bufRule.decompositRule(inter));
              }
            } else {
              
            }*/
          }
        }
        if(!toDelete.isEmpty()) buf.removeAll(toDelete);
        toDelete.clear();
        if(!toInsert.isEmpty()) {
          for(IPRule r : toInsert) {
            if(orule.intersection(r).isEmpty()) {
            //if(!orule.equals(r)) {
              buf.add(r);              
            }
          }
        }
        toInsert.clear();
      }
      toDelete.clear();
      toInsert.clear();
      for(IPRule or : outputRulesList) {
        for(IPRule r : buf) {
          if(or.intersection(r).isEmpty())
            if(!toInsert.contains(r)) toInsert.add(r);
        }        
      }
      outputRulesList.addAll(toInsert);
      buf.clear();
      toInsert.clear();
      /*
      if(!rule.isEmpty()){
        outputRulesList.addAll(decompositRuleByIntersection(rule));      
      } else {
        if(!outputRulesList.contains(rule.getRule1())){
          outputRulesList.add(rule.getRule1());
        }
      }*/
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
