package handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Util;
import models.City;
import models.Venue;

public class VenueHandler {

	
	/**
	 * Updates the venue
	 */
	public static void updateVenue(Venue venue) {
		venue.update();
	}

	
	/**
	 * Saves the venue
	 */
	public static void saveVenue(Venue venue) {
		venue.save();
	}
	
	
	/**
	 * Saves the venue
	 */
	public static Venue saveVenue(String cityId, String name, String address, String venueCity,
								String postcode, String latitude, String longitude) {

		String venueId = Util.getUniqueId();
		Venue venue = new Venue(venueId, name, address, venueCity, postcode, latitude, longitude, 
				System.currentTimeMillis());
		
		City city = City.find.byId(cityId);
		
		venue.setCity(city);
		
		saveVenue(venue);
		
		return venue;
	}
	
	
	/**
	 * Returns a venue based on id
	 */
	public static Venue getVenue(String venueId) {
		return Venue.find.byId(Util.getString(venueId));
	}
	
	
	/**
	 * Returns all venues in a city
	 */
	public static List<Venue> getVenues(String cityId) {
		return Venue.find.where().eq("city_id", cityId).findList();
	}


	/**
	 * Returns a map of all venues (name and id) in a city
	 */
	public static Map<String, String> getVenuesMap(String cityId) {
		Map<String, String> venues = new HashMap<String, String>();
		
		for (Venue venue : getVenues(cityId))
			venues.put(venue.getVenueId(), venue.getName());

		return venues;
	}

}
