package com.vulner.unify_auth.dao;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/25
 * @since 1.0.0
 * @description 账号表的操作（Mybatis）
 */
@Component
public interface AccountsDao {
    /**
     * 通过账号名获取账号信息记录
     * @param accountName 账号名
     * @return 账号信息记录
     */
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
    AccountPo findByAccount(@Param("accountName") String accountName);

    /**
     * 通过账号 UUID 获取账号名
     * @param uuid 账号 UUID
     * @return 账号名
     */
    @Select("SELECT\n" +
            "\ta.`name`\n" +
            "FROM accounts a\n" +
            "WHERE a.uuid=#{uuid};")
    String getAccountNameByUuid(@Param("uuid") String uuid);

    /**
     * 通过账号名获取账号 UUID
     * @param name 账号名
     * @return 账号 UUID
     */
    @Select("SELECT\n" +
            "\ta.`uuid`\n" +
            "FROM accounts a\n" +
            "WHERE a.name=#{name};")
    String getAccountUuidByName(@Param("name") String name);

    /**
     * 更新密码参数
     * @param params 密码参数
     * @return 实际更新的记录数
     */
    @Update("UPDATE accounts a \n" +
            "SET\n" +
            "\ta.max_attempts=#{max_attempts},\n" +
            "\ta.attempts=#{attempts},\n" +
            "\ta.locked=#{locked}\n" +
            "WHERE\n" +
            "\ta.uuid=#{account_uuid};\n")
    int updatePasswdParams(PasswdParamsDto params);

    /**
     * 读取所有的账号记录
     * @return List<AccountPo> 所有的账号记录（列表）
     */
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

    /**
     * 更新账号个人信息
     * @param accountPo 包含账号个人信息的对象
     * @return 实际更新的记录数
     */
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

    /**
     * 新增一条账号记录
     * @param accountPo 包含账号记录的对象
     * @return 实际添加的记录数
     */
    @Insert("INSERT INTO accounts (\n" +
            "\t`uuid`,\n" +
            "\t`name`,\n" +
            "\t`alias`,\n" +
            "\t`password`,\n" +
            "\t`salt`,\n" +
            "\t`max_attempts`,\n" +
            "\t`attempts`,\n" +
            "\t`locked`,\n" +
            "\t`status`,\n" +
            "\t`email`,\n" +
            "\t`mobile`,\n" +
            "\t`gender`,\n" +
            "\t`birthday`,\n" +
            "\t`create_time`\n" +
            ")\n" +
            "VALUES(\n" +
            "\t#{uuid},\n" +
            "\t#{name},\n" +
            "\t#{alias},\n" +
            "\t#{password},\n" +
            "\t#{salt},\n" +
            "\t#{max_attempts},\n" +
            "\t#{attempts},\n" +
            "\t#{locked},\n" +
            "\t#{status},\n" +
            "\t#{email},\n" +
            "\t#{mobile},\n" +
            "\t#{gender},\n" +
            "\t#{birthday},\n" +
            "\t#{create_time}\n" +
            ")")
    int addAccount(AccountPo accountPo);

    /**
     * 删除指定 UUID 的账号
     * @param accountUuid 账号 UUID
     * @return 实际删除的记录数
     */
    @Delete("DELETE FROM accounts a \n" +
            "WHERE a.uuid=#{accountUuid}\n")
    int deleteAccount(@Param("accountUuid") String accountUuid);

    /**
     * 更新账号的状态值
     * @param accountUuid 指定账号的 UUID
     * @param status 新的状态值
     * @return 实际更新的记录数
     */
    @Update("UPDATE accounts a \n" +
            "SET\n" +
            "\ta.`status`=#{status}\n" +
            "WHERE\n" +
            "\ta.uuid=#{accountUuid}")
    int updateStatus(@Param("accountUuid") String accountUuid, @Param("status") int status);

    /**
     * 更新账号的密码
     * @param accountUuid 指定账号的 UUID
     * @param password 新密码
     * @return 实际更新的记录数
     */
    @Update("UPDATE accounts a \n" +
            "SET\n" +
            "\ta.`password`=#{password}\n" +
            "WHERE\n" +
            "\ta.uuid=#{accountUuid}")
    int updatePassword(@Param("accountUuid") String accountUuid, @Param("password") String password);
}
