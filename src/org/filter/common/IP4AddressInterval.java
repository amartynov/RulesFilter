package org.filter.common;

import java.util.ArrayList;

import org.filter.dto.LineProjection;
import org.filter.utils.Utils;

public class IP4AddressInterval {
  
  public static final long maxIpAddress = 0xffffffffL;
  
  private Long laddr;
  private Long raddr;
  
  public IP4AddressInterval(Long l, Long r) {
    laddr = l;
    raddr = r;
  }
  
  public IP4AddressInterval(String addr) {
    String[] buf = addr.split("-");
    if(buf.length != 2) {
      buf = addr.split("/");
      long mask = Utils.strToLong(buf[1]);
      laddr = Utils.strToLong(buf[0]);
      if(mask != 0xffffffffL) {
        mask ^= 0xffffffffL;
        raddr = laddr | mask;
      }
    } else {
      laddr = Utils.strToLong(buf[0]);
      raddr = Utils.strToLong(buf[1]);
    }
  }
  
  public Long getLaddr() {
    return laddr;
  }

  public void setLaddr(Long laddr) {
    this.laddr = laddr;
  }

  public Long getRaddr() {
    return raddr;
  }

  public void setRaddr(Long raddr) {
    this.raddr = raddr;
  }

  public LineProjection getLineProjection(){
    if(laddr == null) {
      return null;
    }
    double start = laddr;
    start /= maxIpAddress;
    double length = 0;
    if(raddr == null) {
      length = 1. / maxIpAddress;
    } else {
      length = ((double)(raddr - laddr)) / maxIpAddress; 
    }
    return new LineProjection(start, length);
  }
  
  public static ArrayList<IP4AddressInterval> intersection(ArrayList<IP4AddressInterval> list1, ArrayList<IP4AddressInterval> list2) {
    ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();
    for(IP4AddressInterval ip1 : list1) {
      for(IP4AddressInterval ip2 : list2) {
        IP4AddressInterval buf = intersection(ip1, ip2);
        if(buf != null) res.add(buf);
      }
    }
    return res.isEmpty() ? null : res;
  }
  
  public static IP4AddressInterval intersection(IP4AddressInterval ip1, IP4AddressInterval ip2){
    IP4AddressInterval res = null;
    if(ip1.getRaddr() != null && ip2.getRaddr() != null) {
      ArrayList<Long> list = Utils.intesection(ip1.getLaddr(), ip1.getRaddr(), ip2.getLaddr(), ip2.getRaddr());
      if(list == null) return null;
      res = new IP4AddressInterval(list.get(0), list.get(1));
    } else if(ip1.getRaddr() != null) {
      Long p = Utils.intersection(ip1.getLaddr(), ip1.getRaddr(), ip2.getLaddr());
      if(p == null) return null;
      res = new IP4AddressInterval(p, null);
    } else if(ip2.getRaddr() != null) {
      Long p = Utils.intersection(ip2.getLaddr(), ip2.getRaddr(), ip1.getLaddr());
      if(p == null) return null;
      res = new IP4AddressInterval(p, null);
    } else if(ip1.getLaddr().equals(ip2.getLaddr())) {
      res = new IP4AddressInterval(ip1.getLaddr(), null);
    }
    return res;
  }
  
  public ArrayList<IP4AddressInterval> decomposit(IP4AddressInterval addr){
    ArrayList<IP4AddressInterval> res = new ArrayList<IP4AddressInterval>();

    if(this.getRaddr() == null && addr.getRaddr() == null) {
      res.add(new IP4AddressInterval(addr.getLaddr(), null));
    } else {
      if(!this.getLaddr().equals(addr.getLaddr())) res.add(new IP4AddressInterval(this.getLaddr(), addr.getLaddr() - 1));
      res.add(new IP4AddressInterval(addr.getLaddr(), addr.getRaddr()));
      if(addr.getRaddr() == null) {
        if(!this.getRaddr().equals(addr.getLaddr())) res.add(new IP4AddressInterval(addr.getLaddr() + 1, this.getRaddr()));
      } else {
        if(!this.getRaddr().equals(addr.getRaddr())) res.add(new IP4AddressInterval(addr.getRaddr() + 1, this.getRaddr()));
      }
    }
    return res;
  }
  
  public boolean equals(IP4AddressInterval address){
    if(address.getLaddr() == null) return false;
    if(!address.getLaddr().equals(this.getLaddr())) return false;
    if(address.getRaddr() == null && this.getRaddr() == null) return true;
    else if(address.getRaddr() != null && this.getRaddr() != null) {
      if(address.getRaddr().equals(this.getRaddr())) {
        return true;
      }
    }
    return false;
  }
  
  public String toString(){
    StringBuilder la = new StringBuilder();
    StringBuilder ra = new StringBuilder();
    
    long lbuf = this.laddr.longValue();
    
    
    if(this.raddr != null){
      la.append(getByte(this.laddr, 0));
      ra.append(getByte(this.raddr, 0));
      for(int i = 1; i < 4; i++){
        la.append(".").append(getByte(this.laddr, i));
        ra.append(".").append(getByte(this.raddr, i));
      }
      la.append("-").append(ra.toString());
      
      /*long rbuf = this.raddr.longValue();
      long mask = 1;
      for(int i = 0; i < 32; i++){
        if((lbuf & mask) != 0) break;
        mask <<= 1;
        mask++;        
      }
      mask >>= 1;
      long buf = mask & rbuf;
      if(buf == mask) {
        mask = ~mask;
        la.append(getByte(this.laddr, 0));
        ra.append(getByte(mask, 0));
        for(int i = 1; i < 4; i++){
          la.append(".").append(getByte(this.laddr, i));
          ra.append(".").append(getByte(mask, i));
        }
        la.append("/").append(ra.toString());
      } else {
        la.append(getByte(this.laddr, 0));
        ra.append(getByte(this.raddr, 0));
        for(int i = 1; i < 4; i++){
          la.append(".").append(getByte(this.laddr, i));
          ra.append(".").append(getByte(this.raddr, i));
        }
        la.append("-").append(ra.toString());
      }*/
    } else {
      la.append(getByte(this.laddr, 0));
      for(int i = 1; i < 4; i++){
        la.append(".").append(getByte(this.laddr, i));
      }
      la.append("/255.255.255.255");
    }
    return la.toString();
  }
  
  private int getByte(long val, int i){
    long m = 0xff000000L;
    m >>= 8 * i;
    long buf = val & m;
    buf >>= 8 * (3 - i);
    return (int)buf;
  }
  
  public boolean sum(IP4AddressInterval addr) {
    return true;
  }
  
  public IP4AddressInterval sub(IP4AddressInterval rule) {
    
    return null;
  }
  
}
