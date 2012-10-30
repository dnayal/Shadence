package controllers;

import models.Experience;
import handlers.ExperienceCategoryHandler;
import handlers.ExperienceHandler;
import handlers.VenueHandler;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import utils.Util;
import views.html.admin.*;

public class Admin extends Controller {
	
	public static Result getExperiences(String cityId) {
		return ok(experiences.render(cityId, ExperienceHandler.getExperiences(cityId)));
	}
	
	
	public static Result newExperience(String cityId) {
		Form<Experience> formExperience = form(Experience.class);
		return ok(experiences_form.render(cityId, formExperience, VenueHandler.getVenuesMap(cityId), 
				ExperienceCategoryHandler.getAllCategoriesMap()));
	}
	
	
	public static Result saveExperience(String cityId) {
		Form<Experience> experienceForm = form(Experience.class).bindFromRequest();
		if(experienceForm.hasErrors())
			return badRequest(experiences_form.render(cityId, experienceForm, 
					VenueHandler.getVenuesMap(cityId), ExperienceCategoryHandler.getAllCategoriesMap()));
		else {
			Experience experience = experienceForm.get();
			if (experience.getExperienceId() == null || 
					experience.getExperienceId().equalsIgnoreCase("")) {
				experience.setExperienceId(Util.getUniqueId());
				ExperienceHandler.saveExperience(experience);
			} else {
				ExperienceHandler.updateExperience(experience);
			}

			return redirect(routes.Admin.getExperiences(cityId));
			
		}
	}
	
	
	public static Result editExperience(String cityId, String experienceId) {
		Form<Experience> experienceForm = form(Experience.class)
				.fill(ExperienceHandler.getExperience(experienceId));

		return ok(experiences_form.render(cityId, experienceForm, VenueHandler.getVenuesMap(cityId), 
				ExperienceCategoryHandler.getAllCategoriesMap()));
	}
	
	
}
