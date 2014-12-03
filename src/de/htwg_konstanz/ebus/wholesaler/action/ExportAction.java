package de.htwg_konstanz.ebus.wholesaler.action;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.Export;

public class ExportAction implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
			
			Export export = new Export();
			ByteArrayOutputStream out = null;
			String search = null;
			String option = null;
			
			//Prüfen ob der Search Parameter gesetzt ist
			if (StringUtils.isNotBlank((String) request.getParameter("search"))){
				search = (String) request.getParameter("searchString");
	            System.out.println(search);
	            export = new Export(search);	
			}
			else{
				export = new Export();
			}
			
			//Prüfen ob der Exportyp Parameter gesetzt ist und ggf auslesen
			if (StringUtils.isNotBlank((String) request.getParameter("exportType"))) {
	            option = request.getParameter("exportType");
	        }
			try{
				 Document document = export.createXMLdocument();
				 DOMSource domSource = new DOMSource(document);
				 out = new ByteArrayOutputStream();
				 String type = "";
				 String applicationType ="";
				 
				 if(checkXML(search, option)){
					 Transformer transformer = TransformerFactory.newInstance().newTransformer();
		                transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

		                transformer.transform(domSource, new StreamResult(out));
		                type = "attachment; filename=exportProduct.xml";
		                applicationType = "application/xml";

				 }
				 else{
					//XHTML transformation
		                Source xslt =
		                        new StreamSource(
		                                Constants.XSLT_PATH);

		                Transformer xformer = TransformerFactory.newInstance().newTransformer(xslt);
		                //needs to be this encoding because of "ß"
		                xformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		                xformer.transform(domSource, new StreamResult(out));
		                type = "attachment; filename=exportProduct.xhtml";
		                applicationType = "application/xhtml";
		 
				 }
				 
				 response.setHeader("Content-Disposition", type);
		            response.setContentType(applicationType);
		            response.setContentLength(out.size());
		            response.getWriter().write(out.toString());
			}
			
			 catch (IOException e) {
	            errorList.add("problem occured");
	            return "exportResult.jsp";

	        }  catch (TransformerConfigurationException e) {
	            errorList.add("problem occured due transformation");
	            return "exportResult.jsp";

	        } catch (TransformerFactoryConfigurationError e) {
	            errorList.add("problem occured due transformation");
	            return "exportResult.jsp";

	        } catch (TransformerException e) {
	            errorList.add("problem occured due transformation");
	            return "exportResult.jsp";

	        } catch (IllegalArgumentException e) {
	            errorList.add("problem occured at connection");
	            return "exportResult.jsp";
	        }
			finally{
				
			}
				return "ExportAction.jsp";
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
