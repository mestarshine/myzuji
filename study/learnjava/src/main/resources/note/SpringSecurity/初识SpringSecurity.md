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

### 源码解析：

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

## 3.2 `@EnableGlobalAuthentication`

`@EnableGlobalAuthentication` 是包含在了 `@EnableWebSecurity` 注解中的，作用通过导入认证管理器 `AuthenticationManager`
来启用全局认证机制。

### 用法

```java
//用法一
@Configuration
@EnableGlobalAuthentication
public class MyGlobalAuthenticationConfiguration {

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
}

//用法二
@Configuration
@EnableWebSecurity
public class MyWebSecurityConfiguration {

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

### 源码解析：

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AuthenticationConfiguration.class)
public @interface EnableGlobalAuthentication {
}
```

`EnableGlobalAuthentication` 的核心逻辑就在 `AuthenticationConfiguration`，`AuthenticationConfiguration`
在 `Spring Security` 扮演着非常重要的作用，它内部包含了 `AuthenticationManager` 用于核心的认证工作。后面将会重点讲解该类。

## 3.3 `@EnableMethodSecurity`

在5.6版本之前一直使用 `@EnableGlobalMethodSecurity` 注解来配置方法安全的授权，之后引入了 `@EnableMethodSecurity`
注解，而其默认是开启的。
几个属性值：

1. `prePostEnabled` 默认为true，使用表达式的方式类计算方法的安全性
    * @PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
    * @PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
    * @PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
    * @PreFilter 允许方法调用,但必须在进入方法之前过滤输入值

# `Spring Security` 核心组件

## `SecurityContextHolder`

`SecurityContextHolder` 持有安全上下文（`securityContext`）的信息，可以通过 `SecurityContextHolder.getContext()` 的静态方法获取。
当前操作的用户是谁，该用户是否已经被认证，他拥有哪些角色权等等，这些都被保存在 `SecurityContextHolder` 中。
`SecurityContextHolder` 默认使用 `ThreadLocalSecurityContextHolderStrategy` 策略来存储认证信息。看到ThreadLocal
也就意味着，这是一种与线程绑定的策略。在web环境下，`Spring Security` 在用户登录时自动绑定认证信息到当前线程，在用户退出时，自动清除当前线程的认证信息。
`SecurityContextHolderFilter` 负载使用 `SecurityContextRepository` 在请求之间加载 `SecurityContext`。

`SecurityContextHolder` 可以设置指定JVM策略（`SecurityContext` 的存储策略），有三种

* `MODE_THREADLOCAL`：SecurityContext 存储在线程中。
* `MODE_INHERITABLETHREADLOCAL`：SecurityContext 存储在线程中，但子线程可以获取到父线程中的 SecurityContext。
* `MODE_GLOBAL`：SecurityContext 在所有线程中都相同。

`SecurityContextHolder` 默认使用 `MODE_THREADLOCAL` 模式，即存储在当前线程中。

## `SecurityContext`

安全上下文信息接口，用户通过 `Spring Security` 的效验后，验证信息存储在 `SecurityContext` 中。
指定义了两个方法，实际上其主要作用就是获取 `Authentication` 对象，如果用户未鉴权，那 `Authentication` 对象将会是空的。
该示例可以通过 `SecurityContextHolder.getContext()` 静态方法可获取到 `SecurityContext`。

## `Authentication`

`Authentication` 是一个接口直接继承 `Principal`，而 `Principal` 位于 `java.security` 包中。由此可见，`Authentication` 在
Spring 中是最高级别的身份/认证抽象。由这个顶级接口，可以获取到用户的 `Principal`（用户信息），`Credentials`（密码），`Authority`
（权限）等信息。 在用户登录认证之前相关信息会封装为一个 `Authentication` 具体实现类的对象，在登录认证成功之后又会生成一个信息更全面，
包含用户权限等信息的 `Authentication` 对象，然后把它保存在 `SecurityContextHolder` 所持有的 `SecurityContext` 中，
供后续的程序进行调用，如访问权限的鉴定等。

接口相关方法：

```java
public interface Authentication extends Principal, Serializable {

    // 用户权限信息（权限列表）默认是 GrantedAuthority 接口的一些实现类 ,通常是代表权限的字符串列表
    Collection<? extends GrantedAuthority> getAuthorities();

    // 密码信息，用户输入的密码字符串，在认证过后通常会被移除，用于保障安全
    Object getCredentials();

    // 细节信息，web应用中的实现接口通常为 WebAuthenticationDetails，它记录了访问者的ip地址和sessionId的值。
    Object getDetails();

    // 最重要的身份信息，在未认证的情况下获取到的是用户名，在已认证的情况下获取到的是 UserDetails (UserDetails也是一个接口，里边的方法有getUsername,getPassword等)，
    // 大部分情况下返回的是UserDetails接口的实现类，也是框架中的常用接口之一。
    Object getPrincipal();

    // 获取当前 Authentication 是否已认证
    boolean isAuthenticated();

    // 设置当前 Authentication 是否已认证（true or false
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

}
```

## `GrantedAuthority`

`Authenticatio.getAuthorities()` 方法返回一个 `GrantedAuthority` 对象数组。
该接口表示了当前用户所拥有的权限（或者角色）信息。用于配置 Web 授权、方法授权、域对象授权等，
该属性通常由 `UserDetailsService` 加载给 `UserDetails`。 这些信息有授权负责对象 `AccessDecisionManager` 来使用，
并决定最终用户是否可以访问某资源（URL或方法调用或域对象）。鉴权时并不会使用到该对象。如果一个用户有几千个这种权限，内存消耗将会非常巨大。

