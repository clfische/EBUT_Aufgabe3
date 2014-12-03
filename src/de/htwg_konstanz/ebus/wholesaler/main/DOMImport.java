package de.htwg_konstanz.ebus.wholesaler.main;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;



public class DOMImport{

	
	public DOMImport(Document document) {
		// TODO Auto-generated constructor stub
		System.out.println("domimport 14");
		Element bmeCat = document.getDocumentElement();
		Element name= (Element) bmeCat.getElementsByTagName("SUPPLIER_NAME").item(0);
		String sname = name.getTagName();
		String bname = name.getTextContent();
		
		System.out.println(sname);
		System.out.println(bname);
		
		NodeList headers = bmeCat.getElementsByTagName("Artikel");
		
		//NodeList headers = bmeCat.getElementsByTagName("header");
		System.out.println("domimport 16" + bmeCat);
		String value = headers.toString();
		System.out.println(headers);
//		for (int i=0; i<headers.getLength();i++){
//			System.out.println(headers.item(i).getFirstChild());
//		}
	}

}