package cn.tycoding.entity;

import java.io.Serializable;

/**
 * @auther TyCoding
 * @date 2018/9/11
 */
public class TreeEntity implements Serializable {

    private Long id; // 节点的id值
    private String name; // 节点对应的名称
    private Boolean isParent; // 是否是父节点
    private Long pid; //当前节点对应的父节点的id值

    public TreeEntity() {
    }

    public TreeEntity(Long id, String name, Boolean isParent, Long pid) {
        this.id = id;
        this.name = name;
        this.isParent = isParent;
        this.pid = pid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
