package org.sh.comm.pkgfilter;

import org.sh.comm.*;

public interface IPkgChecker {
	public boolean check(Object pkg,IPackage type)throws SevErr;
}
