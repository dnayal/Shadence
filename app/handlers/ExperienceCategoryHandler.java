package handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Util;

import models.ExperienceCategory;

public class ExperienceCategoryHandler {

	
	/**
	 * Updates the experience category
	 */
	public static void updateExperienceCategory(ExperienceCategory category) {
		category.update();
	}

	
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
		return ExperienceCategory.find.byId(Util.getString(categoryId));
	}
	
	
	/**
	 * Returns the name of the category based on Id
	 */
	public static String getCategoryName(String categoryId) {
		if (categoryId.equalsIgnoreCase(Util.getStringProperty("category.default")))
			return "All";
		else 
			return ExperienceCategory.find.byId(Util.getString(categoryId)).getName();
	}
	
	
	/**
	 * Returns all experience categories 
	 */
	public static List<ExperienceCategory> getExperienceCategories() {
		List<ExperienceCategory> list = ExperienceCategory.find.all(); 
		ExperienceCategory all = new ExperienceCategory(Util.getStringProperty("category.default"), "All", null);
		list.add(0, all);
		return list;
	}

	
	/**
	 * Returns a map of all categories (name and id)
	 */
	public static Map<String, String> getExperienceCategoriesMap() {
		Map<String, String> categories = new HashMap<String, String>();
		
		for (ExperienceCategory category : getExperienceCategories())
			categories.put(category.getCategoryId(), category.getName());

		return categories;
	}

}
