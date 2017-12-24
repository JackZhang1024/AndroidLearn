package com.lucky.androidlearn.xml;

import java.io.OutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;


public class PersonService {


    /**
     * @param persons
     * @param out
     * @throws Exception
     */
    public static void save(List<Person> persons, OutputStream out) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "persons");
        for (Person person : persons) {
            serializer.startTag(null, "person");
            serializer.attribute(null, "id", person.getId().toString());

            serializer.startTag(null, "name");
            serializer.text(person.getName());
            serializer.endTag(null, "name");

            serializer.startTag(null, "age");
            serializer.text(person.getAge().toString());
            serializer.endTag(null, "age");
            serializer.endTag(null, "person");
        }
        serializer.endTag(null, "persons");
        serializer.endDocument();
        out.flush();
        out.close();
    }
}
