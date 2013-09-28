package org.sh.comm;

import com.mongodb.*;

public interface IPackage extends DBObject{
	public Object getObj(String path);
	public Object getObj(String path,Object obj);
	
	public String getString(String path);
	public int getInt(String path);
	public float getFloat(String path);
	
	public String getString(String path,String ret);
	public int getInt(String path,int ret);
	public float getFloat(String path,float ret);
	
	public String getId();
	public void setId(String id);
	public int length(String path);
	public String type(String path);
	public int set(String path,Object val);
	public int set(String path,int val);
	public int set(String path,float val);
	public void remove(String path);
	public IPackage copy();
	public void foreach(String path,IForeachPackage fp);
	public String toString();
}
