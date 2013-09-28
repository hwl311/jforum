package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.*;


public class TestWorker implements IWorker {
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(TestWorker.class.getName());
	private Mdb mdb = null;
	public TestWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res) throws SevErr{
		XmlParser p = new XmlParser();
		p.parseFile("C:\\Users\\stephen\\Desktop\\pkgfilter.xml");
		res.set("/set/from/pre", "pre");
		ITable table = mdb.getTable("user");
		table.save("{\"a\":\"adsf1\",\"b\":\"baf1\"}");
		ICursor cur = table.find("");
		if(null == cur){
			throw new SevErr(9901,"nothing find");
		}
		
		while(cur.hasNext()){
			logger.info(cur.next());
		}
		
		return 0;
	}
	
	public void setMdb(Mdb mdb){
		this.mdb = mdb;
	}
	public void setTblUser(ITable user){
		
	}
	public void setTblSeq(ITable seq){
		
	}
}
