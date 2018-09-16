<%--
  Created by IntelliJ IDEA.
  User: ty
  Date: 2018/7/20
  Time: 上午8:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户信息</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/lib/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/lib/css/demo.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/lib/css/metroStyle/metroStyle.css"/>
</head>
<body>
<shiro:guest>
    <label style="margin-left: 20px;">欢迎游客访问,<a href="${pageContext.request.contextPath}/login.jsp">点击登录</a></label>
</shiro:guest>

<shiro:user>
    <br/>
    <label style="margin-left: 20px;">欢迎：[<shiro:principal/>]登录，<a
            href="${pageContext.request.contextPath}/logout">点击退出</a></label>
    <br/>
    <hr/>
    <br/>
    <div style="margin-left: 5%;">
        <div>
            <ul class="nav nav-pills nav-stacked" style="width: 10%;border: 1px solid #d9edf7;">
                <li>
                    <a href="${pageContext.request.contextPath}/index.do">首页</a>
                </li>
                <li class="active">
                    <a href="${pageContext.request.contextPath}/user/findAll.do">用户列表</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/roles/findAll.do">角色列表</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/permissions/findAll.do">权限列表</a>
                </li>
            </ul>
        </div>
        <div style="margin-left: 21%;margin-right: 16%;margin-top: -8%;" class="text-center">
            <div style="float: left;margin-bottom: 10px;">
                <button class="btn btn-primary" onclick="editAndCreate()">添加</button>
            </div>
            <center>
                <table class="table table-bordered">
                    <tr>
                        <th>编号</th>
                        <th>用户名</th>
                        <th>密码</th>
                        <th>盐值</th>
                        <th>角色列表</th>
                        <th>操作</th>
                    </tr>
                    <tbody>
                    <c:forEach items="${userList}" var="user">
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.password}</td>
                            <td>${user.salt}</td>
                            <td>${user.roleId}</td>
                            <td hidden="hidden">${user.locked}</td>
                            <td>
                                <input type="button" class="btn btn-primary btn-sm"
                                       onclick="RolesBtn(${user.id}, '${user.username}')"
                                       value="角色"/>
                                <input type="button" class="btn btn-info btn-sm use-btn"
                                       onclick="statusBtn(${user.id}, '${user.username}')"
                                       value="状态"/>
                                <input type="button" class="btn btn-success btn-sm" onclick="editAndCreate(${user.id},'${user.username}')" value="编辑"/>
                                <input type="button" class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})" value="删除"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </center>
        </div>
    </div>

    <!-- 添加-编辑 共用--弹出框 -->
    <div class="modal fade" id="create-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">编辑</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <input class="id" name="id" value="" hidden="hidden">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control username" name="username" id="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">密码</label>
                            <div class="col-sm-5">
                                <input type="password" class="form-control password" name="password">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary create-sure">提交更改</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 权限-弹出框 -->
    <div class="modal fade" id="permission-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">权限信息</h4>
                </div>
                <div class="modal-body">
                    <input hidden="hidden" class="id" value=""/> <!-- 用来存放当前操作用户的id值 -->
                    <!-- 角色-权限的Tree树 -左侧 -->
                    <div class="zTreeDemoBackground" style="float: left;">
                        <ul id="tree" class="ztree"></ul>
                    </div>
                    <!-- 选中节点信息展示 -右侧 -->
                    <div style="float: right;width: 55%;margin-top: -2%;">
                        <li class="title">
                            <h2 style="margin-left: 25%;">&rarr; 选中节点信息展示 &larr;</h2>
                            <textarea class="info" style="margin: 0px;width: 326px;height: 257px;word-wrap: normal;"></textarea>
                        </li>
                    </div>
                </div>
                <div class="modal-footer" style="clear: both;">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="surePermission()">提交更改</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 状态-弹出框 -->
    <div class="modal fade" id="status-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">状态信息</h4>
                </div>
                <div class="modal-body">
                    <input hidden="hidden" class="id" value=""/>
                    <input hidden="hidden" class="username" value=""/>
                    <input hidden="hidden" class="password" value=""/>
                    <input hidden="hidden" class="locked" value=""/>
                    <div class="radio">
                        <label>
                            <input type="radio" name="statusOption" class="use">启用
                        </label>
                    </div>
                    <div class="radio">
                        <label>
                            <input type="radio" name="statusOption" class="unuse">锁定
                        </label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary status-sure">提交更改</button>
                </div>
            </div>
        </div>
    </div>
</shiro:user>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/jquery.ztree.excheck.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/user.js"></script> <!-- 核心js -->
</html>
