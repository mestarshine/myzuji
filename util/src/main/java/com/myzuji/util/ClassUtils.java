package com.myzuji.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * 类反射工具类
 */
public class ClassUtils {

    /**
     * 系统日志输出句柄
     */
    protected static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 在此包内的类型，在日志输出时需要深入
     */
    private static List<String> packageList = new ArrayList<String>();

    static {
        packageList.add("com.ecpss.");
    }

    /**
     * 取得指定包下的所有类集合
     *
     * @param packageName 包名称（例如：com.shengpay)
     *                    类列表
     * @return
     */
    public static Set<Class<?>> getClassSetByPackageName(String packageName) {
        packageName = packageName.replace('.', '/');
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        String fileType = ".class";
        Set<String> classSetByPackageName = getClassSetByPackageName(packageName, fileType);
        for (String string : classSetByPackageName) {
            String classFullName = string.replace('/', '.').replace('\\', '.').substring(0, string.length() - fileType.length());
            try {
                classSet.add(Class.forName(classFullName));
            } catch (Throwable e) {
                // logger.info("取得【" + classFullName + "】的类型信息时发生异常！", e);
            }
        }
        return classSet;
    }

    /**
     * 取得指定包下的所有类集合
     *
     * @param packageName 包名称（例如：com.shengpay)
     *                    类列表
     * @return
     */
    public static Set<Class<?>> getClassSetByPackageName(String packageName, Class<? extends Annotation> atn) {
        packageName = packageName.replace('.', '/');
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        String fileType = ".class";
        Set<String> classSetByPackageName = getClassSetByPackageName(packageName, fileType);
        for (String string : classSetByPackageName) {
            String classFullName = string.replace('/', '.').replace('\\', '.').substring(0, string.length() - fileType.length());
            try {
                Class<?> class1 = Class.forName(classFullName);
                if (class1.getAnnotation(atn) != null) {
                    classSet.add(class1);
                }
            } catch (Throwable e) {
                // logger.debug("取得【" + classFullName + "】的类型信息时发生异常！", e);
            }
        }
        return classSet;
    }

    /**
     * 取得类路径下指定文件夹下指定类型名的文件列表
     *
     * @param dirPath  文件夹名称（例如：com/shengpay)
     * @param fileType 类型名称（例如：.class 或者 .properties)
     * @return 符合要求的文件路面集合(例如 ： \ com \ shengpay \ commons \ core \ propertiesfile \
     *PropertiesFileHandlerImplForSystemFile.class ）
     */
    public static Set<String> getClassSetByPackageName(String dirPath, String fileType) {
        // 取得包含有指定包的所有URL集合
        Enumeration<URL> resources;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources(dirPath.replace('\\', '/'));
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }

