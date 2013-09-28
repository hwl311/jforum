package org.sh.comm.pkgfilter;

import org.sh.comm.IPackage;

public class RefFilter  implements IPkgFilter {
	public boolean check(Object pkg,IPackage type,IPkgChecker checker){
		return false;
	}
}
