package com.vulner.unify_auth.dao;

import com.vulner.common.bean.po.AccountPo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface AccountsDao {
    @Select("SELECT\n" +
            "\ta.uuid,\n" +
            "\ta.account,\n" +
            "\ta.`name`,\n" +
            "\ta.`password`,\n" +
            "\ta.email,\n" +
            "\ta.mobile,\n" +
            "\ta.sex,\n" +
            "\ta.birthday,\n" +
            "\ta.create_time\n" +
            "FROM accounts a\n" +
            "WHERE a.account=#{account};")
    AccountPo findByAccount(String account);
}
