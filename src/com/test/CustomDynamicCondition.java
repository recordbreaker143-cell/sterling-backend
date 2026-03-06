package com.test;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.ycp.japi.YCPDynamicCondition;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;

public class CustomDynamicCondition implements YCPDynamicCondition {

	@Override
	public boolean evaluateCondition(YFSEnvironment arg0, String arg1, Map arg2, String arg3) {
		try {
		YFCDocument inputDoc=YFCDocument.getDocumentFor(arg3);
		YFCElement rootEl=inputDoc.getDocumentElement();
		int MaxOrderStatus=Integer.parseInt(rootEl.getAttribute("MaxOrderStatus"));
		int MinOrderStatus=Integer.parseInt(rootEl.getAttribute("MinOrderStatus"));
		if((MaxOrderStatus  <= 3200) && (MinOrderStatus <=3200)) {
			return true;
		}
		else {
			return false;
		}
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		// TODO Auto-generated method stub
	}

}
