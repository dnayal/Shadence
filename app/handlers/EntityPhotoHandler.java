package handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Configuration;
import play.Logger;
import play.Play;

import utils.Util;

import models.EntityPhoto;

public class EntityPhotoHandler {

	
	/**
	 * Saves the entity photo
	 */
	public static void saveEntityPhotoinDatabase(EntityPhoto photo) {
		photo.save();
	}
	

	/**
	 * Updates the entity photo
	 */
	public static void updateEntityPhotoinDatabase(EntityPhoto photo) {
		photo.update();
	}
	

	/**
	 * Saves the entity photo
	 */
	public static EntityPhoto saveEntityPhotoinDatabase(String photoId, String entityId, int entityType,
			String userId, String location, String originalPhoto, String largePhoto, 
			String mediumPhoto, String smallPhoto, String alternateText, String photoOrder) {
		
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
		
		saveEntityPhotoinDatabase(photo);
		
		return photo;
	}
	
	
	/**
	 * Updates the entity photo
	 */
	public static EntityPhoto updateEntityPhotoinDatabase(String photoId, String entityId, int entityType,
			String userId, String location, String originalPhoto, String largePhoto, 
			String mediumPhoto, String smallPhoto, String alternateText, String photoOrder) {
		
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
		
		updateEntityPhotoinDatabase(photo);
		
		return photo;
	}

	
	public static EntityPhoto saveEntityPhoto(String filename, File uploadedFile, String entityId, 
			int entityType, String userId, String alternateText, String photoOrder) {
		
		Configuration config = Play.application().configuration();
		String photoId = Util.getUniqueId();
		
		String fileExtension = Util.getFileExtension(filename);
		String photos[] = new String[4];
		photos[0] = photoId.concat(config.getString("photos.original.suffix")).concat(fileExtension);
		photos[1] = photoId.concat(config.getString("photos.large.suffix")).concat(fileExtension);
		photos[2] = photoId.concat(config.getString("photos.medium.suffix")).concat(fileExtension);
		photos[3] = photoId.concat(config.getString("photos.small.suffix")).concat(fileExtension);
		
		try {
			FileChannel source = new FileInputStream(uploadedFile).getChannel();
	
			for(String photoName : photos) {
				File photoFile = new File(config.getString("photos.upload.path") + "/" + entityId 
						+ "/" + photoName);
				photoFile.getParentFile().mkdirs();
				
				// this step has to be replaced by actual image manipulation
				FileChannel destination = new FileOutputStream(photoFile).getChannel();
				source.transferTo(0, source.size(), destination);
				destination.close();
			}
			source.close();
			uploadedFile.delete();
		} catch (Exception exception) {
			Logger.error("Error while copying uploaded file [" + uploadedFile.getAbsolutePath() + "]", exception);
		}

		EntityPhoto photo = saveEntityPhotoinDatabase(photoId, entityId, entityType, userId, entityId, 
				photos[0], photos[1], photos[2], photos[3], alternateText, photoOrder);
		
		return photo;
	}
	
	
	public static EntityPhoto updateEntityPhoto(String filename, File uploadedFile, String photoId, 
			String entityId, int entityType, String userId, String alternateText, String photoOrder) {
		
		Configuration config = Play.application().configuration();
		
		String fileExtension = Util.getFileExtension(filename);
		String photos[] = new String[4];
		photos[0] = photoId.concat(config.getString("photos.original.suffix")).concat(fileExtension);
		photos[1] = photoId.concat(config.getString("photos.large.suffix")).concat(fileExtension);
		photos[2] = photoId.concat(config.getString("photos.medium.suffix")).concat(fileExtension);
		photos[3] = photoId.concat(config.getString("photos.small.suffix")).concat(fileExtension);
		
		try {
			FileChannel source = new FileInputStream(uploadedFile).getChannel();
	
			for(String photoName : photos) {
				File photoFile = new File(config.getString("photos.upload.path") + "/" + entityId 
						+ "/" + photoName);
				photoFile.getParentFile().mkdirs();
				
				// this step has to be replaced by actual image manipulation
				FileChannel destination = new FileOutputStream(photoFile).getChannel();
				source.transferTo(0, source.size(), destination);
				destination.close();
			}
			source.close();
			uploadedFile.delete();
		} catch (Exception exception) {
			Logger.error("Error while copying uploaded file [" + uploadedFile.getAbsolutePath() + "]", exception);
		}

		EntityPhoto photo = updateEntityPhotoinDatabase(photoId, entityId, entityType, userId, entityId, 
				photos[0], photos[1], photos[2], photos[3], alternateText, photoOrder);
		
		return photo;
	}
	
	
	/**
	 * Return entity photo of the given id
	 */
	public static EntityPhoto getEntityPhoto(String photoId) {
		return EntityPhoto.find.byId(Util.getString(photoId));
	}
	
	
	/**
	 * Get all entityphotos of a experience
	 */
	public static List<EntityPhoto> getExperiencePhotos(String experienceId) {
		List<EntityPhoto> photoList = EntityPhoto.find.where()
				.eq("entityExperience.experienceId", experienceId)
				.findList();
		return photoList;
	}
	
	
	/**
	 * Get all entityphotos of a city
	 */
	public static List<EntityPhoto> getCityPhotos(String cityId) {
		List<EntityPhoto> photoList = EntityPhoto.find.where()
				.eq("entityCity.cityId", cityId)
				.findList();
		return photoList;
	}
	
	
	/**
	 * Get all entityphotos of a user
	 */
	public static List<EntityPhoto> getUserPhotos(String userId) {
		List<EntityPhoto> photoList = EntityPhoto.find.where()
				.eq("entityUser.userId", userId)
				.findList();
		return photoList;
	}
	
	
	/**
	 * returns the map of photos (photoId, originalphoto path) for an entity
	 */
	public static Map<String, String> getPhotoMap(String entityId, int entityType) {
		Map<String, String> photos = new HashMap<String, String>();
		List<EntityPhoto> photoList = new ArrayList<EntityPhoto>();
		
		switch(entityType) {
			case EntityPhoto.ENTITY_EXPERIENCE:
				photoList = getExperiencePhotos(entityId);
				break;
			case EntityPhoto.ENTITY_CITY:
				photoList = getCityPhotos(entityId);
				break;
			case EntityPhoto.ENTITY_USER:
				photoList = getUserPhotos(entityId);
				break;
		}
		
		for (EntityPhoto photo : photoList)
			photos.put(photo.getPhotoId(), photo.getLargePhotoURL());

		return photos;
	}

}
