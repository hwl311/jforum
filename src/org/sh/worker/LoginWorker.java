package org.sh.worker;

import org.sh.comm.*;


public class LoginWorker implements IWorker{
	static final long serialVersionUID = 0;
	
	public LoginWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(user.isLogin())user.logout();
		
		String username = req.getString("/login/username");
		String passwd = req.getString("/login/passwd");
		user.login(username, passwd);
		return 0;
	}
	
	public void setTblSeq(ITable tblSeq){
		
	}
}
