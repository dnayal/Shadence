package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class ExperienceCategory extends Model {

	@Id
	String categoryId;
	String category;
	long createTimestamp;
	
	public static Finder<String, ExperienceCategory> find = new Finder<String, ExperienceCategory>(String.class, ExperienceCategory.class);
	
	public ExperienceCategory() {}
	
	public ExperienceCategory(String categoryId, String category, long createTimestamp) {
		this.categoryId = categoryId;
		this.category = category;
		this.createTimestamp = createTimestamp;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
