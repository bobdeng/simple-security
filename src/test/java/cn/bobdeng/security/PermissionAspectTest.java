package cn.bobdeng.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionAspectTest {
    @Mock
    PermissionRepository rolePermissionRepository;

    @Test
    public void 当有角色() throws Exception {
        when(rolePermissionRepository.hasPermission(new String[]{"admin"})).thenReturn(true);
        PermissionAspect rolePermissionAspect = new PermissionAspect(rolePermissionRepository);
        Permission permission = new Permission() {

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
        when(rolePermissionRepository.hasPermission(new String[]{"admin"})).thenReturn(false);
        PermissionAspect rolePermissionAspect = new PermissionAspect(rolePermissionRepository);
        Permission permission = new Permission() {

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
