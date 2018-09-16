package cn.tycoding.controller;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Result;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.TreeEntity;
import cn.tycoding.service.RoleService;
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
@Controller
@SuppressWarnings("all")
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RoleService roleService;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/findAll")
    public String findAll(Model model) {
        model.addAttribute("rolesList", roleService.findAll());
        return "page/role";
    }

    /**
     * JSON格式返回角色列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findRolesAllList")
    public List findRolesAllList() {
        return roleService.findAll();
    }

    /**
     * 根据id查询角色数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/findById")
    public Role findById(@RequestParam("id") Long id) {
        try {
            return roleService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新当前角色数据
     *
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public Result update(@RequestBody Role role) {
        try {
            if (role.getDescription() != null) {
                //说明更新了角色的描述信息，那就更新用户表中显示的角色名称
                roleService.updateUserRole_Id(role);
            }
            roleService.update(role);
            return new Result(true, "更新数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 删除当前角色数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Result delete(@RequestParam("id") Long id) {
        try {
            roleService.delete(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 创建角色
     *
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("/create")
    public Result create(@RequestBody Role role) {
        try {
            roleService.create(role);
            return new Result(true, "创建角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 更新角色-权限信息，包含以下参数：
     * <p>
     * id      当前角色的id
     * ids     当前角色-权限的id集合
     * parents 当前角色-权限的是否是父节点的parent集合
     *
     * <p>
     * 之前在User层面上，我们要对其进行角色的更新，也就是说在User用户层面上更新权限，只需要维护用户-角色表数据。
     * 而在角色层面上，只需要维护角色-权限表数据。
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
            Long id = Long.valueOf((String) dataMap.get("id")); //当前操作角色的id
            ArrayList ids = (ArrayList) dataMap.get("ids"); //当前角色更新的权限id的集合体
            ArrayList parents = (ArrayList) dataMap.get("parents"); //当前更新的角色权限parent信息

            // 更新角色的权限关系即需要维护角色-权限表，前端传来了什么数据？ 1、角色id；2、被选中的权限Id集合。
            // 如何更新角色的权限？我们常会想到，根据表的主键update呀，但是，因为页面上展示的数据是后端构建的ZTree实体类JSON数据，其中并不包含表的主键值
            // 所以，这里就采取一种比较极端的方式，先删除此角色所有关联的权限id，再依次关联此角色当前更新的权限id

            //先删除此角色关联的所有权限id
            roleService.deleteAllRolePermissions(id);

            //再依次关联此角色更新的权限id
            for (int i = 0; i < ids.size(); i++) {
                if (!(boolean) parents.get(i)) {
                    //不是父节点才进行关联，因为父节点是角色，子节点才是权限
                    roleService.correlationPermissions(id, Long.valueOf(String.valueOf(ids.get(i))));
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
     * 构建一颗ZTree树
     * 以JSON格式返回数据库中角色表-权限表的所有数据
     * 数据结构类似： [{id: xx, name: 父节点, parent: true}, {id: xx, name: 子节点, parent: false},{子节点....}, {父节点....}, {子节点...}]
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZTreeForRolePermissionAll")
    public List<TreeEntity> getZTreeForRolePermissionAll(@RequestParam("id") Long id) {
        try {
            List<TreeEntity> treeList = new ArrayList<TreeEntity>();
            List<Permission> dataList = roleService.findPermissionByRoleId(id);

            //根据id查询此角色的数据，即为父节点的数据
            Role role = roleService.findById(id);
            treeList.add(new TreeEntity(role.getId(), role.getDescription(), true, role.getPid()));

            //遍历设置子节点数据
            for (Permission permission : dataList) {
                treeList.add(new TreeEntity(permission.getId(), permission.getDescription(), false, permission.getRid()));
            }
            return treeList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据角id查询其所关联的权限根据角色id查询当前role-permission表中关联的数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/findPermissionByRoleId")
    public List<Permission> findPermissionByRoleId(@RequestParam("id") Long id) {
        try {
            return roleService.findRolePermissionByRoleId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加角色 --构建一棵ZTree树
     * <p>
     * 这棵ZTree树仅显示当前角色所关联的权限，不需要得到所有的权限信息
     * <p>
     * 返回的JSON数据结构类似： [{id: xx, name: 父节点, parent: true}, {id: xx, name: 子节点, parent: false}, {子节点...}, {子节点}]
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZTreeForAllRoles")
    public List<TreeEntity> getZTreeForAllRoles() {
        try {
            List<TreeEntity> treeList = new ArrayList<TreeEntity>();
            List<Role> dataList = roleService.findAll();

            for (Role role : dataList) {
                if (role.getPid() != null) {
                    if (role.getPid() == 0) {
                        //说明是父节点
                        treeList.add(new TreeEntity(role.getId(), role.getDescription(), true, role.getPid()));
                    } else {
                        //说明是子节点
                        treeList.add(new TreeEntity(role.getId(), role.getDescription(), false, role.getPid()));
                    }
                }
            }
            return treeList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
