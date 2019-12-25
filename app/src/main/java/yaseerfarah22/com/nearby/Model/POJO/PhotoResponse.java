package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

public class PhotoResponse{

	@SerializedName("photos")
	private Photos photos;

	public void setPhotos(Photos photos){
		this.photos = photos;
	}

	public Photos getPhotos(){
		return photos;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"photos = '" + photos + '\'' + 
			"}";
		}
}