package cn.tycoding.service;

import java.util.List;

/**
 * 封装通用的接口
 *
 * @auther TyCoding
 * @date 2018/9/6
 */
public interface BaseService<T> {

    /**
     * 根据ID查询其所有数据
     *
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 创建用户
     *
     * @param t
     */
    void create(T t);

    /**
     * 根据用户ID删除用户信息
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 更新用户信息
     *
     * @param t
     */
    void update(T t);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

}
