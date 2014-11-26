package de.htwg_konstanz.ebus.wholesaler.action;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.DOMImport;

public class UploadAction implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		// TODO Auto-generated method stub

		InputStream in = null;

		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				System.out.print("Zeile 42");
				List<FileItem> items = upload.parseRequest(request);
				System.out.println("Zeile 44");
				Iterator<FileItem> iter = items.iterator();

				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (!item.isFormField()) {
						String fileName = item.getName();
						if (fileName.length() == 0) {
							throw new Exception(); // Keine Datei gefunden
							// TODO Exception anpassen
						}

						// Dateiendung holen
						String extension = fileName.substring(
								fileName.lastIndexOf(".") + 1,
								fileName.length());

						String contentType = item.getContentType();

						in = item.getInputStream();

						File schemaPath = new File(
								Constants.XSD_VALIDATION_FILEPATH);
						SchemaFactory factorySchema = SchemaFactory
								.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
						Schema schema;
						Document document;

						DocumentBuilderFactory dbf = DocumentBuilderFactory
								.newInstance();
						dbf.setNamespaceAware(true);
						DocumentBuilder parser = dbf.newDocumentBuilder();
						document = parser.parse(in);

						// Prüfen auf XMLTyp
						// TODO: Fehlerausgabe hinzufügen
						if (!contentType.equals("text/xml")
								|| !extension.equals("xml")) {
							errorList.add("Wähle eine XML-Datei aus");
							return "UploadAction.jsp";
						}

						schema = factorySchema.newSchema(schemaPath);
						Validator validator = schema.newValidator();

						validator.validate(new DOMSource(document));

						// TODO Fehlerausgabe noch ergänzen
						try {
							new DOMImport(document);
						} catch (Exception e) {
							errorList.add("Fehler DOMImport");
						}
					}

				}
			} catch (Exception e) {
				errorList.add("Fehler FileFactory");
			}
		}

		return "UploadAction.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_UPLOAD_FILE);
	}

}
