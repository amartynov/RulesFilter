package org.filter.common;

import java.util.ArrayList;

import org.filter.dto.LineProjection;


public class IP4Address {
  
//  private static final IP4Address maxIpAddress = new IP4Address("255.255.255.255/255.255.255.255");
  private static final long maxIpAddress = 0xffffffffL;
	
//	private byte[] addr;
//	private byte[] mask;
  
  private long addr;
  private long mask;
	
	public IP4Address() {};
	
	public IP4Address(String addr) {
		String[] buf = addr.split("/");
		if(buf.length != 2) {
		  this.addr = 0;
		  mask = 0;
			return;
		}
		String a = buf[0];
		String m = buf[1];
//		this.addr = new byte[4];
//		this.mask = new byte[4];
		a = a.replace(".", "/");
		m = m.replace(".", "/");
		String[] abuf = a.split("/");
		String[] mbuf = m.split("/");
		this.addr = Integer.parseInt(abuf[0]); 
		this.mask = Integer.parseInt(mbuf[0]); 
		for(int i = 1; i < 4; i++) {
//			this.addr[i] = (byte)Integer.parseInt(abuf[i]);
//			this.mask[i] = (byte)Integer.parseInt(mbuf[i]);
		  this.addr <<= 8;
		  this.addr |= Integer.parseInt(abuf[i]);
		  this.mask <<= 8;
		  this.mask |= Integer.parseInt(mbuf[i]);
		}
	};
	
	public long getAddr() {
    return addr;
  }

  public void setAddr(long addr) {
    this.addr = addr;
  }

  public long getMask() {
    return mask;
  }

  public void setMask(long mask) {
    this.mask = mask;
  }

  public String toString(){
	  StringBuilder sb = new StringBuilder();
	  StringBuilder mask = new StringBuilder();
/*	  sb.append(addr[0]);
	  mask.append(this.mask[0]);
	  for(int i = 1; i < 4; i++){
	    sb.append(".").append(addr[i]);
	    mask.append(".").append(this.mask[i]);
	  }
	  sb.append("/").append(mask.toString());*/
	  return sb.toString();
	}
	
	public LineProjection getLineProjection(){
	  long m = mask;
	  long a = addr;
//	  long max = toLong(maxIpAddress.addr);
	  double start = a & m;
	  double length = 0;
	  start /= maxIpAddress;
	  if(m == 0xffffffffL){
	    length = 1. / maxIpAddress;
	  } else {
	    m ^= 0xffffffffL;
	    length = a | m;
	    length = length / maxIpAddress - start;
	  }
//	  start = Math.log10(start);
//	  length = Math.log10(length / maxIpAddress);
	  return new LineProjection(start, length);
	}
	
	private long toLong(byte[] b){
	  long res = b[0];
	  for(int i = 1; i < 4; i++){
	    res <<= 8;
	    res |= b[i];
	  }	  
	  return res;
	}
	
	public static ArrayList<IP4Address> intersection(ArrayList<IP4Address> list1, ArrayList<IP4Address> list2) {
	  ArrayList<IP4Address> res = new ArrayList<IP4Address>();
	  for(IP4Address ip1 : list1) {
	    for(IP4Address ip2 : list2) {
	      IP4Address buf = intersection(ip1, ip2);
	      if(buf != null) res.add(buf);
	    }
	  }
	  return res.isEmpty() ? null : res;
	}
	
	public static IP4Address intersection(IP4Address ip1, IP4Address ip2){
//	  long net1 = ip1.getAddr() & ip1.getMask();
//	  long net2 = ip2.getAddr() & ip2.getMask();
	  long mask = ip1.getMask() <= ip2.getMask() ? ip1.getMask() : ip2.getMask();
	  if((ip1.getAddr() & mask) == (ip2.getAddr() & mask)){
	    long host1 = ip1.getAddr() & (~mask);
	    long host2 = ip2.getAddr() & (~mask);
	    IP4Address addr = new IP4Address();
	    if(host1 == host2){
	      addr.setMask(mask);
	      addr.setAddr(ip1.getAddr());
	    } else if(host1 < host2) {
	      addr.setMask(ip2.getMask());
	      addr.setAddr(ip2.getAddr());
	    } else {
	      addr.setMask(ip1.getMask());
        addr.setAddr(ip1.getAddr());
	    }
	    return addr;
	  }
	  return null;
	}
	
}
