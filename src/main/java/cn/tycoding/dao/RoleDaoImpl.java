package cn.tycoding.dao;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.RolePermission;
import cn.tycoding.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
@Component
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void correlationPermissions(Long roleId, Long[] permissionIds) {
        if(permissionIds == null || permissionIds.length == 0){
            return;
        }
        for(Long permissionId : permissionIds){
            if(!exists(roleId, permissionId)){
                roleMapper.correlationPermissions(roleId,permissionId);
            }
        }
    }

    @Override
    public void uncorrelationPermissions(Long roleId, Long[] permissionIds) {
        if(roleId == null || permissionIds.length == 0){
            return;
        }
        for(Long permissionId : permissionIds){
            if(exists(roleId, permissionId)){
                roleMapper.uncorrelationPermissions(roleId, permissionId);
            }
        }
    }

    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }

    /**
     * 查询表中是否存在此数据
     * @param roleId
     * @param permissionId
     * @return
     */
    private boolean exists(Long roleId, Long permissionId) {
        return roleMapper.exists(roleId, permissionId);
    }

    @Override
    public void create(Role role) {
        if (role.getPid() == null){
            role.setPid(0L);
        }
        roleMapper.create(role);
    }

    @Override
    public void delete(Long id) {
        //先将和角色相关的表删除
        roleMapper.deleteUserRole(id);

        //再删除角色表数据
        roleMapper.deleteRole(id);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    public List<Permission> findRolePermissionByRoleId(Long id) {
        return roleMapper.findRolePermissionByRoleId(id);
    }

    @Override
    public List<Permission> findPermissionByRoleId(Long id) {
        return roleMapper.findPermissionByRoleId(id);
    }

    @Override
    public void update(Role role) {
        roleMapper.update(role);
    }

    @Override
    public void deleteAllRolePermissions(Long id) {
        roleMapper.deleteAllRolePermissions(id);
    }

    @Override
    public void updateUserRole_Id(Role role) {
        roleMapper.updateUserRole_Id(role);
    }


}
