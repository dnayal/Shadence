package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class FeaturedEntity extends Model {

	@Id
	@Column(length=100)
	String entityId;
	
	@Required
	@Column(length=20)
	String entityType;

	@Column(length=100)
	String featureDescription;

	/* This field will have 
	 * CategoryId for Experiences & 
	 * UserId for Collection
	 */
	@Column(length=200)
	String specificInformation;

	Long createTimestamp;
	
	public static Finder<String, FeaturedEntity> find = new Finder<String, FeaturedEntity>(String.class, FeaturedEntity.class);

	public FeaturedEntity() {}
	
	public FeaturedEntity(String entityId, String entityType, String featureDescription, 
										String specificInformation, Long createTimestamp) {
		this.entityId = entityId;
		this.entityType = entityType;
		this.featureDescription = featureDescription;
		this.specificInformation = specificInformation;
		this.createTimestamp = createTimestamp;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	public String getSpecificInformation() {
		return specificInformation;
	}

	public void setSpecificInformation(String specificInformation) {
		this.specificInformation = specificInformation;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
}
