package xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public final class XmlUtil {


    public static List<Sitemap> stringToSitemap(String body) {

        try {
            final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            documentFactory.setNamespaceAware(false);
            final DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            final Unmarshaller um = JAXBContext.newInstance(ListSitemap.class)
                    .createUnmarshaller();
            final Document document = documentBuilder.parse(new InputSource(
                    new StringReader(body)));

            final ListSitemap sitemaps = (ListSitemap) um.unmarshal(document);
            return sitemaps.getSitemaps();
        } catch (JAXBException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static List<MapUrl> stringToMapUrl(String body) {

        try {
            final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            documentFactory.setNamespaceAware(false);

            final DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            final Unmarshaller um = JAXBContext.newInstance(ListMapUrl.class)
                    .createUnmarshaller();
            final Document document = documentBuilder.parse(
                    new InputSource(
                            new StringReader(
                                    body)));
            final ListMapUrl listMapUrl = (ListMapUrl) um.unmarshal(document);
            return listMapUrl.getUrlList();

        } catch (ParserConfigurationException | JAXBException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
