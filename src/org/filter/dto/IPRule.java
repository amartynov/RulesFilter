package org.filter.dto;

import java.util.ArrayList;

import org.filter.GraphicAxises;
import org.filter.Protocol;
import org.filter.RuleAction;
import org.filter.common.Activity;
import org.filter.common.IP4AddressInterval;
import org.filter.common.InetPort;
import org.filter.exeption.FilterException;

public class IPRule extends Rule {
	//ip:12:accept:log:0:1:0:tcp:195.208.113.132/255.255.255.255:any:77.88.21.0/255.255.255.0:http:any:any:any:65535:0-255:any:active:yandex
	
	public static String log;
	public static Rule globalIpRule = null;
	public static ArrayList<String> list1 = new ArrayList<String>();
	public static ArrayList<String> list2 = new ArrayList<String>();
	
	private String in = "0";
	private String out = "1";
	private String timeInterval = "0";
	private Protocol protocol;
	private ArrayList<IP4AddressInterval> srcAddress = new ArrayList<IP4AddressInterval>();
	private ArrayList<InetPort> srcPort = new ArrayList<InetPort>();
	private ArrayList<IP4AddressInterval> destAddress = new ArrayList<IP4AddressInterval>();
	private ArrayList<InetPort> destPort = new ArrayList<InetPort>();
	
	/*
	private String priority;
	private String flags_TOS;
	private String fragmentation;
	private String length;
	private String ttl;
	private String icmp_code;
	*/
	private String infoBuf = "any:any:any:65535:0-255";
	private Activity activity = Activity.active;
	private String comment = "";
	
	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public String getInfoBuf() {
    return infoBuf;
  }

  public void setInfoBuf(String infoBuf) {
    this.infoBuf = infoBuf;
  }

  public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ArrayList<IP4AddressInterval> getSrcAddress() {
    return srcAddress;
  }

  public void setSrcAddress(ArrayList<IP4AddressInterval> srcAddress) {
    this.srcAddress = srcAddress;
  }

  public void setDestAddress(ArrayList<IP4AddressInterval> destAddress) {
    this.destAddress = destAddress;
  }
  
  public ArrayList<IP4AddressInterval> getDestAddress() {
    return destAddress;
  }

  public ArrayList<InetPort> getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(ArrayList<InetPort> srcPort) {
		this.srcPort = srcPort;
	}
	
	public ArrayList<InetPort> getDestPort() {
		return destPort;
	}

