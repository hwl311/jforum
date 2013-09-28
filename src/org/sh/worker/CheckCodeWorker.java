package org.sh.worker;

import org.sh.comm.IPackage;
import org.sh.comm.ITable;
import org.sh.comm.IUser;
import org.sh.comm.IWorker;
import org.sh.comm.SevErr;

public class CheckCodeWorker implements IWorker{
	static final long serialVersionUID = 0;
	
	public CheckCodeWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		return 0;
	}
	public void setUser(ITable tblUser){
		
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}

