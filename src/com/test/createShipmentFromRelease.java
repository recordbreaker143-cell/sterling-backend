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

public class createShipmentFromRelease {
	
//	public static String documentToString(Document doc) {
//        try {
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            // indentation for many transformers:
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//            StringWriter sw = new StringWriter();
//            transformer.transform(new DOMSource(doc), new StreamResult(sw));
//            return sw.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

	public Document createShipment(YFSEnvironment env,Document inputDoc) throws ParserConfigurationException, SAXException, IOException, YIFClientCreationException {
		
		YIFApi api=YIFClientFactory.getInstance().getApi();
		
		DocumentBuilder DB=DocumentBuilderFactory.newInstance().newDocumentBuilder();
	//	Document inputDoc=DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\releaseOrder.xml");
		Element rootEl=inputDoc.getDocumentElement();
		String orderNo=rootEl.getAttribute("OrderNo");
		String orderHeaderKey=rootEl.getAttribute("OrderHeaderKey");
		String documentType=rootEl.getAttribute("DocumentType");
		String enterpriseCode=rootEl.getAttribute("EnterpriseCode");
		String ShipNode="HeavyLiftWH1";
		NodeList ItemLine=rootEl.getElementsByTagName("Item");
		Element qty=(Element) rootEl.getElementsByTagName("OrderLine").item(0);
		String orderedQty=qty.getAttribute("OrderedQty");
		int Qty=(int) Double.parseDouble(orderedQty);
		String Quantity=String.valueOf(Qty);
		Element IL=(Element) ItemLine.item(0);
		String ItemID=IL.getAttribute("ItemID");
		String uom=IL.getAttribute("UnitOfMeasure");
		Document shipDoc=DB.newDocument();
		Element shipRootEl=shipDoc.createElement("Shipment");
		Element shipmentLines=shipDoc.createElement("ShipmentLines");
		Element shipmentLine=shipDoc.createElement("ShipmentLine");
		shipmentLine.setAttribute("ItemID", ItemID);
		shipmentLine.setAttribute("Quantity", Quantity);
		shipmentLine.setAttribute("PrimeLineNo", "1");
		shipmentLine.setAttribute("SubLineNo", "1");
		shipmentLine.setAttribute("UnitOfMeasure", uom);
		shipmentLine.setAttribute("OrderNo", orderNo);
		shipmentLine.setAttribute("ReleaseNo", "1");
		shipmentLine.setAttribute("ShipNode", ShipNode);
		shipmentLines.appendChild(shipmentLine);
		shipRootEl.appendChild(shipmentLines);
		shipRootEl.setAttribute("OrderHeaderKey", orderHeaderKey);
		shipRootEl.setAttribute("EnterpriseCode", enterpriseCode);
		shipRootEl.setAttribute("DocumentType", documentType);
		
		shipDoc.appendChild(shipRootEl);
	//	System.out.println(documentToString(shipDoc));
		Document outputDoc=api.invoke(env, "createShipment", shipDoc);
		return outputDoc;
	}
	
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		createShipmentFromRelease cs=new createShipmentFromRelease();
//		cs.createShipment();
//	}
	
}



  

