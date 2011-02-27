package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.filter.Filter;
import org.filter.common.Activity;
import org.filter.dto.IPRule;
import org.filter.utils.Utils;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.IPRuleJCheckBox;
import org.filter.viewelements.MainMenu;

public class StartMenuItemListener implements ActionListener {
  
  @Override
  public void actionPerformed(ActionEvent e) {
    Filter.clearOutput();
    /*ArrayList<IPRule> buf = new ArrayList<IPRule>();
    ArrayList<IPRule> toDelete = new ArrayList<IPRule>();
    ArrayList<IPRule> toInsert = new ArrayList<IPRule>();
    for(IPRuleJCheckBox iruleJCB : Filter.inputCBList) {
      IPRule irule = iruleJCB.getRule();
      if(irule.getActivity() == Activity.inactive || iruleJCB.isEnabled() == false) {
        continue;
      }
     
      buf.add(irule.clone());
      for(IPRule orule : Filter.outputIpRules) {
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
        
        toInsert.clear();
      }
      
      Filter.outputIpRules.addAll(buf);        
//      for(IPRule rule : buf) {
//      }
      buf.clear();
      toInsert.clear();
    }
    
    if(IPRule.globalIpRule != null) {
      for(IPRule r : Filter.outputIpRules) {
        if(r.getRuleAction() == IPRule.globalIpRule.getRuleAction()) {
          toDelete.add(r);
        }
      }
      Filter.outputIpRules.removeAll(toDelete);
    }
    
    int kol = Filter.outputIpRules.size();
    iRule:
    for(int i = 0; i < kol - 1; i++) {
      for(int j = i + 1; j < kol; j++) {
        if(Filter.outputIpRules.get(i).getRuleAction() == Filter.outputIpRules.get(j).getRuleAction()) {
          if(Filter.outputIpRules.get(i).addRule(Filter.outputIpRules.get(j))) {
            Filter.outputIpRules.remove(Filter.outputIpRules.get(j));
            kol--;
            i--;
            continue iRule;
          }          
        }
      }
    }*/
    
    ArrayList<IPRule> newRules = new ArrayList<IPRule>();
    ArrayList<IPRule> oldRules = new ArrayList<IPRule>();
    
    for(IPRuleJCheckBox iruleJCB : Filter.inputCBList) {
      IPRule irule = iruleJCB.getRule();
      if(irule.getActivity() == Activity.active && iruleJCB.isEnabled()) {
        newRules.add(irule);
      }
    }
    
    Filter.outputIpRules.addAll(Utils.getConsistentsRules(newRules, oldRules));
    
    ((CheckBoxPanel)Filter.outputPanel.getComponent(0)).setRules(Filter.outputIpRules);
    for(ActionListener al : MainMenu.exportFileItem.getActionListeners()) {
      if(al instanceof ExportFileMenuItemListener){
        ((ExportFileMenuItemListener) al).setPanel(((CheckBoxPanel)Filter.outputPanel.getComponent(0)));
      }
    }
    MainMenu.exportFileItem.setEnabled(true);
  }
}
