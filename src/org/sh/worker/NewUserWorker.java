package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.*;

public class NewUserWorker implements IWorker{
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(NewUserWorker.class.getName());
	private ITable tblUser = null;
	public NewUserWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(user.isLogin())throw new SevErr(2001,"user already login");
		if(tblUser==null)throw new SevErr(2002,"user table not defined");
		
		String username = req.getString("/newuser/userinfo/name");
		DBPackage qry = new DBPackage();
		qry.set("/user", username);
		IPackage nuser = tblUser.get(qry);
		if(nuser!=null)throw new SevErr(2003,"user already exist");
		
		qry.set("/user[0]", username);
		qry.set("/info",req.getObj("/newuser/userinfo"));
		String salt = user.byte2hex(user.getSalt(4));
		qry.set("/info/passwd", user.encryptPasswd(salt, req.getString("/newuser/userinfo/passwd")));

		if(0!=tblUser.save(qry))throw new SevErr(2004,"database err when save user info");
		logger.debug(qry);
		return 0;
	}
	public void setTblUser(ITable tblUser){
		this.tblUser=tblUser;
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}
