package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.ObjUtils;
import com.vulner.unify_auth.bean.dto.AccountPersonalInfoDto;
import com.vulner.unify_auth.dao.AccountsDao;
import org.springframework.beans.BeanUtils;
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

    public ResponseBean updateAccountPersonalInfo(AccountPersonalInfoDto personalInfo) {
        // 检查参数
        String[] paramsList = ObjUtils.getValuePropertyNames(personalInfo);
        String accountUuid = personalInfo.getUuid();
        // 参数全部为空，或只有uuid，或uuid为空，则返回参数缺失错误
        if (paramsList.length <= 1 || Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.error("ERROR_PARAMS_MISSING");
        }

        // 获取 AccountPo
        AccountPo accountPo = getAccountByUuid(accountUuid);
        if (accountPo == null) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST");
        }

        // 将 personalInfo 拷贝到 AccountPo 对象中
        BeanUtils.copyProperties(personalInfo, accountPo);

        // 更新 AccountPo 到数据库中
        int row = accountsDao.updateAccountPersonalInfo(accountPo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_UPDATE_FAILED");
        }

        return ResponseHelper.success();
    }

    public AccountPo getAccountByUuid(String uuid) {
        String accountName = accountsDao.getAccountNameByUuid(uuid);
        if (Strings.isNullOrEmpty(accountName)) {
            return null;
        }

        return accountsDao.findByAccount(accountName);
    }
}
