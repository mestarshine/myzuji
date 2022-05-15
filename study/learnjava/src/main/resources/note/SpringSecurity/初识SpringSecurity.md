# 1. 初识 `Spring Security`

对于 `Spring Security`，通过官网中的介绍可了解到其大概功能：

1. `Spring Security` 是一个功能强大且高定制化的身份验证和访问控制框架，事实上就是一个保护基于Spring的应用框架安全性的标准。
2. `Spring Security` 是一个重点为Java应用程序提供身份验证和授权的框架。
3. 与所有 `Spring` 项目一样，`Spring Security`的真正强大之处在于它可以很容易地扩展以满足定制需求。

对于`Spring Security`的特性，可以总结为以下几点：

1. 对身份认证和授权提供全面和可扩展的支持。
2. 防止会话固定、劫持请求、跨站点请求伪造等攻击。
3. 与 `Servlet API` 的集成。
4. 与 `Spring Web MVC` 的可选集成。

在进行代码编写前，先对 `Spring Security` 有一个整体上的认识，等进行编写时才能知道对应代码的对应作用。

# 2. `Spring Security` 项目核心jar包介绍

在 `Spring Security` 继承的项目中，主要有四个核心的jar包：

- `spring-security-core.jar`
  `Spring Security` 的核心包，任何 `Spring Security` 的功能都需要此包。
- `spring-security-web.jar`
  `Web` 工程必备，包含过滤器和相关的 `Web` 安全基础结构代码。
- `spring-security-config.jar`
  用于解析 `xml` 配置文件，用到 `Spring Security` 的 `xml` 配置文件的就要用到此包。

由于 `spring-security-web.jar` 和 `spring-security-config.jar` 都依赖了 `spring-security-core.jar`
，所以只需要导入 `spring-security-web.jar` 和 `spring-security-config.jar` 即可。
而在`SpringBoot`中的`spring-boot-starter-security`中，就已经依赖了`spring-security-web.jar`
和`spring-security-config.jar`，`SpringBoot`已经定义好了`spring-boot-starter-security`
，那就直接使用`spring-boot-starter-security`吧，这样能够方便统一`SpringSecurity`的版本。

# 3. `Spring Security` 核心注解

使用 `SpringBoot` 集成 `Spring Security` 只需要配置几个文件，并且需要几个核心注解，基于 `Spring Security 6.0.x` 依次来看看这些注解。

## 3.1 `@EnableWebSecurity`

`@EnableWebSecurity` 是 `Spring Security` 用于启用 Web 安全的注解.

### 用法

```java

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            // Spring Security should completely ignore URLs starting with /resources/
            .requestMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().requestMatchers("/public/**").permitAll().anyRequest()
            .hasRole("USER").and()
            // Possibly more configuration ...
            .formLogin() // enable form based log in
            // set permitAll for all URLs associated with Form Login
            .permitAll();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .roles("ADMIN", "USER")
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    // Possibly more bean methods ...
}
```

源码解析：

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({WebSecurityConfiguration.class,
    SpringWebMvcImportSelector.class,
    OAuth2ImportSelector.class,
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

> 1. 通过注解属性 `debug` 控制 `Spring Security` 是否使用调试模式，默认为false，表示缺省不使用调试模式。
> 2. 导入 `WebSecurityConfiguration`，用于配置 Web 安全过滤器 `FilterChainProxy`
     ，并创建过滤器链 `springSecurityFilterChain` 来保护应用。
> 3. 如果是 `Servlet` 环境，导入 `WebMvcSecurityConfiguration`。
> 4. 如果是 `OAuth2` 环境（spring-security-oauth2-client），导入 `OAuth2ClientConfiguration`。
> 5. 导入 `HttpSecurityConfiguration`，用与配置默认的 `HttpSecurity` ，`HttpSecurity` 在 `spring`
     中使用的多例模式，用户使用通过 `HttpSecurity`可以使用他进行自定义鉴权配置
> 6. 使用注解 `@EnableGlobalAuthentication` 启用全局认证机制，即全局的 `AuthenticationManager`，`AuthenticationManager`
     会在运行时对请求着进行身份验证。
