package com.test;
import java.io.IOException;
//import java.io.StringWriter;
//import java.io.File;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.yantra.yfs.japi.YFSEnvironment;

public class RemoveAttributes {
//	private String toString(Document doc) {
//	    try {
//	        TransformerFactory tf = TransformerFactory.newInstance();
//	        Transformer transformer = tf.newTransformer();
//	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//	        StringWriter writer = new StringWriter();
//	        transformer.transform(new DOMSource(doc), new StreamResult(writer));
//	        return writer.toString();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return "[error serializing document]";
//	    }
//	}

	public Document removeAttributes(YFSEnvironment env, Document inDoc) throws SAXException, IOException, TransformerException, ParserConfigurationException {
//			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
//			DocumentBuilder DB=dbf.newDocumentBuilder();
//			inDoc = DB.parse("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\com\\test\\releaseOrder.xml");
		//	System.out.println("Incoming XML:\n" + toString(inDoc));
			Element rootElement=inDoc.getDocumentElement();
			NodeList tag= rootElement.getElementsByTagName("PersonInfoShipTo");
			NodeList payment=rootElement.getElementsByTagName("PaymentMethod");
			for(int i=0;i<payment.getLength();i++) {
				Node n=payment.item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE) {
					Element e=(Element) n;
					e.setAttribute("CreditCardName", "Tester");
				}
			}
			for(int i=0;i<tag.getLength();i++) {
				Node n=tag.item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE) {
					Element e=(Element) n;
					e.removeAttribute("State");
				}
			}
//			TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            DOMSource source = new DOMSource(inputDoc);
//            StreamResult result = new StreamResult(
//                new File("C:\\Users\\Munideep\\Documents\\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\\SterlingJavaCodes\\src\\releaseOrder_modified.xml")
//            );
//            transformer.transform(source, result);
//            System.out.println("Updated XML saved successfully.");
			
			// after modifications
		//	System.out.println("Outgoing XML:\n" + toString(inDoc));

			return inDoc;
	}
//	public static void main(String[] args) throws SAXException, IOException, TransformerException, ParserConfigurationException {
//		// TODO Auto-generated method stub
//		RemoveAttributes rm=new RemoveAttributes();
//		rm.removeAttributes(null, null);
//	}
}
