package com.dcits.comet.rpc.consumer.annotation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FeignClient
public @interface RpcClient {
    /**
     * The name of the service with optional protocol prefix. Synonym for {@link #name()
     * name}. A name must be specified for all clients, whether or not a url is provided.
     * Can be specified as property key, eg: ${propertyKey}.
     */
    @AliasFor(value ="name",annotation = FeignClient.class)
    String value() default "";

    /**
     * The service id with optional protocol prefix. Synonym for {@link #value() value}.
     *
     * @deprecated use {@link #name() name} instead
     */
    @Deprecated
    String serviceId() default "";

    /**
     * This will be used as the bean name instead of name if present, but will not be used as a service id.
     */
    @AliasFor(value ="contextId",annotation = FeignClient.class)
    String contextId() default "";

    /**
     * The service id with optional protocol prefix. Synonym for {@link #value() value}.
     */
    @AliasFor(value ="value",annotation = FeignClient.class)
    String name() default "";

    /**
     * Sets the <code>@Qualifier</code> value for the feign client.
     */
    @AliasFor(value ="qualifier",annotation = FeignClient.class)
    String qualifier() default "";

    /**
     * An absolute URL or resolvable hostname (the protocol is optional).
     */
    @AliasFor(value ="url",annotation = FeignClient.class)
    String url() default "";

    /**
     * Whether 404s should be decoded instead of throwing FeignExceptions
     */
    @AliasFor(value ="decode404",annotation = FeignClient.class)
    boolean decode404() default false;

    /**
     * A custom <code>@Configuration</code> for the feign client. Can contain override
     * <code>@Bean</code> definition for the pieces that make up the client, for instance
     * {@link feign.codec.Decoder}, {@link feign.codec.Encoder}, {@link feign.Contract}.
     *
     * @see FeignClientsConfiguration for the defaults
     */
    @AliasFor(value ="configuration",annotation = FeignClient.class)
    Class<?>[] configuration() default {};

    /**
     * Fallback class for the specified Feign client interface. The fallback class must
     * implement the interface annotated by this annotation and be a valid spring bean.
     */
    @AliasFor(value ="fallback",annotation = FeignClient.class)
    Class<?> fallback() default void.class;

    /**
     * Define a fallback factory for the specified Feign client interface. The fallback
     * factory must produce instances of fallback classes that implement the interface
     * annotated by {@link FeignClient}. The fallback factory must be a valid spring
     * bean.
     *
     * @see feign.hystrix.FallbackFactory for details.
     */
    @AliasFor(value ="fallbackFactory",annotation = FeignClient.class)
    Class<?> fallbackFactory() default void.class;

    /**
     * Path prefix to be used by all method-level mappings. Can be used with or without
     * <code>@RibbonClient</code>.
     */
    @AliasFor(value ="path",annotation = FeignClient.class)
    String path() default "";

    /**
     * Whether to mark the feign proxy as a primary bean. Defaults to true.
     */
    @AliasFor(value ="primary",annotation = FeignClient.class)
    boolean primary() default true;


}
