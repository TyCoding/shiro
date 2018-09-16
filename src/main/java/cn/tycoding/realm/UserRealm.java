package cn.tycoding.realm;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.User;
import cn.tycoding.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 权限校验
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限校验--执行了doGetAuthorizationInfo...");

        String username = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //注意这里的setRoles和setStringPermissions需要的都是一个Set<String>类型参数
        Set<String> role = new HashSet<String>();
        List<Role> roles = userService.findRoles(username);
        for (Role r : roles){
            role.add(r.getRole());
        }
        authorizationInfo.setRoles(role);
        Set<String> permission = new HashSet<String>();
        List<Permission> permissions = userService.findPermissions(username);
        for (Permission p : permissions){
            permission.add(p.getPermission());
        }
        authorizationInfo.setStringPermissions(permission);

        return authorizationInfo;
    }

    /**
     * 身份校验
     * @param token
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份校验--执行了goGetAuthenticationInfo...");

        String username = (String) token.getPrincipal();

        User user = userService.findByName(username);

        if (user == null) {
            throw new UnknownAccountException(); //没有找到账号
        }

        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //账号锁定
        }

        //交给AuthenticationRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()), //salt=username+salt
                getName() //realm name
        );

        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
