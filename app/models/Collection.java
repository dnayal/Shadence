package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Collection extends Model {

	@Id
	@Column(length=100)
	String collectionId;
	
	@Required
	@Column(length=100)
	String name;

	@Column(length=2000)
	String description;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="user_id")
	@Column(length=100)
	User user;
	
	@JsonIgnore
	Long createTimestamp;
	
	@JsonIgnore
	@ManyToMany(mappedBy="collections")
	public Set<Experience> experiences = new HashSet<Experience>();
	
	public static Finder<String, Collection> find = new Finder<String, Collection>(String.class, Collection.class);

	public Collection() {}
	
	public Collection(String collectionId, String name, String description, User user, Long createTimestamp) {
		this.collectionId = collectionId;
		this.name = name;
		this.description = description;
		this.user = user;
		this.createTimestamp = createTimestamp;
	}
	
	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Set<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(Set<Experience> experiences) {
		this.experiences = experiences;
	}

}
