package models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import utils.Util;

@Entity
public class Experience extends Model {

	@Id
	@Column(length=100)
	String experienceId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="venue_id")
	@Column(length=100)
	Venue venue;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="user_id")
	@Column(length=100)
	User user;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="category_id")
	@Column(length=100)
	ExperienceCategory category;
	
	@Required
	@Column(length=100)
	String name;

	@JsonIgnore
	@Email
	@Column(length=100)
	String email;

	@JsonIgnore
	@Column(length=100)
	String twitter;

	@JsonIgnore
	@Column(length=20)
	String phone;

	@JsonIgnore
	@Required
	@Column(length=5000)
	String description;

	@JsonIgnore
	@Column(length=500)
	String priceDescription;

	/** 
	 * 0 - Free
	 * 1 - Inexpensive
	 * 2 - Moderate
	 * 3 - Expensive
	 * 4 - High End
	 **/
	@JsonIgnore
	Integer priceRating;

	/** Number, stored in hours **/
	@JsonIgnore
	Integer duration;

	@JsonIgnore
	@Column(length=500)
	String scheduleDescription;

	@JsonIgnore
	@Column(length=500)
	String originalSource;
	
	@JsonIgnore
	@Column(length=200)
	String tags;
	
	@JsonIgnore
	Date startDate;
	
	@JsonIgnore
	Date endDate;
	
	/*
	 * It is important to set a default value to 
	 * this field so that ExperienceHandler.getExperiences()
	 * method works properly (because of ebean ne() expression
	 * which does not work for null values
	 */
	@JsonIgnore
	Boolean hidden = false;
	
	@JsonIgnore
	Long createTimestamp;
	
	@OneToMany(mappedBy="entityExperience")
	@OrderBy("photoOrder asc, createTimestamp desc")
	List<EntityPhoto> experiencePhotos;

	@JsonIgnore
	@ManyToMany
	Set<Collection> collections = new HashSet<Collection>();

	public static Finder<String, Experience> find = new Finder<String, Experience>(String.class, Experience.class);

	public Experience() {}

	public Experience(String experienceId, String name, String email, String twitter, String phone, 
			String description, String priceDescription, Integer priceRating, Integer duration, 
			String scheduleDescription,	String originalSource, String tags, Date startDate,
			Date endDate, Boolean hidden, Long createTimestamp) {
		this.experienceId = experienceId;
		this.name = name;
		this.email = email;
		this.twitter = twitter;
		this.phone = phone;
		this.description = description;
		this.priceDescription = priceDescription;
		this.priceRating = priceRating;
		this.duration = duration;
		this.scheduleDescription = scheduleDescription;
		this.originalSource = originalSource;
		this.tags = tags;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hidden = hidden;
		this.createTimestamp = createTimestamp;
	}

	public String getExperienceId() {
		return experienceId;
	}

	public void setExperienceId(String experienceId) {
		this.experienceId = experienceId;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ExperienceCategory getCategory() {
		return category;
	}

	public void setCategory(ExperienceCategory category) {
		this.category = category;
	}

	public String getName() {
		return Util.getString(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return Util.getString(email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTwitter() {
		return Util.getString(twitter);
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getPhone() {
		return Util.getString(phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return Util.getString(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriceDescription() {
		return Util.getString(priceDescription);
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public Integer getPriceRating() {
		return priceRating;
	}

	/**
	 * Returns string equivalent of price rating
	 */
	@JsonIgnore
	public String getPriceRatingDescription() {
		switch(this.priceRating) {
			case 0: return "Free";
			case 1: return "Inexpensive";
			case 2: return "Moderate";
			case 3: return "Expensive";
			case 4: return "High End";
			default: return "High End";
		}
	}

	public void setPriceRating(Integer priceRating) {
		this.priceRating = priceRating;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getScheduleDescription() {
		return Util.getString(scheduleDescription);
	}

	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
	}

	public String getOriginalSource() {
		return Util.getString(originalSource);
	}

	public void setOriginalSource(String originalSource) {
		this.originalSource = originalSource;
	}

	public String getTags() {
		return Util.getString(tags);
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public List<EntityPhoto> getExperiencePhotos() {
		return experiencePhotos;
	}

	public void setExperiencePhotos(List<EntityPhoto> experiencePhotos) {
		this.experiencePhotos = experiencePhotos;
	}

	public Set<Collection> getCollections() {
		return collections;
	}

	public void setCollections(Set<Collection> collections) {
		this.collections = collections;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
}
