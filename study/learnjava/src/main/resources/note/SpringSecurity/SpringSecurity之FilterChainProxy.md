# FilterChainProxy什么时候注入Spring容器中的

`FilterChainProxy`是 `Spring Security`框架的中的核心，它是一个过滤器。在`Spring Security`
中，有一个过滤器名字叫 `springSecurityFilterChain`，它的类型就是 `FilterChainProxy`。

在 `@EnableWebSecurity` 注解中，`@Import` 了一个关键类：`WebSecurityConfiguration`。

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({WebSecurityConfiguration.class, SpringWebMvcImportSelector.class, OAuth2ImportSelector.class,
    HttpSecurityConfiguration.class})
@EnableGlobalAuthentication
public @interface EnableWebSecurity {

    /**
     * Controls debugging support for Spring Security. Default is false.
     * @return if true, enables debug support with Spring Security
     */
    boolean debug() default false;

}
```

首先，WebSecurityConfiguration实现了ImportAware和BeanClassLoaderAware接口，分别实现了setImportMetadata()
和setBeanClassLoader()

```java

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration implements ImportAware, BeanClassLoaderAware {

    private WebSecurity webSecurity;

    private Boolean debugEnabled;

    private List<SecurityConfigurer<Filter, WebSecurity>> webSecurityConfigurers;

    private List<SecurityFilterChain> securityFilterChains = Collections.emptyList();

    private List<WebSecurityCustomizer> webSecurityCustomizers = Collections.emptyList();

    private ClassLoader beanClassLoader;

    @Autowired(required = false)
    private ObjectPostProcessor<Object> objectObjectPostProcessor;

    @Autowired(required = false)
    private HttpSecurity httpSecurity;

    @Bean
    public static DelegatingApplicationListener delegatingApplicationListener() {
        return new DelegatingApplicationListener();
    }

    @Bean
    @DependsOn(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
    public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
        return this.webSecurity.getExpressionHandler();
    }

    /**
     * FilterChainProxy 是在此注入到 spring 容器中
     * AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME的值是：springSecurityFilterChain
     * 所以springSecurityFilterChain()的作用就是向Spring容器中注入一个名为springSecurityChain的bean。
     * @return
     * @throws Exception
     */
    @Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
    public Filter springSecurityFilterChain() throws Exception {
        // 判断是否有 webSecurityConfigurers
        boolean hasFilterChain = !this.securityFilterChains.isEmpty();
        // 如果没有配置，则通过objectObjectPostProcessor来生成一个新的WebSecurityConfigurerAdapter
        if (!hasFilterChain) {
            this.webSecurity.addSecurityFilterChainBuilder(() -> {
                this.httpSecurity.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
                this.httpSecurity.formLogin(Customizer.withDefaults());
                this.httpSecurity.httpBasic(Customizer.withDefaults());
                return this.httpSecurity.build();
            });
        }
        for (SecurityFilterChain securityFilterChain : this.securityFilterChains) {
            this.webSecurity.addSecurityFilterChainBuilder(() -> securityFilterChain);
        }
        for (WebSecurityCustomizer customizer : this.webSecurityCustomizers) {
            customizer.customize(this.webSecurity);
        }
        return this.webSecurity.build();
    }

    @Bean
    @DependsOn(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
    public WebInvocationPrivilegeEvaluator privilegeEvaluator() {
        return this.webSecurity.getPrivilegeEvaluator();
    }

    @Autowired(required = false)
    public void setFilterChainProxySecurityConfigurer(ObjectPostProcessor<Object> objectPostProcessor,
                                                      ConfigurableListableBeanFactory beanFactory) throws Exception {
        this.webSecurity = objectPostProcessor.postProcess(new WebSecurity(objectPostProcessor));
        if (this.debugEnabled != null) {
            this.webSecurity.debug(this.debugEnabled);
        }
        List<SecurityConfigurer<Filter, WebSecurity>> webSecurityConfigurers = new AutowiredWebSecurityConfigurersIgnoreParents(
            beanFactory).getWebSecurityConfigurers();
        webSecurityConfigurers.sort(AnnotationAwareOrderComparator.INSTANCE);
        Integer previousOrder = null;
        Object previousConfig = null;
        for (SecurityConfigurer<Filter, WebSecurity> config : webSecurityConfigurers) {
            Integer order = AnnotationAwareOrderComparator.lookupOrder(config);
            if (previousOrder != null && previousOrder.equals(order)) {
                throw new IllegalStateException("@Order on WebSecurityConfigurers must be unique. Order of " + order
                    + " was already used on " + previousConfig + ", so it cannot be used on " + config + " too.");
            }
            previousOrder = order;
            previousConfig = config;
        }
        for (SecurityConfigurer<Filter, WebSecurity> webSecurityConfigurer : webSecurityConfigurers) {
            this.webSecurity.apply(webSecurityConfigurer);
        }
        this.webSecurityConfigurers = webSecurityConfigurers;
    }

    @Autowired(required = false)
    void setFilterChains(List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Autowired(required = false)
    void setWebSecurityCustomizers(List<WebSecurityCustomizer> webSecurityCustomizers) {
        this.webSecurityCustomizers = webSecurityCustomizers;
    }

    @Bean
    public static BeanFactoryPostProcessor conversionServicePostProcessor() {
        return new RsaKeyConversionServicePostProcessor();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {

        // 从注入的 importMetadata 中获取 @EnableWebSecurity 注解 map 值
        Map<String, Object> enableWebSecurityAttrMap = importMetadata
            .getAnnotationAttributes(EnableWebSecurity.class.getName());
        // 将 map 值封装成 AnnotationAttributes
        AnnotationAttributes enableWebSecurityAttrs = AnnotationAttributes.fromMap(enableWebSecurityAttrMap);
        // 从 AnnotationAttributes 中获取 debug 属性值
        this.debugEnabled = enableWebSecurityAttrs.getBoolean("debug");
        if (this.webSecurity != null) {
            // 将 debugEnabled 保存到 webSecurity 中
            this.webSecurity.debug(this.debugEnabled);
        }
    }

    // 注入类加载器 ClassLoader
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    private static class AnnotationAwareOrderComparator extends OrderComparator {

        private static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();

        @Override
        protected int getOrder(Object obj) {
            return lookupOrder(obj);
        }

        private static int lookupOrder(Object obj) {
            if (obj instanceof Ordered) {
                return ((Ordered) obj).getOrder();
            }
            if (obj != null) {
                Class<?> clazz = ((obj instanceof Class) ? (Class<?>) obj : obj.getClass());
                Order order = AnnotationUtils.findAnnotation(clazz, Order.class);
                if (order != null) {
                    return order.value();
                }
            }
            return Ordered.LOWEST_PRECEDENCE;
        }

    }

}

```

# FilterChainProxy什么时候注入Spring容器中的

# WebSecurityConfiguration类

# WebSecurity类

# AbstractConfiguredSecurityBuilder类

# SecurityConfigurer类

# doBuild()方法

# WebSecurity什么时候被创建的？
