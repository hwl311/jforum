package org.sh.comm;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import com.mongodb.util.*;
import org.apache.log4j.*;
import javax.servlet.http.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * parse package and do working
 * @author Administrator
 *
 */
public class HttpController implements Controller , Serializable, IWorker{
	static final long serialVersionUID = 0;

	static Logger logger = Logger.getLogger(HttpController.class.getName());
	private String msg = "msg";
	private String view = "json";
	private static int seqno = 1;
	private ITable tblSeq = null;

	private List<IWorker> workers = null;
	public IUser user = null;
	private IParser parser = null;

	public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		logger.debug("into handle!");
		int ret = 0;
		IPackage reqPkg=null;
		DBPackage seqPkg = new DBPackage();
		DBPackage resPkg = new DBPackage();
		
		try{
			reqPkg = this.parseMsg(arg0);
			if(reqPkg==null){
				return null;
			}
			
			//init pkg
			resPkg.set("/seqno", ++seqno);
			reqPkg.set("/seqno", seqno);
			
			seqPkg.set("/request", reqPkg.copy());
			seqPkg.set("/begintime", new Date());
			if(tblSeq != null)tblSeq.save(seqPkg);
			
			//upload file if any
			upload(arg0,null);
			
			//do working
			ret=doWorking(user,reqPkg,resPkg);
			
			//result
			resPkg.set("/rescode", ret);
			resPkg.set("/resdesc", "success");
		}
		catch(SevErr e){
			resPkg = new DBPackage();
			resPkg.set("/rescode", e.rescode);
			resPkg.set("/resdesc", e.getMessage());
			resPkg.set("/seqno", seqno);
			logger.warn("catch a SevErr");
			logger.warn("Exception "+e);
		}
		catch(Exception e){
			resPkg = new DBPackage();
			resPkg.set("/rescode", 9999);
			resPkg.set("resdesc", e.getMessage());
			resPkg.set("/seqno", seqno);
			logger.warn("catch a exception");
			
			StackTraceElement ste[] = e.getStackTrace();
			for(int i=0;i<ste.length;i++){
				logger.warn(ste[i].toString());
			}
			logger.warn("Exception "+e);
		}
		
		seqPkg.set("/response", resPkg.copy());
		seqPkg.set("/endtime", new Date());
		seqPkg.set("/rescode", resPkg.getInt("/rescode"));
		seqPkg.set("/resdesc", resPkg.getString("/resdesc"));
		if(tblSeq != null)tblSeq.save(seqPkg);
		setCookie(resPkg,arg1);
		
		logger.info("response:"+resPkg.toString());
		return new ModelAndView(view,"resPkg",seqPkg);
	}
	
	public void setCookie(IPackage resPkg,HttpServletResponse hsr){
		IPackage cpkg = (IPackage)resPkg.getObj("/pub/cookies");
		if(cpkg==null){
			logger.info("No Cookies"+cpkg);
			return ;
		}
		Map<?,?> cmap = cpkg.toMap();
		for(Object key : cmap.keySet()){
			Object val = cmap.get(key);
			if(val instanceof String){
				String str = (String)val;
				Cookie c = new Cookie((String)key,str);
				c.setMaxAge(3600*365);
				c.setPath("/");
				hsr.addCookie(c);
				logger.info("setCookie:"+key+"="+str);
			}
		}
	}
	
	public int doWorking(IUser user,IPackage reqPkg,IPackage resPkg)throws SevErr{
		int ret=0;
		if(workers != null){
			
			logger.debug("do working!");
			for(int i=0;i<workers.size();i++){
				ret = workers.get(i).doWorking(user, reqPkg, resPkg);
			}
			
		}
		return ret;
	}
	
	/**
	 * 
	 */
	public void response(IPackage resPkg){
		
	}
	
	/**
	 * 
	 */
	public void upload(HttpServletRequest req,IPackage pkg){
		
	}
	
	/**
	 * parse req
	 * @param req
	 * @return
	 */
	public IPackage parseMsg(HttpServletRequest req) throws SevErr{
		logger.debug("parse msg from:"+msg);
		if(parser != null){
			return parser.parseMsg(req);
		}
		
		Cookie cookies[] = req.getCookies();
		DBPackage retPkg = new DBPackage();

		String oriPkg[]=req.getParameterValues(msg);
		if(oriPkg==null){
			logger.warn("msg is null");
			throw new SevErr(2701,"msg is null");
		}
		if(oriPkg.length<=0){
			logger.warn("msg is null");
			throw new SevErr(2702,"msg is null");
		}
		
		logger.info("msg is:"+oriPkg[0]);
		try{
			retPkg=DBPackage.parse(oriPkg[0]);
		}catch(JSONParseException e){
			logger.warn("parse msg failed");
			throw new SevErr(2703,"parse msg failed");
		}
		
		DBPackage cs = new DBPackage();
		retPkg.set("/pub/cookies", cs);
		for(int i=0;i<cookies.length;i++){
			cs.put(cookies[i].getName(), cookies[i].getValue());
		}
		return retPkg;
	}
	
	public void setMsg(String msg){
		this.msg=msg;
	}
	public String getMsg(){
		return this.msg;
	}
	public void setWorkers(List<IWorker> workers){
		this.workers = workers;
	}
	public void setUser(IUser user){
		this.user = user;
	}
	public void setParser(IParser parser){
		this.parser = parser;
	}
	public void setView(String view){
		this.view = view;
	}
	public void setTblSeq(ITable tblSeq){
		this.tblSeq = tblSeq;
	}
}
