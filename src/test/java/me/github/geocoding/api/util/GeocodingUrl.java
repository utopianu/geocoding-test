package me.github.geocoding.api.util;

import java.text.MessageFormat;

public enum GeocodingUrl {
	GEOCODING_JSON("/geocode/json"),
	GEOCODING_DEFAULT("/geocode/json?address={0}"),
	GEOCODING_XML("/geocode/xml"),
	REVERSE_GEOCODING_DEFAULT("geocode/json?latlng={0}");
	
	private String url;
	
	GeocodingUrl(String url){
		this.url=url;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getUrl(String... replaceval){
		return MessageFormat.format(url,replaceval);
	}

}
