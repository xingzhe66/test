package com.dcits.comet.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigDecimalSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigIntegerSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import de.javakaffee.kryoserializers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author:
 * @date:
 * @version: 1.0
 * @descriptions: TODO
 */
public class SerializeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtil.class);

    private static final boolean REGISTRATION_REQUIRED = false;

    private static final ThreadLocal<Kryo> KRYO = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            LOGGER.info("初始化Kryo！");
            Kryo kryo = new Kryo() {
                @Override
                @SuppressWarnings("unchecked")
                public com.esotericsoftware.kryo.Serializer<?> getDefaultSerializer(@SuppressWarnings("rawtypes") Class type) {
                    if (type == null) {
                        throw new IllegalArgumentException("type cannot be null.");
                    }

                    if (!type.isArray()) {
                        boolean checkZeroArgConstructor = false;
                        try {
                            type.getDeclaredConstructor();
                            checkZeroArgConstructor = true;
                        } catch (NoSuchMethodException e) {
                        }
                        if (!checkZeroArgConstructor) {
                            LOGGER.warn("{} has no zero-arg constructor and this will affect the serialization performance",
                                    type.getName());
                            return new JavaSerializer();
                        }
                    }
                    return super.getDefaultSerializer(type);
                };
            };

            kryo.setRegistrationRequired(REGISTRATION_REQUIRED);

            kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
            kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
            kryo.register(InvocationHandler.class, new JdkProxySerializer());
            kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
            kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
            kryo.register(Pattern.class, new RegexSerializer());
            kryo.register(BitSet.class, new BitSetSerializer());
            kryo.register(URI.class, new URISerializer());
            kryo.register(UUID.class, new UUIDSerializer());
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);

            // now just added some very common classes
            // optimization
            kryo.register(HashMap.class);
            kryo.register(ArrayList.class);
            kryo.register(LinkedList.class);
            kryo.register(HashSet.class);
            kryo.register(TreeSet.class);
            kryo.register(Hashtable.class);
            kryo.register(Date.class);
            kryo.register(Calendar.class);
            kryo.register(ConcurrentHashMap.class);
            kryo.register(SimpleDateFormat.class);
            kryo.register(GregorianCalendar.class);
            kryo.register(Vector.class);
            kryo.register(BitSet.class);
            kryo.register(StringBuffer.class);
            kryo.register(StringBuilder.class);
            kryo.register(Object.class);
            kryo.register(Object[].class);
            kryo.register(String[].class);
            kryo.register(byte[].class);
            kryo.register(char[].class);
            kryo.register(int[].class);
            kryo.register(float[].class);
            kryo.register(double[].class);

            return kryo;
        };
    };


    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
        Output out = new Output(baos);
        try {
            try {
                KRYO.get().writeClassAndObject(out, object);
            } finally {
                out.close();// 先关闭
            }
            return baos.toByteArray();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
    }


    /**
     * Deserialize the byte array into an object.
     *
     * @param bytes
     *            a serialized object
     * @return the result of deserializing the bytes
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            Input in = new Input(bis);
            try {
                return (T) KRYO.get().readClassAndObject(in);
            } finally {
                in.close();
            }
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
        }
    }
}
