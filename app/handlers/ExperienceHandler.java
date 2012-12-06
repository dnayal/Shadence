package handlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;

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
				String email, String twitter, String phone, String description, String priceDescription, 
				Integer priceRating, Integer duration, String scheduleDescription, String originalSource, 
				String tags, Date startDate, Date endDate, Boolean hidden) {
		
		String experienceId = Util.getUniqueId();
		Experience experience = new Experience(experienceId, name, email, twitter, phone, description, 
				priceDescription, priceRating, duration, scheduleDescription, originalSource, tags,
				startDate, endDate, hidden, System.currentTimeMillis());
		
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
		return Experience.find.byId(Util.getString(experienceId));
	}
	
	
	/**
	 * Generates common expression to get Experiences
	 */
	private static ExpressionList<Experience> getCommonExpressionToGetExperiences(List<Object> venueIds) {
		Date currentDate = new Date();

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			currentDate = dateFormat.parse(dateFormat.format(new Date()));
		} catch (ParseException exception) {
			Logger.error("Unable to parse current date", exception);
		}
		
		ExpressionList<Experience> expressionList = Experience.find.where()
				.in("venue_id", venueIds)
				.ne("hidden", true)
				.or(Expr.ge("endDate", currentDate), Expr.isNull("endDate"));
		
		return expressionList;
	}
	

	/**
	 * Returns all experiences by city
	 * and where experience enddate has not expired
	 */
	public static List<Experience> getExperiences(String cityId) {
		return getExperiences(cityId, Util.getStringProperty("category.default"));
	}
	

	/**
	 * Returns all experiences by city and category
	 */
	public static List<Experience> getExperiences(String cityId, String categoryId) {
		
		List<Experience> experiences = new ArrayList<Experience>();

		ExpressionList<Experience> expressionList = 
				getCommonExpressionToGetExperiences(Venue.find.where().eq("city_id", cityId).findIds());
		
		if (!categoryId.equalsIgnoreCase(Util.getStringProperty("category.default")))
			expressionList = expressionList.eq("category_id", categoryId);
		
		// show latest experiences first
		experiences = expressionList.orderBy("createTimestamp desc").findList();

		return experiences;
	}

	
	/**
	 * Returns all experiences by city, category, duration and price
	 * 
	 * TODO - needs to be integrated with other getExperience() method
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
		List<Object> list = new ArrayList<Object>();
		list.add(venueId);
		
		List<Experience> experiences = getCommonExpressionToGetExperiences(list)
											.orderBy("createTimestamp desc").findList();

		return experiences;
	}
	
	
	/**
	 * Returns all experiences by city - even the expired and hidden ones
	 */
	public static List<Experience> getAllExperiences(String cityId) {
		List<Experience> experiences = Experience.find
				.where()
				.in("venue_id", Venue.find.where().eq("city_id", cityId).findIds())
				.findList();
		
		return experiences;
	}
	

}
