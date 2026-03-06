package com.test;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.fop.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class CreateTaskQRecord {
protected static YIFApi api=null;

	public Document createTaskQ(YFSEnvironment env, Document inputDoc) throws YFSException, YIFClientCreationException, ParserConfigurationException, SAXException, IOException {
		//DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//inputDoc=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\releaseOrder.xml");
		if(inputDoc==null) {
			return inputDoc;
		}
	
		System.out.println("InputDocument for creating TaskQ record"+inputDoc);
		Document doc=createTaskQDocument(env,inputDoc);
		
		Document returnDoc=getApi().invoke(env, "manageTaskQueue", doc);
		//Document returnDoc=null;
		return inputDoc;
	}
	public Document createTaskQDocument(YFSEnvironment env,Document inputDoc) {
		String orderHeaderKey=inputDoc.getDocumentElement().getAttribute("OrderHeaderKey");
		YFCDocument yfcDoc = YFCDocument.createDocument("TaskQueue");
		YFCElement rootEl = yfcDoc.getDocumentElement();
		rootEl.setAttribute("DataType","OrderHeaderKey");
		rootEl.setAttribute("DataKey", orderHeaderKey);
		rootEl.setAttribute("TransactionId","CUSTOMER_NOTIFICATION.0001.ex");
		rootEl.setAttribute("HoldFlag", "N");
		rootEl.setAttribute("Operation", "Manage");
		System.out.println("This is output"+rootEl);
		return rootEl.getOwnerDocument().getDocument();
	}
	protected YIFApi getApi() throws YIFClientCreationException {
		if(api==null) {
			api=YIFClientFactory.getInstance().getLocalApi();
		}
		return api;
	}
//	public static void main(String[] args) throws YFSException, YIFClientCreationException, ParserConfigurationException, SAXException, IOException {
//		CreateTaskQRecord cr=new CreateTaskQRecord();
//		cr.createTaskQ(null, null);
//	}
}
