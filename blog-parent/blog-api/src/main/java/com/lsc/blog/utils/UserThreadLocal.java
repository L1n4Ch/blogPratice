package com.lsc.blog.utils;

import com.lsc.blog.dao.pojo.SysUser;

// UserThreadLocal工具类 本地线程保存用户信息 ThreadLocal用于线程变量隔离
public class UserThreadLocal {

    private UserThreadLocal(){}

    // 线程变量隔离
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }

}
