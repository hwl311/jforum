package org.sh.comm;

public interface IPkgPath {
	public boolean parse(String path);
	public int length();
	public String get(int i);
	public int Type(int i);
}
