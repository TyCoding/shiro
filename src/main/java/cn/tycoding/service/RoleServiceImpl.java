package cn.tycoding.service;

import cn.tycoding.dao.RoleDao;
import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public void correlationPermissions(Long roleId, Long... permissionIds) {
        roleDao.correlationPermissions(roleId, permissionIds);
    }

    @Override
    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
        roleDao.uncorrelationPermissions(roleId, permissionIds);
    }

    @Override
    public Role findById(Long id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Permission> findRolePermissionByRoleId(Long id) {
        return roleDao.findRolePermissionByRoleId(id);
    }

    @Override
    public List<Permission> findPermissionByRoleId(Long id) {
        return roleDao.findPermissionByRoleId(id);
    }

    @Override
    public void deleteAllRolePermissions(Long id) {
        roleDao.deleteAllRolePermissions(id);
    }

    @Override
    public void updateUserRole_Id(Role role) {
        roleDao.updateUserRole_Id(role);
    }

    @Override
    public void create(Role role) {
        roleDao.create(role);
    }

    @Override
    public void delete(Long id) {
        roleDao.delete(id);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

}
