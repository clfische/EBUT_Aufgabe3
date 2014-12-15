package de.htwg_konstanz.ebus.wholesaler.action;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.Export;

public class ExportAction implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		
		System.out.println("test");
		List<BOProduct> productList;
		
		String filename = "";
		
		if(request.getParameter("search") != null) {
			List<BOProduct> productListSearch = ProductBOA.getInstance().findAll();
			productList = new ArrayList<BOProduct>();
			System.out.println("Neue Produktliste");
			for(BOProduct myProduct: productListSearch) {
				System.out.println("Suche in: "+myProduct.getShortDescription());
				if(myProduct.getShortDescription().matches(".*"+request.getParameter("search")+".*")) {
					System.out.println("Adding: "+myProduct.getShortDescription());
					productList.add(myProduct);
				}
			}
		}
		else{
			productList = ProductBOA.getInstance().findAll();
		}
		

		
		
		try {
			Export cbme = new Export(productList);
			// get Filenmae
			filename = cbme.createBMECat();
			// get Count of Exported Articles
			//i = cbme.getCountOfArticles();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Converting Abgeschlossen!");
		
		return "exportResult.jsp?filename=" + filename;
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_EXPORT_FILE);
	}
	
	public boolean checkXML(String search, String option) {

        if(search != null) {
            return true;
        } else if(option.equals("XML")) {
            return true;
        } else {
            return false;
        }
       
    }
	
	

}
