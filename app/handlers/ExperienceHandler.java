package handlers;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Expr;

import utils.Util;
import models.Experience;
import models.ExperienceCategory;
import models.User;
import models.Venue;

public class ExperienceHandler {
	
	
	/**
	 * Updates the experience
	 */
	public static void updateExperience(Experience experience) {
		experience.update();
	}

	
	/**
	 * Saves the experience
	 */
	public static void saveExperience(Experience experience) {
		experience.save();
	}

	
	/**
	 * Saves the experience
	 */
	public static Experience saveExperience(String venueId, String userId, String categoryId, String name, 
				String email, String phone, String description, String priceDescription, Integer priceRating, 
				Integer duration, String scheduleDescription, String originalSource, String tags, 
				Date startDate, Date endDate) {
		
		String experienceId = Util.getUniqueId();
		Experience experience = new Experience(experienceId, name, email, phone, description, 
				priceDescription, priceRating, duration, scheduleDescription, originalSource, tags,
				startDate, endDate, System.currentTimeMillis());
		
		Venue venue = Venue.find.byId(venueId);
		User user = User.find.byId(userId);
		ExperienceCategory category = ExperienceCategory.find.byId(categoryId);
		
		experience.setVenue(venue);
		experience.setUser(user);
		experience.setCategory(category);
		
		saveExperience(experience);
		
		return experience;
	}
	
	
	/**
	 * Returns an experience by id
	 */
	public static Experience getExperience(String experienceId) {
		return Experience.find.byId(experienceId);
	}
	
	
	/**
	 * Returns all experiences by city
	 * and where experience enddate has not expired
	 */
	public static List<Experience> getExperiences(String cityId) {
		List<Experience> experiences = Experience.find
				.where()
				.in("venue_id", Venue.find.where().eq("city_id", cityId).findIds())
				.or(Expr.ge("endDate", new Date()), Expr.isNull("endDate"))
				.findList();
		
		return experiences;
	}
	

	/**
	 * Returns all experiences by city and category
	 */
	public static List<Experience> getExperiences(String cityId, String categoryId) {
		List<Experience> experiences = Experience.find
				.where()
				.in("venue_id", Venue.find.where().eq("city_id", cityId).findIds())
				.eq("category_id", categoryId)
				.or(Expr.ge("endDate", new Date()), Expr.isNull("endDate"))
				.findList();
		
		return experiences;
	}

	
	/**
	 * Returns all experiences by city, category, duration and price
	 */
	public static List<Experience> getExperiences(String cityId, String categoryId, String durationLow, 
						String durationHigh, String priceLow, String priceHigh) {
		List<Experience> experiences = Experience.find
				.where()
				.in("venue_id", Venue.find.where().eq("city_id", cityId).findIds())
				.eq("category_id", categoryId)
				.between("duration", durationLow, durationHigh)
				.between("priceRating", priceLow, priceHigh)
				.or(Expr.ge("endDate", new Date()), Expr.isNull("endDate"))
				.findList();
		
		return experiences;
	}
	
	
	/**
	 * Returns all experiences at the given venue
	 */
	public static List<Experience> getExperiencesAtVenue(String venueId) {
		List<Experience> experiences = Experience.find
				.where()
				.eq("venue_id", venueId)
				.findList();
		
		return experiences;
	}
	
	
	/**
	 * Returns all experiences by city - even the expired ones
	 */
	public static List<Experience> getAllExperiences(String cityId) {
		List<Experience> experiences = Experience.find
				.where()
				.in("venue_id", Venue.find.where().eq("city_id", cityId).findIds())
				.findList();
		
		return experiences;
	}
	

}
