package com.test;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	private static final Logger log=Logger.getLogger(CustomOrderInvoice.class.getName());
	protected static YIFApi api=null;
	public Document customOrderInvoice(YFSEnvironment env,Document inputDoc) throws YFSException, RemoteException, YIFClientCreationException {
		LocalDateTime unqVal=LocalDateTime.now();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		 String formatted = unqVal.format(formatter);
		 log.info("Entering the customOrderInvoice...");
		 System.out.println("Entering customOrdeinvoice");
		 YFCDocument yfcDoc = YFCDocument.getDocumentFor(inputDoc);
		 YFCElement rootEl = yfcDoc.getDocumentElement();
		log.debug("RootElement is extracted..."+rootEl);
		YFCIterableIterator<YFCElement> orderline=(YFCIterableIterator<YFCElement>) rootEl.getChildren("OrderLines");
		while(orderline.hasNext()) {
			YFCElement ol=orderline.next();
			log.info("Creating a new Document for the Custom Invoices...");
			System.out.println("Creating a new Document for the Custom Invoices...");
			YFCDocument newDoc=YFCDocument.createDocument("ExtnInvoices");
			log.debug("Document is created"+newDoc);
			//String InvoiceNo=(String) (rootEl.getAttribute("OrderNo")+'-'+ol.getAttribute("PrimeLineNo"));
			String InvoiceNo = rootEl.getAttribute("OrderNo") + "-" + ol.getAttribute("PrimeLineNo");
			YFCElement newRootEl=newDoc.getDocumentElement();
			newRootEl.setAttribute("InvoiceNo", InvoiceNo);
			String InvoiceKey=formatted+ol.getAttribute("PrimeLineNo");
			log.debug("Invoice Key is generated with ID"+InvoiceKey);
			newRootEl.setAttribute("ExtnInvoiceKey", InvoiceKey);
			newRootEl.setAttribute("ExtnOrderLineKey", ol.getAttribute("OrderLineKey"));
			log.info("Document is created for the custom service invocation...");
			try {
			    getApi().executeFlow(env, "InvoiceServics", newDoc.getDocument());
			    System.out.println("Api Invoked successfully");
			    log.info("API invoked successfully for InvoiceKey=" + InvoiceKey);
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
}
