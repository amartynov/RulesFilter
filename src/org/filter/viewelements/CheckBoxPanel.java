package org.filter.viewelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.filter.Filter;
import org.filter.dto.IPRule;
import org.filter.listeners.IPRuleCBActionListener;
import org.filter.listeners.IPRuleCBMouseListener;
import org.filter.listeners.SelectAllListener;

public class CheckBoxPanel extends JPanel {
  
  private static final long serialVersionUID = -6947150222450710489L;
  
  private ArrayList<IPRuleJCheckBox> rulesJB;
  private JCheckBox selectAll;
  private JScrollPane scroll;
  private GraphicPanel grPanel;
  private JPanel panelCB;
  
  private JPopupMenu menu = null;
  private EditRuleDialog dialog;
  private CheckBoxPanel panel;
  
  public CheckBoxPanel(GraphicPanel grPanel, ArrayList<IPRuleJCheckBox> list) {
    super(null);
    panel = this;
    dialog = new EditRuleDialog(panel);
    menu = new JPopupMenu();
    menu.setSize(100, 100);
    JMenuItem add = new JMenuItem(Filter.labels.getPopupMenuAddLabel());
    add.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        IPRule rule = new IPRule();
//        panel.addRule(rule);
        dialog.newRule(rule);
        dialog.setVisible(true);
      }
    });
    menu.add(add);
    
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
    
    
    scroll.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        //super.mousePressed(e);
        if(e.getButton() == MouseEvent.BUTTON3) {
          menu.show(e.getComponent(), e.getX(), e.getY());
        }
      }
    });
  }

  public void setRules(ArrayList<IPRule> ipRules) {
    for(IPRule rule : ipRules) {
      addRule(rule);
    }
  }
  
  public void addRule(IPRule ipRule) {
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
    ruleCB.addMouseListener(new IPRuleCBMouseListener(ruleCB, this));
    
    if(!Filter.inputCBList.isEmpty()) {
      selectAll.setSelected(true);
      selectAll.setEnabled(true);
      MainMenu.startEditItem.setEnabled(true);
      MainMenu.analizEditItem.setEnabled(true);
    }
    
    if(!Filter.outputCBList.isEmpty()) {
      selectAll.setSelected(true);
      selectAll.setEnabled(true);
      MainMenu.startEditItem.setEnabled(true);
      MainMenu.analizEditItem.setEnabled(true);
      MainMenu.exportFileItem.setEnabled(true);
    }
//    ((SelectAllListener)selectAll.getActionListeners()[0]).addIPRuleCB(ruleCB);
    
    grPanel.setRulesJCB(rulesJB);
  }
  
  public void removeElement(IPRuleJCheckBox ruleCB) {
    panelCB.remove(ruleCB);
    rulesJB.remove(ruleCB);
    
    if(getRulesJB().isEmpty()) {
      selectAll.setSelected(false);
      selectAll.setEnabled(false);
      MainMenu.startEditItem.setEnabled(false);
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
  
  public ArrayList<IPRule> getRules() {
    ArrayList<IPRule> rules = new ArrayList<IPRule>();
    for(IPRuleJCheckBox ruleCB : rulesJB) {
      rules.add(ruleCB.getRule());
    }
    return rules;
  }
  
}
