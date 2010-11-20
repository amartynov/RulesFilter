package org.filter.dto;

import java.util.ArrayList;

import org.filter.Protocol;
import org.filter.RuleAction;
import org.filter.common.Activity;
import org.filter.common.IP4AddressInterval;
import org.filter.common.InetPort;

public class IPRule extends Rule {
	//ip:12:accept:log:0:1:0:tcp:195.208.113.132/255.255.255.255:any:77.88.21.0/255.255.255.0:http:any:any:any:65535:0-255:any:active:yandex
	
	public static String log;
	
	private String in;
	private String out;
	private String timeInterval;
	private Protocol protocol;
	private ArrayList<IP4AddressInterval> srcAddress;
	private ArrayList<InetPort> srcPort;
	private ArrayList<IP4AddressInterval> destAddress;
	private ArrayList<InetPort> destPort;
	
	//TODO: старшинство?
	private String priority;
	private String flags_TOS;
	private String fragmentation;
	private String length;
	private String ttl;
	private String icmp_code;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getFlags_TOS() {
		return flags_TOS;
	}

	public void setFlags_TOS(String flags_TOS) {
		this.flags_TOS = flags_TOS;
	}

	public String getFragmentation() {
		return fragmentation;
	}

	public void setFragmentation(String fragmentation) {
		this.fragmentation = fragmentation;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getIcmp_code() {
		return icmp_code;
	}

	public void setIcmp_code(String icmp_code) {
		this.icmp_code = icmp_code;
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
			return null;
		}
		IPRule rule = new IPRule();
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
			//TODO:
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
		switch(p){
		case ICMP:
			//TODO:
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
			rule.setPriority(params[12]);
			rule.setFlags_TOS(params[13]);
			rule.setFragmentation(params[14]);
			rule.setTtl(params[15]);
			rule.setIcmp_code(params[16]);
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
	  sb.append("ip:").append(getNumber()).append(":").append(getRuleAction().getLabel()).append(":").append(getRuleLog()).append(":");
	  sb.append(getIn()).append(":").append(getOut()).append(":").append(getTimeInterval()).append(":").append(getProtocol().getLabel()).append(":");
	  sb.append(AddressListToString(getSrcAddress())).append(":").append(PortListToString(getSrcPort())).append(":");
	  sb.append(AddressListToString(getDestAddress())).append(":").append(PortListToString(getDestPort())).append(":").append(getActivity());
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
	  rule.setFlags_TOS(rule.getFlags_TOS());
	  rule.setFragmentation(this.getFragmentation());
	  rule.setIcmp_code(this.getIcmp_code());
	  rule.setIn(this.getIn());
	  rule.setLength(this.getLength());
	  rule.setOut(this.getOut());
	  rule.setPriority(this.getPriority());
	  rule.setRuleLog(this.getRuleLog());
	  rule.setTimeInterval(this.getTimeInterval());
	  rule.setTtl(this.getTtl());
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

  public ArrayList<IPRule> sub(IPRule rule) {
    // TODO Auto-generated method stub
    return null;
  }
  
  private ArrayList<IPRule> decompositRule(IPRuleIntersection inter, IPRule rule){
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    int number = 1;
    for(InetPort srcPort : getRulesBySrcPort(rule, inter)) {
      for(InetPort destPort : getRulesByDestPort(rule, inter)) {
        for(IP4AddressInterval srcAddr : getRulesBySrcAddress(rule, inter)) {
          for(IP4AddressInterval destAddr : getRulesByDestAddress(rule, inter)) {
            for(Protocol protocol : getRulesByProtocol(rule, inter)) {
//              IPRule rule = new IPRule();
              IPRule ruleRes = rule.clone();
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
  
  private ArrayList<IP4AddressInterval> getRulesBySrcAddress(IPRule rule, IPRuleIntersection inter) {
    ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();
    for(IP4AddressInterval ruleIpAddr : rule.getSrcAddress()){
      for(IP4AddressInterval interIpAddr : inter.getSrcAddress()){
        res.addAll(ruleIpAddr.decomposit(interIpAddr));
      }
    }
    return res;
  }
  
  private ArrayList<IP4AddressInterval> getRulesByDestAddress(IPRule rule, IPRuleIntersection inter) {
    ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();
    for(IP4AddressInterval ruleIpAddr : rule.getDestAddress()){
      for(IP4AddressInterval interIpAddr : inter.getDestAddress()){
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
