package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response;

import java.util.List;

public class Response<T> extends ResponseBase{
	private List<T> dati;

	public List<T> getDati() {
		return dati;
	}

	public void setDati(List<T> dati) {
		this.dati = dati;
	}   
}
