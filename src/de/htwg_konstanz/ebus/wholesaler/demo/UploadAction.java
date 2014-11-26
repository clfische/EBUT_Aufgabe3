package de.htwg_konstanz.ebus.wholesaler.demo;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;

public class UploadAction implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		// TODO Auto-generated method stub
		return "UploadAction.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_IMPORT_BMECAT);
	}
	
}
