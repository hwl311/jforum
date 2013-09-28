package org.sh.comm;

public interface ICursor {
	public boolean hasNext();
	public IPackage next();
	public String toString();
}
