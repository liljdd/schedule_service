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
		$("#editTask").validate();
	});
</script>
</head>
<body style="padding: 20px;">
	<div class="page-header">
		<h3>编辑定时任务</h3>
	</div>
	<div>
		<form class="form-horizontal" action="task/edit.htm" method="post" id="editTask">
			<input type="hidden" name="jobKey" value="${jobDetail.name }">
			<input type="hidden" name="jobGroup" value="${jobDetail.group }">
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">JOB-Group</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" placeholder="请输入JOB-Group" value="${jobDetail.group }" required
						readonly="readonly">
				</div>
			</div>
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">定时任务名称</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" placeholder="请输入定时任务名称( calssName|methodName )" value="${jobDetail.name }" required
						readonly="readonly">
				</div>
			</div>
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">调用URL</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="invokeUrl" value="${jobDetail.invokeUrl }" placeholder="请输入调用URL" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword3" class="col-sm-4 control-label">表达式</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="cron" placeholder="请输入cron表达式" value="${jobDetail.cronExpression }"
						required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword3" class="col-sm-4 control-label">描述</label>
				<div class="col-sm-4">
					<textarea rows="5" cols="60" draggable="false" placeholder="请输入描述内容" name="description" required>${jobDetail.description }</textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-4 col-sm-10">
					<button type="submit" class="btn btn-primary">提交</button>
				</div>
			</div>
		</form>
	</div>

</body>
</html>