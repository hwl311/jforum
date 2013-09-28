package org.sh.comm.pkgfilter;

import org.apache.log4j.Logger;
import org.sh.comm.IPackage;
import org.sh.comm.SevErr;

public class StringFilter  implements IPkgFilter {
	static private Logger logger = Logger.getLogger(StringFilter.class.getName());
	public boolean check(Object obj,IPackage type,IPkgChecker checker)throws SevErr{
		String opt = type.getString("/<xmlattr>/opt", "1");
		if(opt.equals("0") && (obj==null || obj.toString().equals("")))return true;
		
		if(!(obj instanceof String))throw new SevErr(1140,"not a String");
		
		int length = type.getInt("/<xmlattr>/length");
		int minlen = type.getInt("/<xmlattr>/minlen");
		String str = (String)obj;
		logger.debug(length);
		logger.debug(minlen);
		logger.debug(str.length());
		
		if(str.length() > length)throw new SevErr(1141,"bigger then max length");
		if(str.length() < minlen)throw new SevErr(1142,"litter then min length");
		
		return true;
	}
}
