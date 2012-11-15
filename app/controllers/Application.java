package controllers;

import handlers.ExperienceHandler;

import java.util.ArrayList;
import java.util.List;

import play.Configuration;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import models.Experience;

import utils.Util;
import views.html.*;

public class Application extends Controller {
	
	static Configuration config = Play.application().configuration();
  
	
	public static Result index() {
		return redirect(routes.Application.getExperiences(config.getString("city.default"), config.getString("category.default")));
	}


	public static Result getExperiences(String cityId, String categoryId) {
		List<Experience> experienceList = new ArrayList<Experience>();
		
		if(Util.getString(categoryId).equalsIgnoreCase(config.getString("category.default")))
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

}