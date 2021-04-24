package cn.bobdeng.security;

public interface PermissionRepository {
    boolean hasPermission(String[] roles);
}
