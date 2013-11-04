package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.*;

public class CookieLoginWorker implements IWorker {
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(CookieLoginWorker.class.getName());
	private ITable TblCookie = null;

	@Override
	public int doWorking(IUser user, IPackage req, IPackage res) throws SevErr {
		if(!user.isLogin()){
			String cookieId=req.getString("/pub/cookies/cookieid", "");
			String cookiePw=req.getString("/pub/cookies/cookiepw", "");

			if(cookieId.equals("") || cookiePw.equals("")){
				logger.debug("no cookie login");
				return 0;
			}
			
			IPackage cookie = TblCookie.get(new DBPackage("_id",cookieId));
			if(cookie == null){
				throw new SevErr(2602,"cookie id error");
			}
			
			String passwd = cookie.getString("/passwd", "");
			if(!passwd.equals(cookiePw)){
				logger.warn("cookie passwd error");
				throw new SevErr(2601,"cookie passwd error");
			}
			
			String userId = cookie.getString("/user","");
			String userName = cookie.getString("/username", "");
			user.loginByCookie(userId,userName);
		}
		return 0;
	}
	
	public void setTblCookie(ITable TblCookie){
		this.TblCookie = TblCookie;
	}

}
