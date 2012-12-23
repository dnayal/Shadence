package handlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import play.Logger;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionList;

import utils.Util;
import models.Experience;
import models.ExperienceCategory;
import models.User;
import models.Venue;

public class ExperienceHandler {
	
	
	private static final int ALL_EXPERIENCES_IN_CITY = 1; /*Including invisible and expired ones*/
	private static final int EXPERIENCES_IN_CITY = 2;
	private static final int EXPERIENCES_AT_VENUE = 3;
	
	
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
		
		Venue venue = VenueHandler.getVenue(venueId);
		User user = UserHandler.getUser(userId);
		ExperienceCategory category = ExperienceCategoryHandler.getExperienceCategory(categoryId);
		
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
		
		return getExperiences(cityId, categoryId, 
				Util.getStringProperty("duration.min"), Util.getStringProperty("duration.max"),
				Util.getStringProperty("priceRating.min"), Util.getStringProperty("priceRating.max"),
				Util.getIntegerProperty("application.pagestart"));
	}

	
	/**
	 * Returns all experiences by city, category, duration and price
	 */
	public static List<Experience> getExperiences(String cityId, String categoryId, String durationLow, 
						String durationHigh, String priceLow, String priceHigh, Integer firstRow) {

		List<Experience> experiences = new ArrayList<Experience>();

		ExpressionList<Experience> expressionList = 
				getCommonExpressionToGetExperiences(EXPERIENCES_IN_CITY, cityId);
		
		if (!categoryId.equalsIgnoreCase(Util.getStringProperty("category.default")))
			expressionList = expressionList.eq("category_id", categoryId);
		
		expressionList = expressionList.between("duration", durationLow, durationHigh)
										.between("priceRating", priceLow, priceHigh);

		experiences = expressionList.orderBy("createTimestamp desc")
							.setMaxRows(Util.getIntegerProperty("application.pagesize"))
							.setFirstRow(firstRow).findList();

		return experiences;
	}
	
	
	/**
	 * Returns all experiences at the given venue
	 */
	public static List<Experience> getExperiencesAtVenue(String venueId) {
		return getCommonExpressionToGetExperiences(EXPERIENCES_AT_VENUE, venueId)
				.orderBy("createTimestamp desc").findList();
	}
	
	
	/**
	 * Returns all experiences by city - even the expired and hidden ones
	 */
	public static List<Experience> getAllExperiences(String cityId) {
		return getCommonExpressionToGetExperiences(ALL_EXPERIENCES_IN_CITY, cityId)
				.orderBy("createTimestamp desc").findList();
	}
	

	/**
	 * Returns random list of experiences with the following criteria
	 * - similar category, or
	 * - matching tags
	 * 
	 * If similar experiences were less than maximum allowed, just send the result.
	 * Otherwise, pick random items from the list (equal to maximum number allowed)
	 * and send the result
	 */
	public static List<Experience> getSimilarExperiences(String experienceId) {
		List<Experience> listofSimilarExpriences = new ArrayList<Experience>();
		Experience experience = getExperience(experienceId);
		String tags[] = experience.getTags().split("[\\s,]+");
		ExpressionList<Experience> expressionList = getCommonExpressionToGetExperiences(EXPERIENCES_IN_CITY, 
				experience.getVenue().getCity().getCityId()).ne("experienceId", experienceId);
		Expression expression = Expr.eq("category_id", experience.getCategory().getCategoryId());
		for(String tag: tags) 
			expression = Expr.or(expression, Expr.ilike("tags", "%"+tag+"%"));
		listofSimilarExpriences = expressionList.add(expression).findList();

		int maximumExperiencesCount = Util.getIntegerProperty("experiences.similar.count");
		int similarExperiencesCount = listofSimilarExpriences.size();
		if(similarExperiencesCount > maximumExperiencesCount) {
			Random random = new Random();
			List<Integer> randomNumberList = new ArrayList<Integer>();
			int nextInt = 0;
			while(randomNumberList.size() < maximumExperiencesCount) {
				nextInt = random.nextInt(similarExperiencesCount);
				if(!randomNumberList.contains(nextInt))
					randomNumberList.add(nextInt);
			}
			
			List<Experience> randomListofSimilarExpriences = new ArrayList<Experience>();
			for(int index : randomNumberList)
				randomListofSimilarExpriences.add(listofSimilarExpriences.get(index));
			listofSimilarExpriences = randomListofSimilarExpriences;
		}
		
		return listofSimilarExpriences;
	}
	

	/**
	 * Generates common expression to get Experiences
	 */
	private static ExpressionList<Experience> getCommonExpressionToGetExperiences(int typeOfExperiences, 
			String cityOrVenueId) {
		
		Date currentDate = new Date();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			currentDate = dateFormat.parse(dateFormat.format(new Date()));
		} catch (ParseException exception) {
			Logger.error("Unable to parse current date", exception);
		}
		
		ExpressionList<Experience> expressionList = null;

		switch(typeOfExperiences) {
			case ALL_EXPERIENCES_IN_CITY:
				expressionList = Experience.find.where()
									.eq("venue.city.cityId", cityOrVenueId);
				break;
			case EXPERIENCES_IN_CITY:
				expressionList = Experience.find.where()
									.eq("venue.city.cityId", cityOrVenueId)
									.ne("hidden", true)
									.or(Expr.ge("endDate", currentDate), Expr.isNull("endDate"));
				break;
			case EXPERIENCES_AT_VENUE:
				expressionList = Experience.find.where()
									.eq("venue_id", cityOrVenueId)
									.ne("hidden", true)
									.or(Expr.ge("endDate", currentDate), Expr.isNull("endDate"));
				break;
		}
		
		return expressionList;
	}

}
