package org.sh.comm;

import org.apache.log4j.Logger;
import org.json.*;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;


public class JsonPackage {
	static private Logger logger = Logger.getLogger(JsonPackage.class.getName());
	private JSONObject obj = null;
	public JsonPackage(){
		
	}
	public boolean parse(String msg){
		try{
			obj = new JSONObject(msg);
			return obj!=null;
		}catch(JSONException e){
			logger.warn(e);
			logger.warn("parse json msg failed");
			obj = null;
		}
		return false;
	}
	public Object get(String path){
		if(obj==null)return null;
		PkgPath p = new PkgPath();
		if(!p.parse(path))return null;
		
		Object tmp = obj;
		int length = p.length();
		for(int i=0;i<length;i++){
			if(p.Type(i)==1){
				tmp = ((JSONObject)tmp).get(p.get(i));
			}else{
				tmp = ((JSONArray)tmp).get(Integer.parseInt(p.get(i)));
			}
		}
		return tmp;
	}
	public Object get(String path,Object obj){
		Object tmp;
		try{
			tmp = this.get(path);
		}catch(Exception e){
			tmp = obj;
		}
		return tmp;
	}
	public String getString(String path){
		Object tmp = this.get(path,null);
		if(tmp == null){
			return "";
		}
		return tmp.toString();
	}
	public int getInt(String path){
		Object tmp = this.get(path,new Integer(0));
		if(tmp instanceof Number){
			return ((Number)tmp).intValue();
		}
		return 0;
	}
	public float getFloat(String path){
		Object tmp = this.get(path,new Float(0));
		if(tmp instanceof Number){
			return ((Number)tmp).floatValue();
		}
		return 0.0f;
	}
	public int length(String path){
		if(obj==null)return 0;
		return obj.length();
		//return 0;
	}
	public String type(String path){
		Object tmp = this.get(path,null);
		if(tmp instanceof Integer){
			return "Integer";
		}else if(tmp instanceof Float){
			return "Float";
		}else if(tmp instanceof String){
			return "String";
		}else if(tmp instanceof JSONObject){
			return "Object";
		}else if(tmp instanceof JSONArray){
			return "Array";
		}
		return "null";
	}
	
	public void set(String path,Object val){
		if(obj==null)obj = new JSONObject();
		PkgPath p = new PkgPath();
		if(!p.parse(path))return;
		
		Object tmp = obj;
		Object last = null;
		int length = p.length()-1;
		for(int i=0;i<length;i++){

			if(p.Type(i)==1){
				if(tmp instanceof JSONObject){
					last = tmp;
					tmp = ((JSONObject)tmp).opt(p.get(i));
					if(tmp == null){
						tmp = this.getNextObj(p.Type(i+1));
						((JSONObject)last).put(p.get(i), tmp);
					}
				}else{
					tmp = new JSONObject();
					this.setNewObj(last,p.Type(i-1), p.get(i-1), tmp);
					last = tmp;
					tmp = this.getNextObj(p.Type(i+1));
					((JSONObject)last).put(p.get(i), tmp);
				}
			}else{
				if(tmp instanceof JSONArray){
					last = tmp;
					tmp = ((JSONArray)tmp).opt(Integer.parseInt(p.get(i)));
					if(tmp == null){
						tmp = this.getNextObj(p.Type(i+1));
						((JSONArray)last).put(Integer.parseInt(p.get(i)), tmp);
					}
				}else{
					tmp = new JSONArray();
					this.setNewObj(last,p.Type(i-1), p.get(i-1), tmp);
					last = tmp;
					tmp = this.getNextObj(p.Type(i+1));
					((JSONArray)last).put(Integer.parseInt(p.get(i)), tmp);
				}
			}
		}
		
		if(tmp == null){
			logger.warn("tmp == null");
			return;
		}
		
		if(p.Type(length)==1){
			((JSONObject)tmp).put(p.get(length), val);
		}else{
			if(val!=null)
				((JSONArray)tmp).put(Integer.parseInt(p.get(length)),val);
			else
				((JSONArray)tmp).remove(Integer.parseInt(p.get(length)));
		}
		return;
	}
	
	private void setNewObj(Object last,int type,String key,Object val){
		if(type==1){
			JSONObject obj = (JSONObject)last;
			obj.put(key, val);
		}
		else{
			JSONArray obj = (JSONArray)last;
			obj.put(Integer.parseInt(key), val);
		}
	}
	
	private Object getNextObj(int type){
		if(type==1){
			return new JSONObject();
		}
		else{
			return new JSONArray();
		}
	}

	public void set(String path,int val){
		this.set(path, new Integer(val));
	}
	public void set(String path,float val){
		this.set(path, new Float(val));
	}
	public void remove(String path){
		this.set(path, null);
	}
	public void foreach(String path,IForeachPackage fp){
		Object tmp = this.get(path);
		if(tmp instanceof JSONObject){
			
		}else if(tmp instanceof JSONArray){
			
		}
	}
	public String toString(){
		if(obj==null)return "";
		return obj.toString();
	}
	
	public void setDBObject(DBObject obj){
		this.obj = new JSONObject(obj.toString());
	}
	
	public DBObject getDBObject(){
		return (DBObject)JSON.parse(obj.toString());
	}
}
