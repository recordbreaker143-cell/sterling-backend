package com.test;

import java.io.IOException;
//import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

//import com.jcraft.jsch.Logger;
//import com.sterlingcommerce.woodstock.util.frame.log.Logger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSProcessOrderHoldTypeUE;

public class BuyerRemorseHoldResolution implements YFSProcessOrderHoldTypeUE {
	private static final Logger log =Logger.getLogger(BuyerRemorseHoldResolution.class);
	@Override
	public Document processOrderHoldType(YFSEnvironment env, Document InputDoc) throws YFSUserExitException {
		try {
			YIFApi api=YIFClientFactory.getInstance().getApi();
		//	DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//	Document InputDoc=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\createOrderSuccess.xml");
          //  Element rootEl=InputDoc.getDocumentElement();
			
            YFCElement rootEl = YFCDocument.getDocumentFor(InputDoc).getDocumentElement();
            String Holdflag=rootEl.getAttribute("HoldFlag");
            log.info("This is the HoldFlag"+Holdflag);
            System.out.println("HoldFlag"+Holdflag);
            YFCElement holdTypes = rootEl.getChildElement("OrderHoldTypes");
            YFCElement holdType = holdTypes.getChildElement("OrderHoldType");
            String holdTypeValue = holdType.getAttribute("HoldType");
            log.info("HoldType"+holdTypeValue);
            System.out.println("HoldTypeValue"+holdTypeValue);
            if(Holdflag.equalsIgnoreCase("Y") && "BUYER_REMORSE".equalsIgnoreCase(holdTypeValue)) {
            	log.debug("Inside If condition logic");
            	YFCDocument newDoc=YFCDocument.createDocument("Order");
            	YFCElement newRootEl=newDoc.getDocumentElement();
            	String OrderNo=rootEl.getAttribute("OrderNo");
            	String OrderHeaderKey=rootEl.getAttribute("OrderHeaderKey");
            	String DocumentType=rootEl.getAttribute("DocumentType");
            	newRootEl.setAttribute("OrderHeaderKey",OrderHeaderKey);
            	newRootEl.setAttribute("OrderNo", OrderNo);
            	newRootEl.setAttribute("DocumentType", DocumentType);
            	YFCElement HoldTypes=newRootEl.createChild("OrderHoldTypes");
            	YFCElement HoldType=HoldTypes.createChild("OrderHoldType");
            	HoldType.setAttribute("HoldType", "BUYER_REMORSE");
            	HoldType.setAttribute("Status", "1300");
            	HoldType.setAttribute("ReasonText", "Resolving Hold from UE");
            	log.debug("Input XMl for the changeOrder"+newDoc.toString());
            	System.out.println("Input XML for changeOrder"+newDoc.toString());
            	api.invoke(env, "changeOrder", newDoc.getDocument());
            }
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return InputDoc;
	}

}
