package org.sh.comm;

import java.io.File; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 

import org.apache.log4j.Logger;
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList;

public class XmlParser {
	static private Logger logger = Logger.getLogger(XmlParser.class.getName());
	public XmlParser(){
		
	}
	
	public IPackage parseString(String xml){
		DBPackage pkg = null;
		return pkg;
	}
	
	public IPackage parseFile(String file){
		DBPackage pkg = new DBPackage();
		Document document = parse(file);
		if(document==null)return null;
		Element rootElement = document.getDocumentElement();
		parseNode(rootElement,pkg);
		logger.info(pkg);
		return pkg;
	}
	private void parseNode(Element rootElement,IPackage pkg){
		NodeList nodes = rootElement.getChildNodes(); 
        for (int i=0; i < nodes.getLength(); i++) 
        { 
           Node node = nodes.item(i); 
           String name = node.getNodeName();
           //logger.debug(name);
           //logger.debug(node.getNodeType());
           if (node.getNodeType() == Node.ELEMENT_NODE) {   
        	  //logger.debug("ELEMENT_NODE");
              Element child = (Element) node;
              IPackage subpkg = new DBPackage();
              pkg.set("/"+name, subpkg);
              parseNode(child,subpkg);
           }
        }
        
        NamedNodeMap attr=rootElement.getAttributes();
        if(attr!=null){
           int len=attr.getLength();
           IPackage subpkg = null;
           if(len>0){
	           subpkg = new DBPackage();
	           pkg.set("/<xmlattr>", subpkg);
           }
           
           for(int j=0;j<len;j++){
        	   Node node2=attr.item(j);
        	   //logger.debug(node2.getNodeName());
        	   //logger.debug(node2.getNodeValue());
	           if (node2.getNodeType() == Node.ATTRIBUTE_NODE) { 
	        	   //logger.debug("ATTRIBUTE_NODE");
	               subpkg.set("/"+node2.getNodeName(), node2.getNodeValue());
	            }
           }
        }
	}
	
	public String Pkg2Xml(IPackage pkg){
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
	
	private Document parse(String file){
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		Document document = null;
		try{
			DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
			document = builder.parse(new File(file));
		}catch(Exception e){
			logger.error("parse xml error");
			e.printStackTrace();
			return null;
		}
		return document;
	}
}
