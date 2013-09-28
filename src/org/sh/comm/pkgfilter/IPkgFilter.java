package org.sh.comm.pkgfilter;

import org.sh.comm.*;

public interface IPkgFilter {
	public boolean check(Object pkg,IPackage type,IPkgChecker checker) throws SevErr;
}
