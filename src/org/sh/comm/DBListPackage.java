package org.sh.comm;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.bson.types.*;

import com.mongodb.util.*;


public class DBListPackage extends BasicBSONList implements IPackage {
	private static Logger logger = Logger.getLogger(DBListPackage.class.getName());
	private static final long serialVersionUID = -4415279469780082174L;
	private static Pattern reg = Pattern.compile("^([/\\[])([^/\\[\\]]+)\\]?((/?).*)$");
	 
	/**
     *  Creates an empty object.
     */
    public DBListPackage(){
    }
    
	/**
	 * Returns a JSON serialization of this object
	 * @return JSON serialization
	 */    
	@Override
	public String toString(){
		return JSON.serialize( this );
	}
	
	public boolean isPartialObject(){
		return _isPartialObject;
	}
	
	public void markAsPartialObject(){
		_isPartialObject = true;
	}
	
	public boolean parse(String msg){
    	return false;
    }
	public Object getObj(String path){
		return getObj(path,null);
	}
	public Object getObj(String path,Object obj){
		Matcher m = reg.matcher(path);
		if(!m.matches()){
			return obj;
		}
		
		String key = m.group(2);
		String tmp = m.group(3);
		Object pkg = this.get(key);
		if(tmp.equals("")){
			return pkg;
		}
		
		if(pkg instanceof IPackage){
			return ((IPackage)pkg).getObj(tmp, obj);
		}
		else
			return null;
	}
	public String getString(String path){
		return getString(path,"");
	}
	public String getString(String path,String ret){
		return getObj(path,"").toString();
	}
	public int getInt(String path){
		return getInt(path,0);
	}
	public int getInt(String path,int ret){
		Object tmp = this.getObj(path,new Integer(ret));
		if(tmp instanceof Number){
			return ((Number)tmp).intValue();
		}
		else if(tmp instanceof String){
			return Integer.parseInt((String)tmp);
		}
		return 0;
	}
	public float getFloat(String path){
		return getFloat(path,0.0f);
	}
	public float getFloat(String path,float ret){
		Object tmp = this.getObj(path,new Float(0));
		if(tmp instanceof Number){
			return ((Number)tmp).floatValue();
		}
		else if(tmp instanceof String){
			return Float.parseFloat((String)tmp);
		}
		return 0.0f;
	}
	public String getId(){
		return getObj("/_id").toString();
	}
	public void setId(String id){
		set("/_id",new ObjectId(id));
	}
	public int length(String path){
		return 0;
	}
	public String type(String path){
		Object tmp = this.getObj(path,null);
		if(tmp instanceof Integer){
			return "Integer";
		}

		if(tmp instanceof Float){
			return "Float";
		}

		if(tmp instanceof String){
			return "String";
		}

		if(tmp instanceof List){
			return "Array";
		}
		
		if(tmp instanceof IPackage){
			return "Object";
		}

		return "null";
	}
	public int set(String path,Object val){
		Matcher m = reg.matcher(path);
		if(!m.matches()){
			return 0;
		}
		
		String key = m.group(2);
		String tmp = m.group(3);
		Object pkg = this.get(key);

		
		logger.debug("list "+path);
		if(m.group(1).equals("[")){//set Object
			if(tmp.equals("")){
				logger.debug(val);
				super.put(key, val);
				return 0;
			}
			
			if(m.group(4).equals("/")){
				if(!(pkg instanceof DBPackage)){
					pkg = new DBPackage();
					super.put(key, pkg);
				}
			}
			else{
				if(!(pkg instanceof DBListPackage)){
					pkg = new DBListPackage();
					super.put(key, pkg);
				}
			}
			
			((IPackage)pkg).set(tmp, val);
			
		}
		else {//set Array
			return -100;
		}
		return -1;

	}
	public int set(String path,int val){
		return this.set(path, new Integer(val));
	}
	public int set(String path,float val){
		return this.set(path, new Float(val));
	}
	public void remove(String path){
		Matcher m = reg.matcher(path);
		if(!m.matches()){
			return;
		}
		
		String key = m.group(2);
		String tmp = m.group(3);
		Object pkg = this.get(key);
		
		if(tmp.equals("")){
			//remove
			this.removeField(key);
			return ;
		}
		
		if(pkg instanceof IPackage){
			((IPackage)pkg).remove(tmp);
		}
		else
			return ;
	}
	
	public void foreach(String path,IForeachPackage fp){
		
	}
	
	public IPackage copy() {
		// copy field values into new object
		DBListPackage newobj = new DBListPackage();
		// need to clone the sub obj
		for (int i = 0; i < size(); ++i) {
			Object val = get(i);
			if (val instanceof DBPackage) {
				val = ((DBPackage)val).copy();
			} else if (val instanceof DBListPackage) {
				val = ((DBListPackage)val).copy();
			}
			newobj.add(val);
		}
		return newobj;
	}
	
	private boolean _isPartialObject;
}
