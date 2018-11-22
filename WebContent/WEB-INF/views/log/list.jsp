<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>task log</title>
<jsp:include page="/inc/head.jsp"></jsp:include>
<script src="static/jquery-ui-1.11.3/jquery-ui.min.js"></script>
<script src="static/jquery-ui-1.11.3/jquery-ui.theme.css"></script>
<script type="text/javascript">
	$(function(){
		$(".showLog1,.showLog2").click(function(){
			var showLog = $(this).data("log");
			var dataArr = showLog.split("-");
			$.ajax({
				url:"/log/loadLog.json",
				type:"get",
				data:{id:dataArr[0],logType:dataArr[1]},
				dataType:"json",
				success:function(data){
					loadLogContent(showLog,data);
				},
				error:function(){
					alert("系统错误！");
					loadLogContent(showLog);
				}
			});
		});
		//异步加载改日志
		function loadLogContent(showLog,data){
			if(data && data.length>0){
				$("#"+showLog+" #content").html(data);
			}
			$("#"+showLog).toggle("Blind");
		}
	});
</script>
</head>
<body style="padding: 20px;">
	<div class="page-header">
		<h3>日志列表</h3>
	</div>
	<div class="table-responsive">
		<table class="table table-hover">
			<tr>
				<th>ID</th>
				<th>定时任务</th>
				<th>完成时间</th>
				<th>耗时</th>
				<th>上次触发时间</th>
				<th>下次触发时间</th>
				<th>任务执行结果</th>
				<th>执行中日志</th>
			</tr>
			<c:forEach var="item" items="${scheduleLogs }">
				<tr  class=" <c:choose>
						<c:when test="${item.code==100}">success</c:when>
						<c:when test="${item.code>=200 and item.code<300}">danger</c:when>
						<c:otherwise>warning</c:otherwise>
					</c:choose> ">
					<td>${item.id }</td>
					<td>${item.jobGroup }.${item.jobKey }</td>
					<td>
						<fmt:formatDate value="${item.completeTime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>${item.costTime }ms</td>
					<td>
						<fmt:formatDate value="${item.previousFireTime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>
						<fmt:formatDate value="${item.nextFireTime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>
						<a class="showLog1" data-log="${item.id }-1" href="javascript:void(0);">more</a>
					</td>
					<td>
						<a class="showLog2" data-log="${item.id }-2" href="javascript:void(0);">more</a>
					</td>
				</tr>
				<tr>
					<td colspan="9" style="padding: 0; border-top: 0;">
						<div class="" style="width:100%">
							<div class="collapse" id="${item.id }-1" style="width: 100%;word-break: break-all;">
								<h4 class="text-center"><b>---任务执行结果---</b></h4>
								<pre id="content" class="bs-example" style="white-space: pre-wrap;word-wrap: break-word;">没有日志！</pre>
							</div>
							<div class="collapse" id="${item.id }-2" style="width: 100%;word-break: break-all;">
								<h4 class="text-center"><b>---执行中日志---</b></h4>
								<pre id="content" class="bs-example" style="white-space: pre-wrap;word-wrap: break-word;">>没有日志！</pre>
							</div>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>