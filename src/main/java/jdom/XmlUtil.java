package jdom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class XmlUtil {

    public static final Function<String , List<String>> sitemaps = xml -> XmlUtil.stingToUrlMap(xml,"sitemap","loc");
    public static final Function<String , List<String>> urlSet = xml -> XmlUtil.stingToUrlMap(xml,"url","loc");



    private static List<String> stingToUrlMap(String xml, String parent, String child) {

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(new StringReader(xml));
            Element rootNode = doc.getRootElement();
            LinkedList<String> ls = new LinkedList<>();
            for (Element url : rootNode.getChildren(parent, rootNode.getNamespace())) {
                ls.add(url.getChild(child, rootNode.getNamespace()).getText());
            }
            return ls;
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return null;
    }

}
