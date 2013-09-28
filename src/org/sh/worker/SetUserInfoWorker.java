package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.IPackage;
import org.sh.comm.ITable;
import org.sh.comm.IUser;
import org.sh.comm.IWorker;
import org.sh.comm.SevErr;

public class SetUserInfoWorker implements IWorker{
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(SetUserInfoWorker.class.getName());
	private ITable tblUser = null;
	
	public SetUserInfoWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(tblUser==null)throw new SevErr(2501,"table user not defined");
		if(!user.isLogin())throw new SevErr(2502,"user is not logined");
		
		IPackage pkg = (IPackage)req.getObj("/setinfo/userinfo");
		logger.debug(pkg);
		user.setInfo(pkg);
		return 0;
	}
	
	public void setTblUser(ITable tblUser){
		this.tblUser = tblUser;
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}
