<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>task list</title>
<jsp:include page="/inc/head.jsp"></jsp:include>
<style>
.error {
	color: red;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addTask").validate();
		$("#jobGroup").click(function(){
			if($(this).val()=='FenxiaoGroup'){
				$("#uriInput").val('http://v.wfenxiao.com.cn/task/excute');
			}else if($(this).val()=='FenxiaoPayGroup'){
				$("#uriInput").val('http://zf.wfenxiao.com.cn/task/excute');
			}else{
				$("#uriInput").val('');
			}
		});
	});
</script>
</head>
<body style="padding: 20px;">
	<div class="page-header">
		<h3>新增定时任务</h3>
	</div>
	<div>
		<form class="form-horizontal" action="task/add.htm" method="post" id="addTask">
		  	<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">JOB-Group</label>
				<div class="col-sm-4">
					<select class="form-control" name="jobGroup" id="jobGroup">
						<option value="FenxiaoGroup">FenxiaoGroup</option>
						<option value="FenxiaoPayGroup">FenxiaoPayGroup</option>
						<option value="OtherGroup">OtherGroup</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">定时任务名称</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="jobKey" placeholder="请输入定时任务名称( className|methodName )" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">调用URL</label>
				<div class="col-sm-4">
					<input id="uriInput" value='http://v.wfenxiao.com.cn/task/excute' type="text" class="form-control" name="url" placeholder="请输入调用URL" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword3" class="col-sm-4 control-label">表达式</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="cron" placeholder="请输入cron表达式" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword3" class="col-sm-4 control-label">描述</label>
				<div class="col-sm-4">
					<textarea rows="5" cols="60" draggable="false" placeholder="请输入描述内容" name="description" required></textarea>
				</div>
			</div>
			<!--  -->
			<div class="form-group">
				<div class="col-sm-offset-4 col-sm-10">
					<button type="submit" class="btn btn-primary">提交</button>
				</div>
			</div>
		</form>
	</div>

</body>
</html>