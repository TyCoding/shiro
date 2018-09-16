<%--
  Created by IntelliJ IDEA.
  User: ty
  Date: 2018/7/19
  Time: 上午9:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/lib/bootstrap.min.css"/>
</head>
<body>

<h3 class="text-center">登录</h3>
<hr/>
<form class="form-horizontal" action="login.do" role="form">
    <div class="form-group">
        <label class="col-sm-5 control-label">username:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="username" placeholder="username">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-5 control-label">password:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="password" placeholder="password">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-5 col-sm-5">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="remember">请记住我
                </label>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-5 col-sm-5">
            <button type="submit" class="btn btn-default">登录</button>
        </div>
    </div>
</form>

<h4 class="text-center" style="color:red">${error}</h4>

</body>
</html>
