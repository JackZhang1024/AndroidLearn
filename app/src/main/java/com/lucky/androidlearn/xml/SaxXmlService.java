package com.lucky.androidlearn.xml;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 不必等到文档加载完毕之后才去解析，SAX解析直接进行解析
 *
 * @author Administrator
 */
public class SaxXmlService implements IXmlService {

    @Override
    public List<Person> getPersonsByParseXML(InputStream in) throws Exception {
        // TODO Auto-generated method stub
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        MySaxHandler dh = new MySaxHandler();
        parser.parse(in, dh);
        //返回解析之后生成的集合数据
        return dh.getList();
    }


    //自定义一个Handler继承自DefaultHandler 然后重写其中的方法
    class MySaxHandler extends DefaultHandler {
        private List<Person> persons;
        private String current;
        private Person person;

        @Override
        public void startDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.startDocument();
            persons = new ArrayList<Person>();
        }

        @Override
        public void endDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // TODO Auto-generated method stub
            super.startElement(uri, localName, qName, attributes);
            if ("person".equals(localName)) {
                person = new Person();
                person.setId(Integer.parseInt(attributes.getValue("id")));
            } else if ("name".equals(localName) || "age".equals(localName)) {
                current = localName;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("person".equals(localName)) {
                persons.add(person);
            } else if ("name".equals(localName) || "age".equals(localName)) {
                current = null;
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String s = new String(ch, start, length);
            if ("name".equals(current)) {
                person.setName(s);
            } else if ("age".equals(current)) {
                person.setAge(Integer.parseInt(s));
            }
        }


        //返回persons
        public List<Person> getList() {
            return persons;
        }
    }


    @Override
    public String getXMLDocumentByPersons(List<Person> persons) {
        // TODO Auto-generated method stub
        return null;
    }

}
