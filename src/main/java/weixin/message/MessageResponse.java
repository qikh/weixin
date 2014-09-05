package weixin.message;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageResponse {
    private String toUserName;
    private String fromUserName;
    private Integer createTime;
    private String msgType;
    private String content;
    private String mediaId;
    private String thumbMediaId;
    private String title;
    private String description;
    private String musicUrl;
    private String hqMusicUrl;
    private List<Article> articles;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void reply(String string) {
        this.msgType = "text";
        this.content = string;
    }

    public void replyImage(String mediaId) {
        this.msgType = "image";
        this.mediaId = mediaId;
    }

    public void replyVoice(String mediaId) {
        this.msgType = "voice";
        this.mediaId = mediaId;
    }

    public void replyVideo(String mediaId, String title, String description) {
        this.msgType = "video";
        this.mediaId = mediaId;
        this.title = title;
        this.description = description;
    }

    public void replyMusic(String title, String description, String musicUrl, String hqMusicUrl) {
        this.msgType = "music";
        this.title = title;
        this.description = description;
        this.musicUrl = musicUrl;
        this.hqMusicUrl = hqMusicUrl;
    }

    public void replyArticles(List<Article> articles) {
        this.msgType = "news";
        this.articles = articles;
    }

    public String toXml() {
        if (this.getToUserName() == null || this.getFromUserName() == null
                || this.getCreateTime() == null || this.getMsgType() == null) {
            return null;
        }

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("xml");
        doc.appendChild(rootElement);

        Element el1 = doc.createElement("ToUserName");
        el1.appendChild(doc.createCDATASection(this.getToUserName()));
        rootElement.appendChild(el1);

        Element el2 = doc.createElement("FromUserName");
        el2.appendChild(doc.createCDATASection(this.getFromUserName()));
        rootElement.appendChild(el2);

        Element el3 = doc.createElement("CreateTime");
        el3.setTextContent(this.getCreateTime().toString());
        rootElement.appendChild(el3);

        Element el4 = doc.createElement("MsgType");
        el4.appendChild(doc.createCDATASection(this.getMsgType()));
        rootElement.appendChild(el4);

        switch (msgType) {
        case "text": {
            if (content != null) {
                Element contentElement = doc.createElement("Content");
                contentElement.appendChild(doc.createCDATASection(content));
                rootElement.appendChild(contentElement);
            } else {
                return null;
            }
            break;
        }
        case "image": {
            if (mediaId != null) {
                Element imageElement = doc.createElement("Image");
                rootElement.appendChild(imageElement);

                Element mediaIdElement = doc.createElement("MediaId");
                mediaIdElement.appendChild(doc.createCDATASection(mediaId));
                imageElement.appendChild(mediaIdElement);
            } else {
                return null;
            }
            break;
        }
        case "voice": {
            if (mediaId != null) {
                Element voiceElement = doc.createElement("Voice");
                rootElement.appendChild(voiceElement);

                Element mediaIdElement = doc.createElement("MediaId");
                mediaIdElement.appendChild(doc.createCDATASection(mediaId));
                voiceElement.appendChild(mediaIdElement);
            } else {
                return null;
            }
            break;
        }
        case "video": {
            if (mediaId != null) {
                Element videoElement = doc.createElement("Video");
                rootElement.appendChild(videoElement);

                Element mediaIdElement = doc.createElement("MediaId");
                mediaIdElement.appendChild(doc.createCDATASection(mediaId));
                videoElement.appendChild(mediaIdElement);

                if (title != null) {
                    Element titleElement = doc.createElement("Title");
                    titleElement.appendChild(doc.createCDATASection(title));
                    videoElement.appendChild(titleElement);
                }

                if (description != null) {
                    Element descriptionElement = doc.createElement("Description");
                    descriptionElement.appendChild(doc.createCDATASection(description));
                    videoElement.appendChild(descriptionElement);
                }
            } else {
                return null;
            }
            break;
        }
        case "music": {
            if (thumbMediaId != null) {
                Element musicElement = doc.createElement("Music");
                rootElement.appendChild(musicElement);

                if (title != null) {
                    Element titleElement = doc.createElement("Title");
                    titleElement.appendChild(doc.createCDATASection(title));
                    musicElement.appendChild(titleElement);
                }

                if (description != null) {
                    Element descriptionElement = doc.createElement("Description");
                    descriptionElement.appendChild(doc.createCDATASection(description));
                    musicElement.appendChild(descriptionElement);
                }

                if (musicUrl != null) {
                    Element musicUrlElement = doc.createElement("MusicURL");
                    musicUrlElement.appendChild(doc.createCDATASection(musicUrl));
                    musicElement.appendChild(musicUrlElement);
                }

                if (hqMusicUrl != null) {
                    Element hqMusicUrlElement = doc.createElement("HQMusicUrl");
                    hqMusicUrlElement.appendChild(doc.createCDATASection(hqMusicUrl));
                    musicElement.appendChild(hqMusicUrlElement);
                }

                Element thumbMediaIdElement = doc.createElement("ThumbMediaId");
                thumbMediaIdElement.appendChild(doc.createCDATASection(thumbMediaId));
                musicElement.appendChild(thumbMediaIdElement);

            } else {
                return null;
            }
            break;
        }
        case "news": {
            if (articles != null) {
                Element articleCountElement = doc.createElement("ArticleCount");
                articleCountElement.setTextContent(Integer.valueOf(articles.size()).toString());
                rootElement.appendChild(articleCountElement);

                Element articlesElement = doc.createElement("Articles");
                rootElement.appendChild(articlesElement);

                for (Article article : articles) {
                    Element itemElement = doc.createElement("item");
                    articlesElement.appendChild(itemElement);

                    if (article.getTitle() != null) {
                        Element titleElement = doc.createElement("Title");
                        titleElement.appendChild(doc.createCDATASection(article.getTitle()));
                        itemElement.appendChild(titleElement);
                    }

                    if (article.getDescription() != null) {
                        Element descriptionElement = doc.createElement("Description");
                        descriptionElement.appendChild(doc.createCDATASection(article
                                .getDescription()));
                        itemElement.appendChild(descriptionElement);
                    }

                    if (article.getPicUrl() != null) {
                        Element picUrlElement = doc.createElement("PicUrl");
                        picUrlElement.appendChild(doc.createCDATASection(article.getPicUrl()));
                        itemElement.appendChild(picUrlElement);
                    }

                    if (article.getUrl() != null) {
                        Element urlElement = doc.createElement("Url");
                        urlElement.appendChild(doc.createCDATASection(article.getUrl()));
                        itemElement.appendChild(urlElement);
                    }
                }
            } else {
                return null;
            }
            break;
        }
        }

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");

            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
}
