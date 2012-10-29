package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.db.ebean.Model;

@Entity
public class User extends Model {
	
	@Id
	@Column(length=100)
	String userId;

	@Column(length=100)
	String name;

	@Column(length=100)
	String email;

	@Column(length=100)
	String password;

	@Column(length=5)
	String gender;

	Long createTimestamp;
	
	@OneToMany(mappedBy="entityUser")
	@OrderBy("photoOrder asc, createTimestamp desc")
	List<EntityPhoto> profilePhotos;

	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

	public User() {}
	
	public User(String userId, String name, String email, String password, 
								String gender, Long createTimestamp) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
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
	
}
