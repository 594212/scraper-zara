package jdom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XmlUtil {

    public static final Function<String, List<String>> sitemaps = xml -> XmlUtil.xmlTOList(xml, "sitemap", "loc")
            .stream().map(Element::getText).collect(Collectors.toList());
    public static final Function<String, List<String>> urlSet = xml -> XmlUtil.xmlTOList(xml, "url", "loc")
            .stream().map(Element::getText).collect(Collectors.toList());


    private static List<Element> xmlTOList(String xml, String parent, String child) {

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(new StringReader(xml));
            Element rootNode = doc.getRootElement();
            return rootNode.getChildren(parent, rootNode.getNamespace()).stream().map(
                    element -> element.getChild(child, element.getNamespace()))
                    .collect(Collectors.toList());
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Sitemap> updatedSitemaps(List<Sitemap> oldList, List<Sitemap> newList) {
        newList.removeAll(oldList);
        return newList;
    }

    class Sitemap {
        private final String url;
        private final String updatedAt;

        Sitemap(String url, String updateAt) {
            this.url = url;
            this.updatedAt = updateAt;
        }
    }

}


