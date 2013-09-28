package org.sh.comm;

import javax.servlet.http.HttpServletRequest;

public interface IParser {
	public IPackage parseMsg(HttpServletRequest req);
}
