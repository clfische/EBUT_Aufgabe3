package de.htwg_konstanz.ebus.wholesaler.main;


import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa._BaseBOA;

public class DOMImport{
	
	public String supplierNumber;
	public boolean supplierExists = false;
	
	
	
	public DOMImport(Document document) throws Exception {
		// TODO Auto-generated constructor stub
		
		//Daten vom UploadFile werden "geprüft"
		System.out.println("domimport 14");
		Element bmeCat = document.getDocumentElement();
		Element tempSupplierName= (Element) bmeCat.getElementsByTagName("SUPPLIER_NAME").item(0);
		String supplierName = tempSupplierName.getTextContent();
		
		System.out.println(supplierName);
	
		//Prüfen ob supplier schon existiert
		SupplierBOA supplierBOA = SupplierBOA.getInstance();
		
		List<BOSupplier> boSupplierList = supplierBOA.findAll();
		BOSupplier boSupplier = null;
		
		ProductBOA productBOA = ProductBOA.getInstance();
		
		List<BOProduct> listeBOProduct = productBOA.findAll();
		
		for(BOSupplier sup : boSupplierList){
			if(sup.getCompanyname().equals(supplierName)){
				boSupplier = sup;
				supplierNumber = boSupplier.getSupplierNumber();
				break;
			}
		}
		
		System.out.println("fehler domimport 53");
		
		//Neuen Supplier erstellen
		if(boSupplier==null){
			supplierNumber = SupplierHelper.saveNewSupplier(supplierName);
			System.out.println("dom import 58");
			// Fehler werfen wenn Supplier noch nicht existiert und das er angelegt wird
		}
		System.out.println("fehler domimport 60");
		//Alle Artikel als Nodeliste von der eingelesenen XML holen
		NodeList articles = bmeCat.getElementsByTagName("ARTICLE");

		System.out.println("fehler domimport 64");
		//Prüfen ob es Artikel gibt und nicht NULL sind
		if(articles.getLength() >0 && articles!=null){

			System.out.println("fehler domimport 68");
			for(int i=0; i<articles.getLength(); i++){
				
				//Hole Artikel-Elemente --> Siehe BMECat

				System.out.println("fehler domimport 72");
				Element article = (Element) articles.item(i);
				Element supplier_aid = (Element) article.getElementsByTagName("SUPPLIER_AID").item(0);
				Element article_details = (Element) article.getElementsByTagName("ARTICLE_DETAILS").item(0);
				Element description_short = (Element) article.getElementsByTagName("DESCRIPTION_SHORT").item(0);
				Element description_long = (Element) article.getElementsByTagName("DESCRIPTION_LONG").item(0);
				
				String textSupplier_aid = supplier_aid.getFirstChild().getNodeValue();
				String textDescription_short = description_short.getFirstChild().getNodeValue();
				String textDescription_long = description_long.getFirstChild().getNodeValue();


				System.out.println("fehler domimport 85");
				//Neues Produkt erstellen und die Werte dazu setzen
				BOProduct boProduct = new BOProduct();

				System.out.println("fehler domimport 89"+ (supplierName));
			//BOSupplier g = SupplierBOA.getInstance().findSupplierById(supplierName);
			
			//System.out.println(g);	
			//boProduct.setSupplier(SupplierBOA.getInstance().findSupplierById(supplierName));
				boProduct.setSupplier(boSupplier);
				System.out.println("fehler domimport 92");
				boProduct.setOrderNumberSupplier(textSupplier_aid);
				boProduct.setOrderNumberCustomer(textSupplier_aid);
				boProduct.setShortDescription(textDescription_short);
				boProduct.setLongDescription(textDescription_long);
				
				if (listeBOProduct != null) {
                    for (BOProduct doBoPro : listeBOProduct) {
                        if (doBoPro.getOrderNumberCustomer().equals(
                                boProduct.getOrderNumberCustomer())) {
                            productBOA.delete(doBoPro);

                        }
                    }
                }
				
				//löschen von Produkt fehlt?!

				System.out.println("fehler domimport 96");
				_BaseBOA.getInstance().commit();
				productBOA.saveOrUpdate(boProduct);

				System.out.println("fehler domimport 100");
				//Alle Preise vom Artikel in Tabelle speichern
				
				Element priceDetails = (Element) article.getElementsByTagName("ARTICLE_PRICE_DETAILS").item(0);
				
				//Schleife über alle Artikelpreise
				NodeList articlePriceList = priceDetails.getElementsByTagName("ARTICLE_PRICE");
				
				if(articlePriceList.getLength() > 0 && articlePriceList != null){
					for (int j = 0; j < articlePriceList.getLength(); j++){
						Element articlePrice = (Element) articlePriceList.item(j);
						String price_type = articlePrice.getAttribute("price_type");
						
						//Konvertieren Element to HTML (Preishöhe) 
						Element price_amount = (Element) articlePrice.getElementsByTagName("PRICE_AMOUNT").item(0);
						BigDecimal price_amount_value = getBigD(price_amount);
						Element tax = (Element) articlePrice.getElementsByTagName("TAX").item(0);
						BigDecimal taxValue = getBigD(tax);
						
						NodeList territoryList = article.getElementsByTagName("TERRITORY");
						
						// Spreichern des Preises für jedes "Territory"
						SalesPurchasePriceHelper.saveSalesPruchasePrice(territoryList, boProduct, taxValue, price_amount_value, price_type);
					
						
					}
				}				
			}
		}
	}
	public BigDecimal getBigD(Element element) {
        double tempPRICE_AMOUNT = Double.valueOf(element.getFirstChild().getNodeValue());
        BigDecimal price_amount_value = BigDecimal.valueOf(tempPRICE_AMOUNT);
        return price_amount_value;
    }
}