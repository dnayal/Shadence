package handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import play.Logger;
import utils.Server;
import utils.Util;

import models.Collection;
import models.Experience;
import models.User;

public class CollectionHandler {

	
	public static void saveCollection(Collection collection) {
		collection.save();
	}
	
	
	public static Collection saveCollection(String collectionId, String name, String description, User user, Long createTimestamp) {
		Collection collection = new Collection(collectionId, name, description, user, createTimestamp);
		saveCollection(collection);
		return collection;
	}

	
	public static void updateCollection(Collection collection) {
		collection.update();
	}
	
	
	public static void deleteCollection(String collectionId) {
		Collection collection = getCollection(collectionId);
		List<Experience> experiences = Util.convertSetToList(collection.getExperiences());
		for(Experience experience : experiences)
			collection.getExperiences().remove(experience);
		collection.saveManyToManyAssociations("experiences");
		
		Collection.find.ref(collectionId).delete();
	}
	
	
	public static Collection getCollection(String collectionId) {
		return Collection.find.byId(collectionId);
	}
	
	
	public static List<Collection> getCollectionsOfUser(String userId) {
		return Collection.find.where().eq("user.userId", userId).findList();
	}
	
	
	public static List<Experience> getExperiencesOfCollection(String collectionId) {
		Collection collection = getCollection(collectionId);
		
		List<Experience> experiences = new ArrayList<Experience>(collection.getExperiences());
		
		return experiences;
	}
	
	
	public static void addExperienceToCollection(String collectionId, String experienceId) {
		Experience experience = ExperienceHandler.getExperience(experienceId);
		Collection collection = getCollection(collectionId);
		
		if (experience==null || collection==null) {
			Logger.error("Unable to add experience [" + experienceId + "] to collection [" + collectionId + "]", new RuntimeException());
			return;
		}
		
		Set<Experience> set = new HashSet<Experience>();
		set.add(experience);
		
		collection.setExperiences(set);
		collection.saveManyToManyAssociations("experiences");
	}
	
	
	public static void removeExperienceFromCollection(String collectionId, String experienceId) {
		Experience experience = ExperienceHandler.getExperience(experienceId);
		Collection collection = getCollection(collectionId);

		if (experience==null || collection==null) {
			Logger.error("Unable to remove experience [" + experienceId + "] from collection [" + collectionId + "]", new RuntimeException());
			return;
		}
		
		collection.getExperiences().remove(experience);
		collection.saveManyToManyAssociations("experiences");
	}
	
	
	public static boolean isExperienceCollectedByCurrentUser(String experienceId) {
		User currentUser = Server.getCurrentSessionUser();
		
		if (currentUser == null)
			return false;
		
		List<Collection> collection = Collection.find.where()
							.eq("user.userId", currentUser.getUserId())
							.eq("experiences.experienceId", experienceId)
							.findList();
		if (collection.size() >= 1)
			return true;
		else
			return false;
	}
	
	
	public static List<Collection> getCollectionsWithExperience(String experienceId) {
		List<Collection> collections = Collection.find.where()
				.eq("experiences.experienceId", experienceId)
				.findList();
		return collections;
	}


	public static List<User> getUsersWhoHaveCollectedExperience(String experienceId) {
		List<Collection> collections = Collection.find.where()
				.eq("experiences.experienceId", experienceId)
				.findList();
		
		Object[] userIds = new String[collections.size()];
		
		for(int i=0;i< userIds.length;i++)
			userIds[i] = collections.get(i).getUser().getUserId();
		
		List<User> user = User.find.where()
				.in("userId", userIds).findList();
		
		return user;
	}
	
	
	public static void createInitialCollectionsForUser(User user) {
		String collectionName[] = new String[4];
		collectionName[0] = "Things I want to do";
		collectionName[1] = "Things I have done";
		collectionName[2] = "Plans for the weekend";
		collectionName[3] = "Ideas for going out with friends";
		
		for (String name : collectionName) {
			try {
				saveCollection(Util.getUniqueId(), name, "", user, System.currentTimeMillis());
			} catch(Exception exception) {
				Logger.info("Error while creating initial collection [" + name + "] for user [" + user.getUserId() + "]", exception);
			}
		}

		
	}
	
}
