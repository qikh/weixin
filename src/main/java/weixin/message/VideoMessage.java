package weixin.message;

public class VideoMessage extends Message{
	private String mediaId;
	private String thumbMediaId;

    public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String format) {
        this.thumbMediaId = format;
    }

}
