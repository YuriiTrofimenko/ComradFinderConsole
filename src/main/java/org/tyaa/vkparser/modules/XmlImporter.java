/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser.modules;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javanet.staxutils.IndentingXMLStreamWriter;
import org.tyaa.vkparser.model.TypicalWords;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Юрий
 */
public class XmlImporter
{
    public static TypicalWords getTypicalWords(String _filePath) throws IOException, XMLStreamException {
        
        TypicalWords typicalWords = new TypicalWords();
        
        try {
            
            File fXmlFile = new File(_filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            Node rootNode = doc.getDocumentElement();
            NodeList rootChildNodes = rootNode.getChildNodes();
            ArrayList<Node> rootChildList = new ArrayList<>();
            
            for (int i = 0; i < rootChildNodes.getLength(); i++) {
                if (rootChildNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    rootChildList.add(rootChildNodes.item(i));
                }
            }
            
//            Node interestNode = rootChildNodes.item(0);
//            Node activityNode = rootChildNodes.item(1);
//            Node aboutNode = rootChildNodes.item(2);
//            Node politicalNode = rootChildNodes.item(3);
//            Node religionNode = rootChildNodes.item(4);
//            Node inspiredbyNode = rootChildNodes.item(5);
//            Node peoplemainNode = rootChildNodes.item(6);
//            Node lifemainNode = rootChildNodes.item(7);
//            Node smokingNode = rootChildNodes.item(8);
//            Node alcoholNode = rootChildNodes.item(9);
            
            for (Node currentNode : rootChildList) {
                //switch(currentNode.){
                    
                //}
                System.out.println(currentNode.getNodeName());
                String infoItemName = currentNode.getNodeName();
                NodeList infoItemChildNodes = currentNode.getChildNodes();
                
                switch(infoItemName)
                {
                    case "interest":{
                        //typicalWords.mInterestMap.put(_filePath, Integer.MIN_VALUE);
                        for (int i = 0; i < infoItemChildNodes.getLength(); i++) {
                            
                            if (infoItemChildNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                
                                if (infoItemChildNodes.item(i).getNodeName().equals("variant")) {
                                    
                                    NodeList variantChildNodes =
                                        infoItemChildNodes.item(i).getChildNodes();
                                    for (int j = 0; j < variantChildNodes.getLength(); j++) {
                                    
                                        if (variantChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                        
                                            if (variantChildNodes.item(j).getNodeName().equals("value")) {
                                            
                                                System.out.println("value = " + variantChildNodes.item(j).getTextContent());
                                            } else if (variantChildNodes.item(j).getNodeName().equals("count")) {
                                            
                                                System.out.println("count = " + variantChildNodes.item(j).getTextContent());
                                            }
                                        }
                                    }
                                }
                                //rootChildList.add(rootChildNodes.item(i));
                            }
                        }
                    }
                    default:{}
                }
            }
            
        } catch (Exception e) {
        }
        
        return null;
    }
}
