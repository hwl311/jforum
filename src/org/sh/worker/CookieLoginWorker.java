package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.*;

public class CookieLoginWorker implements IWorker {
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(CookieLoginWorker.class.getName());

	@Override
	public int doWorking(IUser user, IPackage req, IPackage res) throws SevErr {
		if(!user.isLogin()){
			String userId=req.getString("/pub/cookies/userid", "");
			String cookiePw=req.getString("/pub/cookies/cookiepw", "");

			if(userId.equals("") || cookiePw.equals("")){
				logger.info("no cookie login");
				return 0;
			}
			
			user.loginByCookie(userId, cookiePw);
		}
		return 0;
	}

}
