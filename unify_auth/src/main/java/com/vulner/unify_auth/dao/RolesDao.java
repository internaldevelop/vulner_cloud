package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.dto.AccountRoleDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 角色表的操作（Mybatis）
 */
@Component
public interface RolesDao {
    @Select("SELECT\n" +
            "\tr.`uuid`\n" +
            "FROM roles r\n" +
            "WHERE r.name=#{name};")
    String getRoleUuidByName(String name);

}
