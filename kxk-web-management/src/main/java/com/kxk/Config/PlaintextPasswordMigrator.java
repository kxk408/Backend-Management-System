package com.kxk.Config;

import com.kxk.mapper.EmpMapper;
import com.kxk.pojo.Emp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 一次性将 emp.password 中的明文迁移为 BCrypt。
 * 通过 kxk.security.migrate-plaintext-password 开关控制，迁移后请关闭。
 */
@Slf4j
@Component
public class PlaintextPasswordMigrator implements ApplicationRunner {

    @Value("${kxk.security.migrate-plaintext-password:false}")
    private boolean migrateEnabled;

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!migrateEnabled) {
            return;
        }
        List<Emp> emps = empMapper.listIdUsernamePassword();
        int migrated = 0;
        for (Emp emp : emps) {
            String pwd = emp.getPassword();
            if (!StringUtils.hasText(pwd) || isBcryptHash(pwd)) {
                continue;
            }
            empMapper.updatePasswordById(emp.getId(), passwordEncoder.encode(pwd));
            migrated++;
            log.info("已将员工 [{}] 的明文密码迁移为 BCrypt", emp.getUsername());
        }
        log.warn("明文密码迁移完成，共 {} 条。请将 kxk.security.migrate-plaintext-password 改为 false", migrated);
    }

    private boolean isBcryptHash(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}
