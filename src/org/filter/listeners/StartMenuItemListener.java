package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.filter.Protocol;
import org.filter.common.IP4Address;
import org.filter.common.InetPort;
import org.filter.dto.IPRule;
import org.filter.dto.IPRuleIntersection;
import org.filter.viewelements.CheckBoxPanel;

public class StartMenuItemListener implements ActionListener {
  
  private ArrayList<IPRule> outputRulesList;
  private ArrayList<IPRuleIntersection> intersection;
  private JPanel outputPanel;
  
  public StartMenuItemListener(ArrayList<IPRule> outputRulesList, ArrayList<IPRuleIntersection> intersection,JPanel outputPanel) {
    this.outputRulesList = outputRulesList;
    this.outputPanel = outputPanel;
    this.intersection = intersection;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    for(IPRuleIntersection rule : intersection){
      if(!rule.isEmpty()){
        outputRulesList.addAll(decompositRule(rule));      
      }
    }
    ((CheckBoxPanel)outputPanel.getComponent(0)).setRules(outputRulesList);
    
  }
  
  private ArrayList<IPRule> decompositRule(IPRuleIntersection inter){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    
    ArrayList<IPRule> buf = new ArrayList<IPRule>();
    
    for(InetPort srcPort : getRulesBySrcPort(inter.getRule1(), inter)) {
      for(InetPort destPort : getRulesByDestPort(inter.getRule1(), inter)) {
        for(IP4Address srcAddr : getRulesBySrcAddress(inter.getRule1(), inter)) {
          for(IP4Address destAddr : getRulesByDestAddress(inter.getRule1(), inter)) {
            for(Protocol protocol : getRulesByProtocol(inter.getRule1(), inter)) {
//              IPRule rule = new IPRule();
              IPRule rule = inter.getRule1().clone();
              rule.setProtocol(protocol);
              ArrayList<IP4Address> dal = new ArrayList<IP4Address>();
              dal.add(destAddr);
              rule.setDestAddress(dal);
              ArrayList<IP4Address> sal = new ArrayList<IP4Address>();
              sal.add(srcAddr);
              rule.setSrcAddress(sal);
              ArrayList<InetPort> dpl = new ArrayList<InetPort>();
              dpl.add(destPort);
              rule.setDestPort(dpl);
              ArrayList<InetPort> spl = new ArrayList<InetPort>();
              spl.add(srcPort);
              rule.setSrcPort(spl);
              
              rule.setActivity(inter.getRule1().getActivity());
              rule.setLabel(inter.getRule1().getLabel());
              rule.setNumber(inter.getRule1().getNumber());
              
              buf.add(rule);
            }
          }
        }
      }
    }

    /*
    buf.addAll(getRulesBySrcPort(inter.getRule2(), inter));
    
    buf.addAll(getRulesByDestPort(inter.getRule2(), inter));
    
    buf.addAll(getRulesBySrcAddress(inter.getRule2(), inter));
    
    buf.addAll(getRulesByDestAddress(inter.getRule2(), inter));
    
    buf.addAll(getRulesByProtocol(inter.getRule2(), inter));*/

    
    //TODO:
    return buf;
  }
  
  private ArrayList<InetPort> getRulesBySrcPort(IPRule rule, IPRuleIntersection inter){
    ArrayList<InetPort> res = new ArrayList<InetPort>();
    for(InetPort port : rule.getSrcPort()){
      for (InetPort interPort : inter.getSrcPort()) {
        res.addAll(port.decomposit(interPort));
      }
    }
    return res;
  }

  private ArrayList<InetPort> getRulesByDestPort(IPRule rule, IPRuleIntersection inter){
    ArrayList<InetPort> res = new ArrayList<InetPort>();
    for(InetPort port : rule.getDestPort()){
      for (InetPort interPort : inter.getDestPort()) {
        res.addAll(port.decomposit(interPort));
      }
    }
    return res;
  }
  
  private ArrayList<IP4Address> getRulesBySrcAddress(IPRule rule, IPRuleIntersection inter) {
    ArrayList<IP4Address> res = new ArrayList<IP4Address>();
    for(IP4Address ruleIpAddr : rule.getSrcAddress()){
      for(IP4Address interIpAddr : inter.getSrcAddress()){
        res.addAll(ruleIpAddr.decomposit(interIpAddr));
      }
    }
    return res;
  }
  
  private ArrayList<IP4Address> getRulesByDestAddress(IPRule rule, IPRuleIntersection inter) {
    ArrayList<IP4Address> res = new ArrayList<IP4Address>();
    for(IP4Address ruleIpAddr : rule.getDestAddress()){
      for(IP4Address interIpAddr : inter.getDestAddress()){
        res.addAll(ruleIpAddr.decomposit(interIpAddr));
      }
    }
    return res;
  }
  
  private ArrayList<Protocol> getRulesByProtocol(IPRule rule, IPRuleIntersection inter) {
    ArrayList<Protocol> res = new ArrayList<Protocol>();
    res.add(inter.getProtocol());
    return res;
  }
}
