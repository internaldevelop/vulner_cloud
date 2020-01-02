package com.vulner.unify_auth.service.helper;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.unify_auth.dao.AccountsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountHelper {
    @Autowired
    private AccountsDao accountsDao;

    public static void clearAccountSecretInfo(AccountPo accountPo) {
        // 清空密码数据
        accountPo.setPassword("");
        accountPo.setSalt("");
    }
}
