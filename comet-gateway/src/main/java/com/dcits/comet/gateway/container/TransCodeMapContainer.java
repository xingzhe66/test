package com.dcits.comet.gateway.container;


import com.dcits.comet.base.scanner.ClasspathPackageScanner;
import com.dcits.comet.commons.business.ServiceTransfer;
import com.dcits.comet.gateway.property.CometYaml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class TransCodeMapContainer {

    public static final String DL = "|";

//    private ScannerProperties scannerProperties;

    private static List<String> packageNames;

    private final static Map<String, String> TRANS_CODE_MAP =
            new HashMap<String, String>();
//
//    //
//    private static volatile TransCodeMapContainer transCodeMapContainer;

    public static final String COMET_YML = "comet.yml";

    public static final String GATEWAY_API_PACKAGE_NAMES = "gatewayApiPackageNames";

    static {
        try {
            Yaml yaml = new Yaml();
            URL url = TransCodeMapContainer.class.getClassLoader().getResource(COMET_YML);
            if (url == null) {
                log.info(COMET_YML+"配置信息不存在！");
            }
            //comet.yaml文件中的配置数据，然后转换为obj，
            Map map = yaml.load(new FileInputStream(url.getFile()));
            if(null==map){
                log.info(COMET_YML+"配置信息读取失败！");
            }
            log.info(map.toString());
            packageNames = (List<String>) map.get(GATEWAY_API_PACKAGE_NAMES);

        } catch (Exception e) {
            log.info(COMET_YML+"配置信息读取失败！");
            e.printStackTrace();
        }
        new TransCodeMapContainer();
    }

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
        init();
    }

    private void init() {
        for (String packageName : packageNames) {
            packageScan(packageName);
            log.info("交易码映射扫描包：" + packageName + "结束！");
        }
        log.info("=============================================");
        log.info("=========交易码映射扫描包结束！==============");
        log.info("=============================================");
    }

    private void packageScan(String packageName) {
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

//    public static void main(String[] arg) {
//        System.out.println("");
////        new TransCodeMapContainer();
//    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//    }
//
//    public void setScannerProperties(ScannerProperties scannerProperties) {
//        this.scannerProperties = scannerProperties;
//    }
}
