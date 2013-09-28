package org.sh.worker;

import org.sh.comm.IPackage;
import org.sh.comm.ITable;
import org.sh.comm.IUser;
import org.sh.comm.IWorker;
import org.sh.comm.SevErr;

public class LogoutWorker implements IWorker{
	static final long serialVersionUID = 0;
	
	public LogoutWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res) throws SevErr{
		if(!user.isLogin()){
			throw new SevErr(2401,"user no login");
		}
		user.logout();
		return 0;
	}
	public void setTblUser(ITable tblUser){
		
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}