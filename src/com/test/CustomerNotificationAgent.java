package com.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseTaskAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class CustomerNotificationAgent extends YCPBaseTaskAgent {

	 private static YFCLogCategory logger = YFCLogCategory.instance(CustomerNotificationAgent.class.getName());
	protected static YIFApi api=null;
	@Override
	public Document executeTask(YFSEnvironment env, Document inputDoc) throws Exception {
		 logger.beginTimer("CustomTaskAgent.executeTask");
			 
		 try {
			 if(inputDoc!=null) {
				 System.out.println("The document is not null"+inputDoc);
				 logger.info("THis is the input document which is not null"+inputDoc);
			Element rootEl=inputDoc.getDocumentElement();
			String OrderHeaderKey=rootEl.getAttribute("DataKey");
			String DocumentType=rootEl.getAttribute("DocumentType");
	//		String HoldFlag=rootEl.getAttribute("HoldFlag");
				YFCDocument newDoc=YFCDocument.createDocument();
				YFCElement newrootEl=newDoc.createElement("Order");
				newrootEl.setAttribute("Action","MODIFY");
				newrootEl.setAttribute("OrderHeaderKey",OrderHeaderKey);
				newrootEl.setAttribute("DocumentType", DocumentType);
				newrootEl.setAttribute("OrderName", "TestingCustomOrder");
				Document newInputDoc=newDoc.getDocument();
				System.out.println("This is the document"+newInputDoc);
				logger.info("this is the document"+newInputDoc);
				getApi().invoke(env, "changeOrder", newInputDoc);
				
				// For getOrderDetails
				YFCDocument getOrderDoc = YFCDocument.createDocument("Order");
				getOrderDoc.getDocumentElement().setAttribute("OrderHeaderKey", OrderHeaderKey);
				Document output = getApi().invoke(env, "getOrderDetails", getOrderDoc.getDocument());

				//Document output=getApi().invoke(env, "getOrderDetails", newInputDoc);
				logger.info("This is the output Doc going into customer integration server"+output);
				System.out.println("This is the output doc going into integration server"+output);
				 getApi().executeFlow(env, "CustomerNotificationIntegrationServer", output);
			 }
			 
		 }catch(Exception e) {
			 throw new Exception("This is exception from custom agent"+e);
		 }
		// TODO Auto-generated method stub
		return inputDoc;
	}
	private YIFApi getApi() throws Exception {
		if(api==null) {
			api=YIFClientFactory.getInstance().getLocalApi();
		}
		return api;
		
	}

}
