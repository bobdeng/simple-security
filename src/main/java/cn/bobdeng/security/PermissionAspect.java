package cn.bobdeng.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;

@Slf4j
public class PermissionAspect {
    private final PermissionRepository permissionRepository;

    public PermissionAspect(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Before("@annotation(permission)")
    public void before(Permission permission) {
        if (permissionRepository.hasPermission(permission.value())) {
            return;
        }
        throw new PermissionDeniedException();
    }

}
