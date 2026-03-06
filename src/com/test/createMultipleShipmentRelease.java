package com.test;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfs.japi.YFSEnvironment;

public class createMultipleShipmentRelease {
	
//	public static String documentToString(Document doc) {
//      try {
//          TransformerFactory tf = TransformerFactory.newInstance();
//          Transformer transformer = tf.newTransformer();
//          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//          // indentation for many transformers:
//          transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//          StringWriter sw = new StringWriter();
//          transformer.transform(new DOMSource(doc), new StreamResult(sw));
//          return sw.toString();
//      } catch (Exception e) {
//          e.printStackTrace();
//          return null;
//      }
 // }
	public Document createMultiLineShipment(YFSEnvironment env,Document inputDoc) throws ParserConfigurationException, SAXException, IOException, YIFClientCreationException {
		YIFApi api=YIFClientFactory.getInstance().getApi();
		DocumentBuilder DB=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//Document inputDoc=DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\releaseOrder.xml");
		Element rootEl=inputDoc.getDocumentElement();
		Document shipDoc=DB.newDocument();
		Element shipRootEl=shipDoc.createElement("Shipment");
		NodeList orderlines=rootEl.getElementsByTagName("OrderLine");
		Element shipmentLines=shipDoc.createElement("ShipmentLines");
		shipRootEl.appendChild(shipmentLines);
		String docType=rootEl.getAttribute("DocumentType");
		String entCode=rootEl.getAttribute("EnterpriseCode");
		String orderHeaderKey=rootEl.getAttribute("OrderHeaderKey");
		shipRootEl.setAttribute("DocumentType", docType);
		shipRootEl.setAttribute("EnterpriseCode", entCode);
		shipRootEl.setAttribute("OrderHeaderKey", orderHeaderKey);
		shipDoc.appendChild(shipRootEl);
		int releaseNo=1;
		for(int i=0;i<orderlines.getLength();i++) {
			Element ol=(Element) orderlines.item(i);
			Element shipmentLine=shipDoc.createElement("ShipmentLine");
			Element sl=(Element) shipmentLines.appendChild(shipmentLine);
			String orderdedQty=ol.getAttribute("OrderedQty");
			String primeLineNo=ol.getAttribute("PrimeLineNo");
			String subLineNo=ol.getAttribute("SubLineNo");
			String orderLineKey=ol.getAttribute("OrderLineKey");
			String shipNode=ol.getAttribute("ShipNode");
			Element itemLine=(Element)ol.getElementsByTagName("Item").item(0);
			Element orderRelease=(Element)ol.getElementsByTagName("OrderStatus").item(0);
			String orderReleaseKey=orderRelease.getAttribute("OrderReleaseKey");
			String ItemID=itemLine.getAttribute("ItemID");
			String uom=itemLine.getAttribute("UnitOfMeasure");
		//	String unitCost=itemLine.getAttribute("UnitCost");
			sl.setAttribute("ItemID",ItemID);
			sl.setAttribute("UnitOfMeasure", uom);
			sl.setAttribute("PrimeLineNo", primeLineNo);
			sl.setAttribute("SubLineNo", subLineNo);
			sl.setAttribute("Quantity",orderdedQty);
			sl.setAttribute("ShipNode", shipNode);
			sl.setAttribute("ReleaseNo", String.valueOf(releaseNo));
			sl.setAttribute("OrderLineKey", orderLineKey);
			sl.setAttribute("OrderReleaseKey", orderReleaseKey);
			//shipmentLines.appendChild(sl);
		}
		Document outputDoc=api.invoke(env, "createShipment", shipDoc);
		return outputDoc;
	//	System.out.println(documentToString(shipDoc));
	}
	
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		createMultipleShipmentRelease cm=new createMultipleShipmentRelease();
//		cm.createMultiLineShipment();
//	}

}
