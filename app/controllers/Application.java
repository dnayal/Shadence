package controllers;

import models.ExperienceCategory;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
	public static Result index() {
		
		ExperienceCategory experienceCategory = new ExperienceCategory("1", "Adventure", 12345678l);
		return ok(index.render("Shadence has been setup!", experienceCategory));
	}
}