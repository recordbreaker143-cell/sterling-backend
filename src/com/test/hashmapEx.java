package com.test;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.datastax.driver.core.LocalDate;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.util.YFCIterableIterator;

public class hashmapEx {

	
	public void itemquantity() throws ParserConfigurationException, SAXException, IOException {
		
	Map<String,Integer> map=new HashMap<>();
	DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
//	LocalDate now= LocalDate.now();
	Document inputDoc=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\orders.xml");
	YFCElement rootEl=YFCDocument.getDocumentFor(inputDoc).getDocumentElement();
	//YFCElement orderLines=rootEl.getChildElement("OrderLines");
	YFCIterableIterator<YFCElement> list= (YFCIterableIterator<YFCElement>) rootEl.getChildren("OrderLine") ;
	while(list.hasNext()) {
		YFCElement el=list.next();
		String nodename=el.getAttribute("Node");
		int Qty=Integer.parseInt(el.getAttribute("Quantity"));
		if(map.containsKey(nodename)) {
			int oldQty=map.get(nodename);
			map.put(nodename, Qty+oldQty);
		}
		else {
			map.put(nodename,Qty);
		}
	};
//	for(int i=0;i<list.getLength();i++) {
//			//Element el=(Element) n;
//		YFCElement el=(YFCElement) list.item(i);
//			String nodename=el.getAttribute("Node");
//			int Qty=Integer.parseInt(el.getAttribute("Quantity"));
//		
//			if(map.containsKey(nodename)) {
//				int oldQty=map.get(nodename);
//				map.put(nodename, Qty+oldQty);
//			}
//			else {
//				map.put(nodename,Qty);
//			}
//	}
	System.out.println(map);
	System.out.println(map.keySet());
	}
	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
hashmapEx ex=new hashmapEx();
ex.itemquantity();
	}

}
