package tests;

import java.util.Calendar;

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
import controllers.routes;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class TestController extends Controller {
	

	public static Result city(String cityId) {
		return ok(index.render(ExperienceHandler.getExperiences(cityId)));
	}

	
	public static Result cityAndCategory(String cityId, String categoryId) {
		return ok(index.render(ExperienceHandler.getExperiences(cityId, categoryId)));
	}

	
	public static Result allFilters(String cityId, String categoryId, String durationLow,
			String durationHigh, String priceLow, String priceHigh) {
		
		return ok(index.render(ExperienceHandler.getExperiences(cityId, categoryId, durationLow, 
					durationHigh, priceLow, priceHigh)));
	}


	public static Result getExperiencesAtVenue(String venueId) {
		
		return ok(index.render(ExperienceHandler.getExperiencesAtVenue(venueId)));
	}


	public static Result populate() {
		City london = CityHandler.saveCity("london", "London", null, "United Kingdom");
		City cambridge = CityHandler.saveCity("cambridge", "Cambridge", "Cambridgeshire", "United Kingdom");
		
		Venue hydepark = VenueHandler.saveVenue(london.getCityId(), "Hyde Park", null, "NW11 2WE", "51.123", "-0.123");
		Venue finchley = VenueHandler.saveVenue(london.getCityId(), "Finchley Central", "43c, Station Road", "N3 2SH", "51.123", "-0.123");
		Venue cherryhinton = VenueHandler.saveVenue(cambridge.getCityId(), "Cherry Hinton", "334", "CB23AZ", "51.123", "-0.123");
		
		ExperienceCategory outdoor = ExperienceCategoryHandler.saveExperienceCategory("outdoor", "Outdoor");
		ExperienceCategory adventure = ExperienceCategoryHandler.saveExperienceCategory("adventure", "Adventure");
		ExperienceCategory artandcraft = ExperienceCategoryHandler.saveExperienceCategory("artandcraft", "Arts & Crafts");
		
		User deepak = UserHandler.saveUser("Deepak Nayal", "dnayal@gmail.com", null, "M");
		User derek = UserHandler.saveUser("Derek", "rcdd4n@yahoo.com", null, "M");
		
		Experience dayout = ExperienceHandler.saveExperience(hydepark.getVenueId(), deepak.getUserId(), 
				outdoor.getCategoryId(), "Day out in Hyde Park", "info@hydepark.com", null, 
				"Just spend the day buddy", "$34 per person", 3, 8, "Jan::9am-5pm..Feb::10am-4pm", 
				"http://royalparks.com", "parks, nature", null, null);
		
		Experience dinner = ExperienceHandler.saveExperience(finchley.getVenueId(), deepak.getUserId(), 
				outdoor.getCategoryId(), "Thai food and drinks", "thai@food.com", "07771234455", 
				"This is really good", "$100 per couple", 6, 2, "Everyday::12:00pm..2:00am", 
				"http://thai.restaurant.com", "food, thai", null, null);
	
		Calendar starttime = Calendar.getInstance();
		starttime.set(2012, 10, 30);
		Calendar endtime = Calendar.getInstance();
		endtime.set(2012, 11, 10);
		
		Experience winterwonderland = ExperienceHandler.saveExperience(hydepark.getVenueId(), 
				derek.getUserId(), adventure.getCategoryId(), "Winter Wonderland", 
				"contact@winterwonderland.com", "02287879191", "This is a great place to be in winters", 
				"Free Entry", 1, 3, "Everyday::10am-10pm..26th November::10am-6pm", 
				"http://winterwonderland.com", "winter, theme park, night", starttime.getTime(), 
				endtime.getTime());
		
		Experience leisurecenter = ExperienceHandler.saveExperience(cherryhinton.getVenueId(), 
				derek.getUserId(), outdoor.getCategoryId(), "Leisure Center", "fun@leisure.com", null, 
				"Cambridge Leisure Center", "Free Entry. Charge for individual items.", 3, 
				2, "Open 24 hours", "http://leisure.center.com", "casino, games", null, null);
		
		return redirect(routes.Application.index());
	}
	
	
}
