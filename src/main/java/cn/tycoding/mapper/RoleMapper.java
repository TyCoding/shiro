package cn.tycoding.mapper;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
public interface RoleMapper {

    void correlationPermissions(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    void uncorrelationPermissions(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    boolean exists(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    Role findById(Long id);

    void create(Role role);

    void deleteUserRole(Long roleId);

    void deleteRole(Long roleId);

    List<Role> findAll();

    List<Permission> findRolePermissionByRoleId(Long id);

    List<Permission> findPermissionByRoleId(Long id);

    void update(Role role);

    void deleteAllRolePermissions(Long id);

    void updateUserRole_Id(Role role);
}
