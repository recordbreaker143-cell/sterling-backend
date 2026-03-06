package com.test;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yantra.yfs.japi.YFSEnvironment;

public class CreateShipmentFromMsg {
	
		
	
	public Document createShimpents(YFSEnvironment env,Document inputDoc) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		inputDoc=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\createshipmnetMessage.xml");
		Element rootEl=inputDoc.getDocumentElement(); 
		NodeList releaseNo=rootEl.getElementsByTagName("fulfillment_id");
		String release=""; 
		if(releaseNo.getLength()>0) {
			release=releaseNo.item(0).getTextContent().split("_")[1];
		}
		Document newDoc=db.newDocument();
		HashMap shipmnentTagMap=new HashMap<>();
		shipmnentTagMap.put("order_number","OrderNo");
		shipmnentTagMap.put("shipment_number","ShipmentNo");
		shipmnentTagMap.put("shipment_node","ShipNode");
		//shipmnentTagMap.put("order_num","OrderNo");
		Element newrootel=newDoc.createElement("Shipment");
		newrootel.setAttribute("ReleaseNo", release);
		newDoc.appendChild(newrootel);
		NodeList shipmentLines=(NodeList)newDoc.createElement("ShipmentLines");
		newrootel.appendChild((Node) shipmentLines);
		String[] tags={"order_number","shipment_number","shipment_node"};
		for(String tag : tags) {
			NodeList nodelist=rootEl.getElementsByTagName(tag);
			if(nodelist.getLength()>0) {
				Element el=(Element) nodelist.item(0);
				String txt=el.getTextContent();
			newrootel.setAttribute(shipmnentTagMap.get(tag).toString(), txt);	
			}
		}
		NodeList HandlingUnit=rootEl.getElementsByTagName("item");
		System.out.println(HandlingUnit.getLength());
		for(int i=0;i<HandlingUnit.getLength();i++) {
			Element shipmentLine=newDoc.createElement("ShipmentLine");
			((Node) shipmentLines).appendChild(shipmentLine);
			Node n=HandlingUnit.item(i);
			if(n.getNodeType()==Node.ELEMENT_NODE) {
				Element el=(Element) n;
				String val=el.getElementsByTagName("product_ref").item(0).getTextContent();
						String qty=el.getElementsByTagName("quantity").item(0).getTextContent();
						String shipnode=rootEl.getElementsByTagName("shipment_node").item(0).getTextContent();
						shipmentLine.setAttribute("ShipNode",shipnode);
						shipmentLine.setAttribute("ItemID", val);
						shipmentLine.setAttribute("Quantity",qty);
			}
		}
		 Transformer transformer = TransformerFactory.newInstance().newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.transform(new DOMSource(newDoc), new StreamResult(System.out));

		System.out.println(transformer);
		
		
		return null;
	
	}
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		CreateShipmentFromMsg cs=new CreateShipmentFromMsg();
		cs.createShimpents(null, null);
		
	}
}
