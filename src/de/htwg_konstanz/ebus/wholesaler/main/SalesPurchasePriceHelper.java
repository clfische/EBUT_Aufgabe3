package de.htwg_konstanz.ebus.wholesaler.main;

import java.math.BigDecimal;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOAddress;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOPurchasePrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.PriceBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa._BaseBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.vo.Country;
import de.htwg_konstanz.ebus.framework.wholesaler.vo.util.Constants;

public class SalesPurchasePriceHelper {

	public static void saveSalesPruchasePrice(NodeList territoryList,
			BOProduct boProduct, BigDecimal taxValue, BigDecimal price_amount_value,
			String price_type) {
		
		for(int a=0; a<territoryList.getLength(); a++){
		System.out.println("fehler sales 25");
		Element isocode = (Element) territoryList.item(a);
		String iso = isocode.getFirstChild().getNodeValue();
		System.out.println("fehler sales 28");
		Country country = new Country(iso);
		BOCountry boCountry = new BOCountry(country);
		System.out.println("fehler sales 31");
		//Verkaufspreis
		
		BOSalesPrice salesPrice = new BOSalesPrice();salesPrice.setProduct(boProduct);
		System.out.println("fehler sales 35");
		salesPrice.setCountry(boCountry);
		System.out.println("fehler sales 37");
		salesPrice.setLowerboundScaledprice(Constants.DEFAULT_LOWERBOUND_SCALED_PRICE);
		System.out.println("fehler sales 39");
		salesPrice.setPricetype(price_type);
		System.out.println("fehler sales 41");
		salesPrice.setTaxrate(taxValue);
		System.out.println("fehler sales 43");
		salesPrice.setAmount((BigDecimal) price_amount_value);
		System.out.println("fehler sales 45");
		_BaseBOA.getInstance().commit();
		PriceBOA.getInstance().saveOrUpdate(salesPrice);
		System.out.println("fehler sales 43");
		
	
		
		
		//Einkaufspreis
		BOPurchasePrice purchasePrice = new BOPurchasePrice();
		purchasePrice.setAmount((BigDecimal) price_amount_value);
		purchasePrice.setPricetype(price_type);
		purchasePrice.setTaxrate((BigDecimal) taxValue);
		purchasePrice.setProduct(boProduct);
		purchasePrice.setCountry(boCountry);
		purchasePrice.setLowerboundScaledprice(Constants.DEFAULT_LOWERBOUND_SCALED_PRICE);
		System.out.println("feehler sales 53");
		_BaseBOA.getInstance().commit();
		
		System.out.println("fehler sales 56");
		PriceBOA.getInstance().saveOrUpdate(purchasePrice);
		
		
		
		
		
		}
		
		
	}

}
