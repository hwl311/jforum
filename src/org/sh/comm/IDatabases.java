package org.sh.comm;

import com.mongodb.*;

public interface IDatabases {
	public boolean connect(IPackage dbPkg) throws SevErr;
	public boolean isConnected() throws SevErr;
	public void disConnect() throws SevErr;
	public ITable getTable(String tbl) throws SevErr;
	public DBCollection getCollection(String tbl) throws SevErr;
	public void setDbPkg(String dbPkg);
}

