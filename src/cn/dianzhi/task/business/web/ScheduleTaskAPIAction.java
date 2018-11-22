package cn.dianzhi.task.business.web;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dianzhi.task.business.model.ScheduleTask;
import cn.dianzhi.task.business.service.ScheduleTaskService;

/**
 * 
 * @author lee
 * @since 2016年5月19日
 */
@Controller
@RequestMapping("/task/api")
public class ScheduleTaskAPIAction {

  private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskAPIAction.class);

  @Autowired
  private Scheduler scheduler;
  @Autowired
  private ScheduleTaskService scheduleTaskService;
  @Autowired
  private ScheduleTaskAction scheduleTaskAction;
  /**
   * 接口添加任务
   * @param jonKey
   * @param cron
   * @param uri
   * @param description
   * @return
   */
  @ResponseBody
  @RequestMapping("add.json")
  public Map<String,Object> add(
          @RequestParam(value="jobKey")String jobKey
          ,@RequestParam(value="cron")String cron
          ,@RequestParam(value="url")String uri
          ,@RequestParam(value="description")String description
          ){
      Map<String,Object> result = new HashMap<String, Object>();
      result.put("isSuccess", true);
      try{
          if(!scheduleTaskAction.addTask(jobKey, "weixin", cron, uri, description)){
              result.put("isSuccess", false);
              result.put("msg", "任务名称已存在或者CRON表达式错误");
          }
      }catch (Exception e) {
          e.printStackTrace();
          result.put("isSuccess", false);
          result.put("msg", "system error");
      }
      
      return result;
  }
  /**
   * 接口删除任务
   * @param jonKey
   * @param cron
   * @param uri
   * @param description
   * @return
   */
  @ResponseBody
  @RequestMapping("del.json")
  public Map<String,Object> del(
          @RequestParam(value="jobKey")String jobKey
  ){
      Map<String,Object> result = new HashMap<String, Object>();
      result.put("isSuccess", true);
      try{
          JobKey jobKeyObject = new JobKey(jobKey, "weixin");
          ScheduleTask scheduleTask = scheduleTaskService.load(jobKeyObject);
          boolean flag = scheduler.deleteJob(jobKeyObject);
          if (flag && scheduleTask != null) {
              scheduleTaskService.delete(scheduleTask);
          }else{
              result.put("isSuccess", false);
              result.put("msg", "任务不存在或从scheduler中移除失败");
          }
      }catch (Exception e) {
          e.printStackTrace();
          result.put("isSuccess", false);
          result.put("msg", "system error");
      }
      return result;
  }
}
