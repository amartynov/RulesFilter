package org.filter.common;

import org.filter.utils.Utils;


@Deprecated
public class IP4Address {
  
//  private static final IP4Address maxIpAddress = new IP4Address("255.255.255.255/255.255.255.255");
  public static final long maxIpAddress = 0xffffffffL;
	
//	private byte[] addr;
//	private byte[] mask;
  
  private long addr;
  private long mask;
  
	public IP4Address() {};
	
	public IP4Address(String addr, String mask) {
	  String a = addr.replace(".", "/");
    String m = mask.replace(".", "/");
    String[] abuf = a.split("/");
    String[] mbuf = m.split("/");
    
    this.addr = Utils.strToLong(abuf);
    this.mask = Utils.strToLong(mbuf);
	}
	
	public IP4Address(String addr) {
		String a = addr.replace(".", "/");
		
		String[] abuf = a.split("/");
		this.addr = Utils.strToLong(abuf);
		this.mask = 0xffffffffL;
		long buf = 1L;
		while ((this.addr & buf) != 0) {
		  this.mask ^= buf;
		  buf <<= 1;
		}
	}
	
	
	
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

  
  
  
	
	public boolean equals(IP4Address address){
	  if(address.getAddr() == addr && address.getMask() == mask) return true;
	  return false;
	}
	
}
