package org.sh.worker;

import org.sh.comm.IPackage;
import org.sh.comm.ITable;
import org.sh.comm.IUser;
import org.sh.comm.IWorker;
import org.sh.comm.SevErr;

public class GetUserInfoWorker implements IWorker{
	static final long serialVersionUID = 0;
	private ITable tblUser = null;
	
	public GetUserInfoWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(tblUser==null)throw new SevErr(2301,"table user not defined");
		if(!user.isLogin())throw new SevErr(2302,"user is not logined");
		
		res.set("/userinfo", user.getInfo());
		res.set("/pub/cookies/aa", "test ttt");
		return 0;
	}
	
	public void setTblUser(ITable tblUser){
		this.tblUser = tblUser;
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}
