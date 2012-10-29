package handlers;

import java.util.List;

import models.City;

public class CityHandler {

	
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
		return City.find.byId(cityId);
	}
	
	
	/**
	 * Returns all cities
	 */
	public static List<City> getAllCities() {
		return City.find.all();
	}
}
