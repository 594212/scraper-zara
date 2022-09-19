package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "urlset")
public class ListMapUrl {
    private List<MapUrl> urlList;

    @XmlElement(name = "url")
    public List<MapUrl> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<MapUrl> urlList) {
        this.urlList = urlList;
    }
}
