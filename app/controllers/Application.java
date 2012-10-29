package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import models.Experience;

import views.html.*;

public class Application extends Controller {
  
	public static Result index() {
		return ok(index.render(Experience.find.all()));
	}

	// TODO - prepare a test case for all handlers
	
}