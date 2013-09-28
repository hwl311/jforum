package org.sh.comm;

import org.bson.types.*;

public interface ITable {
	public void setDb(IDatabases db);
	public void setTblName(String tblName);
	public String getTblName();
	
	public long count(IPackage qry);
	public long count(String qry);
	
	public ICursor find(IPackage qry);
	public ICursor find(String qry);
	
	public IPackage get(IPackage qry);
	public IPackage get(ObjectId id);
	public IPackage get(String id);
	
	public int save(IPackage pkg);
	public int save(String pkg);
	
	public int update(IPackage pkg,IPackage qry);
	public int update(IPackage pkg,String qry);
	public int update(String pkg,String qry);
	public int update(String pkg,IPackage qry);
}
