package org.sh.comm;

import com.mongodb.util.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.bson.*;
import org.bson.types.*;


public class DBPackage extends BasicBSONObject implements IPackage {
	private static Logger logger = Logger.getLogger(DBPackage.class.getName());
	private static final long serialVersionUID = -4415279469780082164L;
	private static Pattern reg = Pattern.compile("^([/\\[])([^/\\[\\]]+)\\]?((/?).*)$");
	private boolean _isPartialObject;
	/**
     *  Creates an empty object.
     */
    public DBPackage(){
    }
    
    /**
     * creates an empty object
     * @param size an estimate of number of fields that will be inserted
     */
    public DBPackage(int size){
    	super(size);
    }

    /**
     * creates an object with the given key/value
     * @param key  key under which to store
     * @param value value to stor
     */
    public DBPackage(String key, Object value){
        super(key, value);
    }

    /**
     * Creates an object from a map.
     * @param m map to convert
     */
    public DBPackage(Map<?,?> m) {
        super(m);
    }
    
	public boolean isPartialObject(){
        return _isPartialObject;
    }

    public void markAsPartialObject(){
        _isPartialObject = true;
    }
    public static DBPackage parse(String msg){
    	return (DBPackage)JSON.parse(msg, new DBPkgCallback());
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

		if(pkg!=null)logger.warn(pkg.getClass().getName());
		if(pkg instanceof IPackage){
			return ((IPackage)pkg).getObj(tmp, obj);
		}
		else if(pkg instanceof com.mongodb.BasicDBObject){
			IPackage tmpPkg = DBPackage.parse(pkg.toString());
			return ((IPackage)tmpPkg).getObj(tmp, obj);
		}
		else
		{
			logger.warn("don't match type Object");
			return null;
		}
	}
	public String getString(String path){
		return this.getString(path,"");
	}
	public String getString(String path,String ret){
		Object obj = getObj(path,ret);
		if(obj!=null){
			return obj.toString();
		}
		return ret;
	}
	public int getInt(String path){
		return this.getInt(path, 0);
	}
	public int getInt(String path,int ret){
		Object tmp = this.getObj(path,new Integer(ret));
		if(tmp instanceof Number){
			return ((Number)tmp).intValue();
		}
		else if(tmp instanceof String){
			return Integer.parseInt((String)tmp);
		}
		return ret;
	}
	public float getFloat(String path){
		return this.getFloat(path,0.0f);
	}
	public float getFloat(String path,float ret){
		Object tmp = this.getObj(path,new Float(ret));
		if(tmp instanceof Number){
			return ((Number)tmp).floatValue();
		}
		else if(tmp instanceof String){
			return Float.parseFloat((String)tmp);
		}
		return ret;
	}
	public String getId(){
		ObjectId id = (ObjectId)this.getObj("/_id",null);
		if (id == null){
			return "";
		}
		else{
			return id.toString();
		}
	}
	public void setId(String id){
		ObjectId _id = new ObjectId(id);
		this.set("/_id", _id);
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

		
		//logger.debug("obj "+path);
		if(m.group(1).equals("/")){//set Object
			if(tmp.equals("")){
				//logger.debug(val);
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
	public String toString(){
		return JSON.serialize( this );
	}
	
	public IPackage copy() {
		// copy field values into new object
		DBPackage newobj = new DBPackage(this.toMap());
		// need to clone the sub obj
		for (String field : keySet()) {
			Object val = get(field);
			if (val instanceof DBPackage) {
				newobj.put(field, ((DBPackage)val).copy());
			} else if (val instanceof DBListPackage) {
				newobj.put(field, ((DBListPackage)val).copy());
			}
		}
		return newobj;
	}
}


class DBPkgCallback extends JSONCallback{
	public DBPkgCallback(){
		
	}
	
	@Override
	public BSONObject create(){
		return new DBPackage();
	}
	    
	@Override
	protected BSONObject createList() {
		return new DBListPackage();
	}
}
