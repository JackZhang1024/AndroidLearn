package com.lucky.androidlearn.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import com.lucky.androidlearn.R;

import junit.framework.Assert;

public class XmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_xml);
            xmlSerializerCreateJava();
            xmlSerializerCreateAndroid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlSerializerCreateJava() {
        try {
            String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "person1.xml";
            List<Person> persons = new ArrayList<Person>();
            persons.add(new Person(1, "zhangsan", 80));
            persons.add(new Person(2, "lisi", 43));
            persons.add(new Person(3, "wangwu", 12));
            Document doc = PersonServiceJava.saveList2Document(persons);
            PersonServiceJava.saveDocument2XmlFile(filePath, doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void xmlSerializerCreateAndroid() throws Exception {
        List<Person> persons = new ArrayList<Person>();
        persons.add(new Person(1, "zhangsan", 80));
        persons.add(new Person(2, "lisi", 43));
        persons.add(new Person(3, "wangwu", 12));
        File file = Environment.getExternalStorageDirectory();
        File xmlFile = new File(file, "person.xml");
        FileOutputStream outStream = new FileOutputStream(xmlFile);
        PersonService.save(persons, outStream);
    }

    private void doSomething() throws Exception {
        AssetManager mAssess = getAssets();
        InputStream is = mAssess.open("persons.xml");
        //DOM解析
        IXmlService mDomService = new DomXmlService();
        List<Person> persons = mDomService.getPersonsByParseXML(is);
        //SAX解析
//			mSaxService=new SaxXmlService();
//			persons=mSaxService.getPersonsByParseXML(is);
        //PULL解析
//			mPullService=new PullXmlService();
//			persons=mPullService.getPersonsByParseXML(is);
    }

}
