package com.lucky.androidlearn.xml;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class PullXmlService implements IXmlService {

    @Override
    public List<Person> getPersonsByParseXML(InputStream in) throws Exception {
        List<Person> persons = null;
        Person person = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(in, "utf-8");
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch (type) {
                case XmlPullParser.START_DOCUMENT:
                    //创建集合
                    persons = new ArrayList<Person>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("person".equals(tagName)) {
                        person = new Person();
                        String id = parser.getAttributeValue(0);
                        person.setId(Integer.parseInt(id));
                    } else if ("name".equals(tagName)) {
                        person.setName(parser.nextText());
                    } else if ("age".equals(tagName)) {
                        person.setAge(Integer.parseInt(parser.nextText()));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("person".equals(tagName)) {
                        persons.add(person);
                        person = null;
                    }
                    break;
                default:
                    break;
            }
            type = parser.next();
        }
        return persons;
    }

    @Override
    public String getXMLDocumentByPersons(List<Person> persons) {
        // TODO Auto-generated method stub
        return null;
    }

}
