/**
 * Copyright (C) 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.core.fieldTypes.file;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.jbpm.formModeler.core.fieldTypes.CustomFieldType;
import org.jbpm.formModeler.service.bb.mvc.components.URLMarkupGenerator;
import org.mvel2.util.Make;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Sample implementation of a File input that can be used in forms to upload files on a storage system and save the File path inside a String property.
 *
 * This Custom Type must be used only on String properties
 *
 * This is just a Sample, use it only with test purposes.
 */
public class FileCustomType implements CustomFieldType {

    private Logger log = LoggerFactory.getLogger(FileCustomType.class);


    @Inject
    private URLMarkupGenerator urlMarkupGenerator;

    private String getInputId(String namespace, String fieldName) {
        return namespace + "_file_" + fieldName;
    }

    @PostConstruct
    public void init() {
    }

    @Override
    public String getDescription(Locale locale) {
        /*ResourceBundle bundle = ResourceBundle.getBundle("org.jbpm.formModeler.core.fieldTypes.file.messages", locale);
        return bundle.getString("description");*/
        return "Mgb input field";
    }

    @Override
    public Object getValue(Map requestParameters, Map requestFiles, String fieldName, String namespace, Object previousValue, boolean required, boolean readonly, String... params) {
        String id = getInputId(namespace, fieldName);

        String[] inputParams = (String[]) requestParameters.get(id);
        if(inputParams.length <= 0) return "-";
        return inputParams[0];
    }

    @Override
    public String getShowHTML(Object value, String fieldName, String namespace, boolean required, boolean readonly, String... params) {
        return renderField(fieldName, (String) value, namespace, false, (String)value);
    }

    @Override
    public String getInputHTML(Object value, String fieldName, String namespace, boolean required, boolean readonly, String... params) {
        return renderField(fieldName, (String) value, namespace, true && !readonly, (String)value);
    }

    public String renderField(String fieldName, String path, String namespace, boolean showInput, String value) {
       String inputId = fieldName;//getInputId(namespace, fieldName);
        inputId = inputId.replace('.', '_').replace('-','_');

        try {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("inputId", inputId);
            InputStream src = this.getClass().getResourceAsStream("input.ftl");
            freemarker.template.Configuration cfg = new freemarker.template.Configuration();
            BeansWrapper defaultInstance = new BeansWrapper();
            defaultInstance.setSimpleMapWrapper(true);
            cfg.setObjectWrapper(defaultInstance);
            cfg.setTemplateUpdateDelay(0);
            Template temp = new Template(fieldName, new InputStreamReader(src), cfg);
            StringWriter out = new StringWriter();
            temp.process(context, out);
            out.flush();
            return out.getBuffer().toString();
        } catch (Exception e) {
            log.warn("Failed to process template for field '{}'", fieldName, e);
            return e+"";
        }

        // return "<input type=\"file\" name=\""+inputId+"\" id=\""+inputId+"\" value=\"" + value + "\"/>";
    }
}
