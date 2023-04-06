package com.oracle.oracle.training.services.functional;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XMLParseService {

    public  Map<String,Object> parseXML(File file){
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // normalize the document
            document.getDocumentElement().normalize();
            // get the root element
            Element root = document.getDocumentElement();
            return  parseXML(root);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
    private  Map<String,Object> parseXML(Node node){
        NamedNodeMap nodeMap = node.getAttributes();
        Map<String,Object> map = new HashMap<>();

        // checks whether the child nodes are array elements
        Map<String,Integer> count = new HashMap<>();

        // Add all attributes as Node properties
        for (int i = 0; i < nodeMap.getLength(); i++)
        {
            Node tempNode = nodeMap.item(i);
            map.put(tempNode.getNodeName(),tempNode.getTextContent());
        }

        NodeList nodeList = node.getChildNodes();

        // get the frequency of all elements in the child nodes
        for(int i = 0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeType()!=Node.ELEMENT_NODE) continue;
            String nodeName = nodeList.item(i).getNodeName();
            count.put(nodeName,count.getOrDefault(nodeName,0)+1);
        }

        for(int i = 0;i<nodeList.getLength();i++){
            Node currentNode = nodeList.item(i);
            if(currentNode.getNodeType()!=Node.ELEMENT_NODE) continue;
            String currentNodeName = currentNode.getNodeName();
            if(count.get(currentNodeName)>1) {
                map.putIfAbsent(currentNodeName,new ArrayList<>());
                List list = (List)map.get(currentNodeName);
                // check whether the currentNode does not have any child in a list element
                if(hasChilds(currentNode)) list.add(parseXML(currentNode));
                else list.add(currentNode.getTextContent());
                continue;
            }
            // check whether the currentNode does have any child in a list element
            if(hasChilds(currentNode)) {
                map.put(currentNodeName,parseXML(currentNode));
            }else
                map.put(currentNodeName,currentNode.getTextContent());
        }
        return  map;
    }

    private boolean hasChilds(Node node){
        return node.getChildNodes().getLength() > 1;
    }
}
