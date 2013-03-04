package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class ProviderExperience extends Model {
	
	@Id
	@Column(length=100)
	String id;

	@Required
	@Column(length=100)
	String name;

	@Email
	@Required
	@Column(length=100)
	String email;

	@Required
	@Column(length=200)
	String address;

	@Column(length=500)
	String url;

	@Required
	@Column(length=2000)
	String description;

	Long createTimestamp;

	public static Finder<String, ProviderExperience> find = new Finder<String, ProviderExperience>(String.class, ProviderExperience.class);

	public ProviderExperience() {}
	
	public ProviderExperience(String id, String name, String email, String url, String address,  
								String description, Long createTimestamp) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.url = url;
		this.address = address;
		this.description = description;
		this.createTimestamp = createTimestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
}
