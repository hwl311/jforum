package org.sh.comm.pkgfilter;

import org.apache.log4j.Logger;
import org.sh.comm.*;
import java.util.*;

public class ObjectFilter  implements IPkgFilter {
	static private Logger logger = Logger.getLogger(ObjectFilter.class.getName());
	
	public boolean check(Object obj,IPackage type,IPkgChecker checker)throws SevErr{
		String opt = type.getString("/<xmlattr>/opt", "1");
		if(obj!=null)logger.debug(obj.toString());
		if(obj!=null)logger.debug(obj.getClass().getName());
		if(opt.equals("0") && obj==null)return true;
		if(obj==null)throw new SevErr(1401,"缺少Object");
		
		if(obj instanceof List)throw new SevErr(1402,"List 类型错误");
		if(!(obj instanceof IPackage))throw new SevErr(1403,"Object 类型错误");
		
		IPackage pkg = (IPackage)obj;
		String nomust = type.getString("/<xmlattr>/nomust", "0");
		String noempty = type.getString("/<xmlattr>/noempty", "1");
		String free = type.getString("/<xmlattr>/free", "0");

		Map<?,?> pkgMap = pkg.toMap();
		Map<?,?> typeMap = type.toMap();
		if(noempty.equals("1") && pkgMap.isEmpty())throw new SevErr(1404,"Object is empty");
		
		Set<String> alreadyCheckKey = new HashSet<String>();
		
		//检查是否多出元素
		logger.debug("free "+free);
		if(!free.equals("1")){
			Set<?> keys = pkgMap.keySet();
			Iterator<?> it = keys.iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				Object subPkg = pkgMap.get(key);
				IPackage subType = (IPackage)typeMap.get(key);
				if(subType==null)throw new SevErr(1405,key+" 多出子节点");
				logger.debug("subPkg "+subPkg);
				logger.debug("subType "+subType);
				try{
					checker.check(subPkg, subType);
					alreadyCheckKey.add(key);
				}catch(SevErr e){
					throw new SevErr(e.rescode,key + " " + e.getMessage());
				}
			}
		}
		
		//检查是否缺少必要元素
		logger.debug("nomust "+nomust);
		if(!nomust.equals("1")){
			Set<?> keys = typeMap.keySet();
			Iterator<?> it = keys.iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				if(key.equals("<xmlattr>"))continue;
				if(alreadyCheckKey.contains(key))continue;
				Object subPkg = pkgMap.get(key);
				IPackage subType = (IPackage)typeMap.get(key);
				logger.debug("subPkg "+subPkg);
				logger.debug("subType "+subType);
				try{
					checker.check(subPkg, subType);
				}catch(SevErr e){
					throw new SevErr(e.rescode,key + " " + e.getMessage());
				}
			}
		}

		return true;
	}
}
