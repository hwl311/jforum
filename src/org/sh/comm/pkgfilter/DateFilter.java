package org.sh.comm.pkgfilter;

import org.sh.comm.IPackage;

public class DateFilter  implements IPkgFilter {
	public boolean check(Object obj,IPackage type,IPkgChecker checker){
		String opt = type.getString("/<xmlattr>/opt", "1");
		if(opt.equals("0") && (obj==null || obj.toString().equals("")))return true;
		return true;
	}
}
