package com.vulner.unify_auth.dao;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AccountsDao {
    @Select("SELECT\n" +
            "\ta.uuid,\n" +
            "\ta.`name`,\n" +
            "\ta.`alias`,\n" +
            "\ta.`password`,\n" +
            "\ta.`salt`,\n" +
            "\ta.`max_attempts`,\n" +
            "\ta.`attempts`,\n" +
            "\ta.`locked`,\n" +
            "\ta.`status`,\n" +
            "\ta.email,\n" +
            "\ta.mobile,\n" +
            "\ta.gender,\n" +
            "\ta.birthday,\n" +
            "\ta.create_time\n" +
            "FROM accounts a\n" +
            "WHERE a.`name`=#{accountName};")
    AccountPo findByAccount(String accountName);

    @Select("SELECT\n" +
            "\ta.`name`\n" +
            "FROM accounts a\n" +
            "WHERE a.uuid=#{uuid};")
    String getAccountNameByUuid(String uuid);

    @Update("UPDATE accounts a \n" +
            "SET\n" +
            "\ta.max_attempts=#{max_attempts},\n" +
            "\ta.attempts=#{attempts},\n" +
            "\ta.locked=#{locked}\n" +
            "WHERE\n" +
            "\ta.uuid=#{account_uuid};\n")
    int updatePasswdParams(PasswdParamsDto params);

    @Select("SELECT\n" +
            "\ta.uuid,\n" +
            "\ta.`name`,\n" +
            "\ta.`alias`,\n" +
            "\ta.`max_attempts`,\n" +
            "\ta.`attempts`,\n" +
            "\ta.`locked`,\n" +
            "\ta.`status`,\n" +
            "\ta.email,\n" +
            "\ta.mobile,\n" +
            "\ta.gender,\n" +
            "\ta.birthday,\n" +
            "\ta.create_time\n" +
            "FROM accounts a\n")
    List<AccountPo> fetchAllAccounts();

    @Update("UPDATE accounts a \n" +
            "SET\n" +
            "\ta.`alias`=#{alias},\n" +
            "\ta.email=#{email},\n" +
            "\ta.mobile=#{mobile},\n" +
            "\ta.gender=#{gender},\n" +
            "\ta.birthday=#{birthday}\n" +
            "WHERE\n" +
            "\ta.uuid=#{uuid}")
    int updateAccountPersonalInfo(AccountPo accountPo);
}
