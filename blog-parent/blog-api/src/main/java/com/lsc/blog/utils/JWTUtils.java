package com.lsc.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    // JWT：A.B.C
    private static final String jwtToken = "123456lsc!@#$$";    // jwtToken即密钥（C部分）

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);    // 把userId放进map（B部分） claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，HS256（A部分）jwtToken即密钥（C部分）
                .setClaims(claims) // body数据，要唯一，自行设置 （B部分）
                .setIssuedAt(new Date()) // 设置签发时间 （应该是B部分）
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));// 60天的有效时间，单位为毫秒 （应该是B部分）
        String token = jwtBuilder.compact();
        return token;
    }

    // 解析token（Jwts.parser解析，getBody获取userId等信息，即获取B部分信息）
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 本地运行main方法验证checkToken方法是否正确
    public static void main(String[] args) {
        String token = JWTUtils.createToken(100L);
        System.out.println(token); // eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzU2MjY5MzAsInVzZXJJZCI6MTAwLCJpYXQiOjE2NzQ3Mzc4OTd9.Wt67g9adKUldjRSvq96rufHjxEZ-zV0icb3-CXLVzXk
        Map<String, Object> map = JWTUtils.checkToken(token);
        // 是否能打印出userId
        System.out.println(map.get("userId"));  // map.get方法：获取指定键所映射的值，即100
    }
}
