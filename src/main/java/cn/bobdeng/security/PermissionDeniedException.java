package cn.bobdeng.security;

public class PermissionDeniedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "permission denied";
    }
}
