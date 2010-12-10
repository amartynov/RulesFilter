package org.filter.dto;

import java.util.ArrayList;

import org.filter.GraphicAxises;
import org.filter.Protocol;
import org.filter.RuleAction;
import org.filter.common.Activity;
import org.filter.common.IP4AddressInterval;
import org.filter.common.InetPort;

public class IPRule extends Rule {
	//ip:12:accept:log:0:1:0:tcp:195.208.113.132/255.255.255.255:any:77.88.21.0/255.255.255.0:http:any:any:any:65535:0-255:any:active:yandex
	
	public static String log;
	public static Rule globalIpRule = null;
	public static ArrayList<String> list1 = new ArrayList<String>();
	public static ArrayList<String> list2 = new ArrayList<String>();
	
	private String in;
	private String out;
	private String timeInterval;
	private Protocol protocol;
	private ArrayList<IP4AddressInterval> srcAddress;
	private ArrayList<InetPort> srcPort;
	private ArrayList<IP4AddressInterval> destAddress;
	private ArrayList<InetPort> destPort;
	
	/*
	//TODO: старшинство?
	private String priority;
	private String flags_TOS;
	private String fragmentation;
	private String length;
	private String ttl;
	private String icmp_code;
	*/
	private String infoBuf;
	private Activity activity;
	private String comment;
	
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
	
	private static ArrayList<InetPort> strToPort(String portStr) {
		ArrayList<InetPort> portsArray = new ArrayList<InetPort>();
		String[] ports = portStr.split(",");
		for (String p : ports){
			if(p.equals("any")){
				portsArray.add(new InetPort(0, InetPort.maxPort));
			} else {
				String[] val = p.split("-");
				if(val.length == 1){
					Integer n = null;
					InetPort iPort = null;
					try{
						n = Integer.parseInt(val[0]);
						iPort = new InetPort(n);
					} catch (Exception e) {
						iPort = new InetPort(val[0]);
						if(iPort.getPort() == null) {
							log = "unknown port label: " + p;
							return null;
						}
					}
					portsArray.add(iPort);
				} else if(val.length == 2) {
					Integer min = Integer.parseInt(val[0]);
					Integer max = Integer.parseInt(val[1]);
					portsArray.add(new InetPort(min, max));
				}
			}
		}
		return portsArray;
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
		case ICMP:
			//TODO:
		  list2.add(str);
			return null;
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
			rule.setSrcAddress(strToAddress(params[8]));
			rule.setSrcPort(strToPort(params[9]));
			rule.setDestAddress(strToAddress(params[10]));
			rule.setDestPort(strToPort(params[11]));
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
			rule.setSrcAddress(strToAddress(params[8]));
			rule.setSrcPort(strToPort(params[9]));
			rule.setDestAddress(strToAddress(params[10]));
			rule.setDestPort(strToPort(params[11]));
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
	
	private static ArrayList<IP4AddressInterval> strToAddress(String str) {
		ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();
		String[] buf = str.split(",");
		for(String addr : buf){
		  res.add(new IP4AddressInterval(addr));
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
	  case ICMP:
	    //sb.append(AddressListToString(getDestAddress())).append(":");
	    break;
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
	  rule.setDestAddress(this.getDestAddress());
	  rule.setDestPort(this.getDestPort());
	  rule.setLabel(this.getLabel());
	  rule.setNumber(this.getNumber());
	  rule.setProtocol(this.getProtocol());
	  rule.setRuleAction(this.getRuleAction());
	  rule.setSrcAddress(this.getSrcAddress());
	  rule.setSrcPort(this.getSrcPort());	  
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

  public IPRule subRule(IPRule rule) {
    IPRule res = this.clone();
    /*
    res.setSrcAddress();
    res.setSrcPort();
    res.setDestAddress();
    res.setDestPort();
    res.setProtocol();
    ArrayList<IPRule> buf = this.decompositRule(rule);
    ArrayList<IPRule> toDel = new ArrayList<IPRule>();
    for(IPRule rbuf : buf) {
      if(rbuf.equals(rule)) {
        toDel.add(rbuf);
      }
    }
    buf.removeAll(toDel);*/
    
    return res;
  }
  
  private boolean addRule(IPRule rule) {
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
      addSrcAddr(rule.getSrcAddress());
      break;
    case destIP:
      addDestAddr(rule.getDestAddress());
      break;
    case srcPort:
      addSrcPort(rule.getSrcPort());
      break;
    case destPort:
      addDestPort(rule.getDestPort());
      break;
    }
    
    return true;
  }
  
  private void addDestPort(ArrayList<InetPort> destPort) {
    ArrayList<InetPort> buf = new ArrayList<InetPort>();
    for(InetPort p1 : this.getDestPort()) {
      for(InetPort p2 : destPort) {
        p1.add(p2);
      }
    }
    
  }

  private void addSrcPort(ArrayList<InetPort> srcPort2) {
    // TODO Auto-generated method stub
    
  }

  private void addDestAddr(ArrayList<IP4AddressInterval> destAddress2) {
    // TODO Auto-generated method stub
    
  }

  private void addSrcAddr(ArrayList<IP4AddressInterval> srcAddress2) {
    // TODO Auto-generated method stub
    
  }

  public ArrayList<IPRule> decompositRule(IPRule inter){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    int number = 1;
    for(InetPort srcPort : getRulesBySrcPort(inter)) {
      for(InetPort destPort : getRulesByDestPort(inter)) {
        for(IP4AddressInterval srcAddr : getRulesBySrcAddress(inter)) {
          for(IP4AddressInterval destAddr : getRulesByDestAddress(inter)) {
            for(Protocol protocol : getRulesByProtocol(inter)) {
//              IPRule rule = new IPRule();
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

  public boolean decideAnomaly(IPRule rule, IPRuleIntersection inter) {
    if(this.equals(inter)) {
      this.setSrcAddress(rule.getSrcAddress());
      this.setSrcPort(rule.getSrcPort());
      this.setDestAddress(rule.getDestAddress());
      this.setDestPort(rule.getDestPort());
      return true;
    }
    if(rule.equals(inter)) {
      return true;
    }
    
    
    return false;
  }
  
  private int notNullItems() {
    int res = 0;
    if(srcAddress != null) res++;
    if(srcPort != null) res++;
    if(destAddress != null) res++;
    if(destPort != null) res++;
    if(protocol != null) res++;
    return res;
  }
  
}
