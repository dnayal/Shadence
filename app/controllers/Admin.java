package controllers;

import models.City;
import models.Experience;
import models.ExperienceCategory;
import models.User;
import models.Venue;
import handlers.CityHandler;
import handlers.ExperienceCategoryHandler;
import handlers.ExperienceHandler;
import handlers.UserHandler;
import handlers.VenueHandler;
import play.Logger;
import play.Play;
import play.Configuration;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import utils.Util;
import views.html.admin.*;

public class Admin extends Controller {
	
	
	public static Result getMain() {
		Configuration config = Play.application().configuration();
		Logger.info("+-+-+-+-+-+-+-+");
		Logger.info(String.valueOf(Play.isProd()));
		Logger.info(config.getString("shadence.name"));
		Logger.info("+-+-+-+-+-+-+-+");
		return ok(main_admin.render(CityHandler.getCities()));
	}

// EXPERIENCE - START
	public static Result getExperiences(String cityId) {
		return ok(experiences.render(cityId, ExperienceHandler.getAllExperiences(cityId)));
	}

	public static Result showExperienceForm(String cityId, String experienceId) {
		Form<Experience> experienceForm = null;
		if (Util.getString(experienceId).equalsIgnoreCase("new"))
			experienceForm = form(Experience.class);
		else
			experienceForm = form(Experience.class).fill(ExperienceHandler.getExperience(experienceId));

		return ok(experiences_form.render(cityId, experienceForm, VenueHandler.getVenuesMap(cityId), 
				ExperienceCategoryHandler.getExperienceCategoriesMap()));
	}
	
	public static Result saveExperience(String cityId) {
		Form<Experience> experienceForm = form(Experience.class).bindFromRequest();
		if(experienceForm.hasErrors())
			return badRequest(experiences_form.render(cityId, experienceForm, 
					VenueHandler.getVenuesMap(cityId), ExperienceCategoryHandler.getExperienceCategoriesMap()));
		else {
			Experience experience = experienceForm.get();
			if (Util.getString(experience.getExperienceId()).equalsIgnoreCase("")) {
				experience.setExperienceId(Util.getUniqueId());
				experience.setCreateTimestamp(System.currentTimeMillis());
				ExperienceHandler.saveExperience(experience);
			} else {
				experience.setCreateTimestamp(System.currentTimeMillis());
				ExperienceHandler.updateExperience(experience);
			}

			return redirect(routes.Admin.getExperiences(cityId));
			
		}
	}
// EXPERIENCE - END
	
	
// VENUE - START
	public static Result getVenues(String cityId) {
		return ok(venues.render(cityId, VenueHandler.getVenues(cityId)));
	}
	

	public static Result showVenueForm(String cityId, String venueId) {
		Form<Venue> venueForm = null;
		if (Util.getString(venueId).equalsIgnoreCase("new"))
			venueForm = form(Venue.class);
		else
			venueForm = form(Venue.class).fill(VenueHandler.getVenue(venueId));

		return ok(venues_form.render(cityId, venueForm, CityHandler.getCitiesMap()));
	}
	
	
	public static Result saveVenue(String cityId) {
		Form<Venue> venueForm = form(Venue.class).bindFromRequest();
		if(venueForm.hasErrors())
			return badRequest(venues_form.render(cityId, venueForm, CityHandler.getCitiesMap()));
		else {
			Venue venue = venueForm.get();
			if (Util.getString(venue.getVenueId()).equalsIgnoreCase("")) {
				venue.setVenueId(Util.getUniqueId());
				venue.setCreateTimestamp(System.currentTimeMillis());
				VenueHandler.saveVenue(venue);
			} else {
				venue.setCreateTimestamp(System.currentTimeMillis());
				VenueHandler.updateVenue(venue);
			}

			return redirect(routes.Admin.getVenues(cityId));
			
		}
	}
// VENUE - END
	

// EXPERIENCECATEGORY - START
	public static Result getCategories() {
		return ok(categories.render(ExperienceCategoryHandler.getExperienceCategories()));
	}
	

	public static Result showCategoryForm(String categoryId) {
		Form<ExperienceCategory> categoryForm = null;
		if (Util.getString(categoryId).equalsIgnoreCase("new"))
			categoryForm = form(ExperienceCategory.class);
		else
			categoryForm = form(ExperienceCategory.class).fill(ExperienceCategoryHandler.getExperienceCategory(categoryId));

		return ok(categories_form.render(categoryForm));
	}
	
	
	public static Result saveCategory() {
		Form<ExperienceCategory> categoryForm = form(ExperienceCategory.class).bindFromRequest();
		if(categoryForm.hasErrors())
			return badRequest(categories_form.render(categoryForm));
		else {
			ExperienceCategory category = categoryForm.get();
			ExperienceCategory fromDB = ExperienceCategoryHandler.getExperienceCategory(category.getCategoryId());
			if (fromDB == null) {
				category.setCreateTimestamp(System.currentTimeMillis());
				ExperienceCategoryHandler.saveExperienceCategory(category);
			} else {
				category.setCreateTimestamp(System.currentTimeMillis());
				ExperienceCategoryHandler.updateExperienceCategory(category);
			}

			return redirect(routes.Admin.getCategories());
			
		}
	}
// EXPERIENCECATEGORY - END

// CITY - START
	public static Result getCities() {
		return ok(cities.render(CityHandler.getCities()));
	}
	

	public static Result showCityForm(String cityId) {
		Form<City> cityForm = null;
		if (Util.getString(cityId).equalsIgnoreCase("new"))
			cityForm = form(City.class);
		else
			cityForm = form(City.class).fill(CityHandler.getCity(cityId));

		return ok(cities_form.render(cityForm));
	}
	
	
	public static Result saveCity() {
		Form<City> citiesForm = form(City.class).bindFromRequest();
		if(citiesForm.hasErrors())
			return badRequest(cities_form.render(citiesForm));
		else {
			City city = citiesForm.get();
			City fromDB = CityHandler.getCity(city.getCityId());
			if (fromDB == null) {
				city.setCreateTimestamp(System.currentTimeMillis());
				CityHandler.saveCity(city);
			} else {
				city.setCreateTimestamp(System.currentTimeMillis());
				CityHandler.updateCity(city);
			}

			return redirect(routes.Admin.getCities());
			
		}
	}
// CITY - END


// USER - START
	public static Result getUsers() {
		return ok(users.render(UserHandler.getUsers()));
	}
	

	public static Result showUserForm(String userId) {
		Form<User> userForm = null;
		if (Util.getString(userId).equalsIgnoreCase("new"))
			userForm = form(User.class);
		else
			userForm = form(User.class).fill(UserHandler.getUser(userId));

		return ok(users_form.render(userForm));
	}
	
	
	public static Result saveUser() {
		Form<User> userForm = form(User.class).bindFromRequest();
		if(userForm.hasErrors())
			return badRequest(users_form.render(userForm));
		else {
			User user = userForm.get();
			if (Util.getString(user.getUserId()).equalsIgnoreCase("")) {
				user.setUserId(Util.getUniqueId());
				user.setCreateTimestamp(System.currentTimeMillis());
				UserHandler.saveUser(user);
			} else {
				user.setCreateTimestamp(System.currentTimeMillis());
				UserHandler.updateUser(user);
			}

			return redirect(routes.Admin.getUsers());
		}
	}
// USER - END
}
