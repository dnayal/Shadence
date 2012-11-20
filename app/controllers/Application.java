package controllers;

import handlers.ExperienceHandler;
import handlers.VenueHandler;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import models.Experience;
import models.Venue;

import utils.Util;
import views.html.*;

public class Application extends Controller {
	
	
	public static Result index() {
		return redirect(routes.Application.getExperiences(Util.getStringProperty("city.default"), Util.getStringProperty("category.default")));
	}
	
	public static Result aboutShadence() {
		return ok(about.render());
	}


	public static Result getExperiences(String cityId, String categoryId) {
		List<Experience> experienceList = new ArrayList<Experience>();
		
		if(Util.getString(categoryId).equalsIgnoreCase(Util.getStringProperty("category.default")))
			experienceList = ExperienceHandler.getExperiences(cityId);
		else
			experienceList = ExperienceHandler.getExperiences(cityId, categoryId);
		
		return ok(experiences.render(cityId, categoryId, experienceList));
		
	}


	public static Result getExperience(String experienceId) {
		Experience exp = ExperienceHandler.getExperience(experienceId);
		if(exp==null)
			return TODO;
		else
			return ok(experience.render(exp));
	}
	
	public static Result getVenue(String venueId) {
		Venue v = VenueHandler.getVenue(venueId);
		
		if (v==null)
			return TODO;
		else
			return ok(venue.render(v, ExperienceHandler.getExperiencesAtVenue(venueId)));
	}

}