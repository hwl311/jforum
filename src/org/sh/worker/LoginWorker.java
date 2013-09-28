package org.sh.worker;

import org.sh.comm.*;


public class LoginWorker implements IWorker{
	static final long serialVersionUID = 0;
	private ITable tblUser = null;
	
	public LoginWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(tblUser==null)throw new SevErr(2201,"table user not defined");
		if(user.isLogin())user.logout();
		
		String username = req.getString("/login/username");
		String passwd = req.getString("/login/passwd");
		user.login(username, passwd);
		return 0;
	}
	
	public void setTblUser(ITable tblUser){
		this.tblUser = tblUser;
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}
