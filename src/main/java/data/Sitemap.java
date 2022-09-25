package data;

import java.util.Objects;

public class Sitemap {
    private String url;
    private String updatedAt;

    public Sitemap() {
    }

    public Sitemap(String url, String updatedAt) {
        this.url = url;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sitemap sitemap = (Sitemap) o;
        return url.equals(sitemap.url) && updatedAt.equals(sitemap.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, updatedAt);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
