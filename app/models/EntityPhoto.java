package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class EntityPhoto extends Model {

	@Column(length=100)
	String originalPhotoId;

	@ManyToOne
	@JoinColumn(name="entity_city_id")
	@Column(length=100)
	City entityCity; 
	
	@ManyToOne
	@JoinColumn(name="entity_experience_id")
	@Column(length=100)
	Experience entityExperience; 

	@ManyToOne
	@JoinColumn(name="entity_user_id")
	@Column(length=100)
	User entityUser;

	@ManyToOne
	@JoinColumn(name="user_id")
	@Column(length=100)
	User user;

	@Column(length=200)
	String location;

	@Column(length=100)
	String name;

	@Column(length=100)
	String alternateText;

	// The order in which photos will be arranged.
	// If this is not given or is equal for more than one photo
	// createTimestamp is another field to fallback on.
	@Column(length=3)
	String photoOrder;

	Long createTimestamp;
	
	public static Finder<String, EntityPhoto> find = new Finder<String, EntityPhoto>(String.class, EntityPhoto.class);

	public EntityPhoto() {}
	
	public EntityPhoto(String originalPhotoId, String location, String name, String alternateText, 
												String photoOrder, Long createTimestamp) {
		this.originalPhotoId = originalPhotoId;
		this.location = location;
		this.name = name;
		this.alternateText = alternateText;
		this.photoOrder = photoOrder;
		this.createTimestamp = createTimestamp;
	}

	public String getOriginalPhotoId() {
		return originalPhotoId;
	}

	public void setOriginalPhotoId(String originalPhotoId) {
		this.originalPhotoId = originalPhotoId;
	}

	public City getEntityCity() {
		return entityCity;
	}

	public void setEntityCity(City entityCity) {
		this.entityCity = entityCity;
	}

	public Experience getEntityExperience() {
		return entityExperience;
	}

	public void setEntityExperience(Experience entityExperience) {
		this.entityExperience = entityExperience;
	}

	public User getEntityUser() {
		return entityUser;
	}

	public void setEntityUser(User entityUser) {
		this.entityUser = entityUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlternateText() {
		return alternateText;
	}

	public void setAlternateText(String alternateText) {
		this.alternateText = alternateText;
	}

	public String getPhotoOrder() {
		return photoOrder;
	}

	public void setPhotoOrder(String photoOrder) {
		this.photoOrder = photoOrder;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
