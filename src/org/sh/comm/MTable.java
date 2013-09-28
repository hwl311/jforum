package org.sh.comm;

import org.apache.log4j.Logger;

import java.util.*;
import com.mongodb.*;
import org.bson.types.*;

public class MTable implements ITable{
	static private Logger logger = Logger.getLogger(MTable.class.getName());
	private IDatabases mdb = null;
	private DBCollection mtbl = null;
	private String tblName = null;

	public MTable(){
		
	}
	public MTable(DBCollection tbl,String tblName){
		this.tblName = tblName;
		this.mtbl = tbl;
	}
	
	public void setDb(IDatabases db){
		this.mdb = db;
		try{
			if(tblName != null){
				mtbl = this.mdb.getCollection(tblName);
				mtbl.setObjectClass(DBPackage.class);
			}
		}catch(Exception e){
			logger.error("error when get a table");
		}
	}
	public void setTblName(String tblName){
		this.tblName = tblName;
		try{
			if(mdb != null){
				mtbl = mdb.getCollection(tblName);
				mtbl.setObjectClass(DBPackage.class);
			}
		}catch(Exception e){
			logger.error("error when get a table");
		}
	}
	
	public String getTblName(){
		return tblName;
	}
	public long count(IPackage qry){
		return mtbl.count(qry);
	}
	public long count(String qry){
		DBPackage obj = DBPackage.parse(qry);
		return count(obj);
	}
	
	public ICursor find(IPackage qry){
		logger.debug("find "+qry);
		DBCursor cur = mtbl.find(qry);
		return new MCursor(cur);
	}
	public ICursor find(String qry){
		DBPackage obj = DBPackage.parse(qry);
		return find(obj);
	}
	
	public IPackage get(String id){
		return get(new ObjectId(id));
	}
	public IPackage get(ObjectId id){
		DBPackage pkg = new DBPackage("_id",id);
		return get(pkg);
	}
	public IPackage get(IPackage qry){
		return DBObject2IPackage(mtbl.findOne(qry));
	}
	
	public int save(IPackage pkg){
		logger.info("will save "+pkg.toString());
		
		try{
			mtbl.save(pkg,WriteConcern.SAFE);
			logger.info("saved obj id "+pkg.getId());
		}catch(Exception e){
			logger.warn(e);
			logger.warn("error when save obj ...");
			return -1;
		}
		return 0;
	}
	public int save(String pkg){
		DBPackage obj = DBPackage.parse(pkg);
		if(obj == null){
			logger.warn("pkg is null");
		}

		return this.save(obj);	
	}
	
	public void getUpdatePkg(String pre,IPackage pkg,IPackage set){
		Set<?> keys = pkg.keySet();
		Iterator<?> it = keys.iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			Object val = pkg.get(key);
			if(val instanceof IPackage){
				if(pre.equals(""))getUpdatePkg(key,(IPackage)val,set);
				else getUpdatePkg(pre+"."+key,(IPackage)val,set);
			}
			else{
				if(pre.equals(""))set.put(key, pkg.get(key));
				else set.put(pre+"."+key, pkg.get(key));
			}
		}
	}

	public int update(IPackage pkg,IPackage qry){
		pkg.markAsPartialObject();
		logger.info("where "+qry);
		WriteResult ret;
		DBPackage val = new DBPackage();
		getUpdatePkg("",pkg,val);
		logger.debug(val);
		DBPackage set = new DBPackage("$set",val);
		try{
			ret = mtbl.update(qry, set, false, true, WriteConcern.SAFE);
		}catch(Exception e){
			logger.warn(e);
			logger.warn("error when update");
			return -1;
		}
		return ret.getN();
	}
	static public IPackage DBObject2IPackage(DBObject obj){
		if(obj==null)return null;
		IPackage pkg = null;
		if(obj instanceof DBObject && !(obj instanceof List))pkg = new DBPackage(obj.toMap());
		else if(obj instanceof List)pkg = new DBListPackage();
		Set<String> keys = obj.keySet();
		for (String field : keys) {
			Object val = obj.get(field);
			if (val instanceof DBObject || val instanceof BasicDBList) {
				pkg.put(field, DBObject2IPackage((DBObject)val));
			}
			else pkg.put(field, val);
		}
		return pkg;
	}
	public int update(IPackage pkg,String qry){
		return update(pkg,DBPackage.parse(qry));
	}
	public int update(String pkg,String qry){
		return update(DBPackage.parse(pkg),DBPackage.parse(qry));
	}
	public int update(String pkg,IPackage qry){
		return update(DBPackage.parse(pkg),qry);
	}
	
}
