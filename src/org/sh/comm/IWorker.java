package org.sh.comm;

import java.io.Serializable;



public interface IWorker extends Serializable {
	//public int preWork(IUser user,IPackage req,IPackage res) throws SevErr;
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr;
	//public int postWork(IUser user,IPackage req,IPackage res)throws SevErr;
}
