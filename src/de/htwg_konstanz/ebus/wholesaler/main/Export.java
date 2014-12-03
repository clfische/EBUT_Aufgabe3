package de.htwg_konstanz.ebus.wholesaler.main;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;



public class Export {
	private List<BOProduct> productList;
	
	public Export (){
		
	}
	
	public Export (String search){
		
	}

	public final Document createXMLdocument() {
		Document bmeCatdoc = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();

            bmeCatdoc = docBuilder.newDocument();

            Element bmecat = bmeCatdoc.createElement("BMECAT");

            bmecat.setAttribute("version", "1.2");
            bmecat.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            bmecat.appendChild(headerStructure(bmeCatdoc));
            Element catalog = bmeCatdoc.createElement("T_NEW_CATALOG");
            bmecat.appendChild(catalog);

            for (BOProduct articel : this.productList) {
                String desc = articel.getShortDescription();
                catalog.appendChild(createAricle(bmeCatdoc, articel));
            }

            bmeCatdoc.appendChild(bmecat);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return bmeCatdoc;
	}
	
	private Element createAricle(final Document bmeCatdoc, BOProduct product) {
        Element article = bmeCatdoc.createElement("ARTICLE");

        Element supplier_aid = bmeCatdoc.createElement("SUPPLIER_AID");
        supplier_aid.setTextContent(product.getOrderNumberCustomer());
        article.appendChild(supplier_aid);

        Element shortDescr = bmeCatdoc.createElement("ARTICLE_DETAILS");
        article.appendChild(shortDescr);

        Element description_short = bmeCatdoc.createElement("DESCRIPTION_SHORT");
        Element description_long = bmeCatdoc.createElement("DESCRIPTION_LONG");
        description_short.setTextContent(product.getShortDescription());
        description_long.setTextContent(product.getLongDescription());

        shortDescr.appendChild(description_short);
        shortDescr.appendChild(description_long);

        Element article_order_details = bmeCatdoc.createElement("ARTICLE_ORDER_DETAILS");
        Element orderUnit = bmeCatdoc.createElement("ORDER_UNIT");
        Element contentUnit = bmeCatdoc.createElement("CONTENT_UNIT");
        Element no_cu_per_ou = bmeCatdoc.createElement("NO_CU_PER_OU");

        orderUnit.setTextContent("PK");
        contentUnit.setTextContent("C62");
        no_cu_per_ou.setTextContent("10");

        article_order_details.appendChild(orderUnit);
        article_order_details.appendChild(contentUnit);
        article_order_details.appendChild(no_cu_per_ou);

        article.appendChild(article_order_details);

        List<BOCountry> county = CountryBOA.getInstance().findAll();

        Element articlePrice = bmeCatdoc.createElement("ARTICLE_PRICE_DETAILS");

        article.appendChild(articlePrice);
        for (BOCountry boC : county) {

            BOPurchasePrice boPP = PriceBOA.getInstance().findPurchasePrice(product, boC, 1);

            if (boPP != null) {
                articlePrice.appendChild((createPrices(bmeCatdoc, product, boC, boPP)));
            }
        }
        return article;
    }
	
	
	 private Element headerStructure(Document bmeCatdoc) {
	        Element header = bmeCatdoc.createElement("HEADER");

	        Element catalog = bmeCatdoc.createElement("CATALOG");
	        Element language = bmeCatdoc.createElement("LANGUAGE");
	        Element catalog_ID = bmeCatdoc.createElement("CATALOG_ID");
	        Element catalog_version = bmeCatdoc.createElement("CATALOG_VERSION");
	        Element catalog_name = bmeCatdoc.createElement("CATALOG_NAME");

	        catalog.appendChild(language);
	        language.setTextContent("deu");
	        catalog.appendChild(catalog_ID);
	        catalog_ID.setTextContent("HTWG-EBUT-11");
	        catalog.appendChild(catalog_version);
	        catalog_version.setTextContent("1.0");
	        catalog.appendChild(catalog_name);
	        catalog_name.setTextContent("Beispielproduktkatalog fuer E-Business Laborpraktika");

	        Element supplier = bmeCatdoc.createElement("SUPPLIER");
	        Element supplier_name = bmeCatdoc.createElement("SUPPLIER_NAME");
	        supplier.appendChild(supplier_name);
	        supplier_name.setTextContent("KN MEDIA Store");

	        header.appendChild(catalog);
	        header.appendChild(supplier);
	        return header;
	    }
}
