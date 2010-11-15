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
        outputRulesList.addAll(decompositRuleByIntersection(rule));      
      }
    }
    ((CheckBoxPanel)outputPanel.getComponent(0)).setRules(outputRulesList);
    
  }
  
  private ArrayList<IPRule> decompositRuleByIntersection(IPRuleIntersection inter){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    
    ArrayList<IPRule> buf = new ArrayList<IPRule>();
    
    buf.addAll(decompositRule(inter, inter.getRule1()));
    buf.addAll(decompositRule(inter, inter.getRule2()));
    
    //TODO:
    return buf;
  }
  
  private ArrayList<IPRule> decompositRule(IPRuleIntersection inter, IPRule rule){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    int number = 1;
    for(InetPort srcPort : getRulesBySrcPort(rule, inter)) {
      for(InetPort destPort : getRulesByDestPort(rule, inter)) {
        for(IP4Address srcAddr : getRulesBySrcAddress(rule, inter)) {
          for(IP4Address destAddr : getRulesByDestAddress(rule, inter)) {
            for(Protocol protocol : getRulesByProtocol(rule, inter)) {
//              IPRule rule = new IPRule();
              IPRule ruleRes = rule.clone();
              ruleRes.setProtocol(protocol);
              ArrayList<IP4Address> dal = new ArrayList<IP4Address>();
              dal.add(destAddr);
              ruleRes.setDestAddress(dal);
              ArrayList<IP4Address> sal = new ArrayList<IP4Address>();
              sal.add(srcAddr);
              ruleRes.setSrcAddress(sal);
              ArrayList<InetPort> dpl = new ArrayList<InetPort>();
              dpl.add(destPort);
              ruleRes.setDestPort(dpl);
              ArrayList<InetPort> spl = new ArrayList<InetPort>();
              spl.add(srcPort);
              ruleRes.setSrcPort(spl);
              
              int n = Integer.parseInt((new Integer(rule.getNumber())).toString() + number++);
              ruleRes.setNumber(n);
//              ruleRes.setActivity(rule.getActivity());
//              ruleRes.setLabel(rule.getLabel());
              
              res.add(ruleRes);
            }
          }
        }
      }
    }
    
    return res;
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
