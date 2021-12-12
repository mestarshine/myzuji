package com.myzuji.study.java.auth;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 二进制权限控制
 *
 * @author shine
 * @date 2020/02/01
 */
public class Auth {

    /**
     * key:menuid, value:菜单名称
     */
    private static final Map<String, String> menu = new HashMap<String, String>();
    /**
     * key:userid, value:操作权限
     */
    private static final Map<String, String> user = new HashMap<String, String>();

    static {
        menu.put("1", "用户管理");
        menu.put("106", "数据字典");
        menu.put("1200", "订单管理");
        user.put("zhangsan", "1");
        user.put("lisi", "0");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        authorize("zhangsan", "1");
        authorize("zhangsan", "106");
        authorize("zhangsan", "1200");
        System.out.println("===================================");
        checkAll("zhangsan");
        checkAll("lisi");
        System.out.println("===================================");
        revoke("zhangsan", "1");
        checkAll("zhangsan");
        System.out.println("===================================");
        revoke("lisi", "1");
        checkAll("lisi");
    }

    /**
     * 赋权限
     */
    private static void authorize(String userid, String menuid) {
        //存的是36进制
        String rights = user.get(userid);
        BigInteger b36 = new BigInteger(rights, 36);
        b36 = b36.setBit(Integer.parseInt(menuid));
        System.out.println(b36.toString(36));
        user.put(userid, b36.toString(36));
        System.out.println("用户[" + userid + "]被赋予了[" + menu.get(menuid) + "]的访问权限");
    }

    /**
     * 回收权限
     */
    private static void revoke(String userid, String menuid) {
        //存的是36进制
        String rights = user.get(userid);
        BigInteger b36 = new BigInteger(rights, 36);
        b36 = b36.clearBit(Integer.parseInt(menuid));
        user.put(userid, b36.toString(36));
        System.out.println("用户[" + userid + "]被收回了[" + menu.get(menuid) + "]的访问权限");
    }

    /**
     * 检查用户是否有某一个菜单的访问权限
     */
    private static void check(String userid, String menuid) {
        //存的是36进制
        String rights = user.get(userid);
        BigInteger b36 = new BigInteger(rights, 36);
        boolean flag = b36.testBit(Integer.parseInt(menuid));
        System.out.println("用户[" + userid + "]" + (flag ? "有" : "没有") + "访问[" + menu.get(menuid) + "]权限");
    }

    /**
     * 查询用户所有的访问权限
     */
    private static void checkAll(String userid) {
        for (Iterator<String> it = menu.keySet().iterator(); it.hasNext(); ) {
            check(userid, it.next());
        }
    }
}