	public void setDestPort(ArrayList<InetPort> destPort) {
		this.destPort = destPort;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	public String getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

	public static IPRule createRule(String str) {
		String[] params = str.split(":");
		if(!params[0].equals("ip")){
			log = params[0] + " rule skiped";
			if(globalIpRule == null) 	list1.add(str);
			else list2.add(str);
			return null;
		}
		IPRule rule = new IPRule();
		rule.setLabel(params[0]);
		rule.setNumber(Integer.parseInt(params[1]));
		RuleAction ra = RuleAction.getByLabel(params[2]);
		if(ra == null){
			log = rule.getMessage() + ": invalid action " + params[2];
			return null;
		}
		rule.setRuleAction(ra);
		rule.setRuleLog(params[3]);
		
		if(params[1].equals("0")) {
			if(params.length > 4) {
				log = "Invalid global ip rule";
				return null;
			}
			globalIpRule = new Rule();
			globalIpRule.setLabel(rule.getLabel());
			globalIpRule.setNumber(rule.getNumber());
			globalIpRule.setRuleAction(rule.getRuleAction());
			globalIpRule.setRuleLog(rule.getRuleLog());
			return null;
//			return rule;
		}
		
		rule.setIn(params[4]);
		rule.setOut(params[5]);
		rule.setTimeInterval(params[6]);
		Protocol p = Protocol.getByLabel(params[7]);
		if(p == null){
			log = rule.getMessage() + ": invalid protocol " + params[7];
			return null;
		}
		rule.setProtocol(p);
		Activity a;
		StringBuilder sb = new StringBuilder();
		switch(p){
//		case ICMP:
//		  list2.add(str);
//			return null;
			/*rule.setSrcAddress(strToAddress(params[8]));
//			rule.setSrcPort(strToPort(params[9]));
			rule.setDestAddress(strToAddress(params[9]));
//			rule.setDestPort(strToPort(params[11]));
			rule.setPriority(params[12]);
			rule.setFlags_TOS(params[13]);
			rule.setFragmentation(params[14]);
			rule.setTtl(params[15]);
			rule.setIcmp_code(params[16]);
			a = Activity.getByLabel(params[17]);
			if(a == null){
				log = rule.getMessage() + ": invalid activity " + params[17];
				return null;
			}
			rule.setActivity(a);
			rule.setComment(params[18]);
			break;*/
		case TCP:
		  try {
        rule.setSrcAddress(IP4AddressInterval.strToAddress(params[8]));
      } catch (FilterException e) {
        log = rule.getMessage() + ": invalid source address";
        return null;
      }
			try {
        rule.setSrcPort(InetPort.strToPort(params[9]));
      } catch (FilterException e) {
        log = rule.getMessage() + " source port: " + e.getMes();
        return null;
      }
			try {
        rule.setDestAddress(IP4AddressInterval.strToAddress(params[10]));
      } catch (FilterException e) {
        log = rule.getMessage() + ": invalid destin address";
        return null;
      }
			try {
        rule.setDestPort(InetPort.strToPort(params[11]));
      } catch (FilterException e) {
        log = rule.getMessage() + " destin port: " + e.getMes();
        return null;
      }
			/*
			rule.setPriority(params[12]);
			rule.setFlags_TOS(params[13]);
			rule.setFragmentation(params[14]);
			rule.setTtl(params[15]);
			rule.setIcmp_code(params[16]);*/
			for(int i = 12; i < 17; i++){
			  sb.append(params[i]).append(":");
			}
			sb.append(params[17]);
			rule.setInfoBuf(sb.toString());
			a = Activity.getByLabel(params[18]);
			if(a == null){
				log = rule.getMessage() + ": invalid activity " + params[18];
				return null;
			}
			rule.setActivity(a);
			rule.setComment(params[19]);
			break;
		case UDP:
		  try {
		    rule.setSrcAddress(IP4AddressInterval.strToAddress(params[8]));		    
		  } catch (FilterException e) {
        log = rule.getMessage() + " source address: " + e.getMes();
        return null;
      }
		  try {
		    rule.setSrcPort(InetPort.strToPort(params[9]));		    
		  } catch (FilterException e) {
        log = rule.getMessage() + " source port: " + e.getMes();
        return null;
      }
			try {
			  rule.setDestAddress(IP4AddressInterval.strToAddress(params[10]));
      } catch (FilterException e) {
        log = rule.getMessage() + ": invalid destin address";
        return null;
      }
			try {
        rule.setDestPort(InetPort.strToPort(params[11]));
      } catch (FilterException e) {
        log = rule.getMessage() + " destin port: " + e.getMes();
        return null;
      }
			/*
			rule.setPriority(params[12]);
			rule.setFlags_TOS(params[13]);
			rule.setFragmentation(params[14]);
			rule.setTtl(params[15]);
			rule.setIcmp_code(params[16]); */
      for(int i = 12; i < 16; i++){
        sb.append(params[i]).append(":");
      }
      sb.append(params[16]);
      rule.setInfoBuf(sb.toString());
			a = Activity.getByLabel(params[17]);
			if(a == null){
				log = rule.getMessage() + ": invalid activity " + params[17];
				return null;
			}
			rule.setActivity(a);
			rule.setComment(params[18]);
			break;
		}
		
		return rule;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<LineProjection> getSrcPortLineList(){
		return getPortLineList(srcPort);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<LineProjection> getDestPortLineList(){
		return getPortLineList(destPort);
	}
	
	private ArrayList<LineProjection> getPortLineList(ArrayList<InetPort> list) {
	  if(list == null) return null;
		ArrayList<LineProjection> res = new ArrayList<LineProjection>();
		for(InetPort port : list) {
			//TODO: icmp rule do not have a port
			res.add(port.getLineProjection());
		}
		return res;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<LineProjection> getSrcAddrLineList(){
	  return getAddrLineList(srcAddress);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<LineProjection> getDestAddrLineList(){
    return getAddrLineList(destAddress);
  }
	
	private ArrayList<LineProjection> getAddrLineList(ArrayList<IP4AddressInterval> list){
	  if(list == null) return null;
	  ArrayList<LineProjection> res = new ArrayList<LineProjection>();
    for(IP4AddressInterval addr : list) {
      LineProjection pr = addr.getLineProjection();
      if(pr != null) res.add(pr);
    }
    return res;
	}
	
	public ArrayList<LineProjection> getProtocolLineList(){
	  if(protocol == null) return null;
	  ArrayList<LineProjection> res = new ArrayList<LineProjection>();
	  res.add(protocol.getLineProjection());
	  return res;
	}
	
	public String toString(){
	  StringBuilder sb = new StringBuilder();
	  sb.append(super.toString());
	  sb.append(":").append(getIn()).append(":").append(getOut()).append(":").append(getTimeInterval()).append(":");
	  Protocol protocol = getProtocol();
	  sb.append(protocol.getLabel()).append(":");
	  sb.append(AddressListToString(getSrcAddress())).append(":");
	  switch(protocol) {
	  case TCP:
	    sb.append(PortListToString(getSrcPort())).append(":");
	    sb.append(AddressListToString(getDestAddress())).append(":").append(PortListToString(getDestPort())).append(":");
	    sb.append(getInfoBuf()).append(":").append(getActivity()).append(":").append(getComment());
	    break;
	  case UDP:
	    sb.append(PortListToString(getSrcPort())).append(":");
	    sb.append(AddressListToString(getDestAddress())).append(":").append(PortListToString(getDestPort())).append(":");
	    sb.append(getInfoBuf()).append(":").append(getActivity()).append(":").append(getComment());
	    break;
	  //case ICMP:
	    //sb.append(AddressListToString(getDestAddress())).append(":");
	   // break;
	  }
	  return sb.toString();
	}
	
	private String AddressListToString(ArrayList<IP4AddressInterval> list) {
	  StringBuilder sb = new StringBuilder();
    int len = list.size();
    if(len != 0) {
      sb.append(list.get(0).toString());       
      for(int i = 1; i < len; i++) {
        sb.append(",").append(list.get(i).toString());
      }
    }
    return sb.toString();
	}
	
	private String PortListToString(ArrayList<InetPort> list) {
    StringBuilder sb = new StringBuilder();
    int len = list.size();
    if(len != 0) {
      sb.append(list.get(0).toString());       
      for(int i = 1; i < len; i++) {
        sb.append(",").append(list.get(i).toString());
      }
    }
    return sb.toString();
  }
	
	public IPRuleIntersection intersection(IPRule rule){
	  IPRuleIntersection res = new IPRuleIntersection();
	  res.setRule1(this);
	  res.setRule2(rule);
	  res.setSrcAddress(IP4AddressInterval.intersection(this.srcAddress, rule.getSrcAddress()));
	  res.setDestAddress(IP4AddressInterval.intersection(this.destAddress, rule.getDestAddress()));
	  res.setSrcPort(InetPort.intersection(this.srcPort, rule.getSrcPort()));
	  res.setDestPort(InetPort.intersection(this.destPort, rule.getDestPort()));
	  res.setProtocol(this.protocol == rule.getProtocol() ? this.protocol : null);
	  
	  if(res.getProtocol() == null &&
	      res.getSrcAddress() == null &&
	      res.getSrcPort() == null &&
	      res.getDestAddress() == null &&
	      res.getDestPort() == null) return null;
	  return res;
	}
	
	
	/**
	 * Returns true if one of the mandatory fields is null 
	 * @return
	 * true if one of the mandatory fields is null
	 */
	public boolean isEmpty(){
	  if(this.destAddress == null) return true;
	  if(this.srcAddress == null) return true;
	  if(this.destPort == null) return true;
	  if(this.srcPort == null) return true;
	  if(this.protocol == null) return true;
	  return false;
	}
	
	public IPRule clone(){
	  IPRule rule = new IPRule();
	  rule.setActivity(this.getActivity());
	  ArrayList<IP4AddressInterval> newDestAddr = new ArrayList<IP4AddressInterval>();
	  for(IP4AddressInterval addr : this.getDestAddress()) {
	    newDestAddr.add(addr.clone());
	  }
	  rule.setDestAddress(newDestAddr);
	  ArrayList<InetPort> newDestPort = new ArrayList<InetPort>();
	  for(InetPort port : this.getDestPort()) {
	    newDestPort.add(port.clone());
	  }
	  rule.setDestPort(newDestPort);
	  rule.setLabel(this.getLabel());
	  rule.setNumber(this.getNumber());
	  rule.setProtocol(this.getProtocol());
	  rule.setRuleAction(this.getRuleAction());
	  ArrayList<IP4AddressInterval> newSrcAddr = new ArrayList<IP4AddressInterval>();
	  for(IP4AddressInterval addr : this.getSrcAddress()) {
      newSrcAddr.add(addr.clone());
    }
	  rule.setSrcAddress(newSrcAddr);
	  ArrayList<InetPort> newSrcPort = new ArrayList<InetPort>();
    for(InetPort port : this.getSrcPort()) {
      newSrcPort.add(port.clone());
    }
	  rule.setSrcPort(newSrcPort);	  
	  rule.setComment(this.getComment());
	  /*
	  rule.setFlags_TOS(rule.getFlags_TOS());
	  rule.setFragmentation(this.getFragmentation());
	  rule.setIcmp_code(this.getIcmp_code());
	  rule.setLength(this.getLength());
	  rule.setPriority(this.getPriority());
	  rule.setTtl(this.getTtl());*/
	  rule.setInfoBuf(this.getInfoBuf());
	  rule.setIn(this.getIn());
	  rule.setOut(this.getOut());
	  rule.setRuleLog(this.getRuleLog());
	  rule.setTimeInterval(this.getTimeInterval());
	  return rule;
	}
	
	public boolean equals(IPRule rule){
	  if(protocol != rule.getProtocol() ||
	     !AddressEquals(destAddress, rule.getDestAddress()) ||
	     !AddressEquals(srcAddress, rule.getSrcAddress()) ||
	     !PortEquals(destPort, rule.getDestPort()) ||
	     !PortEquals(srcPort, rule.getSrcPort())){
	    return false;
	  }
	  return true;
	}
	
	private boolean AddressEquals(ArrayList<IP4AddressInterval> addr1, ArrayList<IP4AddressInterval> addr2) {
	  boolean flag = false;
	  for(IP4AddressInterval interval1 : addr1) {
	    for(IP4AddressInterval interval2 : addr2) {
	      if(interval1.equals(interval2)){
	        flag = true;
	      }
	    }
	    if(flag) {
	      flag = false;
	    } else {
	      return false;
	    }
	  }
	  return true;
	}
	
	private boolean PortEquals(ArrayList<InetPort> port1, ArrayList<InetPort> port2) {
    boolean flag = false;
    for(InetPort p1 : port1) {
      for(InetPort p2 : port2) {
        if(p1.equals(p2)){
          flag = true;
        }
      }
      if(flag) {
        flag = false;
      } else {
        return false;
      }
    }
    return true;
  }
  
  public boolean addRule(IPRule rule) {
    if(!this.getProtocol().equals(rule.getProtocol())) return false;
    int noteq = 0;
    GraphicAxises flag = GraphicAxises.srcIP;
    if(!AddressEquals(this.getSrcAddress(), rule.getSrcAddress())) {
      noteq++;
    } 
    
    if(!AddressEquals(this.getDestAddress(), rule.getDestAddress())) {
      noteq++;
      flag = GraphicAxises.destIP;
    } 
    
    if(!PortEquals(this.getSrcPort(), rule.getSrcPort())) {
      noteq++;
      flag = GraphicAxises.srcPort;
    } 
    
    if(!PortEquals(this.getDestPort(), rule.getDestPort())) {
      noteq++;
      flag = GraphicAxises.destPort;
    } 
    
    if(noteq != 1) return false;
    switch(flag) {
    case srcIP:
      return addSrcAddr(rule.getSrcAddress());
    case destIP:
      return addDestAddr(rule.getDestAddress());
    case srcPort:
      return addSrcPort(rule.getSrcPort());
    case destPort:
      return addDestPort(rule.getDestPort());
    }
    return false;
  }
  
  private boolean addDestPort(ArrayList<InetPort> destPort) {
    /*ArrayList<InetPort> buf = new ArrayList<InetPort>();
    for(InetPort p1 : this.getDestPort()) {
      for(InetPort p2 : destPort) {
        p1.add(p2);
      }
    }*/
    return false;
  }

  private boolean addSrcPort(ArrayList<InetPort> srcPort2) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean addDestAddr(ArrayList<IP4AddressInterval> destAddress) {
    ArrayList<IP4AddressInterval> toInsert = new ArrayList<IP4AddressInterval>();
    ADDR:
    for(IP4AddressInterval ip1 : this.destAddress) {
      if(destAddress.size() == 0) break;
      for(IP4AddressInterval ip2 : destAddress) {
        if(!ip1.add(ip2)) {
          toInsert.add(ip2);
        } 
        destAddress.remove(ip2);
        continue ADDR;
      }
    }
    this.destAddress.addAll(toInsert);
    return true;
  }

  private boolean addSrcAddr(ArrayList<IP4AddressInterval> srcAddress) {
    ArrayList<IP4AddressInterval> toInsert = new ArrayList<IP4AddressInterval>();
    ADDR:
    for(IP4AddressInterval ip1 : this.srcAddress) {
      if(srcAddress.size() == 0) break;
      for(IP4AddressInterval ip2 : srcAddress) {
        if(!ip1.add(ip2)) {
          toInsert.add(ip2);
        } 
        srcAddress.remove(ip2);
        continue ADDR;
      }
    }
    this.srcAddress.addAll(toInsert);
    return true;
  }

  public ArrayList<IPRule> decompositRule(IPRule inter){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    int number = 1;
    for(InetPort srcPort : getRulesBySrcPort(inter)) {
      for(InetPort destPort : getRulesByDestPort(inter)) {
        for(IP4AddressInterval srcAddr : getRulesBySrcAddress(inter)) {
          for(IP4AddressInterval destAddr : getRulesByDestAddress(inter)) {
            for(Protocol protocol : getRulesByProtocol(inter)) {
              IPRule ruleRes = this.clone();
              ruleRes.setProtocol(protocol);
              ArrayList<IP4AddressInterval> dal = new ArrayList<IP4AddressInterval>();
              dal.add(destAddr);
              ruleRes.setDestAddress(dal);
              ArrayList<IP4AddressInterval> sal = new ArrayList<IP4AddressInterval>();
              sal.add(srcAddr);
              ruleRes.setSrcAddress(sal);
              ArrayList<InetPort> dpl = new ArrayList<InetPort>();
              dpl.add(destPort);
              ruleRes.setDestPort(dpl);
              ArrayList<InetPort> spl = new ArrayList<InetPort>();
              spl.add(srcPort);
              ruleRes.setSrcPort(spl);
              
              int n = Integer.parseInt((new Integer(this.getNumber())).toString() + number++);
              ruleRes.setNumber(n);
              res.add(ruleRes);
            }
          }
        }
      }
    }
    
    return res;
  }
  
  private ArrayList<InetPort> getRulesBySrcPort(IPRule inter){
    ArrayList<InetPort> res = new ArrayList<InetPort>();
    for(InetPort port : this.getSrcPort()){
      for (InetPort interPort : inter.getSrcPort()) {
        res.addAll(port.decomposit(interPort));
      }
    }
    return res;
  }

  private ArrayList<InetPort> getRulesByDestPort(IPRule inter){
    ArrayList<InetPort> res = new ArrayList<InetPort>();
    for(InetPort port : this.getDestPort()){
      for (InetPort interPort : inter.getDestPort()) {
        res.addAll(port.decomposit(interPort));
      }
    }
    return res;
  }
  
  private ArrayList<IP4AddressInterval> getRulesBySrcAddress(IPRule inter) {
    ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();
    for(IP4AddressInterval ruleIpAddr : this.getSrcAddress()){
      for(IP4AddressInterval interIpAddr : inter.getSrcAddress()){
        res.addAll(ruleIpAddr.decomposit(interIpAddr));
      }
    }
    return res;
  }
  
  private ArrayList<IP4AddressInterval> getRulesByDestAddress(IPRule inter) {
    ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();
    for(IP4AddressInterval ruleIpAddr : this.getDestAddress()){
      for(IP4AddressInterval interIpAddr : inter.getDestAddress()){
        res.addAll(ruleIpAddr.decomposit(interIpAddr));
      }
    }
    return res;
  }
  
  private ArrayList<Protocol> getRulesByProtocol(IPRule inter) {
    ArrayList<Protocol> res = new ArrayList<Protocol>();
    res.add(inter.getProtocol());
    return res;
  }
  
  public String getSrcAddrStr() {
    return getAddrStr(srcAddress);
  }
  
  public String getDestAddrStr() {
    return getAddrStr(destAddress);
  }
  
  private String getAddrStr(ArrayList<IP4AddressInterval> addr) {
    String res = addr.get(0).toString();
    int i;
    for(i = 1; i < addr.size(); i++) {
      res += "," + addr.get(i).toString();
    }
    return res;
    
  }
  
  public String getSrcPortStr() {
    return getPortStr(srcPort);
  }
  
  public String getDestPortStr() {
    return getPortStr(destPort);
  }
  
  private String getPortStr(ArrayList<InetPort> port) {
    String res = port.get(0).toString();
    int i;
    for(i = 1; i < port.size(); i++) {
      res += "," + port.get(i).toString();
    }
    return res;
    
  }

}