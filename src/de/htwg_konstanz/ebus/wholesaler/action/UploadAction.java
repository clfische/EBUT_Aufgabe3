package de.htwg_konstanz.ebus.wholesaler.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.DOMImport;

public class UploadAction implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		// TODO Auto-generated method stub
		System.out.println("uploadactionbefore");
		InputStream in = null;

		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				System.out.print("Zeile 42");
				List<FileItem> items = upload.parseRequest(request);
				System.out.println("Zeile 44");
				Iterator<FileItem> iter = items.iterator();
				System.out.println(items);

				while (iter.hasNext()) {
					FileItem item = iter.next();
					
					
					if (!item.isFormField()) {
						String fileName = item.getName();
						if (fileName.length() == 0) {
							errorList.add("Keine Datei ausgew‰hlt");
                            return "UploadAction.jsp"; 
							//throw new NoFileChosenException(); // Keine Datei gefunden
							// TODO Exception anpassen
						}

						// Dateiendung holen
						String extension = fileName.substring(
								fileName.lastIndexOf(".") + 1,
								fileName.length());

						String contentType = item.getContentType();

						in = item.getInputStream();
						//InputStreamReader reader = new InputStreamReader(in);
						//String value = item.getString();
						//System.out.println(value);
						
						File schemaPath = new File(
								Constants.XSD_VALIDATION_FILEPATH);
						System.out.println(schemaPath);
						SchemaFactory factorySchema = SchemaFactory
								.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
						System.out.println(factorySchema);
						Schema schema;
						Document document;

						DocumentBuilderFactory dbf = DocumentBuilderFactory
								.newInstance();
						dbf.setNamespaceAware(true);
						DocumentBuilder parser = dbf.newDocumentBuilder();
						document = parser.parse(in);
						System.out.println(parser);
						System.out.println(document.getNodeName());
						System.out.println("UploadAction 79");
						// Pr√ºfen auf XMLTyp
						// TODO: Fehlerausgabe hinzuf√ºgen
						if (!contentType.equals("text/xml")
								|| !extension.equals("xml")) {
							errorList.add("W‰hle eine XML-Datei aus");
							return "UploadAction.jsp";
						}

						System.out.println("UploadAction 88");
						schema = factorySchema.newSchema(schemaPath);
						Validator validator = schema.newValidator();
						System.out.println("UploadAction 91");
						//validator.validate(new DOMSource(document));
						try {
							validator.validate(new DOMSource(document));
						} catch (Exception e) {
							errorList.add("Dokument ist nicht valide!");
							System.out.println("Dokument ist nicht valide!");
						}
						System.out.println("UploadAction 93");
						// TODO Fehlerausgabe noch erg√§nzen
						try {
							new DOMImport(document);
							System.out.println("Import erfolgreich");
						} catch (Exception e) {
							//errorList.add("Import wurde nicht erfolgreich beendet!");
						}
//					 } catch (FactoryConfigurationError e) {
//                         errorList.add("couldnt convert");
//                     
//                     } catch (Exception e) {
//                         errorList
//                                 .add("error occured, some Elements couldnt be found in database");
//                     }
					}

				}
			} catch (ParserConfigurationException e) {
				errorList.add("Fehler wohlgeformt");
			
		} catch (Exception e) {
			errorList.add("Fehler FileFactory");
		}
			
//			} catch (FileUploadException ex) {
//                errorList.add("File Upload Failed due to " + ex);
//            } catch (IOException ex) {
//                // validate
//                errorList.add("ValidationError " + ex);
//            } catch (ParserConfigurationException ex) {
//                // from DocumentBuilder
//                errorList.add("DocumentBuilder Error: " + ex);
//            } catch (SAXException ex) {
//                // from validate
//                errorList.add("ValidationError " + ex);
//            } 
		}

		return "UploadAction.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_UPLOAD_FILE);
	}

}