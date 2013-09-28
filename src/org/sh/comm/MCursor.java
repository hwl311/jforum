package org.sh.comm;

import com.mongodb.DBCursor;

public class MCursor implements ICursor{
	private DBCursor cur = null;
	public MCursor(DBCursor cur){
		this.cur = cur;
	}
	
	public boolean hasNext(){
		return cur.hasNext();
	}
	
	public IPackage next(){
		return MTable.DBObject2IPackage(cur.next());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(!cur.hasNext()){
			return "[]";
		}
		sb.append("[");
		sb.append(cur.next());
		
		while(cur.hasNext()){
			sb.append(",");
			sb.append(cur.next());
		}
		sb.append("]");
		return sb.toString();
	}
}
