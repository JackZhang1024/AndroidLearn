package com.lucky.androidlearn.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PersonServiceJava {
	
	public static Document saveList2Document(List<Person> persons) throws ParserConfigurationException{
		DocumentBuilderFactory factory=   DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document doc=builder.newDocument();
		Element root=doc.createElement("persons");
		doc.appendChild(root);
		for(Person  person: persons){
			Element p=doc.createElement("person");
//			p.setAttribute("id", "man");
			Element name=doc.createElement("name");
			name.setTextContent(person.getName());
			p.appendChild(name);
			Element age=doc.createElement("age");
			age.setTextContent(person.getAge()+"");
			p.appendChild(age);
			root.appendChild(p);
		}
		return doc;
	}
	
	
	public static void saveDocument2XmlFile(String filePath,Document doc){
		try {
			TransformerFactory factory=TransformerFactory.newInstance();
			Transformer transformer=factory.newTransformer();
			DOMSource xmlSource=new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			File file=new File(filePath);
			if(file.exists()) file.delete();
			FileOutputStream  outputStream=new FileOutputStream(new File(filePath));
			StreamResult outputTarget=new StreamResult(outputStream);
			transformer.transform(xmlSource, outputTarget);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	

}
