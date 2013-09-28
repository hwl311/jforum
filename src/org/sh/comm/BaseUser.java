package org.sh.comm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.*;
import org.bson.types.*;

import org.apache.log4j.Logger;

public class BaseUser implements IUser{
	private static Logger logger = Logger.getLogger(BaseUser.class.getName());
	static final long serialVersionUID = 1234;
	private static Pattern reg = Pattern.compile("^(.*):(.*)$");
	
	private ITable tblUser = null;
	private IPackage noLoginPkg = null;
	private boolean isLogin = false;
	private String loginName = null;
	private ObjectId userId = null;
	private String passwdEncrypt = null;
	private IPackage userQry = null;
	
	public boolean login(String username,String passwd)throws SevErr{
		if(tblUser != null){
			DBPackage qry = new DBPackage();
			qry.set("/user", username);
			IPackage userPkg = tblUser.get(qry);
			if(userPkg != null){
				logger.debug(userPkg);
				logger.debug(userPkg.getString("/info/name"));
				isLogin = checkPasswd(passwd,userPkg.getString("/info/passwd"));
				if(isLogin){
					passwdEncrypt = userPkg.getString("/info/passwd");
					userId = (ObjectId)userPkg.getObj("/_id");
					userQry = new DBPackage("_id",userId);
					loginName = username;
				}
				else
					throw new SevErr(2103,"login false,user or passwd error");
			}
			else
				throw new SevErr(2103,"login false,user or passwd error");
		}
		else throw new SevErr(2104,"table user not defined");
		return isLogin;
	}
	
	public boolean changePasswd(String oldPasswd,String newPasswd) throws SevErr{
		if(!isLogin){
			return false;
		}
		
		String newPw = null;
		String salt = null;
		if(checkPasswd(oldPasswd,passwdEncrypt)){
			newPw = encryptPasswd(salt,newPasswd);
			newPw = salt + ":" + newPw;
			DBPackage obj = new DBPackage();
			int ret = tblUser.update(obj, userQry);
			return ret == 1;
		}
		return false;
	}
	
	private boolean checkPasswd(String oriPasswd,String pw) throws SevErr{
		logger.debug(pw);
		Matcher m = reg.matcher(pw);
		if(!m.matches())throw new SevErr(2106,"regex error or passwd error");
		String salt = m.group(1);
		String tmpPw = encryptPasswd(salt,oriPasswd);
		logger.debug(tmpPw);
		logger.debug(pw);
		return pw.equals(tmpPw);
	}
	
	public String encryptPasswd(String salt,String oriPasswd) throws SevErr{
		logger.debug(salt);
		logger.debug(oriPasswd);
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5"); 
		    md5.update(salt.getBytes()); 
		    md5.update(oriPasswd.getBytes());
		}
		catch(Exception e){
			logger.warn(e.getMessage());
			throw new SevErr(2101,"encrypt passwd error");
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append(salt);
		sb.append(":");
		sb.append(byte2hex(md5.digest().clone()));
		logger.debug(sb.toString());
		return sb.toString();
	}
	public byte[] getSalt(int len)throws SevErr{
		byte bytes[] = new byte[len];
		try{
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(bytes);
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new SevErr(2102,"SecureRandom error");
		}
		return bytes;
	}
	public String byte2hex(byte[] b) //二行制转字符串
	{
		StringBuilder sb = new StringBuilder();
		String stmp=null;
		for (int n=0;n<b.length;n++)
		{
			stmp=(Integer.toHexString(b[n] & 0XFF));
			if (stmp.length()==1){
				sb.append("0");
				sb.append(stmp);
			}
			else sb.append(stmp);
		}
		return sb.toString();
	}
	public void logout(){
		isLogin = false;
	}
	
	public boolean isLogin(){
		return isLogin;
	}
	
	public IPackage getInfo(){
		if(isLogin){
			IPackage userinfo = tblUser.get(userId);
			return (IPackage)userinfo.get("info");
		}
		else{
			return noLoginPkg;
		}
	}
	
	public void setInfo(String info){
		noLoginPkg = DBPackage.parse(info);
	}
	public void setInfo(IPackage info){
		DBPackage pkg = new DBPackage("info",info);
		tblUser.update(pkg, userQry);
	}
	public void setAppInfo(String app,IPackage info){
		DBPackage pkg = new DBPackage(app,info);
		tblUser.update(pkg, userQry);
	}
	
	public String getUserId(){
		return userId.toString();
	}
	 
	public String getLoginName(){
		return loginName;
	}
	public void setTblUser(ITable tblUser){
		this.tblUser = tblUser;
	}
}