        // 分别从文件夹或JAR中取得类集合
        Set<String> classSet = new HashSet<String>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            logger.info("从路径【" + url + "】加载类信息！");
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                try {
                    getClassSetForJar(dirPath, url, classSet, fileType);
                } catch (Exception e) {
                    throw new RuntimeException("", e);
                }
            } else if ("file".equals(protocol)) {
                String filePath;
                try {
                    filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("", e);
                }
                findAndAddClassesInPackageByFile(dirPath, filePath, classSet, fileType);
            }
        }
        return classSet;
    }

    /**
     * @param packageName
     * @param url
     * @param classSet
     * @param fileType
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void getClassSetForJar(String packageName, URL url, Set<String> classSet, String fileType) throws IOException, ClassNotFoundException {
        JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String jarEntryName = jarEntry.getName();
            if (jarEntry.isDirectory()) {
                continue;
            }
            if (!jarEntryName.startsWith(packageName)) {
                continue;
            }
            if (!jarEntryName.endsWith(fileType)) {
                continue;
            }
            classSet.add(jarEntryName);
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param classSet
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, Set<String> classSet, final String fileType) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (file.isDirectory()) || (file.getName().endsWith(fileType));
            }
        });

        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + '/' + file.getName(), file.getAbsolutePath(), classSet, fileType);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName();
                classSet.add(packageName + '/' + className);
            }
        }
    }

    /**
     * 返回指定方法的参数的KEY数组
     *
     * @param method
     * @return
     */
    public static String[] getMethodParamsKey(Method method) {
        StringBuffer methodFullName = new StringBuffer(getMethodFullName(method));

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            methodFullName.append((i == 0 ? "(" : ",") + parameterTypes[i].getName() + (i + 1 == parameterTypes.length ? ")" : ""));
        }

        String[] paramsKeyArr = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            paramsKeyArr[i] = methodFullName + ".param" + i + "(" + parameterTypes[i].getName() + ")";
        }
        return paramsKeyArr;
    }

    /**
     * 取得指定类的位置信息
     *
     * @param aclass
     * @return
     */
    public static URL getUrlByClass(Class<?> aclass) {
        if (aclass == null) {
            return null;
        }

        ProtectionDomain protectionDomain = aclass.getProtectionDomain();
        if (protectionDomain == null) {
            return null;
        }

        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            return null;
        }

        return codeSource.getLocation();
    }


    /**
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean inLogOutPackage(Object obj) {
        if (obj == null) {
            return false;
        }

        Class<? extends Object> class1 = obj.getClass();
        if (class1 == null) {
            return false;
        }

        Package package1 = class1.getPackage();
        if (package1 == null) {
            return false;
        }

        String string = package1.getName();
        for (String packageName : packageList) {
            if (string.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取方法调用信息
     *
     * @param methodName 方法名称
     * @param args       参数数组
     * @return
     */
    public static String getMethodCallInfo(String methodName, Object[] args) {
        StringBuffer callInfo = new StringBuffer();
        callInfo.append(methodName + "(");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                callInfo.append(i > 0 ? "," : "");
                callInfo.append(String.valueOf(args[i]));
            }
        }
        callInfo.append(")");
        return callInfo.toString();
    }

    /**
     * 获取方法签名信息(例如:test(java.lang.String,java.lang.Long))
     *
     * @param method
     * @return
     */
    public static String getMethodSign(Method method) {
        // 参数信息
        StringBuffer callInfo = new StringBuffer();
        callInfo.append(method.getName() + "(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            callInfo.append(i > 0 ? "," : "");
            callInfo.append(parameterTypes[i].getName());
        }
        callInfo.append(")");
        return callInfo.toString();
    }

    /**
     * 取得完整的方法签名
     *
     * @param method (例如:com.sdo.transbutton.common.proxyfactroy.JmsClientProxy.
     *               test (java.lang.String,java.lang.Long))
     * @return
     */
    public static String getMethodSignFull(Method method) {
        return method.getDeclaringClass().getName() + "." + getMethodSign(method);
    }

    /**
     * 取得指定类型所有方法的签名列表
     *
     * @param cla
     * @return
     */
    public static List<String> getMethodSignList(Class<?> cla) {
        Method[] methods = cla.getDeclaredMethods();
        List<String> methodSignList = new ArrayList<String>();
        for (Method method : methods) {
            methodSignList.add(getMethodSign(method));
        }
        return methodSignList;
    }

    /**
     * 取得指定类型所有方法对应接口列表的映射
     *
     * @param cla
     * @return<方法签名,接口列表>
     */
    public static Map<String, List<Class<?>>> getMethod2InterfaceMap(Class<?> cla) {
        Type[] genericInterfaces = cla.getGenericInterfaces();
        Map<String, List<Class<?>>> interfaceMethodSignMap = new HashMap<String, List<Class<?>>>();

        for (int i = 0; i < genericInterfaces.length; i++) {
            Class<?> gi = (Class<?>) genericInterfaces[i];
            Method[] dms = gi.getDeclaredMethods();
            for (Method method : dms) {
                String methodSign = getMethodSign(method);
                List<Class<?>> classList = interfaceMethodSignMap.get(methodSign);
                if (classList == null) {
                    classList = new ArrayList<Class<?>>();
                    interfaceMethodSignMap.put(methodSign, classList);
                }
                classList.add(gi);
            }
        }

        return interfaceMethodSignMap;
    }

    public static String getMethodSimpleName(Method method) {
        return getMethodSimpleName(null, method);
    }

    /**
     * 取得方法简称(所属类无包名)
     *
     * @param targetObj
     * @param method
     * @return
     */
    public static String getMethodSimpleName(Object targetObj, Method method) {
        if (targetObj != null) {
            String objectFullClassName = getObjectFullClassName(targetObj);
            return objectFullClassName.substring(objectFullClassName.lastIndexOf(".") + 1) + "." + method.getName();
        } else {
            return method.getDeclaringClass().getSimpleName() + "." + method.getName();// 所属类名
        }
    }

    /**
     * @param targetObj
     * @return
     */
    private static String getObjectFullClassName(Object targetObj) {
        String simpleName = targetObj.getClass().getSimpleName();
        String objStr = targetObj.toString();
        String regex_proxy = "\\$Proxy\\d+";
        int indexOf = objStr.indexOf("@");
        if (Pattern.matches(regex_proxy, simpleName) && indexOf != -1) {
            return objStr.substring(0, indexOf);
        } else {
            return simpleName;
        }

    }

    /**
     * 取得一个方法的完整名称
     *
     * @param method
     * @return
     */
    public static String getMethodFullName(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    /**
     * 获取一个属性的get方法的名称
     *
     * @param propertyName
     * @return
     */
    public static String getGetMethodName(String propertyName) {
        // 验证参数合法性
        if (StringUtils.isBlank(propertyName)) {
            throw new RuntimeException("无法取得属性[" + propertyName + "]对应的get方法的名称!");
        }

        // 取得首支付大写形式
        String firstChar = String.valueOf(propertyName.charAt(0)).toUpperCase();
        return "get" + firstChar + propertyName.substring(1);
    }

    /**
     * 获取一个属性的get方法的名称
     *
     * @param propertyName
     * @return
     */
    public static String getSetMethodName(String propertyName) {
        // 验证参数合法性
        if (StringUtils.isBlank(propertyName)) {
            throw new RuntimeException("无法取得属性[" + propertyName + "]对应的get方法的名称!");
        }

        // 取得首支付大写形式
        String firstChar = String.valueOf(propertyName.charAt(0)).toUpperCase();
        return "set" + firstChar + propertyName.substring(1);
    }

    /**
     * 取得指定对象指定域的值
     *
     * @param obj
     * @param aField
     * @return
     */
    public static Object getFieldValue(Object obj, Field aField) {
        // 若当前域为常量域,则返回空信息
        boolean isConstantsField = isConstantsField(aField);
        if (isConstantsField) {
            return null;
        }

        // 获取域属性名和属性值
        Object fieldValue = null;
        try {
            aField.setAccessible(true);
            fieldValue = aField.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("取得对象【" + obj + "】的域【" + aField + "】的值时发生异常：", e);
        }
        return fieldValue;
    }

    /**
     * 取得指定对象的指定域的值
     *
     * @param jp
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getDeclaredFieldValue(Object jp, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> class1 = jp.getClass();
        Field aField = class1.getDeclaredField(fieldName);
        aField.setAccessible(true);
        Object fieldValue = aField.get(jp);
        return fieldValue;
    }

    /**
     * 判断一个域是否为常量域
     *
     * @param aField
     * @return
     */
    public static boolean isConstantsField(Field aField) {
        return isStaticField(aField) && isFinalField(aField);
    }

    /**
     * 判断一个域是否为Final域
     *
     * @param aField
     * @return
     */
    public static boolean isFinalField(Field aField) {
        int modifiers2 = aField.getModifiers();
        boolean isFinalField = Modifier.isFinal(modifiers2);
        return isFinalField;
    }

    /**
     * 判断一个域是否为static域
     *
     * @param aField
     * @return
     */
    public static boolean isStaticField(Field aField) {
        int modifiers = aField.getModifiers();
        boolean isStaticField = Modifier.isStatic(modifiers);
        return isStaticField;
    }

    /**
     * 获取域的类型的泛型的类型
     *
     * @param field
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public static Class<?> getFieldActualType(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            throw new RuntimeException("域【" + field + "】的类型不支持泛型！");
        }

        Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
        if (types.length > 1) {
            throw new RuntimeException("域【" + field + "】的类型具有多个泛型信息！");
        }

        return (Class<?>) types[0];
    }

    /**
     * 将Object值设置到特定对象的特定属性中
     *
     * @param aimObj
     * @param field
     * @param filedValue
     */
    public static void setObjectValueToField(Object aimObj, Field field, Object filedValue) {
        try {
            if (filedValue != null) {
                field.setAccessible(true);
                field.set(aimObj, filedValue);
            }
        } catch (Exception e) {
            throw new RuntimeException("将属性[" + field.getName() + "]的值[" + filedValue + "]设置到对象[" + aimObj + "]时发生异常", e);
        }
    }

    /**
     * 使用指定类加载器克隆一个目标对象
     *
     * @param srcObj
     * @param targetClass
     * @return
     */
    public static Object cloneObjectByClassLoad(Object srcObj, Class<?> targetClass) {
        try {
            Object newInstance = targetClass.newInstance();
            copyValue(srcObj, newInstance);
            logger.debug("将对象【" + srcObj + "(来自[" + getUrlByClass(srcObj.getClass()) + "])" + "】转化成了对象【" + newInstance + "(来自[" + getUrlByClass(newInstance.getClass()) + "])" + "】不相符，进行了类型转换！");
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    public static Object newInstance(Class<?> targetClass) {
        try {
            return newAInstance(targetClass);
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    public static Object newAInstance(Class<?> targetClass) throws Exception {
        try {
            Field module = targetClass.getDeclaredField("MODULE$");
            module.setAccessible(true);
            return module.get(null);
        } catch (Exception e) {
        }

        // 默认构造函数不可用的情况下,寻找单例方法创建实例
        try {
            Method declaredMethod = targetClass.getDeclaredMethod("getInstance");
            if (Modifier.isStatic(declaredMethod.getModifiers())) {
                return declaredMethod.invoke(targetClass);
            }
        } catch (Exception e) {
        }

        // 有默认构造函数的情况下，优先使用默认构造函数创建实例
        try {
            Constructor<?> defaultConstructor = targetClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * 拷贝对象信息
     *
     * @param orig 源对象
     * @param dest 目标对象
     */
    public static void copyValue(Object orig, Object dest) {
        // 判断源对象为空的情况
        if (orig == null) {
            return;
        }

        Field[] origFields = orig.getClass().getDeclaredFields();
        Class<? extends Object> destClass = dest.getClass();
        for (Field field : origFields) {
            if (isFinalField(field) || isStaticField(field)) {
                continue;
            }

            // 取得源对象域值
            field.setAccessible(true);
            Object origValue = null;
            try {
                origValue = field.get(orig);
            } catch (Exception e) {
                throw new RuntimeException("从对象【" + orig + "】中取得域【" + field + "】的值时发生异常", e);
            }

            // 取得目标对象域信息
            Field destField;
            try {
                destField = destClass.getDeclaredField(field.getName());
            } catch (Exception e1) {
                throw new RuntimeException("", e1);
            }

            // 判断目标对象是否存在对应域
            if (destField == null) {
                throw new RuntimeException("未能找到对象【" + dest + "】的【" + field.getName() + "】域！");
            }

            // 对目标对象域进行设置值
            destField.setAccessible(true);
            try {
                destField.set(dest, origValue);
            } catch (Exception e) {
                throw new RuntimeException("向对象【" + dest + "】的【" + destField + "】域赋值【" + origValue + "】时发生异常", e);
            }
        }
    }


    /**
     * 取得指定对象指定方法的相同方法引用(主要用于获取接口方法在指定对象所属类型的实现方法引用)
     *
     * @param obj
     * @param interfaceMethod
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static Method getMethod(Object obj, Method interfaceMethod) {
        try {
            return obj.getClass().getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    /**
     * 取得对象的类型信息
     *
     * @param obj
     * @return
     */
    public static String getObjType(Object obj) {
        if (obj == null) {
            return CLASS_NAME_NULL;
        }
        return obj.getClass().getName();
    }

    /**
     * 类型信息:null
     */
    public static final String CLASS_NAME_NULL = "null";

    /**
     * 判断两个类直接的父子关系
     *
     * @param subClass
     * @param supClass
     * @return
     */
    public static boolean asSubclass(Class<?> subClass, Class<?> supClass) {
        return supClass.isAssignableFrom(subClass);
    }

    /**
     * 取得对象的类型信息
     *
     * @param obj
     * @return
     */
    public static Class<?> getClass(Object obj) {
        if (obj == null) {
            return null;
        }

        return obj.getClass();
    }


    /**
     * 获取类的泛型类型
     *
     * @param ownerClass
     * @param idx
     * @return
     */
    public static Class<?> getGenericType(Class<?> ownerClass, int idx) {
        Type genericSuperclass = ownerClass.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            throw new RuntimeException("宿主类【" + ownerClass + "】的父类型【" + genericSuperclass + "】不是泛型类型！");
        }
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (idx > actualTypeArguments.length) {
            throw new RuntimeException("指定索引【" + idx + "】大于泛型数量【" + actualTypeArguments.length + "】");
        }
        return (Class<?>) actualTypeArguments[idx];
    }

    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            return true;
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    public static Class getArrayOrigClass(Class dtoClass) {
        if (!dtoClass.isArray()) {
            return dtoClass;
        }

        String name = dtoClass.getName();
        String replace = name.replace("[", "").replace(";", "");
        String origClassName = replace;
        if (replace.startsWith("L")) {
            origClassName = replace.substring(1);
        }
        try {
            return Class.forName(origClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("为能找到类型【" + origClassName + "】！");
        }
    }

}
