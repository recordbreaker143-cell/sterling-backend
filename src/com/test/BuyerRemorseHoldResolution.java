package com.test;

import java.io.IOException;
//import java.util.logging.Logger;
import java.rmi.RemoteException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.Node;
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
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSProcessOrderHoldTypeUE;

public class BuyerRemorseHoldResolution implements YFSProcessOrderHoldTypeUE {
	private static final Logger log =Logger.getLogger("systemlogger");
	@Override
	public Document processOrderHoldType(YFSEnvironment env, Document InputDoc) throws YFSUserExitException {
		try {
			YIFApi api=YIFClientFactory.getInstance().getApi();
		//	DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//	Document InputDoc=db.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\createOrderSuccess.xml");
          //  Element rootEl=InputDoc.getDocumentElement();
            YFCElement rootEl = YFCDocument.getDocumentFor(InputDoc).getDocumentElement();
            log.info("----Executing the processOrderHoldType------");
            System.out.println("----Executing the processOrderHoldType------");
            YFCNodeList<YFCElement> HoldType=rootEl.getElementsByTagName("OrderHoldType");
            for(int i=0;i<HoldType.getLength();i++) {
            	 log.debug("----Executing the for loop------");
                 System.out.println("----Executing the processOrderHoldType------");
            		YFCElement el= HoldType.item(i);
            		String HoldVal=el.getAttribute("HoldType");
            		String status=el.getAttribute("Status");
            		 log.debug("HoldVal"+HoldVal);
                     System.out.println("HoldVal"+HoldVal);
            		if("BUYER_REMORSE".equalsIgnoreCase(HoldVal) && status.equals("1100")) {
            			  YFCDocument newDoc = YFCDocument.createDocument("Order");
            			    YFCElement newRootEl = newDoc.getDocumentElement();
            			    newRootEl.setAttribute("OrderHeaderKey", rootEl.getAttribute("OrderHeaderKey"));
            			    newRootEl.setAttribute("OrderNo", rootEl.getAttribute("OrderNo"));
            			    newRootEl.setAttribute("EnterpriseCode", rootEl.getAttribute("EnterpriseCode"));
            			    newRootEl.setAttribute("DocumentType", rootEl.getAttribute("DocumentType"));
            			    YFCElement holdTypesEl = newRootEl.createChild("OrderHoldTypes");
            			    YFCElement holdTypeEl = holdTypesEl.createChild("OrderHoldType");
            			    holdTypeEl.setAttribute("HoldType", "BUYER_REMORSE");
            			    holdTypeEl.setAttribute("Status", "1300");
            			    holdTypeEl.setAttribute("ReasonText", "Resolving Hold from UE");
            			    log.debug("This is the created document"+newDoc.toString());
            			    System.out.println("This is the created document"+newDoc.toString());
            			    api.invoke(env, "changeOrder", newDoc.getDocument());
            		}
            }
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// TODO Auto-generated method stub
		return InputDoc;
	}

}
