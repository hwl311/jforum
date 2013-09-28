package org.sh.comm.pkgfilter;

import java.util.*;

import org.sh.comm.*;

public class ArrayFilter implements IPkgFilter {
	public boolean check(Object obj,IPackage type,IPkgChecker checker)throws SevErr{
		String opt = type.getString("/<xmlattr>/opt", "1");
		if(opt.equals("0") && obj==null)return true;
		if(!(obj instanceof List))throw new SevErr(1201,"List ¿‡–Õ¥ÌŒÛ");
		int length=type.getInt("/<xmlattr>/length",10000);
		int minlen=type.getInt("/<xmlattr>/minlen",1);
		
		List<?> list = (List<?>)obj;
		int len = list.size();
		if(len>length || len<minlen)throw new SevErr(1202,"Array length Error");
		Map<?,?> map = type.toMap();
		Object[] keys = map.keySet().toArray();
		IPackage subType = (IPackage)map.get(keys[0]);
		
		for(int i=0;i<len;i++){
			Object subObj = list.get(i);
			if(!checker.check(subObj, subType))throw new SevErr(1203,"Array Content Error");
		}
		return true;
	}
}
