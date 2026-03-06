package com.test;

import java.rmi.RemoteException;
import java.time.LocalDate;

import org.w3c.dom.Document;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.util.YFCIterableIterator;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSBeforeCreateOrderUE;

public class ImplementUE implements YFSBeforeCreateOrderUE {
	//LocalDate date=LocalDate.now();
	
	
	protected static YIFApi oapi=null;
	
	@Override
	public String beforeCreateOrder(YFSEnvironment arg0, String arg1) throws YFSUserExitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document beforeCreateOrder(YFSEnvironment env, Document inputDoc) throws YFSUserExitException {
		
		// TODO Auto-generated method stub
		YFCElement rootEl=YFCDocument.getDocumentFor(inputDoc).getDocumentElement();
		YFCElement OrderLines=rootEl.getChildElement("OrderLines");
		YFCIterableIterator<YFCElement> OrderLine=(YFCIterableIterator<YFCElement>) OrderLines.getChildren("OrderLine");
		while(OrderLine.hasNext()) {
				YFCElement el=OrderLine.next();
				String Qty=el.getAttribute("OrderedQty");
				YFCElement itemTag=el.getChildElement("Item");
				String itemID=itemTag.getAttribute("ItemID");
				YFCDocument reserveDoc=YFCDocument.createDocument("Promise");
				YFCElement reserveInput=reserveDoc.getDocumentElement();
				reserveInput.setAttribute("OrganizationCode", rootEl.getAttribute("EnterpriseCode"));
				reserveInput.setAttribute("CheckInventory", "Y");
				YFCElement reserveParams=reserveInput.createChild("ReservationParameters");
				YFCElement promiseLines=reserveInput.createChild("PromiseLines");
				YFCElement promiseline=promiseLines.createChild("PromiseLine");
				promiseline.setAttribute("ItemID", itemID);
				promiseline.setAttribute("RequiredQty", Qty);
				try {
					Document outputDoc=getApi().invoke(env, "reserveAvailableInventory",reserveDoc.getOwnerDocument().getDocument());
					YFCElement outputRootEl=(YFCElement) outputDoc.getDocumentElement();
					YFCElement outputPromiseLines=outputRootEl.getChildElement("PromiseLines");
					YFCIterableIterator<YFCElement> outputPromiseLine= (YFCIterableIterator<YFCElement>) outputPromiseLines.getChildren("PromiseLine");
					while(outputPromiseLine.hasNext()) {
						YFCElement pl=outputPromiseLine.next();
						String reservationID=pl.getElementsByTagName("Reservation").item(0).getAttribute("ReservationID");
						if(reservationID!=null && !reservationID.isEmpty()) {
							el.setAttribute("ReservationID", reservationID);
						}
					}
				} catch (YFSException | RemoteException | YIFClientCreationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	
		return rootEl.getOwnerDocument().getDocument();
	}
	
	protected YIFApi getApi() throws YIFClientCreationException {
		if(oapi==null) {
			oapi=YIFClientFactory.getInstance().getApi();
		}
		return oapi;
	}

}
