package handlers;

import java.util.List;

import models.ProviderExperience;

public class ProviderExperienceHandler {
	
	
	public static void saveProviderExperience(ProviderExperience experience) {
		experience.save();
	}
	
	
	public static void updateProviderExperience(ProviderExperience experience) {
		experience.update();
	}
	
	
	public static void deleteProviderExperience(String experienceId) {
		ProviderExperience.find.ref(experienceId).delete();
	}
	
	
	public static List<ProviderExperience> getProviderExperiences() {
		return ProviderExperience.find.all();
	}

}
