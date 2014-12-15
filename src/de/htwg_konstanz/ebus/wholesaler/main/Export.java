package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.vo.Country;




public class Export {
	private List<BOProduct> productList;
	private int i = 0;
	private int j = 0;
	private String filename;
	
	public Export (List<BOProduct> productList){
		this.productList = productList;
	}
	

	public String createBMECat() throws ParserConfigurationException, TransformerConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.newDocument();
		
		Element root = document.createElement("BMECAT");
		Attr version = document.createAttribute("version");
		version.setValue("1.01");
		
		root.setAttributeNode(version);
		
		Element header = document.createElement("HEADER");
		Element catalog = document.createElement("CATALOG");
		Element lang = document.createElement("LANGUAGE");
		Element catId = document.createElement("CATALOG_ID");
		Element catVers = document.createElement("CATALOG_VERSION");
		Element catName = document.createElement("CATALOG_NAME");
		Element dTime = document.createElement("DATETIME");
		Element datum = document.createElement("DATE");
		Element supplier = document.createElement("SUPPLIER");
		Element supName = document.createElement("SUPPLIER_NAME");
		Element tNewCat = document.createElement("T_NEW_CATALOG");
		
		document.appendChild(root);
		root.appendChild(header);
		header.appendChild(catalog);
		catalog.appendChild(lang);
		lang.appendChild(document.createTextNode("deu"));
		catalog.appendChild(catId);
		catId.appendChild(document.createTextNode("Tandem02"));
		catalog.appendChild(catVers);
		catVers.appendChild(document.createTextNode("1.0"));
		catalog.appendChild(catName);
		catName.appendChild(document.createTextNode("Neuer Katalog"));
		catalog.appendChild(dTime);
		dTime.appendChild(datum);
		datum.appendChild(document.createTextNode(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
		header.appendChild(supplier);
		supplier.appendChild(supName);
		supName.appendChild(document.createTextNode("Clements&Ron"));
		root.appendChild(tNewCat);
		
		Element[] article = new Element[productList.size()];
		Element[] supAID = new Element[productList.size()];
		Element[] aDetails =  new Element[productList.size()];
		Element[] descShort = new Element[productList.size()];
		Element[] descLong = new Element[productList.size()];
		Element[] ean = new Element[productList.size()];
		Element[] supAltAID = new Element[productList.size()];
		Element[] manuName = new Element[productList.size()];
		Element[] artOrderDetails = new Element[productList.size()];
		Element[] orderUnit = new Element[productList.size()];
		Element[] aPriceDetails = new Element[productList.size()];
		Element[] aPrice = new Element[productList.size()];
		Attr[] priceType = new Attr[productList.size()];
		Element[] pAmount = new Element[productList.size()];
		Element[] pCurrency = new Element[productList.size()];
		Element[] tax = new Element[productList.size()];
		Element[] territory = new Element[productList.size()];
		
		
		for(BOProduct product: this.productList) {
			j++;
			article[i] = document.createElement("ARTICLE");
			tNewCat.appendChild(article[i]);
			supAID[i] = document.createElement("SUPPLIER_AID");
			article[i].appendChild(supAID[i]);
			supAID[i].appendChild(document.createTextNode(product.getOrderNumberCustomer()));
			aDetails[i] = document.createElement("ARTICLE_DETAILS");
			article[i].appendChild(aDetails[i]);
			descShort[i] = document.createElement("DESCRIPTION_SHORT");
			aDetails[i].appendChild(descShort[i]);
			descShort[i].appendChild(document.createTextNode(product.getShortDescription()));
			descLong[i] = document.createElement("DESCRIPTION_LONG");
			
			ifExistsAppendChild(product.getLongDescription(), aDetails[i], descLong[i], document);
			supAltAID[i] = document.createElement("SUPPLIER_ALT_AID");
			
			ifExistsAppendChild(product.getMaterialNumber().toString(), aDetails[i], supAltAID[i], document);
			
			manuName[i] = document.createElement("MANUFACTURER_NAME");
			
			ifExistsAppendChild(product.getManufacturer(), aDetails[i], manuName[i], document);
			
			artOrderDetails[i] = document.createElement("ARTICLE_ORDER_DETAILS");
			article[i].appendChild(artOrderDetails[i]);
			orderUnit[i] = document.createElement("ORDER_UNIT");;
			artOrderDetails[i].appendChild(orderUnit[i]);
			orderUnit[i].appendChild(document.createTextNode("U1"));
			aPriceDetails[i] = document.createElement("ARTICLE_PRICE_DETAILS");
			article[i].appendChild(aPriceDetails[i]);
			aPrice[i] = document.createElement("ARTICLE_PRICE");
			aPriceDetails[i].appendChild(aPrice[i]);
			priceType[i] = document.createAttribute("price_type");
			priceType[i].setValue("net_customer_exp");
			pAmount[i] = document.createElement("PRICE_AMOUNT");
			aPrice[i].setAttributeNode(priceType[i]);
			aPrice[i].appendChild(pAmount[i]);
			pAmount[i].appendChild(document.createTextNode(product.getSalesPrices().get(0).getAmount().toString()));
			
			
			tax[i] = document.createElement("TAX");
			
			ifExistsAppendChild(product.getSalesPrice(new BOCountry(new Country("DE"))).getTaxrate().toString(), aPrice[i], tax[i], document);
	
			i+=1;
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		validateDocument(document);
		filename = "BMECat"+System.currentTimeMillis()+".xml";
		
		
		
	
		StreamResult result = new StreamResult(new File("/Applications/XAMPP/xamppfiles/htdocs/"+filename));
		
		try {
			System.out.println("vor transform result");
			transformer.transform(source, result);
			System.out.println("nach transform result");
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//transfromForWeb(document, filename);
		//new DropFiles(filename).start();
		return filename;
		
	}
	
	
	private void transfromForWeb(Document document, String filename) {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource("C:\\Temp\\BMECatToWeb.xslt"));
			FileOutputStream fos = new FileOutputStream("/Applications/XAMPP/xamppfiles/htdocs/"+filename+".html");
			transformer.transform(
					new StreamSource("/Applications/XAMPP/xamppfiles/htdocs/"+filename),
					new StreamResult(fos)
					);
			fos.flush();
			fos.close();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void validateDocument(Document xmlDOM) {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		URL schemaURL;
		try {
			schemaURL = new File("/Applications/XAMPP/xamppfiles/htdocs/bmecat_new_catalog_1_2_simple_without_NS.xsd").toURI().toURL();
			Schema schema = sf.newSchema(schemaURL); 
			Validator validator = schema.newValidator();
			DOMSource source = new DOMSource(xmlDOM);
			validator.validate(source);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void ifExistsAppendChild(String name, Element parent, Element child, Document doc) {
		if(name != null) {
			parent.appendChild(child);
			child.appendChild(doc.createTextNode(name));
		}
	}
}
