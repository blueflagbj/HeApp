package com.wade.mobile.util.xml;

import android.os.Build;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MobileXMLHandler extends DefaultHandler {
    public String TEXT = "text";
    private Map<String, Set<String>> attrs;
    private StringBuilder builder;
    private Map<String, List<Map<String, String>>> config;
    private Map<String, Integer> configIndex;
    private StringBuilder currentPath;
    private String suffix = "/";

    public MobileXMLHandler(Map<String, Set<String>> attrs2) {
        this.attrs = attrs2;
    }

    public void startDocument() throws SAXException {
        super.startDocument();
        this.config = new HashMap();
        this.configIndex = new HashMap();
        this.builder = new StringBuilder();
        this.currentPath = new StringBuilder();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        this.builder.append(ch, start, length);
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (Build.VERSION.SDK_INT < 8) {
            this.currentPath.append(name).append(this.suffix);
        } else {
            this.currentPath.append(name).append(this.suffix);
        }
        String node = this.currentPath.toString().substring(0, this.currentPath.length() - 1).toUpperCase();
        Set<String> attr = this.attrs.get(node);
        List<Map<String, String>> datas = this.config.get(node);
        Map<String, String> data = null;
        if (attr != null && attr.size() > 0) {
            data = new HashMap<>();
            for (String qName : attr) {
                String value = attributes.getValue(qName);
                if (value != null) {
                    data.put(qName, value);
                }
            }
        }
        if (data != null) {
            if (datas == null) {
                datas = new ArrayList<>();
            }
            this.configIndex.put(node, Integer.valueOf(datas.size()));
            datas.add(data);
            this.config.put(node, datas);
            return;
        }
        this.configIndex.put(node, -1);
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        Map<String, String> data;
        super.endElement(uri, localName, name);
        int len = this.currentPath.length();
        if (this.currentPath.substring(len - 1).equals(this.suffix)) {
            this.currentPath.setLength(len - 1);
        }
        String node = this.currentPath.toString().trim().toUpperCase();
        String value = this.builder.toString().trim();
        if (!value.equals("")) {
            List<Map<String, String>> datas = this.config.get(node);
            if (datas == null) {
                datas = new ArrayList<>();
                this.config.put(node, datas);
            }
            int index = this.configIndex.get(node).intValue();
            if (index < 0) {
                data = new HashMap<>();
                datas.add(data);
            } else {
                data = datas.get(index);
            }
            data.put(this.TEXT, value);
        }
        this.builder.setLength(0);
        if (this.currentPath.lastIndexOf(this.suffix) > 0) {
            this.currentPath.setLength(this.currentPath.lastIndexOf(this.suffix) + 1);
        }
    }

    public Map<String, List<Map<String, String>>> getConfig() {
        return this.config;
    }
}