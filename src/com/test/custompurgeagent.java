package com.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class custompurgeagent extends YCPBaseAgent{
	private static YFCLogCategory log=YFCLogCategory.instance(custompurgeagent.class);
	protected static YIFApi api=null;

	public List<Document> getJobs(YFSEnvironment env, Document criteria, Document lastMessageCreated) throws Exception{
	//	DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		YFCDocument yfcdoc=YFCDocument.getDocumentFor(criteria);
		System.out.println("get jobs called with criteria"+ yfcdoc.getString());
		 log.info("getJobs called with criteria: " + yfcdoc.getString());
	List<Document> jobs=new ArrayList<>();
	String lastkey=null;
	Document output;
	if (lastMessageCreated != null) {
	    YFCElement ele = YFCDocument.getDocumentFor(lastMessageCreated).getDocumentElement();
	    lastkey = ele.getAttribute("DeliveryKey");
	    output = getApi().executeFlow(env, "getDLServiceList",
	              createListInput(criteria.getDocumentElement(), lastkey));
	    System.out.println("you are in else condition"+YFCDocument.getDocumentFor(output).getString());
	    log.info("you are in else condition"+YFCDocument.getDocumentFor(output).getString());
	} else {
	  output = getApi().executeFlow(env, "getDLServiceList",createListInput(criteria.getDocumentElement(), null));
		//output=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\agentmessage.xml");
	    System.out.println("you are in else condition"+YFCDocument.getDocumentFor(output).getString());
	    log.info("you are in else condition"+YFCDocument.getDocumentFor(output).getString());
	}

	YFCDocument yfcdoc1 = YFCDocument.getDocumentFor(output);
	System.out.println("list api output" + yfcdoc1.getString());
	log.info("list api output" + yfcdoc1.getString());

	NodeList nodeList = output.getDocumentElement().getElementsByTagName("DeliveryInstructions");
	for (int i = 0; i < nodeList.getLength(); i++) {
	    Element record = (Element) nodeList.item(i);
	    String key = record.getAttribute("DeliveryKey");
	    if (key != null && !key.isEmpty()) {
	        jobs.add(createJobDocument(key));
	        log.info("Created job for key: " + key);
	    }
	}
	System.out.println("Created job for key:"+ lastkey);
	log.info("Created job for key: " + lastkey);
	log.info("these are the jobs"+jobs);
	System.out.println("these are the jobs"+jobs);
	return jobs;
	}
	private Document createListInput(Element criteriaElement, String lastKey) {
	    // Create the YFCDocument and root element
	    YFCDocument inputDoc = YFCDocument.createDocument("DeliveryInstructionsList");
	    YFCElement input = inputDoc.getDocumentElement();

	    // Set attributes
	    input.setAttribute("RetentionDays", criteriaElement.getAttribute("RetentionDays"));
	    input.setAttribute("MaximumRecords", criteriaElement.getAttribute("NumRecordsToBuffer"));
	    input.setAttribute("Live", criteriaElement.getAttribute("Live"));
	    if (lastKey != null && !lastKey.isEmpty()) {
	        input.setAttribute("DeliveryKey", lastKey);
	        input.setAttribute("DeliveryKeyQryType", "GT");
	    }

	    // ✅ Log using inputDoc directly
	    System.out.println("this is input xml for passing into get jobs " + inputDoc.getString());
	    log.info("This is the input xml created for passing into get jobs " + inputDoc.getString());

	    // Return the underlying W3C Document
	    return inputDoc.getDocument();
	}
	   private Document createJobDocument(String key) {
	        YFCElement record = YFCDocument.createDocument("DeliveryInstructions").getDocumentElement();
	        record.setAttribute("DeliveryKey", key);
	        System.out.println("This is the job document"+ record.getString());
	        log.info("This is the job document"+ record.getString());
	        return record.getOwnerDocument().getDocument();
	    }

	    private YIFApi getApi() throws Exception {
	        if (api == null) {
	            api = YIFClientFactory.getInstance().getLocalApi();
	        }
	        return api;
	    }


	@Override
	public void executeJob(YFSEnvironment env, Document jobDoc) throws Exception {
		 System.out.println("executeJob:start");
	        if (jobDoc != null) {
	        	System.out.println("executing the job"+YFCDocument.getDocumentFor(jobDoc));
	        	log.info("executing the job"+YFCDocument.getDocumentFor(jobDoc));
	      //      System.out.println("executeJob: " + XMLUtil.getStringFromDocument(jobDoc));
	            // Call your delete/archive API
	         getApi().executeFlow(env, "deleteDeliveryInstructions", jobDoc);
	        }
	        System.out.println("executeJob:end");	// TODO Auto-generated method stub
		
	} 



}
