package cn.bobdeng.security;

public interface RolePermissionRepository {
    boolean hasAnyRole(String[] roles);
}
