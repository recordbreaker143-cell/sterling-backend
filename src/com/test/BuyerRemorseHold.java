package com.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.ycp.japi.util.YCPBaseTaskAgent;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class BuyerRemorseHold extends YCPBaseAgent{
	YFCLogCategory log=YFCLogCategory.instance(BuyerRemorseHold.class);
	YIFApi api=null;
	String outputgetOrderListXML="<OrderList LastOrderHeaderKey=\"\" LastRecordSet=\"\" ReadFromHistory=\"\" TotalOrderList=\"\">\r\n"
			+ "    <Order>\r\n"
			+ "<OrderHoldTypes>\r\n"
			+ "<OrderHoldType HoldType=\"\" LastHoldTypeDate=\"\" OrderAuditKey=\"\" OrderHeaderKey=\"\" OrderLineKey=\"\" ReasonText=\"\" ResolverUserId=\"\" Status=\"\" StatusDescription=\"\" TransactionId=\"\" TransactionName=\"\">\r\n"
			+ "<OrderHoldTypeLogs>\r\n"
			+ "<OrderHoldTypeLog OrderAuditKey=\"\" OrderHoldTypeLogKey=\"\" ReasonText=\"\" ResolverUserId=\"\" Status=\"\" StatusDescription=\"\" TransactionId=\"\" TransactionName=\"\" UserId=\"\"/>\r\n"
			+ "</OrderHoldTypeLogs>\r\n"
			+ "</OrderHoldType>\r\n"
			+ "</OrderHoldTypes>\r\n"
			+ "    </Order>\r\n"
			+ "</OrderList>";
	DocumentBuilder DB=null;
	public List getJobs(YFSEnvironment env, Document inXML, Document lastMessage) throws Exception {
		List aList=new ArrayList<Document>();
		String Key=null;
		Document Output=null;
		log.info("Running getJobs for the BuyerRemorseHoldList Orders...");
		System.out.println("Running getJobs for the BuyerRemorseHoldList Orders...");
		if(lastMessage!=null) {
			log.info("Last Message found processing with last message key...");
			System.out.println("Last Message found processing with last message key...");
			Element rootEl=lastMessage.getDocumentElement();
			log.debug("found root element..."+rootEl);
			System.out.println("found root element..."+rootEl);
			Key=rootEl.getAttribute("TaskQKey");
			env.setApiTemplate("getOrderList", outputgetOrderListXML);
			Output=getApi().invoke(env, "getOrderList", createListDocument(rootEl,Key));
			log.debug("Output returned successfully with list of orders"+Output.toString());
			System.out.println("Output returned successfully with list of orders"+Output.toString());
			env.clearApiTemplate("getOrderList");
		}
		else {
			Element rootEl=inXML.getDocumentElement();
			env.setApiTemplate("getOrderList", outputgetOrderListXML);
			Output=getApi().invoke(env, "getOrderList", createListDocument(rootEl,null));
			log.debug("Output returned successfully with list of orders"+Output.toString());
			System.out.println("Output returned successfully with list of orders"+Output.toString());
			env.clearApiTemplate("getOrderList");
		}
		
		Element OutputRootEl=Output.getDocumentElement();
		NodeList OrderList=OutputRootEl.getElementsByTagName("Order");
		for(int i=0;i<OrderList.getLength();i++) {
			Node node=OrderList.item(i);
				Element el=(Element) node;
				NodeList holdList=el.getElementsByTagName("OrderHoldType");
			if(holdList.getLength()!=0) {
				Document newListDoc=getDocumentInstance().newDocument();
				Element e=(Element) newListDoc.importNode(el,true);
				newListDoc.appendChild(e);
				log.debug("Before adding the document to aList"+newListDoc);
				System.out.println("Before adding the document to aList"+newListDoc);
				aList.add(newListDoc);
			}	
		}
		log.info("getJobs is successful");
		System.out.println("getJobs is successful");
		return aList;
	}
	
	public Document createListDocument(Element rootEl, String lastKey) throws ParserConfigurationException {
		log.info("Entered the list create input method...");
		System.out.println("Entered the list create input method...");
		Document inputDoc=getDocumentInstance().newDocument();
		Element DocRootEl=inputDoc.createElement("Order");
		inputDoc.appendChild(DocRootEl);
		DocRootEl.setAttribute("RetentionDays", rootEl.getAttribute("RetentionDays"));
		DocRootEl.setAttribute("NumRecordsToBuffer",rootEl.getAttribute("NumRecordsToBuffer"));
		DocRootEl.setAttribute("MaximumRecords", rootEl.getAttribute("MaximumRecords"));
		if(lastKey!=null && !lastKey.isEmpty()) {
			DocRootEl.setAttribute("OrderHeaderKey", lastKey);
			DocRootEl.setAttribute("OrderHeaderKeyQryType", "LT");
		}
		log.info("List Document prepared before sending to getOrderlist api"+inputDoc);
		System.out.println("List Document prepared before sending to getOrderlist api"+inputDoc);
		return inputDoc;
	}

	
	protected YIFApi getApi() throws YIFClientCreationException{
		if(api==null) {
			api=YIFClientFactory.getInstance().getLocalApi();
		}
		return api;
	}
	
	protected DocumentBuilder getDocumentInstance() throws ParserConfigurationException {
		if(DB==null) {
		DB=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		return DB;
	}
	
	
	public Document ChangeOrderDoc(Document inputDoc) throws ParserConfigurationException{
		log.info("Entered the change order document creation method...");
		System.out.println("Entered the change order document creation method...");
		Element rootEl=inputDoc.getDocumentElement();
		String OrderNo=rootEl.getAttribute("OrderNo");
		String OrderHeaderKey=rootEl.getAttribute("OrderHeaderKey");
		String DocumentType=rootEl.getAttribute("DocumentType");
		String EnterpriseCode=rootEl.getAttribute("EnterpriseCode");
		Node holdType=rootEl.getElementsByTagName("OrderHoldType").item(0);
		Element hold=(Element) holdType;
		String HoldValue=hold.getAttribute("HoldType");
		Document newDoc=getDocumentInstance().newDocument();
		Element el=newDoc.createElement("Order");
		newDoc.appendChild(el);
		el.setAttribute("OrderNo",OrderNo);
		el.setAttribute("OrderHeaderKey", OrderHeaderKey);
		el.setAttribute("DocumentType", DocumentType);
		el.setAttribute("HoldFlag", "N");
		el.setAttribute("EnterpriseCode", EnterpriseCode);
		Element HoldTypes=newDoc.createElement("OrderHoldTypes");
		Element HoldType=newDoc.createElement("OrderHoldType");
		el.appendChild(HoldTypes);
		HoldTypes.appendChild(HoldType);
		HoldType.setAttribute("HoldType", HoldValue);
		HoldType.setAttribute("Status", "1300");
		log.info("This is the changeOrder api input xml"+newDoc);
		System.out.println("This is the changeOrder api input xml"+newDoc);
		return newDoc;
	}
	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc) throws Exception {
		// TODO Auto-generated method stub
		if(inputDoc!=null) {
			getApi().invoke(env,"changeOrder",ChangeOrderDoc(inputDoc));
		}else {
			log.info("No input Document is provided...");
			System.out.println("No input Document is provided...");
		}
	}
}
