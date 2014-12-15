package de.htwg_konstanz.ebus.wholesaler.main;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOAddress;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.AddressBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.CountryBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.vo.util.Constants;

public class SupplierHelper {

	public static String saveNewSupplier(String supplierName) throws Exception {
		
		BOCountry country = CountryBOA.getInstance().findCountry(Constants.DEFAULT_COUNTRY_ISO_CODE);
		
		if(country == null){
			throw new Exception("Kein Land gefunden");
		}
		
		//Adresse erzeugen
		System.out.println("fehler supplierhelper 22");
		
		BOAddress address = new BOAddress();
		address.setStreet("Brauneggerstraﬂe");
		address.setZipcode("Zipcode");
		address.setCity("Konstanz");
		address.setCountry(country);
		AddressBOA.getInstance().saveOrUpdate(address);
		
		//Lieferanten erzeugen

		System.out.println("fehler supplierhelper 22");
		BOSupplier supplierBO = new BOSupplier();
		supplierBO.setAddress(address);
		supplierBO.setFirstname("KN Media");
		supplierBO.setLastname("Store");
		supplierBO.setWsUserName("kn_media");
		supplierBO.setWsPassword("kn_store");
		
		//supplierBO.setWsCatalogEndpoint(null);
		//supplierBO.setWsOrderEndpoint(null);
		supplierBO.setWsCatalogEndpoint("http://localhost:8080/ess/ProductCatalogService");
	    supplierBO.setWsOrderEndpoint("http://localhost:8080/ess/OrderService");
		
		//Companyname nicht gefunden?!

		System.out.println("fehler supplierhelper 22");
		supplierBO.setCompanyname(supplierName);

		System.out.println("fehler supplierhelper 22");
		SupplierBOA.getInstance().saveOrUpdate(supplierBO);
				
		return supplierBO.getSupplierNumber();
	}

}
