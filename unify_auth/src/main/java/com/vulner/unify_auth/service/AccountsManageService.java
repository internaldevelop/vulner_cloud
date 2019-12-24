package com.vulner.unify_auth.service;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.dao.AccountsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountsManageService {
    @Autowired
    private AccountsDao accountsDao;

    public ResponseBean getAllAccounts() {
        List<AccountPo> accountsList = accountsDao.fetchAllAccounts();
        if (accountsList == null || accountsList.size() == 0) {
            return ResponseHelper.error("ERROR_NONE_ACCOUNTS");
        } else {
            return ResponseHelper.success(accountsList);
        }
    }
}
