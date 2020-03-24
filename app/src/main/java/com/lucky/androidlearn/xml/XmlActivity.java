package com.lucky.androidlearn.xml;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class XmlActivity extends AppCompatActivity {

    private static final String TAG = "XmlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_xml);
            findViewById(R.id.btn_xml_create_java).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xmlSerializerCreateJava();
                }
            });
            findViewById(R.id.btn_xml_create_android).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xmlSerializerCreateAndroid();
                }
            });
            findViewById(R.id.btn_dom_parse).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    domParse();
                }
            });
            findViewById(R.id.btn_sax_parse).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saxParse();
                }
            });
            findViewById(R.id.btn_pull_parse).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pullParse();
                }
            });

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

    public void xmlSerializerCreateAndroid() {
        try {
            List<Person> persons = new ArrayList<Person>();
            persons.add(new Person(1, "zhangsan", 80));
            persons.add(new Person(2, "lisi", 43));
            persons.add(new Person(3, "wangwu", 12));
            File file = Environment.getExternalStorageDirectory();
            File xmlFile = new File(file, "person.xml");
            FileOutputStream outStream = new FileOutputStream(xmlFile);
            PersonService.save(persons, outStream);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void domParse() {
        try {
            AssetManager mAssess = getAssets();
            InputStream is = mAssess.open("persons.xml");
            //DOM解析
            IXmlService mDomService = new DomXmlService();
            List<Person> persons = mDomService.getPersonsByParseXML(is);
            Log.e(TAG, "domParse: "+persons.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saxParse() {
        try {
            AssetManager mAssess = getAssets();
            InputStream is = mAssess.open("persons.xml");
            //SAX解析
            IXmlService mSaxService = new SaxXmlService();
            List<Person> persons = mSaxService.getPersonsByParseXML(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pullParse() {
        try {
            AssetManager mAssess = getAssets();
            InputStream is = mAssess.open("persons.xml");
            //DOM解析
            IXmlService mDomService = new PullXmlService();
            List<Person> persons = mDomService.getPersonsByParseXML(is);
        } catch (Exception e) {

        }

    }

}
