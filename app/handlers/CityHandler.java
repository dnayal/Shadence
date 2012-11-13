package handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Util;

import models.City;

public class CityHandler {

	
	/**
	 * Updates the city
	 */
	public static void updateCity(City city) {
		city.update();
	}

	
	/**
	 * Saves the city
	 */
	public static void saveCity(City city) {
		city.save();
	}
	

	/**
	 * Saves the city
	 * 
	 * Unlike other handlers, we are not generating cityId here, 
	 * as it will usually be the city name. For e.g., london for London,
	 * munich for Munich and delhi for Delhi
	 */
	public static City saveCity(String cityId, String name, String stateOrCounty, String country) {
		City city = new City(cityId, name, stateOrCounty, country, System.currentTimeMillis());
		
		saveCity(city);
		
		return city;
	}
	
	
	/**
	 * Returns a city by id
	 */
	public static City getCity(String cityId) {
		return City.find.byId(Util.getString(cityId));
	}
	
	
	/**
	 * Returns all cities
	 */
	public static List<City> getCities() {
		return City.find.all();
	}
	
	
	/**
	 * Returns a map of all cities (name and id)
	 */
	public static Map<String, String> getCitiesMap() {
		Map<String, String> cities = new HashMap<String, String>();
		
		for (City city : getCities())
			cities.put(city.getCityId(), city.getName());

		return cities;
	}
	
	
}
