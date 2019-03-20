package com.dcits.comet.gateway.container;


import com.dcits.comet.base.scanner.ClasspathPackageScanner;
import com.dcits.comet.commons.business.ServiceTransfer;
import com.dcits.comet.gateway.property.ScannerProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransCodeMapContainer implements InitializingBean {

    public static final String DL = "|";

    @Autowired
    private ScannerProperties scannerProperties;

//    @Value("${transfer.sanner.packageNames}")
//    private List<String> packageNames;


    private final static Map<String, String> TRANS_CODE_MAP =
            new HashMap<String, String>();

//
//    private static volatile TransCodeMapContainer transCodeMapContainer;
//
//    static {
//        new TransCodeMapContainer();
//    }
//
//    private static TransCodeMapContainer getInstance() {
//        if (transCodeMapContainer == null) {
//            synchronized (TransCodeMapContainer.class) {
//                if (transCodeMapContainer == null) {
//                    transCodeMapContainer = new TransCodeMapContainer();
//                }
//            }
//        }
//        return transCodeMapContainer;
//    }

    private TransCodeMapContainer() {
        //init();
    }

    private void init(String packageName) {
        ClassLoader cl = TransCodeMapContainer.class.getClassLoader();
        ClasspathPackageScanner scan = new ClasspathPackageScanner(packageName);
        List<String> classNameList = null;
        try {
            classNameList = scan.getFullyQualifiedClassNameList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classNameList != null) {
            for (String className : classNameList) {
                Class<?> aClass = null;
                try {
                    aClass = cl.loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Method[] methods = aClass.getMethods();
                //.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(ServiceTransfer.class)) {
                        ServiceTransfer serviceTransfer = method.getAnnotation(ServiceTransfer.class);
                        TRANS_CODE_MAP.put(serviceTransfer.messageType().getMessageType() + DL
                                        + serviceTransfer.messageCode() + DL + serviceTransfer.serviceCode().getServiceCode()
                                , serviceTransfer.uri());
                    }

                }
            }
        }
    }

    public static String getUri(String MsgType, String MsgCode, String serviceType) {
        return TRANS_CODE_MAP.get(MsgType + DL + MsgCode + DL + serviceType);

    }

    public static void main(String[] arg) {
        new TransCodeMapContainer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (String packageName:scannerProperties.getPackageNames()) {
            init(packageName);
        }
    }
}
