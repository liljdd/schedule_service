package cn.dianzhi.task.business.job;

import cn.dianzhi.task.business.model.ScheduleTask;
import cn.dianzhi.task.business.service.ScheduleLogService;
import cn.dianzhi.task.business.service.ScheduleTaskService;
import cn.dianzhi.task.business.util.AESTool;
import cn.dianzhi.task.business.util.HttpUtils;
import cn.dianzhi.task.business.util.SystemConfig;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 定时任务.
 * 
 * @author lee
 * @since 2016年5月19日
 */
public class JobHttpModel implements Job {

  private static final Logger logger = LoggerFactory.getLogger(JobHttpModel.class);
  public static final String SCHEDULE_TASK_ID = "schedule_task_id";
  
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    int scheduleTaskId = (Integer) context.getJobDetail().getJobDataMap().get("schedule_task_id");
    logger.info("任务执行中,scheduleTaskId:{},context:{}", scheduleTaskId, context);

    ScheduleTask scheduleTask = ScheduleTaskService.load(scheduleTaskId);
    if (scheduleTask == null) {
      logger.error("查询scheduleTask异常");
      return;
    }
    ScheduleTask updateSheduleTask = new ScheduleTask();
    updateSheduleTask.setId(scheduleTaskId);
    updateSheduleTask.setInvokeCount(scheduleTask.getInvokeCount() + 1);

    long start = System.currentTimeMillis();

    //执行任务
    doJobs(context, scheduleTask);

    updateSheduleTask.setCostTime(System.currentTimeMillis() - start);
    ScheduleTaskService.update(updateSheduleTask);

  }

  /**
   * 执行定时任务，invokeURL.
   * 
   * @param context job execute context
   */
  private void doJobs(JobExecutionContext context, ScheduleTask scheduleTask) {
    String excuteToken = UUID.randomUUID().toString().replaceAll("-", "");
    JSONObject result = new JSONObject();
    result.put("excuteToken", excuteToken);
    
    result.put("code", SystemConfig.SUCCESS);
    result.put("context", context.toString());
    String invokeUrl = scheduleTask.getInvokeUrl();
    try {

      //调用的url
      //放入参数 
      Map<String,Object> param = new HashMap<String, Object>();
      param.put("callBack", SystemConfig.CALLBACK);
      param.put("excuteToken", excuteToken);
      param.put("logSaveURI", SystemConfig.LOG_SAVE_URI);
      
      Map<String, String> data = new HashMap<String, String>();
      data.put("param", JSONObject.toJSONString(param));
      data.put("taskName", AESTool.encodeAES(scheduleTask.getName(), SystemConfig.TASK_KEY_ONE));
      data.put("token", AESTool.encodeAES(scheduleTask.getName(), SystemConfig.TASK_KEY_TWO));
      //调用其他项目的url 去实际执行这个task
      String result2 = HttpUtils.post(invokeUrl, data, 100000, 3);
      
      JSONObject resultObject = JSONObject.parseObject(result2);
      //将请求的返回结果放入log表中
      result.put("code", resultObject.getInteger("code").equals(100));
      result.put("msg", resultObject.getString("msg"));
    } catch (Exception err) {
      logger.error("执行任务失败", err);
      result.put("code", SystemConfig.ERROR);
      result.put("msg", StringUtils.isBlank(err.getMessage())?"调用uri错误["+invokeUrl:err.getMessage()+"]");
    } finally {
        //插入log
      ScheduleLogService.insertLogInfo(context, result);
      
    }
  }

}