## `UserDetails`

`UserDetails` 存储的就是用户信息，这个接口规范了用户详细信息所拥有的字段，譬如用户名、密码、账号是否过期、是否锁定等。
在 `Spring Security` 中，获取当前登录的用户的信息,一般情况是需要在这个接口上面进行扩展，用来对接自己系统的用户。

接口方法如下：

```java
public interface UserDetails extends Serializable {

    // 获取用户权限，本质上是用户角色信息
    Collection<? extends GrantedAuthority> getAuthorities();

    // 获取密码
    String getPassword();

    // 获取用户名
    String getUsername();

    // 账户是否过期
    boolean isAccountNonExpired();

    // 账户是否被锁定
    boolean isAccountNonLocked();

    // 密码是否过期
    boolean isCredentialsNonExpired();

    // 账户是否可用
    boolean isEnabled();

}
```

## `UserDetailsService`

这个接口只提供一个接口 `loadUserByUsername(String username)`，用来获取 `UserDetails`，它代表了最详细的用户信息，
这个接口涵盖了一些必要的用户信息字段，
一般情况我们都是通过扩展这个接口来显示获取我们的用户信息，然后组装成一个 `UserDetails` 并返回。
用户登录时传递的用户名和密码也是通过这里这查找出来的用户名和密码进行校验，但是真正的校验不在这里，
而是由 `AuthenticationManager` 以及 `AuthenticationProvider` 负责的，需要强调的是，如果用户不存在，不应返回NULL，而要抛出异常UsernameNotFoundException

与 `Authentication` 接口很类似，比如它们都拥有 `username`，`authorities`，区分他们也是本文的重点内容之一。
`Authentication.getCredentials()`与`UserDetails.getPassword()`需要被区分对待，前者是用户提交的密码凭证，后者是用户正确的密码，
认证器其实就是对这两者的比对。`Authentication.getAuthorities()`实际是由`UserDetails.getAuthorities()`传递而形成的。
`Authentication.getUserDetails()` 其中的 `UserDetails` 用户详细信息便是经过了 `AuthenticationProvider` 之后被填充的
`UserDetailsService` 和 `AuthenticationProvider` 两者的职责常常被人们搞混，`UserDetailsService`
只负责从特定的地方加载用户信息，可以是数据库、redis缓存、接口等。

## `AuthenticationManager`

`AuthenticationManager`是认证相关的核心接口，它的作用就是校验 `Authentication`
，如果验证失败会抛出 `AuthenticationException` 异常。
`AuthenticationException` 是一个抽象类，因此代码逻辑并不能实例化一个 `AuthenticationException` 异常并抛出，
实际上抛出的异常通常是其实现类，如 `DisabledException`,`LockedException`,`BadCredentialsException`
等。`BadCredentialsException`可能会比较常见，即密码错误的时候。

这个接口只有一个方法 `authenticate()`,方法运行后可能会有三种情况：

* 验证成功，返回一个带有用户信息的Authentication。
* 验证失败，抛出一个AuthenticationException异常。
* 无法判断，返回null。

`AuthenticationManager` 是认证相关的核心接口，也是发起认证的出发点，因为在实际需求中，我们可能会允许用户使用用户名+密码登录，
同时允许用户使用邮箱+密码， 手机号码+密码登录，甚至，可能允许用户使用指纹登录，所以说 `AuthenticationManager` 一般不直接认证，
`AuthenticationManager` 接口的常用实现类 `ProviderManager ` 内部会维护一个 `List<AuthenticationProvider>` 列表，存放多种认证方式，
实际上这是委托者模式的应用（Delegate）。也就是说，核心的认证入口始终只有一个：AuthenticationManager，
不同的认证方式：用户名+密码（`UsernamePasswordAuthenticationToken`），
邮箱+密码，手机号码+密码登录则对应了三个 `AuthenticationProvider`。其中有一个重要的实现类是 `ProviderManager`

## `ProviderManager`

`ProviderManager` 是 `AuthenticationManager` 最常见的实现，它也不自己处理验证，
而是将验证委托给其所配置的 `AuthenticationProvider` 列表，然后会依次调用每一个 `AuthenticationProvider` 进行认证，
或者通过简单地返回null来跳过验证。
如果所有实现都返回null，那么 `ProviderManager` 将抛出一个 `ProviderNotFoundException`
。这个过程中只要有一个 `AuthenticationProvider` 验证成功，
就不会再继续做更多验证，会直接以该认证结果作为 `ProviderManager` 的认证结果。

## `AuthenticationProvider`

`AuthenticationProvider` 接口提供了两个方法，一个是真正的认证，另一个是满足什么样的身份信息才进行如上认证。

Spring 提供了几种AuthenticationProvider的实现：

1. `AnonymousAuthenticationProvider` 匿名用户身份信息认证
2. `DaoAuthenticationProvider` 从数据库中读取用户信息验证身份
3. `JaasAuthenticationProvider`  从 `JASS` 登陆配置中获取用户信息验证身份
4. `JwtAuthenticationProvider` 基于JWT进行用户信息验证
5. `RememberMeAuthenticationProvider` 已存 cookie 中的用户信息身份验证
6. `RunAsImplAuthenticationProvider` 对身份已被管理器替换的用户进行验证
7. `TestingAuthenticationProvider` 单元测试时使用
