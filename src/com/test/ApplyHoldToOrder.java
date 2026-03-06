package com.test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;

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

import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfs.japi.YFSEnvironment;

public class ApplyHoldToOrder {
//	static YIFApi oApi=null;
//	 public static String documentToString(Document doc) {
//	        try {
//	            TransformerFactory tf = TransformerFactory.newInstance();
//	            Transformer transformer = tf.newTransformer();
//	            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//	            // indentation for many transformers:
//	            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//	            StringWriter sw = new StringWriter();
//	            transformer.transform(new DOMSource(doc), new StreamResult(sw));
//	            return sw.toString();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return null;
//	        }
//	    }
//	 
//	 <OrderHoldTypes>
//	 <OrderHoldType HoldType="Required" ReasonText="" ResolverUserId="" Status=""/>
//	 </OrderHoldTypes>
	public Document applyHold(YFSEnvironment env,Document inputDoc) throws SAXException, IOException, ParserConfigurationException, YIFClientCreationException {
		
		YIFApi api = YIFClientFactory.getInstance().getApi();
		 
		DocumentBuilderFactory DBF=DocumentBuilderFactory.newInstance();
		DocumentBuilder DB=DBF.newDocumentBuilder();
//		Document inputDoc=DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\createOrderSuccess.xml");
		Element rootElement=inputDoc.getDocumentElement();
		String orderHeaderKey=rootElement.getAttribute("OrderHeaderKey");
		String orderNo=rootElement.getAttribute("OrderNo");
		Document outputDoc=DB.newDocument();
		Element outputEle=outputDoc.createElement("Order");
		outputEle.setAttribute("OrderNo", orderNo);
		outputEle.setAttribute("OrderHeaderKey", orderHeaderKey);
		outputEle.setAttribute("HoldFlag", "Y");
		outputEle.setAttribute("HoldReasonCode", "Address Validation");
		Element HoldList=outputDoc.createElement("OrderHoldTypes");
		Element Hold=outputDoc.createElement("OrderHoldType");
		Hold.setAttribute("HoldType", "YCD_VERIFY_ADDRESS");
		HoldList.appendChild(Hold);
		outputEle.appendChild(HoldList);
	//	System.out.println(orderNo);
		//System.out.println(orderHeaderKey);
		outputDoc.appendChild(outputEle);
	//	System.out.println(documentToString(outputDoc));
		Document output = api.invoke(env, "changeOrder", outputDoc);
		return output;
	}
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		// TODO Auto-generated method stub	
//		ApplyHoldToOrder ap=new  ApplyHoldToOrder();
//		ap.applyHold();
//	}
//	@Override
//	public void setProperties(Properties arg0) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}

}
