package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.*;
import java.util.*;

public class CookieManagerWorker implements IWorker {
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(CookieManagerWorker.class.getName());
	private ITable TblCookie = null;

	@Override
	public int doWorking(IUser user, IPackage req, IPackage res) throws SevErr {
		String cookieid=req.getString("/pub/cookies/cookieid", "");
		String cookiepw=req.getString("/pub/cookies/cookiepw", "");
		if(cookiepw.equals(""))cookiepw = user.byte2hex(user.getSalt(16));
		logger.info(cookieid);
		
		if(cookieid.equals("")){
			cookieid = user.byte2hex(user.getSalt(16));
			logger.info("new cookieid:"+cookieid);
			res.set("/pub/cookies/cookieid",cookieid);
			
			DBPackage cookie = new DBPackage("_id",cookieid);
			cookie.put("createtime", new Date());
			cookie.put("lasttime", new Date());
			
			if(user.isLogin()){
				cookie.put("passwd", cookiepw);
				cookie.put("user", user.getUserId());
				cookie.put("username", user.getLoginName());
				cookie.set("/"+user.getUserId(), new Date());
				
				res.set("/pub/cookies/cookiepw", cookiepw);
			}
			TblCookie.save(cookie);
		}
		else{
			IPackage cookie = TblCookie.get(new DBPackage("_id",cookieid));
			if(cookie==null)throw new SevErr(2801,"cookie id error");
			cookie.set("/lasttime", new Date());
			
			if(user.isLogin()){
				cookie.set("/passwd", cookiepw);
				cookie.set("/user", user.getUserId());
				cookie.set("/username", user.getLoginName());
				cookie.set("/"+user.getUserId(), new Date());
				res.set("/pub/cookies/cookiepw", cookiepw);
			}
			else{
				cookie.set("/passwd", "");
				cookie.set("/user", "");
				cookie.set("/username", "");
				res.set("/pub/cookies/cookiepw", "");
			}
			TblCookie.save(cookie);
		}
		return 0;
	}
	
	public void setTblCookie(ITable TblCookie){
		this.TblCookie = TblCookie;
	}

}
