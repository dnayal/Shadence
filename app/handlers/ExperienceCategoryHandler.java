package handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ExperienceCategory;
import models.Venue;

public class ExperienceCategoryHandler {

	
	/**
	 * Saves the experience category
	 */
	public static void saveExperienceCategory(ExperienceCategory category) {
		category.save();
	}
	

	/**
	 * Saves the experience category
	 */
	public static ExperienceCategory saveExperienceCategory(String categoryId, String name) {
		ExperienceCategory category = new ExperienceCategory(categoryId, name, System.currentTimeMillis());

		saveExperienceCategory(category);
		
		return category;
	}
	

	/**
	 * Returns an experience category by id
	 */
	public static ExperienceCategory getExperienceCategory(String categoryId) {
		return ExperienceCategory.find.byId(categoryId);
	}
	
	
	/**
	 * Returns all experience categories 
	 */
	public static List<ExperienceCategory> getAllCategories() {
		return ExperienceCategory.find.all();
	}

	
	/**
	 * Returns a map of all categories (name and id)
	 */
	public static Map<String, String> getAllCategoriesMap() {
		Map<String, String> categories = new HashMap<String, String>();
		
		for (ExperienceCategory category : getAllCategories())
			categories.put(category.getCategoryId(), category.getName());

		return categories;
	}

}
