package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.filter.Filter;
import org.filter.common.Activity;
import org.filter.dto.Anomaly;
import org.filter.dto.IPRule;
import org.filter.dto.IPRuleIntersection;
import org.filter.viewelements.AnalizDialog;
import org.filter.viewelements.IPRuleJCheckBox;

public class AnalizMenuItemListener implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    ArrayList<String> mes = new ArrayList<String>();
    
    ArrayList<IPRuleJCheckBox> list = null;
    switch(Filter.panelNumber) {
    case 0:
      list = Filter.inputCBList;
      break;
    case 1:
      list = Filter.outputCBList;
      break;
    }
    
    if(list == null) {
      JOptionPane.showMessageDialog(null, "Ошибка: не могу найти список правил");
      return;
    }
    
    for(int i = 0; i < list.size(); i++) {
      IPRule iRule = list.get(i).getRule();
      if(iRule.getActivity() == Activity.inactive || list.get(i).isEnabled() == false) {
        continue;
      }
      for(int j = i + 1; j < list.size(); j++) {
        IPRule jRule = list.get(j).getRule();
        
        if(jRule.getActivity() == Activity.inactive || list.get(j).isEnabled() == false) {
          continue;
        }
        
        IPRuleIntersection inter = iRule.intersection(jRule);
        if(inter == null || inter.isEmpty()) {
          continue;
        }
        if(iRule.getRuleAction() == jRule.getRuleAction()) {
          mes.add(Anomaly.REDUNDANCY.getMessage(iRule.getNumber(), jRule.getNumber()));
        } else {
          if(inter.equals(iRule)) {
            mes.add(Anomaly.GENERALIZATION.getMessage(iRule.getNumber(), jRule.getNumber()));
          } else if(inter.equals(jRule)) {
            mes.add(Anomaly.SHADOWING.getMessage(iRule.getNumber(), jRule.getNumber()));
          } else {
            mes.add(Anomaly.CORRELATION.getMessage(iRule.getNumber(), jRule.getNumber()));
          }
        }
      }
    }
    
    StringBuilder sb = new StringBuilder();
    if (mes.isEmpty()) {
      JOptionPane.showMessageDialog(null, "Аномалий не обнаружено");
    } else {
      for(String str : mes) {
        sb.append(str);
      }
      new AnalizDialog(sb.toString(), mes.size()).setVisible(true);
    }
  }

}
