package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "sitemap")
public class Sitemap {
    private String url;
    private String updatedAt;

    @XmlElement(name = "loc")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement(name = "lastmod")
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdateAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
