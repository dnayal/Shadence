package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.db.ebean.Model;

@Entity
public class Experience extends Model {

	@Id
	@Column(length=100)
	String experienceId;

	@ManyToOne
	@JoinColumn(name="venue_id")
	@Column(length=100)
	Venue venue;

	@ManyToOne
	@JoinColumn(name="user_id")
	@Column(length=100)
	User user;

	@ManyToOne
	@JoinColumn(name="category_id")
	@Column(length=100)
	ExperienceCategory category;
	
	@Column(length=100)
	String name;

	@Column(length=50)
	String email;

	@Column(length=20)
	String phone;

	@Column(length=5000)
	String description;

	@Column(length=100)
	String priceDescription;

	/** 
	 * 0 - Free
	 * 1 - Cheap
	 * 2 - Normal
	 * 3 - Expensive 
	 **/
	Integer priceRating;

	/** Number, stored in hours **/
	Integer duration;

	@Column(length=100)
	String scheduleDescription;

	@Column(length=200)
	String originalSource;
	
	@Column(length=100)
	String tags;
	
	Date startDate;
	
	Date endDate;
	
	Long createTimestamp;
	
	@OneToMany(mappedBy="entityExperience")
	@OrderBy("photoOrder asc, createTimestamp desc")
	List<EntityPhoto> experiencePhotos;
	
	public static Finder<String, Experience> find = new Finder<String, Experience>(String.class, Experience.class);

	public Experience() {}

	public Experience(String experienceId, String name, String email, String phone, 
			String description, String priceDescription, Integer priceRating, Integer duration, 
			String scheduleDescription,	String originalSource, String tags, Date startDate,
			Date endDate, Long createTimestamp) {
		this.experienceId = experienceId;
		this.name = name;
		this.email = email;
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriceDescription() {
		return priceDescription;
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public Integer getPriceRating() {
		return priceRating;
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
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
	}

	public String getOriginalSource() {
		return originalSource;
	}

	public void setOriginalSource(String originalSource) {
		this.originalSource = originalSource;
	}

	public String getTags() {
		return tags;
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
	
}
