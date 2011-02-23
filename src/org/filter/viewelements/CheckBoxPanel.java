package org.filter.viewelements;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.filter.Filter;
import org.filter.dto.IPRule;
import org.filter.listeners.IPRuleCBActionListener;
import org.filter.listeners.IPRuleCBMouseListener;
import org.filter.listeners.SelectAllListener;

public class CheckBoxPanel extends JPanel{
  
  private static final long serialVersionUID = -6947150222450710489L;
  
  private ArrayList<IPRuleJCheckBox> rulesJB;
  private JCheckBox selectAll;
  private JScrollPane scroll;
  private GraphicPanel grPanel;
  private JPanel panelCB;
  
  public CheckBoxPanel(GraphicPanel grPanel, ArrayList<IPRuleJCheckBox> list) {
    super(null);
    this.grPanel = grPanel;
    
    selectAll = new JCheckBox(Filter.labels.getSelectAll());
    selectAll.setSelected(false);
    selectAll.setEnabled(false);
    selectAll.setBounds(0, 5, 200, 40);
    
    scroll = new JScrollPane();
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setBounds(0, 45, 200, 310);
    
    this.add(selectAll);
    this.add(scroll);
    this.setBounds(0, 0, 200, 355);
    
    panelCB = new JPanel();
    panelCB.setLayout(new BoxLayout(panelCB, BoxLayout.Y_AXIS));
    
    //rulesJB = new ArrayList<IPRuleJCheckBox>();
    rulesJB = list;
    
    scroll.setViewportView(panelCB);
    
    selectAll.addActionListener(new SelectAllListener(grPanel, rulesJB));
  }

  public void setRules(ArrayList<IPRule> ipRules) {
    for(IPRule rule : ipRules) {
      addRule(rule);
    }
  }
  
  public void addRule(IPRule ipRule) {
    if(rulesJB.isEmpty()) {
      selectAll.setSelected(true);
      selectAll.setEnabled(true);
    }
    IPRuleJCheckBox ruleCB = new IPRuleJCheckBox(ipRule, "IP rule " + ipRule.getNumber());
    ruleCB.addActionListener(new IPRuleCBActionListener(this));
    switch (ipRule.getActivity()) {
    case active:
      ruleCB.setSelected(true);
      selectAll.setSelected(selectAll.isSelected());
      break;
    case inactive:
      ruleCB.setSelected(false);
      selectAll.setSelected(false);
      break;
    }
    rulesJB.add(ruleCB);
    panelCB.add(ruleCB);
    ruleCB.addMouseListener(IPRuleCBMouseListener.getInstance(rulesJB, this));
    
//    ((SelectAllListener)selectAll.getActionListeners()[0]).addIPRuleCB(ruleCB);
    
    grPanel.setRulesJCB(rulesJB);
  }
  
  public void removeElement(IPRuleJCheckBox ruleCB) {
    panelCB.remove(ruleCB);
    rulesJB.remove(ruleCB);
    
    grPanel.newPaint();
    this.repaint();
  }
  
  public void removeElement(IPRule rule) {
    for(IPRuleJCheckBox ruleCB : rulesJB) {
      if(ruleCB.getRule() == rule) {
        panelCB.remove(ruleCB);
        rulesJB.remove(ruleCB);
        break;
      }
    }
    
    grPanel.newPaint();
    this.repaint();
  }

  public ArrayList<IPRuleJCheckBox> getRulesJB() {
    return rulesJB;
  }

  public GraphicPanel getGrPanel() {
    return grPanel;
  }

  public JCheckBox getSelectAll() {
    return selectAll;
  }
  
  public void clear() {
    rulesJB.clear();
    grPanel.setRulesJCB(rulesJB);
    panelCB.removeAll();
    selectAll.setEnabled(false);
    selectAll.setSelected(false);
  }
  
}
