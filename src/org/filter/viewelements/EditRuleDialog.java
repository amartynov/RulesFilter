package org.filter.viewelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.filter.Filter;
import org.filter.Protocol;
import org.filter.RuleAction;
import org.filter.common.IP4AddressInterval;
import org.filter.common.InetPort;
import org.filter.dto.IPRule;
import org.filter.exeption.FilterException;
import org.filter.utils.Utils;

public class EditRuleDialog extends JDialog {
  
  private static final long serialVersionUID = 5316532735580586765L;
  
  private IPRule rule;
  private JTextField numberTA;
  private JComboBox protocolTA;
  private JTextField sourceIPTA;
  private JTextField sourcePortTA;
  private JTextField destIPTA;
  private JTextField destPortTA;
  private JComboBox actionTA;
  private CheckBoxPanel panel;
  
  private boolean newRule = false;
  private Boolean saved = null;
  
  public EditRuleDialog(final CheckBoxPanel panel) {
    super();
    
    //this.rulesJB = rulesJB;
    this.panel = panel;
    
    int labelsY = 5;
    int labelsHeight = 20;
    int XGap = 5;
    int nextX = 0;
    int taY = labelsY * 2 + labelsHeight;
    int logHeight = 100;
    int logY = taY + labelsY + labelsHeight;
    int buttonY = logY + labelsY + logHeight;
    int buttonHeight = 30;
    
    int numberWidth = Filter.labels.getNumberLabel().length() * 10;
    int protocolWidth = Filter.labels.getProtocolLabel().length() * 10;
    int ipWidth = (Filter.labels.getSourceIPLabel().length() + 6) * 10;
    int portWidth = Filter.labels.getSourcePortLabel().length() * 10;
    int actionWidth = Filter.labels.getActionLabel().length() * 10;
    
    int sizeX = XGap * 9 + numberWidth + protocolWidth + (ipWidth + portWidth) * 2 + actionWidth;
    int sizeY = labelsY * 5 + buttonY + buttonHeight;
    
    this.setLayout(null);
    this.setSize(sizeX, sizeY);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setModal(false);
    
    JLabel numberLabel = new JLabel(Filter.labels.getNumberLabel());
    numberLabel.setBounds(nextX += XGap, labelsY, numberWidth, labelsHeight);
    numberTA = new JTextField();
    numberTA.setBounds(nextX, taY, numberWidth, labelsHeight);
    
    JLabel protocolLabel = new JLabel(Filter.labels.getProtocolLabel());
    protocolLabel.setBounds(nextX += (XGap  + numberWidth), labelsY, protocolWidth, labelsHeight);
    protocolTA = new JComboBox(Protocol.values());
    protocolTA.setBounds(nextX, taY, protocolWidth, labelsHeight);
    
    JLabel sourceIPLabel = new JLabel(Filter.labels.getSourceIPLabel());
    sourceIPLabel.setBounds(nextX += (XGap + protocolWidth), labelsY, ipWidth, labelsHeight);
    sourceIPTA = new JTextField();
    sourceIPTA.setBounds(nextX, taY, ipWidth, labelsHeight);
    
    JLabel sourcePortLabel = new JLabel(Filter.labels.getSourcePortLabel());
    sourcePortLabel.setBounds(nextX += (XGap + ipWidth), labelsY, portWidth, labelsHeight);
    sourcePortTA = new JTextField();
    sourcePortTA.setBounds(nextX, taY, portWidth, labelsHeight);
    
    JLabel destIPLabel = new JLabel(Filter.labels.getDestIPLabel());
    destIPLabel.setBounds(nextX += (XGap + portWidth), labelsY, ipWidth, labelsHeight);
    destIPTA = new JTextField();
    destIPTA.setBounds(nextX, taY, ipWidth, labelsHeight);
    
    JLabel destPortLabel = new JLabel(Filter.labels.getDestPortLabel());
    destPortLabel.setBounds(nextX += (XGap + ipWidth), labelsY, portWidth, labelsHeight);
    destPortTA = new JTextField();
    destPortTA.setBounds(nextX, taY, portWidth, labelsHeight);
    
    JLabel actionLabel = new JLabel(Filter.labels.getActionLabel());
    actionLabel.setBounds(nextX += (XGap + portWidth), labelsY, actionWidth, labelsHeight);
    actionTA = new JComboBox(RuleAction.values());
    actionTA.setBounds(nextX, taY, actionWidth, labelsHeight);
    
    final JTextArea logTA = new JTextArea();
    logTA.setEditable(false);
    
    JScrollPane jsbLog = new JScrollPane(logTA);
    jsbLog.setBounds(XGap, logY, sizeX - 3 * XGap, logHeight);
    jsbLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    final EditRuleDialog dialog = this;
    
    JButton exitButton = new JButton(Filter.labels.getButtonExitLabel());
    int cbWidth = (Filter.labels.getButtonCancelLabel().length() + 5) * 10;
    exitButton.setBounds(sizeX / 2 + XGap, buttonY, cbWidth, buttonHeight);
    exitButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        logTA.setText("");
        /*if(saved != null) {
          if(!saved) {
            IPRuleCBMouseListener.panel.removeElement(rule);            
          }
        }*/
//        if (panel != null) {
//          panel.removeElement(rule);              
//        }
        dialog.dispose();
      }
    });
    
    JButton validateButton = new JButton(Filter.labels.getButtonValidateLabel());
    int validateWidth = (Filter.labels.getButtonValidateLabel().length() + 5) * 10;
    validateButton.setBounds(sizeX / 2 - XGap - validateWidth, buttonY, validateWidth, buttonHeight);
    
    validateButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean fail = false;
        logTA.setText("");
        int number;
        ArrayList<IP4AddressInterval> destAddress = null;
        ArrayList<IP4AddressInterval> srcAddress = null;
        Protocol protocol = null;
        ArrayList<InetPort> srcPort = null;
        ArrayList<InetPort> destPort = null;
        RuleAction ruleAction = null;
        
        if(numberTA.getText().trim().isEmpty()) {
          logTA.append("Не указан номер правила\n");
          fail = true;
        } else {
          try {
            number = Integer.parseInt(numberTA.getText());            
            if(newRule) {
              for(IPRuleJCheckBox rulecb : panel.getRulesJB()) {
                if(rulecb.getRule().getNumber() == number) {
                  logTA.append("Правило с номером " + number + " существует\n");
                  fail = true;
                }
              }
            }          
          } catch (NumberFormatException exeption) {
            logTA.append("Номер правила указан неверно\n");
            fail = true;
          }
        }

        protocol = (Protocol)protocolTA.getSelectedItem();
        if(protocol == null) {
          logTA.append("Неверно указан протокол\n");
          fail = true;
        }
        
        try {
          destAddress = IP4AddressInterval.strToAddress(destIPTA.getText());
        } catch (FilterException exeption) {
          logTA.append("Неверно указан адрес приемника: " + destIPTA.getText() + " (" + exeption.getMes() + ")" + "\n");
          fail = true;
        }
        
        try {
          srcAddress = IP4AddressInterval.strToAddress(sourceIPTA.getText());
        } catch (FilterException exeption) {
          logTA.append("Неверно указан адрес источника: " + sourceIPTA.getText() + " (" + exeption.getMes() + ")" + "\n");
          fail = true;
        }
        
        try {
          srcPort = InetPort.strToPort(sourcePortTA.getText());
        } catch (FilterException e1) {
          logTA.append("Неверно указан порт источника: " + sourcePortTA.getText() + " (" + e1.getMes() + ")" + "\n");
          fail = true;
        }
        
        try {
          destPort = InetPort.strToPort(destPortTA.getText());
        } catch (FilterException e1) {
          logTA.append("Неверно указан порт приемника: " + destPortTA.getText() + " (" + e1.getMes() + ")" + "\n");
          fail = true;
        }
        
        ruleAction = (RuleAction)actionTA.getSelectedItem(); 
        if(ruleAction == null) {
          logTA.append("Неверно указано действие\n");
          fail = true;
        }
        
        if(!fail) {
          e.getSource();
          if(JOptionPane.showConfirmDialog(null, "Сохранить правило?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            rule.setNumber(Integer.parseInt(numberTA.getText()));
            rule.setDestAddress(destAddress);
            rule.setSrcAddress(srcAddress);
            rule.setProtocol(protocol);
            rule.setSrcPort(srcPort);
            rule.setDestPort(destPort);
            rule.setRuleAction(ruleAction);
            saved = true;
            
            ArrayList<IPRule> newRules = new ArrayList<IPRule>();
            newRules.add(rule);
            
            
            if(panel != null) {
              if (Filter.panelNumber == 1) {
                if(newRules.isEmpty()) {
                  logTA.append("Правило не принесет результата");
                }
                panel.setRules(Utils.getConsistentsRules(newRules, panel.getRules()));
              } else {
                panel.setRules(newRules);
              }
            }              
          }          
        }
      }
    });
    
    this.add(numberLabel);
    this.add(protocolLabel);
    this.add(sourceIPLabel);
    this.add(sourcePortLabel);
    this.add(destIPLabel);
    this.add(destPortLabel);
    this.add(actionLabel);
    
    this.add(numberTA);
    this.add(protocolTA);
    this.add(sourceIPTA);
    this.add(sourcePortTA);
    this.add(destIPTA);
    this.add(destPortTA);
    this.add(actionTA);
    
    this.add(jsbLog);
    
    this.add(exitButton);
    this.add(validateButton);
  }

  public void newRule(IPRule rule) {
    this.rule = rule;
    this.setTitle("New rule");
    
    newRule = true;
    saved = false;
    
    numberTA.setText("");
    protocolTA.setSelectedIndex(0);
    sourceIPTA.setText("");
    sourcePortTA.setText("");
    destIPTA.setText("");
    destPortTA.setText("");
    actionTA.setSelectedIndex(0);
  }
  
  public void setRule(IPRule rule) {
    this.rule = rule;
    
    this.setTitle("Rule " + rule.getNumber());
    
    newRule = false;
    saved = null;
    
    numberTA.setText(new Integer(rule.getNumber()).toString());
    protocolTA.setSelectedItem(rule.getProtocol());
    
    sourceIPTA.setText(rule.getSrcAddrStr());
    sourcePortTA.setText(rule.getSrcPortStr());
    destIPTA.setText(rule.getDestAddrStr());
    destPortTA.setText(rule.getDestPortStr());
    actionTA.setSelectedItem(rule.getRuleAction());
  }
}