package org.sh.comm;

public class SevErr extends Exception{
	static final long serialVersionUID = 0;
	public int rescode = 9999;
	public SevErr(int errno,String errmsg){
		super(errmsg);
		this.rescode = errno;
	}
	public SevErr(String errmsg){
		super(errmsg);
	}

}
