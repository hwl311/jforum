package org.sh.comm;

import org.apache.log4j.Logger;

import com.mongodb.*;

public class Mdb implements IDatabases{
	static private Logger logger = Logger.getLogger(Mdb.class.getName());
	private IPackage dbPkg = null;
	private Mongo mongo = null;
	private DB mdb = null;
	
	public boolean connect(IPackage dbPkg){
		if(mdb != null){
			mdb = null;
			mongo.close();
			mongo = null;
		}
		
		logger.info("will connect to "+dbPkg);
		this.dbPkg = dbPkg;
		
		try{
			mongo = new Mongo(dbPkg.getString("/host"),dbPkg.getInt("/port"));
			mdb = mongo.getDB(dbPkg.getString("/dbname"));
			if(mdb == null){
				mongo.close();
				mongo=null;
				return false;
			}

			if(dbPkg.type("/user") != "null"){
				boolean isOk = mdb.authenticate(dbPkg.getString("/user"), dbPkg.getString("/passwd").toCharArray());
				logger.warn(1);
				if(!isOk){
					mdb = null;
					mongo.close();
					mongo = null;
				}
			}
			
		}catch(Exception e){
			if(mongo!=null)mongo.close();
			mongo = null;
			mdb = null;
			logger.warn("can't connect to db "+dbPkg);
			logger.warn(e);
		}
		return mdb!=null;
	}
	
	public boolean isConnected(){
		return mdb!=null;
	}
	public void disConnect(){
		mdb = null;
		if(mongo!=null)mongo.close();
		mongo = null;
	}
	
	public DBCollection getCollection(String tbl) throws SevErr{
		logger.info("getTable "+tbl);
		if(mdb == null){
			this.connect(this.dbPkg);
		}
		if(mdb == null){
			throw new SevErr(123,"test");
		}
		return mdb.getCollection(tbl);
	}
	public ITable getTable(String tbl) throws SevErr{
		ITable mtbl = new MTable(this.getCollection(tbl),tbl);
		return mtbl;
	}
	
	public void setDbPkg(String dbPkg){

		this.dbPkg = new DBPackage();
		if(null == (this.dbPkg = DBPackage.parse(dbPkg))){
			this.dbPkg = null;
		}
		logger.info("dbPkg is "+this.dbPkg);
	}
}
