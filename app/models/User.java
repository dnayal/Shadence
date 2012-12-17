package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import utils.Server;

@Entity
public class User extends Model {
	
	@Id
	@Column(length=100)
	String userId;

	@Column(length=100)
	String name;

	@Required
	@Email
	@Column(length=100, unique=true)
	String email;

	@Column(length=100)
	String password;

	@Column(length=5)
	String gender;

	Date birthdate;

	@Column(length=100)
	String city;

	@Column(length=100)
	String country;

	@Column(length=500)
	String roles;

	Long createTimestamp;
	
	@OneToMany(mappedBy="entityUser")
	@OrderBy("photoOrder asc, createTimestamp desc")
	List<EntityPhoto> profilePhotos;

	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

	public User() {}
	
	public User(String userId, String name, String email, String password, String gender, 
						Date birthdate, String city, String country, String roles, Long createTimestamp) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.birthdate = birthdate;
		this.city = city;
		this.country = country;
		this.roles = roles;
		this.createTimestamp = createTimestamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public List<EntityPhoto> getProfilePhotos() {
		return profilePhotos;
	}

	public void setProfilePhotos(List<EntityPhoto> profilePhotos) {
		this.profilePhotos = profilePhotos;
	}
	
	public Boolean hasProfilePhotos() {
		if (profilePhotos == null)
			return false;
		else 
			if (profilePhotos.size() < 1)
				return false;
			else
				return true;
	}
	
	/**
	 * Returns main profile photo
	 */
	public String getMainProfilePhoto() {
		if(profilePhotos == null || profilePhotos.size() < 1) {
			return Server.getAssetAt("shadence/images/_default_user_.png");
		} else {
			return profilePhotos.get(0).getMediumPhotoURL();
		}
	}
	
	
	/**
	 * Returns the birthdate in a user friendly format
	 */
	public String getFormatedBirthdate() {
		if (birthdate == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMM, yyyy");
		return dateFormat.format(birthdate);
		
	}
	
}
