package cn.tycoding.service;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.User;

import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
public interface UserService extends BaseService<User> {

    /**
     * 添加用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    void correlationRoles(Long userId, Long... roleIds);

    /**
     * 移除用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    void uncorrelationRoles(Long userId, Long... roleIds);

    /**
     * 根据用户名查找其他角色
     *
     * @param username
     * @return
     */
    List<Role> findRoles(String username);

    /**
     * 根据用户名查找其他权限
     *
     * @param username
     * @return
     */
    List<Permission> findPermissions(String username);

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    User findByName(String username);

    /**
     * 删除此用户关联的所有角色信息
     * @param id
     */
    void deleteAllUserRoles(Long id);
}
