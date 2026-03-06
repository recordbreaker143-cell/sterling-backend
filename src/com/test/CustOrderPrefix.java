package com.test;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSGetOrderNoUE;

public class CustOrderPrefix implements YFSGetOrderNoUE {

	@Override
	public String getOrderNo(YFSEnvironment env, Map inputMap)
	        throws YFSUserExitException {

	    try {
	        String enterprise = (String) inputMap.get("EnterpriseCode");
	        if (enterprise == null || enterprise.isEmpty()) {
	            enterprise = "DEFAULT";
	        }

	        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document ccDoc = db.newDocument();
	        Element root = ccDoc.createElement("CommonCode");
	        root.setAttribute("CodeType", "ORDERPREFIX");
	        root.setAttribute("OrganizationCode", enterprise);
	        ccDoc.appendChild(root);

	        YIFApi api = YIFClientFactory.getInstance().getApi();
	        Document ccOut = api.invoke(env, "getCommonCodeList", ccDoc);

	        String prefix = "ORD";
	        NodeList list = ccOut.getElementsByTagName("CommonCode");
	        if (list.getLength() > 0) {
	            prefix = ((Element) list.item(0)).getAttribute("CodeValue");
	        }

	        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")
	                .format(new java.util.Date());

	        return prefix + timestamp;

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new YFSUserExitException(e.getMessage());
	    }
	}

}
