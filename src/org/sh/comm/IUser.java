package org.sh.comm;

import java.io.Serializable;

public interface IUser extends Serializable {
	public boolean login(String username,String passwd)throws SevErr;
	public void logout();
	public boolean isLogin();
	public boolean changePasswd(String oldPasswd,String newPasswd)throws SevErr;
	public String encryptPasswd(String salt,String oriPasswd)throws SevErr;
	public byte[] getSalt(int len)throws SevErr;
	public String byte2hex(byte[] b);
	public IPackage getInfo();
	public void setInfo(String info);
	public void setInfo(IPackage info);
	public void setAppInfo(String app,IPackage info);
	public String getUserId();
	public String getLoginName();
}
