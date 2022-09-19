package xml;

import xml.Sitemap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sitemapindex")
public class ListSitemap {
    private List<Sitemap> sitemaps;

    @XmlElement(name = "sitemap")
    public List<Sitemap> getSitemaps() {
        return sitemaps;
    }

    public void setSitemaps(List<Sitemap> sitemaps) {
        this.sitemaps = sitemaps;
    }
}
