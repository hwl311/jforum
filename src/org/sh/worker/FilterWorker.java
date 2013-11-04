package org.sh.worker;

import org.apache.log4j.Logger;
import org.sh.comm.*;
import org.sh.comm.pkgfilter.*;

import java.util.*;

public class FilterWorker implements IWorker,IPkgChecker{
	static final long serialVersionUID = 0;
	static private Logger logger = Logger.getLogger(FilterWorker.class.getName());
	private Map<String,Object> filters = null;
	private String file = null;
	private IPackage pkgcfg = null;
	public FilterWorker(){
		
	}
	
	public int doWorking(IUser user,IPackage req,IPackage res)throws SevErr{
		if(pkgcfg==null)throw new SevErr(1101,"报文格式定义错误");
		if(!check(req,pkgcfg))throw new SevErr(1102,"报文检查错误");
		return 0;
	}
	
	public boolean check(Object pkg,IPackage cfg)throws SevErr{
		if(cfg==null)throw new SevErr(1103,"报文错误");
		IPackage type = null;
		String typeName = cfg.getString("/<xmlattr>/type");
		
		if(typeName.equals("Ref")){
			String ref=cfg.getString("/<xmlattr>/ref","");
			type=(IPackage)pkgcfg.getObj(ref,null);
			typeName=type.getString("/<xmlattr>/type");
		}
		else{
			type=cfg;
		}
		logger.debug(type);
		if(typeName==null||typeName.equals(""))typeName="Object";
		if(type.getString("/<xmlattr>/opt", "1").equals("0")){
			if(pkg==null)return true;
		}
		IPkgFilter filter = (IPkgFilter)filters.get(typeName);
		if(filter == null)throw new SevErr(1104,"type error");
		return filter.check(pkg, type, this);
	}
	
	public void setFilters(Map<String,Object> filters){
		this.filters = filters;
	}
	
	public void setCfgFile(String file){
		this.file=file;
		XmlParser p = new XmlParser();
		this.pkgcfg = p.parseFile(file);
	}
	
	public String getCfgFile(){
		return file;
	}
	public void setUser(ITable tblUser){
		
	}
	public void setTblSeq(ITable tblSeq){
		
	}
}
