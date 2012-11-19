package handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Image;
import utils.Util;

import models.EntityPhoto;

public class EntityPhotoHandler {

	public static final int INSERT_OPERATION = 1;
	public static final int UPDATE_OPERATION = 2;
	
	
	/**
	 * Saves the entity photo
	 */
	public static void saveEntityPhoto(EntityPhoto photo, int operationType) {
		switch(operationType) {
			case INSERT_OPERATION:
				photo.save();
				break;
			case UPDATE_OPERATION:
				photo.update();
				break;
		}
	}
	

	/**
	 * Saves the entity photo
	 */
	public static EntityPhoto saveEntityPhoto(int operationType, String photoId, 
			String entityId, int entityType, String userId, String location, String originalPhoto, 
			String largePhoto, String mediumPhoto, String smallPhoto, String alternateText, String photoOrder) {
		
		EntityPhoto photo = new EntityPhoto(photoId, location, originalPhoto, largePhoto, 
				mediumPhoto, smallPhoto, alternateText, photoOrder, System.currentTimeMillis());

		switch(entityType) {
			case EntityPhoto.ENTITY_EXPERIENCE:
				photo.setEntityExperience(ExperienceHandler.getExperience(entityId));
				break;
			case EntityPhoto.ENTITY_CITY:
				photo.setEntityCity(CityHandler.getCity(entityId));
				break;
			case EntityPhoto.ENTITY_USER:	
				photo.setEntityUser(UserHandler.getUser(entityId));
				break;
		}
		
		photo.setUser(UserHandler.getUser(userId));
		
		saveEntityPhoto(photo, operationType);
		
		return photo;
	}
	
	
	/**
	 * Processes image in multiple sizes and then saves record in database
	 */
	public static EntityPhoto processEntityPhoto(int operationType, String filename, File uploadedFile, String photoId, 
			String entityId, int entityType, String userId, String alternateText, String photoOrder) {
		
		if (operationType==INSERT_OPERATION)
			photoId = Util.getUniqueId();
		
		String fileExtension = Util.getFileExtension(filename);
		String photos[] = new String[4];
		photos[0] = photoId.concat(Util.getStringProperty("photos.original.suffix")).concat(fileExtension);
		photos[1] = photoId.concat(Util.getStringProperty("photos.large.suffix")).concat(fileExtension);
		photos[2] = photoId.concat(Util.getStringProperty("photos.medium.suffix")).concat(fileExtension);
		photos[3] = photoId.concat(Util.getStringProperty("photos.small.suffix")).concat(fileExtension);
		
		String pathPrefix = Util.getStringProperty("photos.upload.path") + "/" + entityId + "/";

		// copy original file first
		File originalPhoto = new File(pathPrefix.concat(photos[0]));
		Util.copyFile(uploadedFile, originalPhoto);
		uploadedFile.delete();

		Image.resizeAndSaveImage(originalPhoto, new File(pathPrefix.concat(photos[1])), 
				Util.getIntegerProperty("photos.large.size"), Util.getFileExtension(filename).substring(1));
		Image.resizeAndSaveImage(originalPhoto, new File(pathPrefix.concat(photos[2])), 
				Util.getIntegerProperty("photos.medium.size"), Util.getFileExtension(filename).substring(1));
		Image.resizeAndSaveImage(originalPhoto, new File(pathPrefix.concat(photos[3])), 
				Util.getIntegerProperty("photos.small.size"), Util.getFileExtension(filename).substring(1));
		
		EntityPhoto photo = saveEntityPhoto(operationType, photoId, entityId, entityType, 
				userId, entityId, photos[0], photos[1], photos[2], photos[3], alternateText, photoOrder);
		
		return photo;
			
	}
	
	
	/**
	 * Return entity photo of the given id
	 */
	public static EntityPhoto getEntityPhoto(String photoId) {
		return EntityPhoto.find.byId(Util.getString(photoId));
	}
	
	
	/**
	 * Returns photos for the given entity
	 */
	public static List<EntityPhoto> getEntityPhotos(int entityType, String entityId) {
		List<EntityPhoto> photoList = new ArrayList<EntityPhoto>();
		
		switch (entityType) {
		case EntityPhoto.ENTITY_EXPERIENCE:
			photoList = EntityPhoto.find.where().eq("entityExperience.experienceId", entityId).findList();
			break;
		case EntityPhoto.ENTITY_USER:
			photoList = EntityPhoto.find.where().eq("entityUser.userId", entityId).findList();
			break;
		case EntityPhoto.ENTITY_CITY:
			photoList = EntityPhoto.find.where().eq("entityCity.cityId", entityId).findList();
			break;
		}
		
		return photoList;
	}
	
	
	/**
	 * Returns the map of photos (photoId, originalphoto path) for an entity
	 */
	public static Map<String, String> getPhotoMap(String entityId, int entityType) {
		Map<String, String> photos = new HashMap<String, String>();
		List<EntityPhoto> photoList = getEntityPhotos(entityType, entityId);
		
		for (EntityPhoto photo : photoList)
			photos.put(photo.getPhotoId(), photo.getLargePhotoURL());

		return photos;
	}

}
