package com.wade.mobile.frame.engine;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public abstract class AbstractTemplateEngine implements ITemplateEngine {
    protected StringWriter out;

    /* access modifiers changed from: protected */
    public abstract void render(Map<?, ?> map, Writer writer) throws Exception;

    public void bind(Writer paramWriter, Map<?, ?> paramMap) throws Exception {
        try {
            System.out.println("AbstractTemplate-bind00:");
            render(paramMap, paramWriter);

        } finally {
            if (paramWriter != null)
                paramWriter.close();
        }
    }

    public void bind(Map<?, ?> paramMap) throws Exception {
        System.out.println("AbstractTemplate-bind01:");
        this.out = new StringWriter();
        try {
            render(paramMap, this.out);
            System.out.println("AbstractTemplate-bind012:");
        } finally {
            if (this.out != null)
                this.out.close();
        }
    }
    public String getHtml() {
        return (this.out == null) ? "" : this.out.getBuffer().toString();
    }

}