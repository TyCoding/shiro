package cn.tycoding.controller;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Result;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.TreeEntity;
import cn.tycoding.service.PermissionService;
import cn.tycoding.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther TyCoding
 * @date 2018/9/7
 */
@SuppressWarnings("all")
@RequestMapping("/permissions")
@Controller
public class PermissionsController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    /**
     * 查询所有
     *
     * @param model
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @RequestMapping("/findAll")
    public String findAll(Model model) {
        model.addAttribute("permissionsList", permissionService.findAll());
        return "page/permission";
    }

    /**
     * JSON格式返回权限列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findPermissionsAllList")
    public List findPermissionAllList() {
        return permissionService.findAll();
    }

    /**
     * 根据id查询权限数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/findById")
    public Permission findById(@RequestParam("id") Long id) {
        try {
            return permissionService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新当前权限数据
     *
     * @param permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public Result update(@RequestBody Permission permission) {
        try {
            permissionService.update(permission);
            return new Result(true, "更新数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 删除当前权限数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Result delete(@RequestParam("id") Long id) {
        try {
            permissionService.delete(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 创建权限
     *
     * @param permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/create")
    public Result create(@RequestBody Permission permission) {
        try {
            if (permission.getRid() != null) {
                //如果创建权限时选择了关联的角色，那么就更新角色-权限表数据
                permissionService.correlationRoles(permission.getId(), permission.getRid());
            }
            permissionService.create(permission);
            return new Result(true, "创建权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 根据权限id查询其所关联的角色数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/findRoleByPermissionId")
    public List<Role> findRoleByPermissionId(@RequestParam("id") Long id) {
        try {
            return permissionService.findRoleByPermissionId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新角色-权限信息，包含以下参数：
     * <p>
     * id      当前权限的id
     * ids     当前角色-权限的id集合
     * parents 当前角色-权限的是否是父节点的parent集合
     *
     * <p>
     * 之前在User层面上，我们要对其进行角色的更新，也就是说在User用户层面上更新权限，只需要维护用户-角色表数据。
     * 而在角色层面上，只需要维护角色-权限表数据。此处在权限层面上，也是维护角色-权限表数据
     * <p>
     * 误区：始终要记得，同一个角色拥有不同权限的情况是不存在的，如果两个用户拥有不同的权限，那么他们一定拥有了不同的角色
     *
     * @return
     * @Param dataMap 以上参数数据的Map集合
     *
     * <p>
     * 为什么要用Map接收？
     * 如果想要传递对象(如数组)参数，ajax就必须发送post请求，而post请求传递对象参数就要用JSON.stringify()格式化数据为JSON字符串才能传递；
     * 一旦使用了JSON.stringify()格式化数据，传递的就是JSON字符串，后端必须使用String类型接收，而我们传递的数据中包含了普通变量、数组等多种数据，所以使用使用Map接收，并指定泛型是String类型
     *
     * <p>
     * 前端传递进来的参数在Map中封装的数据结构类似：
     * {0:{key: id, value: {....}}, 1:{key: ids, value: {...}}, 2:{key: parents, value: {...}}}
     */
    @ResponseBody
    @RequestMapping(value = "/updateRolesPermissions", method = RequestMethod.POST)
    public Result updateRolesPermissions(@RequestBody Map<String, Object> dataMap) {
        try {
            Long id = Long.valueOf((String) dataMap.get("id")); //当前操作权限的id
            ArrayList ids = (ArrayList) dataMap.get("ids"); //当前权限更新的关联角色的id集合体
            ArrayList parents = (ArrayList) dataMap.get("parents"); //当前更新的角色权限parent信息

            // 更新权限与角色关系即需要维护角色-权限表，前端传来了什么数据？ 1、权限id；2、被选中的角色Id集合。
            // 如何更新权限关联的角色？我们常会想到，根据表的主键update呀，但是，因为页面上展示的数据是后端构建的ZTree实体类JSON数据，其中并不包含表的主键值
            // 所以，这里就采取一种比较极端的方式，先删除此权限所有关联的角色id，再依次关联此权限当前更新的角色id

            //先删除此权限关联的所有角色id
            permissionService.deleteAllPermissionsRoles(id);

            //再依次关联此去权限更新的角色id
            for (int i = 0; i < ids.size(); i++) {
                if (!(boolean) parents.get(i)) {
                    //不是父节点才进行关联，因为父节点是角色，子节点才是权限
                    permissionService.correlationRoles(id, Long.valueOf(String.valueOf(ids.get(i))));
                }
            }
            System.out.println(dataMap);
            return new Result(true, "更新数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 新增-- 构建一棵Tree树
     */
    @ResponseBody
    @RequestMapping("/getRolesZTree")
    public List<TreeEntity> getRolesZTree() {
        try {
            List<TreeEntity> treeList = new ArrayList<TreeEntity>();
            List<Role> dataList = roleService.findAll();
            for (Role role : dataList) {
                if (role.getPid() == 0) {
                    //说明是父节点
                    treeList.add(new TreeEntity(role.getId(), role.getDescription(), true, role.getPid()));
                } else {
                    //说明是子节点
                    treeList.add(new TreeEntity(role.getId(), role.getDescription(), false, role.getPid()));
                }
            }
            return treeList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
