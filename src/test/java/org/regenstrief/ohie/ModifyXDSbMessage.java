package org.regenstrief.ohie;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.UUID;
import java.util.Random;

public class ModifyXDSbMessage {

    public void modify(String fileName) {

        Random random = new Random();

        try {
            String filepath = "/Users/snkasthu/SourceCode/w/ohie-integration-tests/src/test/resources/xds/" + fileName;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList staffList = doc.getElementsByTagName("ExtrinsicObject");
            for (int z = 0; z < staffList.getLength(); z++) {

                String id = UUID.randomUUID().toString();
                String RegistryPackageId = UUID.randomUUID().toString();

                NamedNodeMap attr = staffList.item(z).getAttributes();
                Node nodeAttr = attr.getNamedItem("id");
                nodeAttr.setTextContent("urn:uuid:" + id);


                Node staffDocument = doc.getElementsByTagName("Document").item(0);

                NamedNodeMap attrDocument = staffDocument.getAttributes();
                Node nodeAttrstaffDocument = attrDocument.getNamedItem("id");
                nodeAttrstaffDocument.setTextContent("urn:uuid:" + id);


                Node staffAssociation = doc.getElementsByTagName("Association").item(0);

                NamedNodeMap attrAssociation = staffAssociation.getAttributes();
                Node nodeAttrAssociation = attrAssociation.getNamedItem("id");
                nodeAttrAssociation.setTextContent("urn:uuid:" + UUID.randomUUID().toString());

                Node targetObjectAssociation = attrAssociation.getNamedItem("targetObject");
                targetObjectAssociation.setTextContent("urn:uuid:" + id);

                Node sourceObjectAssociation = attrAssociation.getNamedItem("sourceObject");
                sourceObjectAssociation.setTextContent("urn:uuid:" + RegistryPackageId);


                Node staffRegestry = doc.getElementsByTagName("RegistryPackage").item(0);

                NamedNodeMap attrRegestry = staffRegestry.getAttributes();
                Node nodeAttrRegestry = attrRegestry.getNamedItem("id");
                nodeAttrRegestry.setTextContent("urn:uuid:" + RegistryPackageId);


                NodeList list = doc.getElementsByTagName("ExternalIdentifier");
                for (int x = 0; x < list.getLength(); x++) {

                    Node node = list.item(x);

                    NamedNodeMap attrId = node.getAttributes();
                    Node nodeAttrId = attrId.getNamedItem("id");
                    nodeAttrId.setTextContent("urn:uuid:" + UUID.randomUUID().toString());

                    Node nodeAttrclassifiedObject = attrId.getNamedItem("registryObject");
                    nodeAttrclassifiedObject.setTextContent("urn:uuid:" + id);

                    Node nodeIdentificationScheme = attrId.getNamedItem("identificationScheme");
                    if (nodeIdentificationScheme.getTextContent().equals("urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8")) {
                        Node nodeAttrvalue = attrId.getNamedItem("value");
                        nodeAttrvalue.setTextContent("2.16.840.1.113883.3.72.5.9.1.1.5549314503" + random.nextInt((999999999 - 111111111) + 1) + 111111111);
                    }

                    if (nodeIdentificationScheme.getTextContent().equals("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
                        Node nodeAttrvalue = attrId.getNamedItem("value");
                        nodeAttrvalue.setTextContent("2.16.840.1.113883.3.72.5.9.1.0.5386503746" + random.nextInt((999999999 - 111111111) + 1) + 111111111);
                    }

                    if (nodeIdentificationScheme.getTextContent().equals("urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446") || nodeIdentificationScheme.getTextContent().equals("urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8") || nodeIdentificationScheme.getTextContent().equals("urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832")) {
                        Node nodeAttrvalue = attrId.getNamedItem("registryObject");
                        nodeAttrvalue.setTextContent("urn:uuid:" + RegistryPackageId);
                    }

                }

                NodeList listClassification = doc.getElementsByTagName("Classification");
                for (int i = 0; i < listClassification.getLength(); i++) {

                    Node node = listClassification.item(i);

                    NamedNodeMap attrId = node.getAttributes();
                    Node nodeAttrId = attrId.getNamedItem("id");
                    nodeAttrId.setTextContent("urn:uuid:" + UUID.randomUUID().toString());

                    Node nodeAttrclassifiedObject = attrId.getNamedItem("classifiedObject");
                    nodeAttrclassifiedObject.setTextContent("urn:uuid:" + id);

                    Node nodeIdentificationScheme = attrId.getNamedItem("classificationScheme");

                    if (nodeIdentificationScheme != null) {
                        if (nodeIdentificationScheme.getTextContent().equals("urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d") || nodeIdentificationScheme.getTextContent().equals("urn:uuid:aa543740-bdda-424e-8c96-df4873be8500")) {
                            Node nodeAttrvalue = attrId.getNamedItem("classifiedObject");
                            nodeAttrvalue.setTextContent("urn:uuid:" + RegistryPackageId);
                        }
                    }


                    Node classificationNodeIdentificationScheme = attrId.getNamedItem("classificationNode");
                    if (classificationNodeIdentificationScheme != null) {
                        if (classificationNodeIdentificationScheme.getTextContent().equals("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd")) {
                            Node nodeAttrvalue = attrId.getNamedItem("classifiedObject");
                            nodeAttrvalue.setTextContent("urn:uuid:" + RegistryPackageId);
                        }
                    }


                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filepath));
                transformer.transform(source, result);
            }

                System.out.println("Done");

            }catch(ParserConfigurationException pce){
                pce.printStackTrace();
            }catch(TransformerException tfe){
                tfe.printStackTrace();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }catch(SAXException sae){
                sae.printStackTrace();
            }
        }
    }


