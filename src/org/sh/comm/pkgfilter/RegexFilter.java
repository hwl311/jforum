package org.sh.comm.pkgfilter;

import java.util.regex.*;

import org.apache.log4j.Logger;
import org.sh.comm.*;

public class RegexFilter  implements IPkgFilter {
	static private Logger logger = Logger.getLogger(RegexFilter.class.getName());
	private Pattern reg = null;
	public boolean check(Object obj,IPackage type,IPkgChecker checker)throws SevErr{
		String opt = type.getString("/<xmlattr>/opt", "1");
		if(opt.equals("0") && (obj==null || obj.toString().equals("")))return true;
		if(obj instanceof IPackage)throw new SevErr(1501,"can not be a IPackage");
		String str = obj.toString();
		
		Matcher m = null;
		try{
			if(reg!=null){
				logger.debug(str);
				m = reg.matcher(str);
			}
			else{
				Pattern reg2 = Pattern.compile(type.getString("/<xmlattr>/reg"));
				m = reg2.matcher(str);
			}
		}catch(Exception e){
			throw new SevErr(1502,e.getMessage());
		}
		
		if(!m.matches())throw new SevErr(1503,"matches false");
		
		return true;
	}
	public void setPattern(String pattern){
		logger.debug(pattern);
		reg = Pattern.compile(pattern);
	}
}

