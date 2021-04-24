package cn.bobdeng.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;

@Slf4j
public class RolePermissionAspect {
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionAspect(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Before("@annotation(permission)")
    public void before(RolePermission permission) {
        if (rolePermissionRepository.hasAnyRole(permission.value())) {
            return;
        }
        throw new PermissionDeniedException();
    }

}
