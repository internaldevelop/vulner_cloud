package com.vulner.unify_auth.service.helper;

import com.vulner.common.bean.po.AccountPo;

public class AccountHelper {

    public static void clearAccountSecretInfo(AccountPo accountPo) {
        // 清空密码数据
        accountPo.setPassword("");
        accountPo.setSalt("");
    }
}
