package weixin.message;

public class LinkMessage extends Message {
    private String title;
    private String description;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String mediaId) {
        this.title = mediaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String format) {
        this.description = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
