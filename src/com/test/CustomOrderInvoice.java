package com.test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.util.YFCIterableIterator;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class CustomOrderInvoice {
	private static final Logger log=Logger.getLogger(CustomOrderInvoice.class);
	protected static YIFApi api=null;
	public Document customOrderInvoice(YFSEnvironment env,Document inputDoc) throws YFSException, YIFClientCreationException, ParserConfigurationException, SAXException, IOException {
//		DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		Document inputDoc=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\createOrderSuccess.xml");
//		LocalDateTime unqVal=LocalDateTime.now();
//		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//		 String formatted = unqVal.format(formatter);
		 log.info("Entering the customOrderInvoice...");
		// System.out.println("Entering customOrdeinvoice service now");
		YFCDocument yfcDoc = YFCDocument.getDocumentFor(inputDoc);
		 YFCElement rootEl = yfcDoc.getDocumentElement();
		 YFCElement ols=rootEl.getChildElement("OrderLines");
		// log.debug("RootElement is extracted..."+rootEl);
		YFCIterableIterator<YFCElement> orderline=(YFCIterableIterator<YFCElement>) ols.getChildren("OrderLine");
		System.out.println(orderline.toString());
		while(orderline.hasNext()) {
			YFCElement ol=orderline.next();
			log.info("Creating a new Document for the Custom Invoices...");
			System.out.println(ol.toString());
			System.out.println("Creating a new Document for the Custom Invoices...");
			YFCDocument newDoc=YFCDocument.createDocument("ExtnInvoices");
			log.debug("Document is created"+newDoc);
			//String InvoiceNo=(String) (rootEl.getAttribute("OrderNo")+'-'+ol.getAttribute("PrimeLineNo"));
			String InvoiceNo = rootEl.getAttribute("OrderNo") + "-" + ol.getAttribute("PrimeLineNo");
			YFCElement newRootEl=newDoc.getDocumentElement();
			newRootEl.setAttribute("ExtnInvoiceNo", InvoiceNo);
			String InvoiceKey = String.valueOf(System.currentTimeMillis());
			log.debug("Invoice Key is generated with ID"+InvoiceKey);
			log.debug("Invoice Key is generated with ID"+InvoiceNo);
			log.debug(ol.getAttribute("OrderLineKey"));
			newRootEl.setAttribute("ExtnInvoiceKey", InvoiceKey);
			newRootEl.setAttribute("ExtnOrderLineKey", ol.getAttribute("OrderLineKey"));
			log.info("Document is created for the custom service invocation...");
			try {
				YFCDocument output=YFCDocument.createDocument("ExtnInvoices");
				YFCElement outputRootEl=output.getDocumentElement();
				outputRootEl.setAttribute("ExtnOrderLineKey",ol.getAttribute("OrderLineKey"));
				Document outputDoc=getApi().executeFlow(env, "getInvoiceDetails", output.getDocument());
				YFCElement outRoot = YFCDocument.getDocumentFor(outputDoc).getDocumentElement();
				if(!outRoot.hasChildNodes()) {
			    getApi().executeFlow(env, "CreateInvoiceService", newDoc.getDocument());
			    System.out.println("Api Invoked successfully");
			    log.info("API invoked successfully for InvoiceKey=" + InvoiceKey);
				}
			} catch (Exception e) {
			    log.error("Error invoking InvoiceServices for InvoiceKey=" + InvoiceKey, e);
			    throw e; // rethrow so OMS knows it failed
			}
		}
		return inputDoc;
		
	}
	protected YIFApi getApi() throws YIFClientCreationException {
		if(api==null) {
			api=YIFClientFactory.getInstance().getApi();
		}
		return api;
	}
//	public static void main(String[] args) throws YFSException, YIFClientCreationException, ParserConfigurationException, SAXException, IOException {
//		CustomOrderInvoice c=new CustomOrderInvoice();
//		c.customOrderInvoice(null);
//	}
}
