package cn.bobdeng.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RolePermissionAspectTest {
    @Mock
    RolePermissionRepository rolePermissionRepository;

    @Test
    public void 当有角色() throws Exception {
        when(rolePermissionRepository.hasAnyRole(new String[]{"admin"})).thenReturn(true);
        RolePermissionAspect rolePermissionAspect = new RolePermissionAspect(rolePermissionRepository);
        RolePermission permission = new RolePermission() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] value() {
                return new String[]{"admin"};
            }
        };
        try {
            rolePermissionAspect.before(permission);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test(expected = PermissionDeniedException.class)
    public void 当没有有角色() throws Exception {
        when(rolePermissionRepository.hasAnyRole(new String[]{"admin"})).thenReturn(false);
        RolePermissionAspect rolePermissionAspect = new RolePermissionAspect(rolePermissionRepository);
        RolePermission permission = new RolePermission() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] value() {
                return new String[]{"admin"};
            }
        };
        rolePermissionAspect.before(permission);
    }
}
