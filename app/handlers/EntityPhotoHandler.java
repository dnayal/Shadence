package handlers;

import models.City;
import models.EntityPhoto;
import models.Experience;
import models.User;

public class EntityPhotoHandler {

	
	/**
	 * Saves the entity photo
	 */
	public static void saveEntityPhoto(EntityPhoto photo) {
		photo.save();
	}
	

	/**
	 * Saves the entity photo
	 */
	public static EntityPhoto saveEntityPhoto(String originalPhotoId, String entityCityId, 
			String entityExperienceId, String entityUserId, String userId, String location, 
			String name, String alternateText, String photoOrder) {
		
		EntityPhoto photo = new EntityPhoto(originalPhotoId, location, name, alternateText, 
				photoOrder, System.currentTimeMillis());
		
		if (entityCityId!=null && !entityCityId.equalsIgnoreCase("")) {
			City city = City.find.byId(entityCityId);
			photo.setEntityCity(city);
		} else if (entityExperienceId!=null && !entityExperienceId.equalsIgnoreCase("")){
			Experience experience = Experience.find.byId(entityExperienceId);
			photo.setEntityExperience(experience);
		} else if (entityUserId!=null && !entityUserId.equalsIgnoreCase("")){
			User user = User.find.byId(entityUserId);
			photo.setEntityUser(user);
		}
		
		User user = User.find.byId(userId);
		photo.setUser(user);
		
		saveEntityPhoto(photo);
		
		return photo;
	}

}
