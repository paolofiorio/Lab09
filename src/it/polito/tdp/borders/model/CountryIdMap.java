package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;

public class CountryIdMap {

	private Map<Integer,Country> idMap;

	public CountryIdMap() {
		idMap = new HashMap<Integer, Country>();
	}
	public Country get(int countryCode) {
		return idMap.get(countryCode);
	}
	
	public Country get(Country country) {
		Country old = idMap.get(country.getcCode());
		if (old == null) {
			idMap.put(country.getcCode(), country);
			return country;
		}
		return old;
	}
	
	public void put(Country country, int countryCode) {
		idMap.put(countryCode, country);
}
	
}
