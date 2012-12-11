package controllers;

import handlers.ExperienceHandler;
import handlers.VenueHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import models.Experience;
import models.Venue;

import utils.Util;
import views.html.*;

public class Application extends Controller {
	
	
	public static Result index() {
//		return ok(menatwork.render());
		return redirect(routes.Application.getExperiences(Util.getStringProperty("city.default"), Util.getStringProperty("category.default")));
	}
	
	
	public static Result aboutShadence() {
		return ok(about.render());
	}
	

	public static Result getExperiences(String cityId, String categoryId) {
		List<Experience> experienceList = new ArrayList<Experience>();
		
		// Refer Experience model for min and max values of priceRating
		// Minimum duration for any experience is 1 hour, 
		// max duration has been set to 10000 (more than a year) in order to cover all scenarios
		Map<String, String[]> parameters = request().queryString();
		String priceLow = (parameters.get("priceLow")==null)
				?Util.getStringProperty("priceRating.min"):Util.getString(parameters.get("priceLow")[0]);
		String priceHigh = (parameters.get("priceHigh")==null)
				?Util.getStringProperty("priceRating.max"):Util.getString(parameters.get("priceHigh")[0]);
		String durationLow = (parameters.get("durationLow")==null)
				?Util.getStringProperty("duration.min"):Util.getString(parameters.get("durationLow")[0]);
		String durationHigh = (parameters.get("durationHigh")==null)
				?Util.getStringProperty("duration.max"):Util.getString(parameters.get("durationHigh")[0]);
		
		experienceList = ExperienceHandler.getExperiences(cityId, Util.getString(categoryId), 
													durationLow, durationHigh, priceLow, priceHigh);
		
		String format = (parameters.get("format")==null)?"":Util.getString(parameters.get("format")[0]);
		
		if(format.equalsIgnoreCase(Util.getStringProperty("format.json"))) {
			ObjectMapper mapper = new ObjectMapper();
			StringBuffer json = new StringBuffer();
			try {
				json.append(mapper.writeValueAsString(experienceList));
			} catch (Exception exception) {
				Logger.error("Error while creating JSON string.", exception);
			}
			return ok(json.toString());
		} else {
			return ok(experiences.render(cityId, categoryId, experienceList));
		}
		
	}


	public static Result getExperience(String experienceId) {
		Experience exp = ExperienceHandler.getExperience(Util.getString(experienceId));
		if(exp==null)
			return redirect(routes.Application.index());
		else
			return ok(experience.render(exp));
	}
	
	
	public static Result getVenue(String venueId) {
		Venue v = VenueHandler.getVenue(Util.getString(venueId));
		
		if (v==null)
			return redirect(routes.Application.index());
		else
			return ok(venue.render(v, ExperienceHandler.getExperiencesAtVenue(venueId)));
	}

}