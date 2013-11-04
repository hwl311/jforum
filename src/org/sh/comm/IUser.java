package org.sh.comm;

import java.io.Serializable;

public interface IUser extends Serializable {
	public boolean login(String username,String passwd)throws SevErr;
	public boolean loginByCookie(String userid,String username)throws SevErr;
	public void logout() throws SevErr;
	public boolean isLogin() throws SevErr;
	public boolean changePasswd(String oldPasswd,String newPasswd)throws SevErr;
	public String encryptPasswd(String salt,String oriPasswd)throws SevErr;
	public byte[] getSalt(int len)throws SevErr;
	public String byte2hex(byte[] b);
	public void setInfo(String info);
	public void setInfo(IPackage info) throws SevErr;
	public IPackage getInfo() throws SevErr;
	public void setAppInfo(String app,IPackage info) throws SevErr;
	public void setAppInfo(String app,String info) throws SevErr;
	public IPackage getAppInfo(String app) throws SevErr;
	public String getUserId() throws SevErr;
	public String getLoginName() throws SevErr;
}
