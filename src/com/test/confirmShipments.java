package com.test;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfs.japi.YFSEnvironment;

public class confirmShipments {
	
	public void ConfirmShipment(YFSEnvironment env,Document inputDoc) throws ParserConfigurationException, SAXException, IOException, YIFClientCreationException {
		DocumentBuilder DB=DocumentBuilderFactory.newInstance().newDocumentBuilder();
	//	Document inputDoc=DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\confirmShipment.xml");
		YIFApi api=YIFClientFactory.getInstance().getApi();
		Element rootEl=inputDoc.getDocumentElement();
		NodeList shiproot=rootEl.getElementsByTagName("Shipment");
		for(int i=0;i<shiproot.getLength();i++) {
			Element shiprootEl=(Element)shiproot.item(i);
			Document shipDoc=DB.newDocument();
			Element shipEl=shipDoc.createElement("Shipment");
			shipDoc.appendChild(shipEl);
			String shipNo=shiprootEl.getAttribute("ShipmentNo");
			String shipKey=shiprootEl.getAttribute("ShipmentKey");
			String docType=shiprootEl.getAttribute("DocumentType");
			String enterprise=shiprootEl.getAttribute("EnterpriseCode");
			shipEl.setAttribute("EnterpriseCode", enterprise);
			shipEl.setAttribute("DocumentType", docType);
			shipEl.setAttribute("ShipmentNo", shipNo);
			shipEl.setAttribute("ShipmentKey", shipKey);
			api.invoke(env, "confirmShipment", shipDoc);
		}
	}

}
