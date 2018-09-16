package cn.tycoding.controller;

import cn.tycoding.entity.*;
import cn.tycoding.service.RoleService;
import cn.tycoding.service.UserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * @date 2018/7/19
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/user")
public class UserController {

    //注入
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    /**
     * 查询用户列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/findAll")
    public String findAllUser(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "page/user";
    }

    /**
     * 根据username查询
     *
     * @param username
     * @return
     */
    @RequestMapping("/findByUsername")
    @ResponseBody
    public User findByUsername(String username) {
        return userService.findByName(username);
    }

    /**
     * 创建用户
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/create")
    @RequiresRoles(value={"admin","personnel-resource"}, logical = Logical.OR)
    public Result create(@RequestBody User user) {
        try {
            userService.create(user);
            return new Result(true, "创建用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresRoles(value={"admin","personnel-resource"}, logical = Logical.OR)
    public Result update(@RequestBody User user) {
        try {
            userService.update(user);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresRoles(value = {"admin", "personnel-resource"}, logical = Logical.OR)
    public Result delete(@RequestParam("id") Long id){
        try{
            userService.delete(id);
            return new Result(true,"删除用户数据成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"发生未知错误");
        }
    }

    /**
     * 根据用户名查找其角色
     *
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/findRoles")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    @RequiresPermissions(value = {"role:view", "role:*"}, logical = Logical.OR)
    public List<Role> findRoles(String username) {
        return userService.findRoles(username);
    }

    /**
     * 更新用户角色信息，包含以下参数：
     * <p>
     * id      当前用户的id
     * ids     当前用户的角色的id集合
     * parents 当前节点是否是父节点
     *
     * @param dataMap 以上参数数据的Map集合
     * @return <p>
     * 为什么要用Map接收？
     * 如果想要传递对象(如数组)参数，ajax就必须发送post请求，而post请求传递对象参数就要用JSON.stringify()格式化数据为JSON字符串才能传递；
     * 一旦使用了JSON.stringify()格式化数据，传递的就是JSON字符串，后端必须使用String类型接收，而我们传递的数据中包含了普通变量、数组等多种数据，所以使用使用Map接收，并指定泛型是String类型
     *
     * <p>
     * 前端传递进来的参数在Map中封装的数据结构类似：
     * {0:{key: id, value: {...}}, 1:{key: ids, value: {...}}, 2:{key:pids, value: {...}}, 3:{key:parents, value: {...}}}
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserRoles", method = RequestMethod.POST)
    public Result updateUserRoles(@RequestBody Map<String, Object> dataMap) {
        try {
            // 依次获取到封装在Map中的不同对象的集合数据。每个数组的长度都是相通的，因为他们代表同一个节点的不同数据，
            Long id = Long.valueOf((String) dataMap.get("id")); //当前用户的id
            ArrayList ids = (ArrayList) dataMap.get("ids"); //当前用户的角色节点的id集合
            ArrayList parents = (ArrayList) dataMap.get("parents"); //当前用户角色是否是父节点判断的集合
            ArrayList names = (ArrayList) dataMap.get("names"); //当前用户角色的名称集合

            // 更新用户角色即需要维护用户-角色表，前端传来了什么数据？ 1、用户id；2、被选中的角色Id。
            // 如何更新用户角色？我们常会想到，根据表的主键update呀，但是，因为页面上展示的数据是后端构建的ZTree实体类JSON数据，其中并不包含表的主键值
            // 所以，这里就采取一种比较极端的方式，先删除此用户所有关联的角色id，再依次关联此用户当前调整的角色信息
            userService.deleteAllUserRoles(id);

            String role_id = ""; //初始化user表中role_id列数据

            //ids和parents的长度永远相同，所以遍历哪个都行
            if (ids.size() == 1){
                //说明只有一个节点，且这个节点被选中了，只能证明这个用户角色没有子节点，应该与用户关联
                userService.correlationRoles(id, Long.valueOf(String.valueOf(ids.get(0))));
                role_id = "[" + String.valueOf(names.get(0)) + "]";
            }else{
                for (int i = 0; i < ids.size(); i++) {
                    if (!(boolean) parents.get(i)) {
                        //不是父节点，才给此用户关联，用户关联的是子节点，不是父节点
                        userService.correlationRoles(id, Long.valueOf(String.valueOf(ids.get(i))));

                        //更新user表中role_id的数据
                        role_id += "[" + String.valueOf(names.get(i)) + "]";
                    }
                }
            }
            //单独更新user表中role_id列数据
            User user = new User();
            user.setRoleId(role_id);
            user.setId(id);
            user.setLocked(null);
            userService.update(user);

            System.out.println(role_id);
            System.out.println(dataMap);
            return new Result(true, "更新用户角色信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发生未知错误");
        }
    }

    /**
     * 构建一棵用户角色的ZTree树，以JSON格式返回给页面
     * 可以给用户指定角色，但是并不负责指定角色的权限。也就是说：对用户，只需要指定用户拥有的角色，只需要维护：用户-角色表
     * 误点：不可能存在同一个角色拥有不同权限的情况，所以在用户层面上我们仅需要维护用户角色表，不需要维护角色-权限表
     * 格式：
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZTreeForUserRoles")
    public List<TreeEntity> getTreeForUserRoles() {
        try {
            List<TreeEntity> treeList = new ArrayList<TreeEntity>();
            List<Role> roleList = roleService.findAll();

            for (Role role : roleList) {
                // 为tree树节点添加数据，节点pid为0的都是父节点，其他为子节点
                if(role.getPid() != null){
                    if (role.getPid() == 0) {
                        treeList.add(new TreeEntity(role.getId(), role.getDescription(), true, (long) 0));
                    } else {
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
