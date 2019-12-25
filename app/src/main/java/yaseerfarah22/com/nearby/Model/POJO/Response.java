package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{

	@SerializedName("suggestedFilters")
	SuggestedFilters SuggestedFiltersObject;
	@SerializedName("suggestedRadius")
	private float suggestedRadius;
	@SerializedName("headerLocation")
	private String headerLocation;
	@SerializedName("headerFullLocation")
	private String headerFullLocation;
	@SerializedName("headerLocationGranularity")
	private String headerLocationGranularity;
	@SerializedName("totalResults")
	private float totalResults;
	@SerializedName("suggestedBounds")
	SuggestedBounds SuggestedBoundsObject;
	@SerializedName("groups")
	private List<GroupsItem> groups;



	public SuggestedFilters getSuggestedFiltersObject() {
		return SuggestedFiltersObject;
	}

	public void setSuggestedFiltersObject(SuggestedFilters suggestedFiltersObject) {
		SuggestedFiltersObject = suggestedFiltersObject;
	}

	public float getSuggestedRadius() {
		return suggestedRadius;
	}

	public void setSuggestedRadius(float suggestedRadius) {
		this.suggestedRadius = suggestedRadius;
	}

	public String getHeaderLocation() {
		return headerLocation;
	}

	public void setHeaderLocation(String headerLocation) {
		this.headerLocation = headerLocation;
	}

	public String getHeaderFullLocation() {
		return headerFullLocation;
	}

	public void setHeaderFullLocation(String headerFullLocation) {
		this.headerFullLocation = headerFullLocation;
	}

	public String getHeaderLocationGranularity() {
		return headerLocationGranularity;
	}

	public void setHeaderLocationGranularity(String headerLocationGranularity) {
		this.headerLocationGranularity = headerLocationGranularity;
	}

	public float getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(float totalResults) {
		this.totalResults = totalResults;
	}

	public SuggestedBounds getSuggestedBoundsObject() {
		return SuggestedBoundsObject;
	}

	public void setSuggestedBoundsObject(SuggestedBounds suggestedBoundsObject) {
		SuggestedBoundsObject = suggestedBoundsObject;
	}



	public void setGroups(List<GroupsItem> groups){
		this.groups = groups;
	}

	public List<GroupsItem> getGroups(){
		return groups;
	}


}