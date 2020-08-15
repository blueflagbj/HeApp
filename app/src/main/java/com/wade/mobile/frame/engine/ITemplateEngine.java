package com.wade.mobile.frame.engine;


import java.io.Writer;
import java.util.Map;

public interface ITemplateEngine {
    void bind(Writer writer, Map<?, ?> map) throws Exception;

    void bind(Map<?, ?> map) throws Exception;

    String getHtml();
}