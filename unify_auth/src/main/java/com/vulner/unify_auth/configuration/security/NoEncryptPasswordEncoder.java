package com.vulner.unify_auth.configuration.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码验证(无加密)
 *
 * @author Jason
 * @create 2019/12/13
 * @since 1.0.0
 */
public class NoEncryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return (String) charSequence;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals((String) charSequence);
    }
}
