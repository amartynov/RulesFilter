package org.filter.viewelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
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
import org.filter.exeption.FilterExeption;
import org.filter.listeners.IPRuleCBMouseListener;

public class EditRuleDialog extends JDialog {
  
  private static final long serialVersionUID = 5316532735580586765L;
  
  private IPRule rule;
  private JTextField numberTA;
  private JTextField protocolTA;
  private JTextField sourceIPTA;
  private JTextField sourcePortTA;
  private JTextField destIPTA;
  private JTextField destPortTA;
  private JTextField actionTA;
  
  private ArrayList<IPRuleJCheckBox> rulesJB;
  
  private boolean newRule = false;
  private Boolean saved = null;
  
  public EditRuleDialog(final ArrayList<IPRuleJCheckBox> rulesJB) {
    super();
    
    this.rulesJB = rulesJB;
    
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
    protocolTA = new JTextField();
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
    actionTA = new JTextField();
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
        if(saved != null) {
          if(!saved) {
            IPRuleCBMouseListener.panel.removeElement(rule);            
          }
        }
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
              for(IPRuleJCheckBox rulecb : rulesJB) {
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

        protocol = Protocol.getByLabel(protocolTA.getText());
        if(protocol == null) {
          logTA.append("Неверно указан протокол: " + protocolTA.getText() + "\n");
          fail = true;
        }
        
        /*if(!IP4AddressInterval.validate(sourceIPTA.getText())) {
          logTA.append("Неверно указан адрес источника: " + sourceIPTA.getText() + "\n");
          fail = true;
        }*/
        
        try {
          IP4AddressInterval inter = new IP4AddressInterval(destIPTA.getText());
          destAddress = new ArrayList<IP4AddressInterval>();
          destAddress.add(inter);
        } catch (FilterExeption exeption) {
          logTA.append("Неверно указан адрес приемника: " + destIPTA.getText() + "\n");
          fail = true;
        }
        
        try {
          IP4AddressInterval inter = new IP4AddressInterval(sourceIPTA.getText());
          srcAddress = new ArrayList<IP4AddressInterval>();
          srcAddress.add(inter);
        } catch (FilterExeption exeption) {
          logTA.append("Неверно указан адрес источника: " + sourceIPTA.getText() + "\n");
          fail = true;
        }
        
        if(!InetPort.validate(sourcePortTA.getText())){
          logTA.append("Неверно указан порт источника: " + sourcePortTA.getText() + "\n");
          fail = true;
        } else {
          srcPort = IPRule.strToPort(sourcePortTA.getText());
        }
        
        if(!InetPort.validate(destPortTA.getText())) {
          logTA.append("Неверно указан порт приемника: " + destPortTA.getText() + "\n");
          fail = true;
        } else {
          destPort = IPRule.strToPort(destPortTA.getText());
        }
        
        ruleAction = RuleAction.getByLabel(actionTA.getText()); 
        if(ruleAction == null) {
          logTA.append("Неверно указано действие: " + actionTA.getText() + "\n");
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
    protocolTA.setText("");
    sourceIPTA.setText("");
    sourcePortTA.setText("");
    destIPTA.setText("");
    destPortTA.setText("");
    actionTA.setText("");
  }
  
  public void setRule(IPRule rule) {
    this.rule = rule;
    
    this.setTitle("Rule " + rule.getNumber());
    
    newRule = false;
    saved = null;
    
    numberTA.setText(new Integer(rule.getNumber()).toString());
    protocolTA.setText(rule.getProtocol().getLabel());
    sourceIPTA.setText(rule.getSrcAddress().get(0).toString());
    sourcePortTA.setText(rule.getSrcPort().get(0).toString());
    destIPTA.setText(rule.getDestAddress().get(0).toString());
    destPortTA.setText(rule.getDestPort().get(0).toString());
    actionTA.setText(rule.getRuleAction().getLabel());
  }
}