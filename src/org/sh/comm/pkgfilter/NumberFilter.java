package org.sh.comm.pkgfilter;

import org.apache.log4j.Logger;
import org.sh.comm.IPackage;
import org.sh.comm.SevErr;
import org.sh.worker.FilterWorker;

public class NumberFilter  implements IPkgFilter {
	static private Logger logger = Logger.getLogger(FilterWorker.class.getName());
	
	public boolean check(Object obj,IPackage type,IPkgChecker checker)throws SevErr{
		int n=0;
		String opt = type.getString("/<xmlattr>/opt", "1");
		if(opt.equals("0") && (obj==null || obj.toString().equals("")))return true;
		if(!(obj instanceof Number) && !(obj instanceof String))throw new SevErr(1301,"not a Number");
		else if(obj instanceof String){
			try{
				n = Integer.parseInt((String)obj);
			}catch(NumberFormatException e){
				throw new SevErr(1304,"not a number string");
			}
		}
		else{
			n = ((Number)obj).intValue();
		}
		
		int max = type.getInt("/<xmlattr>/max",999999999);
		int min = type.getInt("/<xmlattr>/min",-999999999);
		
		logger.debug(max);
		logger.debug(min);
		logger.debug(n);
		if(n > max)throw new SevErr(1302,"bigger then max value");
		if(n < min)throw new SevErr(1303,"litter then min value");
		
		return true;
	}
}
