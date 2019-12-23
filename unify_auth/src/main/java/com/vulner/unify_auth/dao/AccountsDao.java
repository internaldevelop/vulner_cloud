package com.vulner.unify_auth.dao;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface AccountsDao {
    @Select("SELECT\n" +
            "\ta.uuid,\n" +
            "\ta.account,\n" +
            "\ta.`name`,\n" +
            "\ta.`password`,\n" +
            "\ta.`salt`,\n" +
            "\ta.`max_attempts`,\n" +
            "\ta.`attempts`,\n" +
            "\ta.`locked`,\n" +
            "\ta.`status`,\n" +
            "\ta.email,\n" +
            "\ta.mobile,\n" +
            "\ta.sex,\n" +
            "\ta.birthday,\n" +
            "\ta.create_time\n" +
            "FROM accounts a\n" +
            "WHERE a.account=#{account};")
    AccountPo findByAccount(String account);

    @Update("UPDATE accounts a \n" +
            "SET\n" +
            "\ta.max_attempts=#{max_attempts},\n" +
            "\ta.attempts=#{attempts},\n" +
            "\ta.locked=#{locked}\n" +
            "WHERE\n" +
            "\ta.uuid=#{account_uuid};\n")
    int updatePasswdParams(PasswdParamsDto params);
}
