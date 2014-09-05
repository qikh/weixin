package weixin.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import weixin.message.TextMessage;

public class MessageUtil {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    public static Map<String, String> xmlToMap(String xmlStr) {
        Map<String, String> request = new HashMap<String, String>();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.debug(e.getMessage());
            return null;
        }

        try {
            Document document = builder.parse(new InputSource(new StringReader(xmlStr)));
            Node rootNode = document.getFirstChild();
            if (rootNode != null) {
                NodeList nodes = rootNode.getChildNodes();
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Node node = nodes.item(i);
                    String content = node.getTextContent();
                    request.put(node.getNodeName(), content);
                }
            }
            return request;
        } catch (SAXException e) {
            logger.debug(e.getMessage());
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }

        return null;
    }

    public static TextMessage xmlToTextMessage(String xmlStr) {
        TextMessage request = new TextMessage();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        try {
            Document document = builder.parse(new InputSource(new StringReader(xmlStr)));
            Node rootNode = document.getFirstChild();
            if (rootNode != null) {
                NodeList nodes = rootNode.getChildNodes();
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Node node = nodes.item(i);
                    String content = node.getTextContent();
                    if (node.getNodeName().equals("ToUserName")) {
                        request.setToUserName(content);
                    } else if (node.getNodeName().equals("FromUserName")) {
                        request.setFromUserName(content);
                    } else if (node.getNodeName().equals("CreateTime")) {
                        request.setCreateTime(Integer.valueOf(content));
                    } else if (node.getNodeName().equals("MsgType")) {
                        request.setMsgType(content);
                    } else if (node.getNodeName().equals("Content")) {
                        request.setContent(content);
                    } else if (node.getNodeName().equals("MsgId")) {
                        request.setMsgId(Long.valueOf(content));
                    }
                }
            }
            return request;
        } catch (SAXException e) {
            logger.debug(e.getMessage());
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }

        return null;
    }

}
