package com.dcits.comet.commons.exception;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.commons.exception.xml.ErrorCode;
import com.dcits.comet.commons.exception.xml.ErrorCodes;
import com.dcits.comet.commons.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 错误码加载
 *
 * @author wangyun
 */
public class ExceptionContainer {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionContainer.class);

    private final static String DEFAULT_LANGUAGE = "zh_CN";

    private final static String DEFAULT_ERROR_CODE_CONFIG = "classpath*:config/*_errorCode_*.*";
    public static final String XML = ".xml";

    private static boolean inited = false;

    private static final String DEFAULT_ERRORCODE_SYSINDICATOR = "DEFAULT";

    /**
     * LANGUAGE_ERROR_MAPPINGS结构 { language:{ [ errorCodeFileName:{ errorCode:{
     * errorMessage } } ] } }
     */
    private final static Map<String, List<Map<String, HashMap<String, String>>>> LANGUAGE_ERROR_MAPPINGS =
            new HashMap<String, List<Map<String, HashMap<String, String>>>>();
    /**
     * language，内部错误码，外部系统编号
     */
    private final static Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ErrorCode>>> INNERCODE_OUTSIDECODE_MAPPING =
            new HashMap<>();

    /**
     * language，外部系统编号，外部错误码
     */
    private final static Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ErrorCode>>> OUTSIDECODE_INNERCODE_MAPPING =
            new HashMap<>();

    static {
        LANGUAGE_ERROR_MAPPINGS.put(DEFAULT_LANGUAGE, new ArrayList<>());
        INNERCODE_OUTSIDECODE_MAPPING.put(DEFAULT_LANGUAGE,
                new ConcurrentHashMap<>());
        OUTSIDECODE_INNERCODE_MAPPING.put(DEFAULT_LANGUAGE,
                new ConcurrentHashMap<>());
    }

    private static List<String> configKeys;

    private static Resource[] locations;

    public ExceptionContainer() {
        init();
    }


    public static void print() {
        StringBuilder sb = new StringBuilder(200);
        String newLine = "\r\n", tab = "\t";
        for (String language : LANGUAGE_ERROR_MAPPINGS.keySet()) {
            sb.append(language).append(newLine);
            for (Map<String, HashMap<String, String>> fileNameMap : LANGUAGE_ERROR_MAPPINGS.get(language)) {
                for (String fileName : fileNameMap.keySet()) {
                    sb.append(tab).append(fileName).append(newLine);
                    HashMap<String, String> mapping = fileNameMap.get(fileName);
                    for (String code : mapping.keySet()) {
                        sb.append(tab).append(tab).append(code).append("=").append(mapping.get(code))
                                .append(newLine);
                    }
                }
            }
        }
        LOGGER.info("\r\n{}", sb);
    }


    private static void init() {
        if (locations == null) {
            locations = ResourceUtil.getResource(DEFAULT_ERROR_CODE_CONFIG);
        }
        if (locations != null && locations.length > 0) {
            configKeys = new ArrayList<>();
        }
        for (Resource resource : locations) {
            configKeys.add(resource.getFilename());
            loadErrorCodeConfig(resource);
        }
        inited = true;
        print();
    }


    /**
     * @param res
     */
    private static void loadErrorCodeConfig(Resource res) {
        String file = res.getFilename();

        String language = file.substring(file.indexOf("errorCode_") + 10, file.lastIndexOf("."));

        List<Map<String, HashMap<String, String>>> errorMapList = LANGUAGE_ERROR_MAPPINGS.get(language);
        if (errorMapList == null) {
            errorMapList = new ArrayList<Map<String, HashMap<String, String>>>();
            LANGUAGE_ERROR_MAPPINGS.put(language, errorMapList);
        }

        HashMap<String, String> oldErrorMap = null;
        Map<String, String> newErrorMap = null;
        boolean errorFileMapExist = false;

        // 错误码文件对应的map是否已经存在
        for (Map<String, HashMap<String, String>> errorFileMap : errorMapList) {
            if (errorFileMap.containsKey(file)) {
                errorFileMapExist = true;
                oldErrorMap = errorFileMap.get(file);
                break;
            }
        }

        // 不存在,新增
        if (!errorFileMapExist) {
            Map<String, HashMap<String, String>> errorFileMap =
                    new HashMap<>();
            errorFileMap.put(file, loadNewConfig(res));
            errorMapList.add(errorFileMap);
        }
        // 存在,合并、清理
        else {
            newErrorMap = loadNewConfig(res);
            oldErrorMap.putAll(newErrorMap);
            List<String> tmpRemoveKeyList = new ArrayList<String>();
            // 统计需要删除的key
            for (String key : oldErrorMap.keySet()) {
                if (!newErrorMap.containsKey(key)) {
                    tmpRemoveKeyList.add(key);
                }
            }
            // 删除key
            for (String key : tmpRemoveKeyList) {
                oldErrorMap.remove(key);
            }
        }

        LOGGER.debug("error properties config:{}", JSON.toJSONString(errorMapList));

        LOGGER.info("load error properties file  > {} success !", file);
    }


    private static HashMap<String, String> loadNewConfig(Resource res) {
        InputStream in = null;
        try {
            in = res.getInputStream();
            HashMap<String, String> map = new HashMap<String, String>();

            if (res.getFilename().endsWith(XML)) {
                ErrorCodes errorCodes = parseErrorCodeXml(in);
                for (ErrorCode ec : errorCodes.getErrorCode()) {
                    map.put(ec.getCode(), ec.getMessage());
                    INNERCODE_OUTSIDECODE_MAPPING.get(DEFAULT_LANGUAGE).putIfAbsent(ec.getCode(),
                            new ConcurrentHashMap<>());
                    INNERCODE_OUTSIDECODE_MAPPING.get(DEFAULT_LANGUAGE).get(ec.getCode())
                            .put(errorCodes.getExternalSysIndicate(), ec);

                    OUTSIDECODE_INNERCODE_MAPPING.get(DEFAULT_LANGUAGE).putIfAbsent(
                            errorCodes.getExternalSysIndicate(), new ConcurrentHashMap<>());
                    OUTSIDECODE_INNERCODE_MAPPING.get(DEFAULT_LANGUAGE)
                            .get(errorCodes.getExternalSysIndicate()).put(ec.getOutCode(), ec);
                }
            } else {
                Properties prop = new Properties();
                prop.load(in);
                for (Object key : prop.keySet()) {
                    map.put((String) key, prop.getProperty((String) key));
                }
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(String.format("加载%s失败!", res.getFilename()), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.warn("", e);
            }
        }
    }


    private static ErrorCodes parseErrorCodeXml(InputStream in) {
        try {
            Unmarshaller unmarshaller =
                    JAXBContext.newInstance(new Class[]{ErrorCodes.class},
                            Collections.<String, Object>emptyMap()).createUnmarshaller();

            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            Object obj = unmarshaller.unmarshal(xmlInputFactory.createXMLStreamReader(new StreamSource(in)));
            return (ErrorCodes) obj;
        } catch (JAXBException | FactoryConfigurationError | XMLStreamException e) {
            throw new RuntimeException("解析错误码xml文件错误!", e);
        }
    }


    /**
     * 根据错误码获取对应的错误信息, 并将参数替换通配符{}
     *
     * @param errorCode 错误码
     * @param pattern   匹配参数
     * @return
     */
    public static String getErrorMessage(String errorCode, String... pattern) {
        if (!inited) {
            init();
        }

        List<Map<String, HashMap<String, String>>> errorMapList =
                LANGUAGE_ERROR_MAPPINGS.get(DEFAULT_LANGUAGE);
        String message = null;
        for (Map<String, HashMap<String, String>> map : errorMapList) {
            for (Map<String, String> errorMap : map.values()) {
                if (errorMap.containsKey(errorCode)) {
                    message = errorMap.get(errorCode);
                    break;
                }
            }
        }

        if (!StringUtils.hasLength(message)) {
            LOGGER.warn("未找到错误码[{}]的定义!", errorCode);
            if (pattern != null && pattern.length > 0) {
                message = pattern.length > 1 ? Arrays.toString(pattern) : pattern[0];
            }
        }

        return MessageFormatter.arrayFormat(message, pattern).getMessage();
    }


    /**
     * 获取外部系统错误码
     *
     * @param innerErrorCode      内部错误码
     * @param externalSysIndicate 外系统编号
     * @return 当对应的外部错误码不存在时返回内部错误码
     */
    public static String getExternalCode(String innerErrorCode, String externalSysIndicate) {
        externalSysIndicate =
                externalSysIndicate == null ? DEFAULT_ERRORCODE_SYSINDICATOR : externalSysIndicate;
        ConcurrentHashMap<String, ErrorCode> map =
                INNERCODE_OUTSIDECODE_MAPPING.get(DEFAULT_LANGUAGE).get(innerErrorCode);
        if (map != null) {
            ErrorCode ec = null;
            if ((ec = map.get(externalSysIndicate)) != null) {
                return ec.getOutCode();
            }
        }

        return innerErrorCode;
    }


    /**
     * 根据外部系统错误码获取内部错误码
     *
     * @param externalCode        外部错误码
     * @param externalSysIndicate 外系统编号
     * @return 当对应的内部错误码不存在时返回外部错误码
     */
    public static String getInnerCode(String externalCode, String externalSysIndicate) {
        externalSysIndicate =
                externalSysIndicate == null ? DEFAULT_ERRORCODE_SYSINDICATOR : externalSysIndicate;
        ConcurrentHashMap<String, ErrorCode> map =
                OUTSIDECODE_INNERCODE_MAPPING.get(DEFAULT_LANGUAGE).get(externalSysIndicate);
        if (map != null) {
            ErrorCode ec = null;
            if ((ec = map.get(externalCode)) != null) {
                return ec.getCode();
            }
        }

        return externalCode;
    }
}
