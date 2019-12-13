package com.vulner.unify_auth.service;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.unify_auth.bean.dto.AccountRoleDto;
import com.vulner.unify_auth.bean.dto.RolePermissionDto;
import com.vulner.unify_auth.dao.AccountRolesDao;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.dao.RolePermissionsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailService")
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountsDao accountsDao;
    @Autowired
    private AccountRolesDao accountRolesDao;
    @Autowired
    private RolePermissionsDao rolePermissionsDao;

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        AccountPo accountPo = accountsDao.findByAccount(accountName);
        if (accountPo == null) {
            throw new UsernameNotFoundException(accountName);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // 可用性 :true:可用 false:不可用
        boolean enabled = true;
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;

        // 获取该账户的所有角色
        List<AccountRoleDto> rolesList = accountRolesDao.getAccountRoles(accountPo.getUuid());
        for (AccountRoleDto role : rolesList) {
            // 角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole_name());
            grantedAuthorities.add(grantedAuthority);
            // 获取当前角色的所有权限
            List<RolePermissionDto> permissionsList = rolePermissionsDao.getPermissions(role.getRole_uuid());
            for (RolePermissionDto permission : permissionsList) {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getPermission_uri());
                grantedAuthorities.add(authority);
            }
        }
        User user = new User(accountPo.getAccount(), accountPo.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return user;
    }

}
