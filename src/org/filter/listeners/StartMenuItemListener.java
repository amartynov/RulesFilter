package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.filter.dto.IPRule;
import org.filter.viewelements.CheckBoxPanel;

public class StartMenuItemListener implements ActionListener {
  
  private ArrayList<IPRule> inputRulesList;
  private ArrayList<IPRule> outputRulesList;
  private JPanel outputPanel;
  
  public StartMenuItemListener(ArrayList<IPRule> inputRulesList, ArrayList<IPRule> outputRulesList, JPanel outputPanel) {
    this.inputRulesList = inputRulesList;
    this.outputRulesList = outputRulesList;
    this.outputPanel = outputPanel;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    for (int i = 0; i < inputRulesList.size() - 1; i++){
      IPRule irule = inputRulesList.get(i);
      ArrayList<IPRule> interList = new ArrayList<IPRule>(); 
      for(int j = i + 1; j < inputRulesList.size(); j++){
        IPRule inter = irule.intersection(inputRulesList.get(j));
        //TODO:
        if(inter.isEmpty()) {
          
        } else {
          
        }
      }
    }
    ((CheckBoxPanel)outputPanel.getComponent(0)).setRules(inputRulesList);
    
  }

}
