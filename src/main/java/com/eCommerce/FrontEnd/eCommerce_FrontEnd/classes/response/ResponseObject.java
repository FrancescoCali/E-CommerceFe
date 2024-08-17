package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response;

public class ResponseObject<T> extends ResponseBase{
	private T dati;

	public T getDati() {
		return dati;
	}

	public void setDati(T dati) {
		this.dati = dati;
	}   
}
