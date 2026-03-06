package com.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import com.yantra.interop.japi.YIFApi;
//import com.yantra.interop.japi.YIFClientCreationException;
//import com.yantra.interop.japi.YIFClientFactory;

public class cancelOrder {
	public void CancelOrderLines() throws ParserConfigurationException, SAXException, IOException {
		Map<String, String> map=new HashMap<>();
		DocumentBuilder DB=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document inputDoc=DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\releaseOrder.xml");
		Element rootEl=inputDoc.getDocumentElement();
	//	YIFApi api=YIFClientFactory.getInstance().getApi();
		NodeList orderlines=rootEl.getElementsByTagName("OrderLine");
		for(int i=0; i< orderlines.getLength();i++) {
			Element ol=(Element) orderlines.item(i);
			NodeList  itemline=ol.getElementsByTagName("Item");
				Element itemID=(Element) itemline.item(0);
				String itemname=itemID.getAttribute("ItemID");
				String itemdesc=itemID.getAttribute("ItemShortDesc");
				map.put(itemname,itemdesc);
		}
		System.out.println(map);
	}
public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		cancelOrder co=new cancelOrder();
		co.CancelOrderLines();
	}
}
