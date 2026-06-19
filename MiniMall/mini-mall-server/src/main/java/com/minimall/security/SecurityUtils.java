package com.minimall.security;

import com.minimall.common.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BusinessException(401, "未登录");
        }
        Object principal = auth.getPrincipal();
        if (!(principal instanceof Integer)) {
            throw new BusinessException(401, "未登录");
        }
        return (Integer) principal;
    }
}
