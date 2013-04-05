package handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import utils.Util;

import models.Collection;
import models.Experience;
import models.FeaturedEntity;


public class FeaturedEntityHandler {

	public static final String EXPERIENCE_ENTITY = "experience";
	public static final String COLLECTION_ENTITY = "collection";

	
	public static FeaturedEntity getFeaturedExperience() {
		List<FeaturedEntity> list = FeaturedEntity.find.where().eq("entityType",EXPERIENCE_ENTITY).findList();
		Random random = new Random();
		Integer randomIndex = random.nextInt(list.size());
		return list.get(randomIndex);
	}
	

	public static FeaturedEntity getFeaturedCollection() {
		List<FeaturedEntity> list = FeaturedEntity.find.where().eq("entityType",COLLECTION_ENTITY).findList();
		Random random = new Random();
		Integer randomIndex = random.nextInt(list.size());
		return list.get(randomIndex);
	}
	
	
	public static FeaturedEntity getFeaturedExperience(String categoryId) {
		// NOTE - specificInformation field of FeaturedEntity model contains 
		// categoryId for experiences and userId for collections
		List<FeaturedEntity> list = FeaturedEntity.find.where()
				.eq("entityType",EXPERIENCE_ENTITY).eq("specificInformation", categoryId).findList();
		Random random = new Random();
		Integer randomIndex = random.nextInt(list.size());
		return list.get(randomIndex);
	}
	
	
	public static void saveFeaturedEntity(FeaturedEntity entity) {
		entity.save();
	}
	
	
	public static void removeFeaturedEntity(String entityId) {
		FeaturedEntity.find.ref(entityId).delete();
	}
	

	public static List<FeaturedEntity> getAllFeaturedEntities() {
		return FeaturedEntity.find.all();
	}
	

	public static List<FeaturedEntity> getFeaturedEntitiesForBanner(String categoryId) {
		boolean isMainBanner = true;
		if (!categoryId.equalsIgnoreCase(Util.getStringProperty("category.default")))
			isMainBanner = false;
		
		int BANNER_SIZE = 3;
		if (!isMainBanner)
			BANNER_SIZE = 1;
		
		int nextEntityType = 0; // Default Entity Type is Experience
		FeaturedEntity entity = null;
		boolean isUnique = false;
		Random random = new Random();
		List<FeaturedEntity> list = new ArrayList<FeaturedEntity>();
		
		for(int i=0;i<BANNER_SIZE;i++) {
			if(isMainBanner)
				nextEntityType = random.nextInt(2);
			switch(nextEntityType) {
				case 1: // Collection
					isUnique = false;
					while(!isUnique) {
						entity = getFeaturedCollection();
						if(!list.contains(entity)) {
							list.add(entity);
							isUnique = true;
						}
					}
					break;
				
				default: // Experience
					isUnique = false;
					while(!isUnique) {
						if(isMainBanner) {
							entity = getFeaturedExperience();
							if(entity.getFeatureDescription().equalsIgnoreCase(""))
								entity.setFeatureDescription("Featured Experience");
						} else {
							entity = getFeaturedExperience(categoryId);
							if(entity.getFeatureDescription().equalsIgnoreCase(""))
								entity.setFeatureDescription("Featured " + ExperienceCategoryHandler.getCategoryName(categoryId) + " Experience");
						}
						if(!list.contains(entity)) {
							list.add(entity);
							isUnique = true;
						}
					}
					break;
			}
		}
		
		return list;
	}

}
