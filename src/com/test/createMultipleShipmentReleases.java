package com.test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfs.japi.YFSEnvironment;

public class createMultipleShipmentReleases {
//	
//	public static String documentToString(Document doc) {
//  try {
//      TransformerFactory tf = TransformerFactory.newInstance();
//      Transformer transformer = tf.newTransformer();
//      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//      // indentation for many transformers:
//      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//      StringWriter sw = new StringWriter();
//      transformer.transform(new DOMSource(doc), new StreamResult(sw));
//      return sw.toString();
//  } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//  }
// }
	public Document createMultipleShipmentRelease(YFSEnvironment env,Document inputDoc) throws ParserConfigurationException, SAXException, IOException, YIFClientCreationException {
		YIFApi api=YIFClientFactory.getInstance().getApi();
		DocumentBuilder DB=DocumentBuilderFactory.newInstance().newDocumentBuilder();
	//	Document inputDoc=DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\releaseOrder.xml");
		Element rootEl=inputDoc.getDocumentElement();
		String orderHeaderKey=rootEl.getAttribute("OrderHeaderKey");
		String orderNo=rootEl.getAttribute("OrderNo");
		String entCode=rootEl.getAttribute("EnterpriseCode");
		NodeList orderLines=rootEl.getElementsByTagName("OrderLine");
		int length=orderLines.getLength();
		Document outputDoc=null;
		String template="<Shipment ActualDeliveryDate=\"\" ActualFreightCharge=\"\" ActualShipmentDate=\"\" AllowOverage=\"\" AppointmentNo=\"\" AssignedToUserId=\"\" AssociationAction=\"\" BBNMinVolume=\"\" BBNMinWeight=\"\" BackroomPickRequired=\"\" BillToAddressKey=\"\" BillToCustomerId=\"\" BolNo=\"\" BreakBulkLoadKey=\"\" BreakBulkNode=\"\" BuyerMarkForNodeId=\"\" BuyerOrganizationCode=\"\" BuyerReceivingNodeId=\"\" CODPayMethod=\"\" CarrierAccountNo=\"\" CarrierServiceCode=\"\" CarrierType=\"\" Code=\"\" CommercialValue=\"\" Currency=\"\" DeliveryCode=\"\" DeliveryMethod=\"\" DeliveryPlanKey=\"\" DeliveryTS=\"\" DestinationZone=\"\" DoNotVerifyCaseContent=\"\" DoNotVerifyPalletContent=\"\" DocumentType=\"\" DownLoadCount=\"\" EmailReturnLabel=\"\" EnterpriseCode=\"\" EspCheckRequired=\"\" ExpectedDeliveryDate=\"\" ExpectedShipmentDate=\"\" ExportTaxpayerID=\"\" FreightTerms=\"\" FromAddressKey=\"\" FromAppointment=\"\" HasOtherShipments=\"\" HazardousMaterialFlag=\"\" HoldLocation=\"\" ITDate=\"\" ITNNo=\"\" ITNo=\"\" InvoiceComplete=\"\" IsPackProcessComplete=\"\" IsPackingRequired=\"\" IsPackingRequiredWithContents=\"\" IsProductPlacingComplete=\"\" IsShipmentLevelIntegration=\"\" IsSingleOrder=\"\" LevelOfService=\"\" LinesEntered=\"\" Lockid=\"\" ManifestKey=\"\" ManifestNo=\"\" ManuallyEntered=\"\" MergeNode=\"\" NextAlertTs=\"\" NumOfCartons=\"\" NumOfPallets=\"\" OrderAvailableOnSystem=\"\" OrderHeaderKey=\"\" OrderNo=\"\" OrderReleaseKey=\"\" OriginZone=\"\" OverrideManualShipmentEntry=\"\" PackAndHold=\"\" PackListType=\"\" ParentShipmentKey=\"\" PickListNo=\"\" PickTicketPrinted=\"\" PickticketNo=\"\" PipelineKey=\"\" PodNo=\"\" ProNo=\"\" ProfileID=\"\" ReceivingNode=\"\" ReleaseNo=\"\" RequestedCarrierServiceCode=\"\" RequestedDeliveryDate=\"\" RequestedShipmentDate=\"\" ReturnAuthorizationNumber=\"\" ReturnBillingAccount=\"\" ReturnByDate=\"\" ReturnCarrierService=\"\" ReturnFreightTerms=\"\" SCAC=\"\" ScacAndService=\"\" ScacAndServiceKey=\"\" ScacIntegrationRequired=\"\" SealNo=\"\" SellerOrganizationCode=\"\" ShipDate=\"\" ShipMode=\"\" ShipNode=\"\" ShipToCustomerId=\"\" ShipVia=\"\" ShipmentClosedFlag=\"\" ShipmentConfirmUpdatesDone=\"\" ShipmentConsolidationGroupId=\"\" ShipmentContainerizedFlag=\"\" ShipmentDeliverUpdatesDone=\"\" ShipmentKey=\"\" ShipmentNo=\"\" ShipmentPlannedFlag=\"\" ShipmentSortLocationId=\"\" ShipmentType=\"\" Status=\"\" StatusDate=\"\" ToAppointment=\"\" TotalActualCharge=\"\" TotalEstimatedCharge=\"\" TotalNumOfPickableSKUs=\"\" TotalVolume=\"\" TotalVolumeUOM=\"\" TotalWeight=\"\" TotalWeightUOM=\"\" TrackingNo=\"\" TrailerNo=\"\" WorkOrderApptKey=\"\" WorkOrderKey=\"\">\r\n"
				+ "	<ToAddress AddressLine1=\"\" AddressLine2=\"\" AddressLine3=\"\" AddressLine4=\"\" AddressLine5=\"\" AddressLine6=\"\" AlternateEmailID=\"\" Beeper=\"\" City=\"\" Company=\"\" Country=\"\" DayFaxNo=\"\" DayPhone=\"\" Department=\"\" EMailID=\"\" ErrorTxt=\"\" EveningFaxNo=\"\" EveningPhone=\"\" FirstName=\"\" HttpUrl=\"\" IsCommercialAddress=\"\" JobTitle=\"\" LastName=\"\" MiddleName=\"\" MobilePhone=\"\" OtherPhone=\"\" PersonID=\"\" PersonInfoKey=\"\" PreferredShipAddress=\"\" State=\"\" Suffix=\"\" TaxGeoCode=\"\" Title=\"\" UseCount=\"\" VerificationStatus=\"\" ZipCode=\"\"/>\r\n"
				+ "	<BillToAddress AddressLine1=\"\" AddressLine2=\"\" AddressLine3=\"\" AddressLine4=\"\" AddressLine5=\"\" AddressLine6=\"\" AlternateEmailID=\"\" Beeper=\"\" City=\"\" Company=\"\" Country=\"\" DayFaxNo=\"\" DayPhone=\"\" Department=\"\" EMailID=\"\" ErrorTxt=\"\" EveningFaxNo=\"\" EveningPhone=\"\" FirstName=\"\" HttpUrl=\"\" IsCommercialAddress=\"\" JobTitle=\"\" LastName=\"\" MiddleName=\"\" MobilePhone=\"\" OtherPhone=\"\" PersonID=\"\" PersonInfoKey=\"\" PreferredShipAddress=\"\" State=\"\" Suffix=\"\" TaxGeoCode=\"\" Title=\"\" UseCount=\"\" VerificationStatus=\"\" ZipCode=\"\"/>\r\n"
				+ "	<FromAddress AddressLine1=\"\" AddressLine2=\"\" AddressLine3=\"\" AddressLine4=\"\" AddressLine5=\"\" AddressLine6=\"\" AlternateEmailID=\"\" Beeper=\"\" City=\"\" Company=\"\" Country=\"\" DayFaxNo=\"\" DayPhone=\"\" Department=\"\" EMailID=\"\" ErrorTxt=\"\" EveningFaxNo=\"\" EveningPhone=\"\" FirstName=\"\" HttpUrl=\"\" IsCommercialAddress=\"\" JobTitle=\"\" LastName=\"\" MiddleName=\"\" MobilePhone=\"\" OtherPhone=\"\" PersonID=\"\" PersonInfoKey=\"\" PreferredShipAddress=\"\" State=\"\" Suffix=\"\" TaxGeoCode=\"\" Title=\"\" UseCount=\"\" VerificationStatus=\"\" ZipCode=\"\"/>\r\n"
				+ "	<MarkForAddress AddressLine1=\"\" AddressLine2=\"\" AddressLine3=\"\" AddressLine4=\"\" AddressLine5=\"\" AddressLine6=\"\" AlternateEmailID=\"\" Beeper=\"\" City=\"\" Company=\"\" Country=\"\" DayFaxNo=\"\" DayPhone=\"\" Department=\"\" EMailID=\"\" ErrorTxt=\"\" EveningFaxNo=\"\" EveningPhone=\"\" FirstName=\"\" HttpUrl=\"\" IsCommercialAddress=\"\" JobTitle=\"\" LastName=\"\" MiddleName=\"\" MobilePhone=\"\" OtherPhone=\"\" PersonID=\"\" PersonInfoKey=\"\" PreferredShipAddress=\"\" State=\"\" Suffix=\"\" TaxGeoCode=\"\" Title=\"\" UseCount=\"\" VerificationStatus=\"\" ZipCode=\"\"/>\r\n"
				+ "	<ShipNode AcceptanceRequired=\"\" ActivateFlag=\"\" CanShipToAllNodes=\"\" CanShipToOtherAddresses=\"\" ContactAddressKey=\"\" CurrentBolNumber=\"\" DefaultDeclaredValue=\"\" Description=\"\" ExportLicenseExpDate=\"\" ExportLicenseNo=\"\" ExportTaxpayerId=\"\" GenerateBolNumber=\"\" IdentifiedByParentAs=\"\" InterfaceSubType=\"\" InterfaceType=\"\" InventoryTracked=\"\" Inventorytype=\"\" IsItemBasedAllocationAllowed=\"\" Latitude=\"\" Localecode=\"\" Longitude=\"\" MaintainInventoryCost=\"\" NodeType=\"\" OwnerKey=\"\" OwnerType=\"\" PicklistType=\"\" ReceivingNode=\"\" RequiresChangeRequest=\"\" ReturnCenterFlag=\"\" ReturnsNode=\"\" ShipNode=\"\" ShipNodeAddressKey=\"\" ShipNodeClass=\"\" ShipnodeKey=\"\" ShipnodeType=\"\" ShippingNode=\"\" SupplierKey=\"\" ThreePlNode=\"\" TimeDiff=\"\"/>\r\n"
				+ "	<ShipmentLines>\r\n"
				+ "		<ShipmentLine ActualQuantity=\"\" BackroomPickComplete=\"\" BackroomPickedQty=\"\" BackroomPickedQuantity=\"\" BuyerMarkForNodeId=\"\" CancelReason=\"\" ChainedFromOrderHeaderKey=\"\" ChainedFromOrderLineKey=\"\" CountryOfOrigin=\"\" CustomerPickedQuantity=\"\" CustomerPoLineNo=\"\" CustomerPoNo=\"\" DocumentType=\"\" ExternalReleaseIdentifier=\"\" FifoNo=\"\" GroupSequenceNumber=\"\" IsCustomerPickComplete=\"\" IsHazmat=\"\" IsPackComplete=\"\" IsPickable=\"\" ItemDesc=\"\" ItemID=\"\" KitCode=\"LK\" LevelOfService=\"\" NetWeight=\"\" NetWeightUom=\"\" OrderAvailableOnSystem=\"\" OrderHeaderKey=\"\" OrderLineKey=\"\" OrderNo=\"\" OrderReleaseKey=\"\" OverShipQuantity=\"\" PickLocation=\"\" PickLocationSeq=\"\" PlacedQuantity=\"\" PrimeLineNo=\"\" ProductClass=\"\" Quantity=\"\" ReceivedQuantity=\"\" ReleaseNo=\"\" RequestedSerialNo=\"\" ReturnShippingLabelLevel=\"\" Segment=\"\" SegmentType=\"\" ShipToCustomerId=\"\" ShipmentConsolidationGroupId=\"\" ShipmentKey=\"\" ShipmentLineKey=\"\" ShipmentLineNo=\"\" ShipmentSubLineNo=\"\" ShortageQty=\"\" ShortageResolutionReason=\"\" SubLineNo=\"\" UnitOfMeasure=\"\" WaveNo=\"\">\r\n"
				+ "		</ShipmentLine>\r\n"
				+ "	</ShipmentLines>\r\n"
				+ "</Shipment>";
				
		Document templateDoc=DB.parse(new InputSource(new StringReader(template)));		
		env.setApiTemplate("createShipment",templateDoc);
		for(int i=0;i<length;i++) {
			Document shipDoc=DB.newDocument();
			Element shipRootEl=shipDoc.createElement("Shipment");
			shipRootEl.setAttribute("OrderNo", orderNo);
			shipRootEl.setAttribute("OrderHeaderKey", orderHeaderKey);
			shipRootEl.setAttribute("EnterpriseCode", entCode);
			Element shipmentLines=shipDoc.createElement("ShipmentLines");
			Element shipmentLine=shipDoc.createElement("ShipmentLine");
			shipmentLines.appendChild(shipmentLine);
			shipRootEl.appendChild(shipmentLines);
			shipDoc.appendChild(shipRootEl);
			Element ol=(Element) orderLines.item(i);
			Element os=(Element) rootEl.getElementsByTagName("OrderStatus").item(i);
			String orderReleaseKey=os.getAttribute("OrderReleaseKey");
			String orderLineKey=ol.getAttribute("OrderLineKey");
			String qty=ol.getAttribute("RemainingQty");
			String shipNode=ol.getAttribute("ShipNode");
			String primeLineNo=ol.getAttribute("PrimeLineNo");
			String subLineNo=ol.getAttribute("SubLineNo");
			shipmentLine.setAttribute("OrderLineKey",orderLineKey);
			shipmentLine.setAttribute("PrimeLineNo", primeLineNo);
			shipmentLine.setAttribute("SubLineNo", subLineNo);
			shipmentLine.setAttribute("ShipNode", shipNode);
			shipmentLine.setAttribute("Quantity", qty);
			shipmentLine.setAttribute("OrderReleaseKey", orderReleaseKey);
			//System.out.println(documentToString(shipDoc));
		 outputDoc=api.invoke(env,"createShipment",shipDoc);
		//return outputDoc;
		}
		return outputDoc;
	}
//public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, YIFClientCreationException {
//	createMultipleShipmentReleases cm=new createMultipleShipmentReleases();
//	cm.createMultipleShipmentRelease(null,null);
//}
}
