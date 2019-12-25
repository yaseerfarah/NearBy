package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

public class ImageResponse{

	@SerializedName("meta")
	private Meta meta;

	@SerializedName("response")
	private PhotoResponse response;

	public void setMeta(Meta meta){
		this.meta = meta;
	}

	public Meta getMeta(){
		return meta;
	}

	public void setResponse(PhotoResponse response){
		this.response = response;
	}

	public PhotoResponse getResponse(){
		return response;
	}

	@Override
 	public String toString(){
		return 
			"ImageResponse{" + 
			"meta = '" + meta + '\'' + 
			",response = '" + response + '\'' + 
			"}";
		}
}