package com.lucky.androidlearn.xml;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomXmlService implements IXmlService {

    @Override
    public List<Person> getPersonsByParseXML(InputStream is) throws Exception {
        // TODO Auto-generated method stub
        List<Person> persons = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        //获取的是根元素 persons
        Element root = document.getDocumentElement();
        // 得到一个集合，里面存放在XML文件中所有的person
        NodeList nodelist = root.getElementsByTagName("person");
        // 在这块判断集合是否为空 或者集合的元素个数为0
        if (nodelist == null || nodelist.getLength() == 0) {
            return null;
        }
        //初始化集合
        persons = new ArrayList<Person>();
        //生成List集合的数据
        for (int i = 0; i < nodelist.getLength(); i++) {
            Element node = (Element) nodelist.item(i);
            Person p = new Person();
            int id = Integer.parseInt(node.getAttribute("id"));
            String name = getAttrText(node, "name");
            String age = getAttrText(node, "age");
            p.setAge(Integer.parseInt(age));
            p.setName(name);
            p.setId(id);

            persons.add(p);

        }
        return persons;
    }

    public String getAttrText(Element element, String name) {
        String content = null;
        //这个方法是获取person节点下面的所有节点
        NodeList nodelist2 = element.getChildNodes();
        for (int k = 0; k < nodelist2.getLength(); k++) {
            Node node = nodelist2.item(k);
            //获取节点的名称
            String nodeName = node.getNodeName();
            if (nodeName.equals(name)) {
                content = node.getTextContent();
            }
        }
        return content;
    }


    @Override
    public String getXMLDocumentByPersons(List<Person> persons) {
        // TODO Auto-generated method stub
        return null;
    }

}
