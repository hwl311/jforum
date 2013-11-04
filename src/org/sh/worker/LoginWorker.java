package org.sh.worker;

import org.sh.comm.*;
import java.util.*;


public class LoginWorker implements IWorker{
	static final long serialVersionUID = 0;
	
	public LoginWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(user.isLogin())user.logout();
		
		String username = req.getString("/login/username");
		String passwd = req.getString("/login/passwd");
		user.login(username, passwd);
		if(user.isLogin()){
			String cookiepw = req.getString("/pub/cookies/cookiepw", "");
			if(cookiepw.equals(""))cookiepw = user.byte2hex(user.getSalt(16));
			
			res.set("/pub/cookies/userid", user.getUserId());
			res.set("/pub/cookies/cookiepw", cookiepw);
			DBPackage cookie = new DBPackage();

			cookie.set("/"+cookiepw+"/stat", "A");
			cookie.set("/"+cookiepw+"/date", new Date());
			cookie.set("/"+cookiepw+"/user", username);
			user.setAppInfo("cookies", cookie);
		}
		return 0;
	}
	
	public void setTblSeq(ITable tblSeq){
		
	}
}
