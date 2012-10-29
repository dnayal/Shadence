package handlers;

import utils.Util;
import models.City;
import models.Venue;

public class VenueHandler {

	
	/**
	 * Saves the venue
	 */
	public static void saveVenue(Venue venue) {
		System.out.println(venue.getVenueId());
		venue.save();
	}
	
	
	/**
	 * Saves the venue
	 */
	public static Venue saveVenue(String cityId, String name, String address, 
								String postcode, String latitude, String longitude) {

		String venueId = Util.getUniqueId();
		Venue venue = new Venue(venueId, name, address, postcode, latitude, longitude, 
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
		return Venue.find.byId(venueId);
	}
}
