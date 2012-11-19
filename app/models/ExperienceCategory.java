package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class ExperienceCategory extends Model {

	@Id
	@Required
	@Column(length=100)
	String categoryId;

	@Required
	@Column(length=100)
	String name;
	
	Long createTimestamp;
	
	public static Finder<String, ExperienceCategory> find = new Finder<String, ExperienceCategory>(String.class, ExperienceCategory.class);
	
	public ExperienceCategory() {}
	
	public ExperienceCategory(String categoryId, String name, Long createTimestamp) {
		this.categoryId = categoryId;
		this.name = name;
		this.createTimestamp = createTimestamp;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
}
