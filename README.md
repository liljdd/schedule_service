# 定时任务服务
	部署到“/”路径 
## 程序中调用添加任务
	http://xxx.xx/task/api/add.json?jobKey=xx&cron=0/1 * * *&url=xxx&description=xxx
	jobKey 			定时任务名称，不可重复。（必须）
	cron 			定时任务cron表达式。（必须）
	url				定时任务具体执行地址。（必须）
	description	    定时任务描述。（必须）
------------
    调用示例
	Map<String,String> param = new HashMap<String, String>();
	param.put("jobKey", "xxx");
	param.put("cron", "1-2 * * * * ? ");
	param.put("description", "定时发送消息");
	param.put("url", "http://xxx.xx");
	HttpUtils.post("http://xxx.xx/task/api/add.json", param);
## 程序中删除定时任务
	http://xxx.xx/task/api/del.json?jobKey=xx
	jobKey			定时任务名称。（必须）
----------
    调用示例
	Map<String,String> param = new HashMap<String, String>();
	param.put("jobKey", "xxx");
	HttpUtils.post("http://xxx.xx/task/api/del.json", param);
### 以上的定时任务都默认添加到"weixin"组中


	