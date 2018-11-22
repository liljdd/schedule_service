<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>用户登录</title>
    <jsp:include page="/inc/head.jsp"></jsp:include>
    <script type="text/javascript">
    	$(function(){
			$('#login').click(function(){
				var userName = $('input#userName').val();
				var password = $('input#password').val();

				if(userName==''){
					alert('用户名不可为空');
					return false;
				}

				if(password==''){
					alert('密码不可为空');
					return false;
				}
				$.ajax({
					url:'/user/login.json',
					data:{userName:userName,password:password},
					dataType:"json",
					type:"post",
					success:function(data){
						console.log(data);
						if(data.isSuccess){
							window.location.href="/task/list.do";
						}else{
							alert(data.msg);
						}
					},
					error:function(){
						alert('系统错误！');
					}
				});
			});
        });
    </script>
  </head>
  
  <body>
  	<div class="form-horizontal" role="form" style="width: 400px;margin: 9% auto 0px;">
	  <div class="form-group">
	    <label for="inputEmail3" class="col-sm-3 control-label">用户名</label>
	    <div class="col-sm-8">
	      <input type="text" class="form-control" id="userName" placeholder="用户名">
	    </div>
	  </div>
	  <div class="form-group">
	    <label for="inputPassword3" class="col-sm-3 control-label">密码</label>
	    <div class="col-sm-8">
	      <input type="password" class="form-control" id="password" placeholder="密码">
	    </div>
	  </div>
	  <div class="form-group">
	    <div class="col-sm-offset-3 col-sm-8">
	      <button class="btn btn-default" id="login">登录</button>
	    </div>
	  </div>
	</div>
  </body>
</html>
