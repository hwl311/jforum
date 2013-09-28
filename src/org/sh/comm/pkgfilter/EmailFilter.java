package org.sh.comm.pkgfilter;

//import java.util.regex.Pattern;

import org.sh.comm.IPackage;

public class EmailFilter  implements IPkgFilter {
	//static private Pattern reg = Pattern.compile("");
	public boolean check(Object pkg,IPackage type,IPkgChecker checker){
		return false;
	}
}
