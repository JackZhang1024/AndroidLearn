package com.lucky.androidlearn.xml;

import java.io.InputStream;
import java.util.List;


public interface IXmlService {
    //因为三种方法都要去解析XML所以可以使三种方法都实现该接口即可
    public List<Person> getPersonsByParseXML(InputStream in) throws Exception;

    //创建出XML文档
    public String getXMLDocumentByPersons(List<Person> persons);

}
