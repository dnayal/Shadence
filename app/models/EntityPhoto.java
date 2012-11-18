package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.Play;
import play.db.ebean.Model;
import utils.Util;

@Entity
public class EntityPhoto extends Model {

	@Id
	@Column(length=100)
	String photoId;

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
	String originalPhoto;

	@Column(length=100)
	String largePhoto;

	@Column(length=100)
	String mediumPhoto;

	@Column(length=100)
	String smallPhoto;

	@Column(length=100)
	String alternateText;

	// The order in which photos will be arranged.
	// If this is not given or is equal for more than one photo
	// createTimestamp is another field to fallback on.
	@Column(length=3)
	String photoOrder;

	Long createTimestamp;
	
	public static Finder<String, EntityPhoto> find = new Finder<String, EntityPhoto>(String.class, EntityPhoto.class);
	public static final int ENTITY_EXPERIENCE = 1;
	public static final int ENTITY_CITY = 2;
	public static final int ENTITY_USER = 3;
	
	public EntityPhoto() {}

	public EntityPhoto(String photoId, String location, String originalPhoto, 
			String largePhoto, String mediumPhoto, String smallPhoto, String alternateText, 
			String photoOrder, Long createTimestamp) {
		this.photoId = photoId;
		this.location = location;
		this.originalPhoto = originalPhoto;
		this.largePhoto = largePhoto;
		this.mediumPhoto = mediumPhoto;
		this.smallPhoto = smallPhoto;
		this.alternateText = alternateText;
		this.photoOrder = photoOrder;
		this.createTimestamp = createTimestamp;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
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

	public String getOriginalPhoto() {
		return originalPhoto;
	}

	public void setOriginalPhoto(String originalPhoto) {
		this.originalPhoto = originalPhoto;
	}

	public String getLargePhoto() {
		return largePhoto;
	}

	public void setLargePhoto(String largePhoto) {
		this.largePhoto = largePhoto;
	}

	public String getMediumPhoto() {
		return mediumPhoto;
	}

	public void setMediumPhoto(String mediumPhoto) {
		this.mediumPhoto = mediumPhoto;
	}

	public String getSmallPhoto() {
		return smallPhoto;
	}

	public void setSmallPhoto(String smallPhoto) {
		this.smallPhoto = smallPhoto;
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
	
	public String getOriginalPhotoURL() {
		return "/".concat(Util.getProperty("photos.web.path"))
				.concat("/").concat(location)
				.concat("/").concat(originalPhoto);
	}

	public String getLargePhotoURL() {
		return "/".concat(Util.getProperty("photos.web.path"))
				.concat("/").concat(location)
				.concat("/").concat(largePhoto);
	}

	public String getMediumPhotoURL() {
		return "/".concat(Util.getProperty("photos.web.path"))
				.concat("/").concat(location)
				.concat("/").concat(mediumPhoto);
	}

	public String getSmallPhotoURL() {
		return "/".concat(Util.getProperty("photos.web.path"))
				.concat("/").concat(location)
				.concat("/").concat(smallPhoto);
	}

}
