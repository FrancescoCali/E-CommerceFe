package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WebUtils {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object convertInObject(Object inp, Class tipo) {
		Object res = new Object();
		ObjectMapper mapper = new ObjectMapper();
		try {
			res = mapper.convertValue(inp, tipo);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return res;
	}

}
