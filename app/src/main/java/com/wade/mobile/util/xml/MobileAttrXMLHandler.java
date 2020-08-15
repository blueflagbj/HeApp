package com.wade.mobile.util.xml;

import android.os.Build;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class MobileAttrXMLHandler extends DefaultHandler {
    private Map<String, Set<String>> attrs;
    private StringBuilder currentPath;
    private String suffix = "/";

    public void startDocument() throws SAXException {
        super.startDocument();
        this.attrs = new HashMap();
        this.currentPath = new StringBuilder();
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        Set<String> attrSet;
        String qName;
        super.startElement(uri, localName, name, attributes);
        if (Build.VERSION.SDK_INT < 8) {
            this.currentPath.append(name).append(this.suffix);
        } else {
            this.currentPath.append(name).append(this.suffix);
        }
        String node = this.currentPath.toString().substring(0, this.currentPath.length() - 1).toUpperCase();
        if (this.attrs.get(node) != null) {
            attrSet = this.attrs.get(node);
        } else {
            attrSet = new HashSet<>();
        }
        int len = attributes.getLength();
        for (int i = 0; i < len; i++) {
            if (Build.VERSION.SDK_INT < 8) {
                qName = attributes.getLocalName(i);
            } else {
                qName = attributes.getQName(i);
            }
            attrSet.add(qName);
        }
        this.attrs.put(node, attrSet);
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        int len = this.currentPath.length();
        if (this.currentPath.substring(len - 1).equals(this.suffix)) {
            this.currentPath.setLength(len - 1);
        }
        if (this.currentPath.lastIndexOf(this.suffix) > 0) {
            this.currentPath.setLength(this.currentPath.lastIndexOf(this.suffix) + 1);
        }
    }

    public Map<String, Set<String>> getAttrs() {
        return this.attrs;
    }
}