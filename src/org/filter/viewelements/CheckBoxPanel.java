package org.filter.viewelements;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
  
  public CheckBoxPanel(GraphicPanel grPanel) {
    super(null);
    this.grPanel = grPanel;
    
    selectAll = new JCheckBox("Select All");
    selectAll.setSelected(false);
    selectAll.setEnabled(false);
    selectAll.setBounds(0, 5, 200, 40);
    
    scroll = new JScrollPane();
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setBounds(0, 45, 200, 310);
    
    this.add(selectAll);
    this.add(scroll);
    this.setBounds(0, 0, 200, 355);
  }

  public void setRules(ArrayList<IPRule> ipRules) {
    JPanel panelCB = new JPanel();
    panelCB.setLayout(new BoxLayout(panelCB, BoxLayout.Y_AXIS));
    
    rulesJB = new ArrayList<IPRuleJCheckBox>(); 
    boolean flag = true;
    for(IPRule r : ipRules) {
      IPRuleJCheckBox ruleCB = new IPRuleJCheckBox(r, "IP rule " + r.getNumber());
      ruleCB.addActionListener(new IPRuleCBActionListener(this));
      switch(r.getActivity()){
      case active:
        ruleCB.setSelected(true);
        break;
      case inactive:
        ruleCB.setSelected(false);
        flag = false;
        break;
      }
      rulesJB.add(ruleCB);
      panelCB.add(ruleCB);
//      panelCB.addMouseListener(new IPRuleCBMouseListener(r));
      ruleCB.addMouseListener(new IPRuleCBMouseListener(r));
    }
    scroll.setViewportView(panelCB);
    selectAll.addActionListener(new SelectAllListener(rulesJB, grPanel));
    selectAll.setEnabled(true);
    selectAll.setSelected(flag);
    grPanel.setRulesJCB(rulesJB);
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
  
  
  
}
