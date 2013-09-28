package org.sh.comm;

import java.util.*;
import java.util.regex.*;



public class PkgPath implements IPkgPath{
	//static private Logger logger = Logger.getLogger(PkgPath.class.getName());
	static private int MAX_DEP = 20;
	//		"/a/b_d[09][0]/sdf/"
	static private Pattern reg = Pattern.compile("^([/\\[])([^/\\[\\]]+)\\]?(/?.*)$");
	private List<String> val = new ArrayList<String>(MAX_DEP);
	private List<Integer> type = new ArrayList<Integer>(MAX_DEP);
	
	public boolean parse(String path){
		val.clear();
		type.clear();
		String tmp = path;
		//logger.debug(tmp);
		if(tmp == null||tmp == "")return false;
		for(;;){
			Matcher m = reg.matcher(tmp);
			if(!m.matches()){
				break;
			}
			//logger.debug(m.group(1));
			//logger.debug(m.group(2));
			//logger.debug(m.group(3));
			if(m.group(1).equals("/")){
				val.add(m.group(2));
				type.add(1);
			}
			else if(m.group(1).equals("[")){
				val.add(m.group(2));
				type.add(2);
			}
			tmp = m.group(3);
		}
		
		/*
		 * for(int i=0;i<this.length();i++){
			logger.info(this.get(i));
			logger.info(this.Type(i));
		}*/
		return val.size()>0;
	}
	public int length(){
		return val.size();
	}
	public String get(int i){
		return val.get(i);
	}
	public int Type(int i){
		return type.get(i);
	}
}
