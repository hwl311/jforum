package org.sh.comm;

import com.mongodb.*;

public class PkgCursor implements ICursor{
	DBCursor cur = null;
	public PkgCursor(DBCursor cur){
		this.cur = cur;
	}
	public boolean hasNext(){
		return cur.hasNext();
	}
	public IPackage next(){
		IPackage dbobj = (IPackage)cur.next();
		return dbobj;
	}
}
