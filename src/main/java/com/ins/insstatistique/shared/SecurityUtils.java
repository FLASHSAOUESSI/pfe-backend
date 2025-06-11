package com.ins.insstatistique.shared;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {


    public static  String getUserId() {
        var test = SecurityContextHolder.getContext().getAuthentication();
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
