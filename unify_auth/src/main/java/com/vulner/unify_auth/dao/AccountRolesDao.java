package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.dto.AccountRoleDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AccountRolesDao {
    @Select("SELECT\n" +
            "\tar.account_uuid,\n" +
            "\tr.uuid AS role_uuid,\n" +
            "\tr.name AS role_name\n" +
            "FROM account_roles ar\n" +
            "LEFT JOIN roles r ON ar.role_uuid=r.uuid\n" +
            "WHERE ar.account_uuid=#{accountUuid};")
    List<AccountRoleDto> getAccountRoles(String accountUuid);
}
