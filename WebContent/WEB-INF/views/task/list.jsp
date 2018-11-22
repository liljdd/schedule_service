<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>task list</title>
<jsp:include page="/inc/head.jsp"></jsp:include>
</head>
<body style="padding: 20px;">
	<div class="page-header">
		<h3>
			定时任务列表 <small><a href="task/to-add.htm" class="btn btn-info btn-sm">新增添加定时任务</a></small>
		</h3>
	</div>
	<div class="table-responsive">
		<table class="table table-hover">
			<tr>
				<th>Job Group</th>
				<th>定时任务</th>
				<th>URL</th>
				<th>执行表达式</th>
				<th>STATE</th>
				<th>已执行次数</th>
				<th>上次执行时间</th>
				<th>下次执行时间</th>
				<th>日志</th>
				<th>操作</th>
			</tr>
			<c:forEach var="item" items="${taskList }">
				<tr>
					<td>${item.group }</td>
					<td>${item.name }</td>
					<td>${item.invokeUrl }</td>
					<td>${item.cronExpression }</td>
					<td>
						<c:choose>
							<c:when test="${item.state == 'NORMAL' }">
								<button class="btn btn-success btn-xs">正常</button>
							</c:when>
							<c:when test="${item.state == 'PAUSED' }">
								<div class="btn btn-warning btn-xs">暂停</div>
							</c:when>
						</c:choose>
					</td>
					<td>${item.invokeCount }</td>
					<td>
						<fmt:formatDate value="${item.previousFireTime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>
						<fmt:formatDate value="${item.nextFireTime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>
						<a href="log/list.htm?scheduleTaskId=${item.id }" class="btn btn-info btn-xs">日志</a>
					</td>
					<td style="min-width: 140px;">
						<a href="task/to-edit.htm?scheduleTaskId=${item.id }" class="btn btn-info btn-xs">编辑</a>
						<c:if test="${item.state eq 'NORMAL'}">
							<a href="task/operate.htm?operate=pause&jobKey=${item.name }&jobGroup=${item.group}" class="btn btn-info btn-xs">暂停</a>
						</c:if>
						<c:if test="${item.state eq 'PAUSED' }">
							<a href="task/operate.htm?operate=resume&jobKey=${item.name }&jobGroup=${item.group}" class="btn btn-info btn-xs">恢复</a>
						</c:if>
						<button onclick="delJob('${item.name }', '${item.group}')" class="btn btn-danger btn-xs" data-toggle="modal"
							data-target="#myModal">删除</button>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">警告</h4>
				</div>
				<div class="modal-body">
					确定要删除该Job: <span id="msg"></span> 吗？
					<input type="hidden" id="jobKey" />
					<input type="hidden" id="jobGroup" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info btn-sm" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-info btn-sm" data-dismiss="modal" id="delConfirm">确认</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	function delJob(jobKey, jobGroup) {
		$("#jobKey").val(jobKey);
		$("#jobGroup").val(jobGroup);
		$("#msg").text(" [ " + jobGroup + "." + jobKey + " ] ");
	}

	$("#delConfirm").click(function() {
		window.location = "task/operate.htm?operate=delete&jobKey=" + $("#jobKey").val() + "&jobGroup=" + $("#jobGroup").val();
	});
</script>

</html>