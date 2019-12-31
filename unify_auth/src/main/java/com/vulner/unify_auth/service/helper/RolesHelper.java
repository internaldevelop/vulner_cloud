package com.vulner.unify_auth.service.helper;

public class RolesHelper {

    public static String getDefaultRole() {
        return "ROLE_GUEST";
    }

    public static boolean validRoleName(String roleName) {
        // 角色名必须以"ROLE_"开头
        if (!roleName.startsWith("ROLE_")) {
            return false;
        }
        return true;
    }
}
