package com.dcits.comet.batch.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTemplateContext {
    private static final Logger logger = LoggerFactory.getLogger(FileTemplateContext.class);
    private static FileTemplateContext instance;
    private Map<String, Data> configurations = new ConcurrentHashMap();
    private final String fileDir = "templates/txt";

    private FileTemplateContext() {
    }

    public static FileTemplateContext getInstance() {
        if (null == instance) {
            Class var0 = FileTemplateContext.class;
            synchronized(FileTemplateContext.class) {
                if (null == instance) {
                    instance = new FileTemplateContext();

                    try {
                        instance.loadTemplateFileDir();
                    } catch (IntrospectionException | DecoderException | ClassNotFoundException var3) {
                        throw new RuntimeException("解析模板文件失败", var3);
                    }
                }
            }
        }

        return instance;
    }

    public Data getConfig(TemplateType templateType, String key) {
        Data data = (Data)this.configurations.get(templateType + key);
        if (data == null) {
            throw new RuntimeException("配置模板" + templateType + "[" + key + "]不存在");
        } else {
            return data;
        }
    }

    private void loadTemplateFileDir() throws ClassNotFoundException, IntrospectionException, DecoderException {
        this.configurations.clear();
        List<String> subFiles = this.getResourceFile("templates/txt", ".xml");
        Iterator subFileIterator = subFiles.iterator();

        while(subFileIterator.hasNext()) {
            String subFile = (String)subFileIterator.next();
            Data data = (Data)this.parseXML(Data.class, subFile);
            if (data.hasSplit() && !data.hasLineSeparator()) {
                throw new RuntimeException("the template file " + subFile + " has split , you must define the line separator ");
            }

            String clazz = data.getClazz();
            if (this.configurations.get(data.getTemplateType() + clazz) != null) {
                throw new IllegalArgumentException("clazz[" + clazz + "]模板文件重复配置了,请删除一个!");
            }

            this.configurations.put(data.getTemplateType() + clazz, data);
            BeanInfo beanInfo = Introspector.getBeanInfo(Class.forName(clazz));
            PropertyDescriptor[] propDesc = beanInfo.getPropertyDescriptors();
            List<Column> cols = data.getColumnList();
            Iterator iterator = cols.iterator();

            while(iterator.hasNext()) {
                Column col = (Column)iterator.next();
                if (data.isFixLengthFile() && col.getLength() <= 0) {
                    throw new RuntimeException("the template file " + subFile + " is fixed length file , but column length is empty or less than zero ,you must define the column length ");
                }

                PropertyDescriptor[] arr$ = propDesc;
                int len$ = propDesc.length;

                for(int i = 0; i < len$; ++i) {
                    PropertyDescriptor prop = arr$[i];
                    String name = prop.getName();
                    if (col.getName().equals(name)) {
                        col.setWriteValueMethod(prop.getWriteMethod());
                        col.setReadValueMethod(prop.getReadMethod());
                        break;
                    }
                }

                String regex = col.getRegex();
                if (regex != null && !regex.trim().equals("")) {
                    col.setPattern(Pattern.compile(regex));
                }

                String tranSrc = col.getTranSrc();
                if (tranSrc != null && !tranSrc.trim().equals("")) {
                    String tranDest = col.getTranDest();
                    if (tranDest == null || tranDest.trim().equals("") || tranDest.split(",").length != tranSrc.split(",").length) {
                        throw new RuntimeException("the template file " + subFile + " tran source [" + tranSrc + "] not match tran dest [" + tranDest + "]");
                    }

                    String[] src = tranSrc.split(",");
                    String[] dest = tranDest.split(",");

                    for(int i = 0; i < src.length; ++i) {
                        col.addTrans(src[i], dest[i]);
                    }
                }

                data.setPerRecordLength(data.getPerRecordLength() + col.getLength());
                if (data.hasSplit() && data.getSplit().startsWith("0x")) {
                    data.setSplit("\\" + new String(Hex.decodeHex(data.getSplit().substring(2).toCharArray())));
                }

                if (data.hasLineSeparator() && data.getLineSeparator().startsWith("0x")) {
                    data.setLineSeparator(new String(Hex.decodeHex(data.getLineSeparator().substring(2).toCharArray())));
                }
            }
        }

    }

    private List<String> getResourceFile(String path, final String extensions) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ArrayList fileNameLists = new ArrayList();

        try {
            Enumeration urls = classLoader.getResources(path);

            while(true) {
                label40:
                while(urls.hasMoreElements()) {
                    URL temp = (URL)urls.nextElement();
                    String protocol = temp.getProtocol();
                    File file;
                    if ("jar".equals(protocol)) {
                        file = null;
                        JarURLConnection connection = (JarURLConnection)temp.openConnection();
                        JarFile jarFile = connection.getJarFile();
                        Enumeration en = jarFile.entries();

                        while(true) {
                            String entryName;
                            do {
                                do {
                                    if (!en.hasMoreElements()) {
                                        continue label40;
                                    }

                                    JarEntry entry = (JarEntry)en.nextElement();
                                    entryName = entry.getName();
                                    logger.debug("Current entry name is {}", entryName);
                                } while(!entryName.startsWith(path));
                            } while(extensions != null && !extensions.trim().equals("") && !entryName.toUpperCase().endsWith(extensions.toUpperCase()));

                            fileNameLists.add(entryName);
                        }
                    } else {
                        if (!"file".equals(protocol)) {
                            throw new RuntimeException("Unknow Protocol [" + protocol + "]");
                        }

                        file = new File(temp.getFile());
                        logger.debug(file.getAbsolutePath());
                        String[] files = file.list(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return extensions == null || extensions.trim().equals("") || name.toUpperCase().endsWith(extensions.toUpperCase());
                            }
                        });
                        String[] arr$ = files;
                        int len$ = files.length;

                        for(int i$ = 0; i$ < len$; ++i$) {
                            String f = arr$[i$];
                            fileNameLists.add(path + File.separatorChar + f);
                        }
                    }
                }

                return fileNameLists;
            }
        } catch (Exception var14) {
            throw new RuntimeException("获取资源文件出错", var14);
        }
    }

    private <M> M parseXML(Class<M> className, String xmlPath) {
        InputStream is = null;

        Object var8;
        try {
            JAXBContext context = JAXBContext.newInstance(className);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClassLoader loader = this.getClass().getClassLoader();
            is = loader.getResourceAsStream(xmlPath);
            M m = (M) unmarshaller.unmarshal(is);
            var8 = m;
        } catch (JAXBException var17) {
            throw new RuntimeException(var17);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var16) {
                    ;
                }
            }

        }

        return (M) var8;
    }
}
