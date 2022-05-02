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
