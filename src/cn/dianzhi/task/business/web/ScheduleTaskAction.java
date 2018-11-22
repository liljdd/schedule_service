package cn.dianzhi.task.business.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.dianzhi.task.business.job.JobHttpModel;
import cn.dianzhi.task.business.model.ScheduleLog;
import cn.dianzhi.task.business.model.ScheduleTask;
import cn.dianzhi.task.business.service.JobService;
import cn.dianzhi.task.business.service.ScheduleLogService;
import cn.dianzhi.task.business.service.ScheduleTaskService;
import cn.dianzhi.task.business.util.AESTool;
import cn.dianzhi.task.business.util.SystemConfig;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author lee
 * @since 2016年5月19日
 */
@Controller
@RequestMapping("/task/")
public class ScheduleTaskAction {

  private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskAction.class);

  @Autowired
  private Scheduler scheduler;
  @Autowired
  private ScheduleTaskService scheduleTaskService;
  @Autowired
  private ScheduleLogService scheduleLogService;

  /**
   * list.
   * 
   * @param model
   * @return
   */
  @RequestMapping("list")
  public String list(Model model) {
    List<ScheduleTask> taskList = scheduleTaskService.loadAll();
    model.addAttribute("taskList", taskList);
    return "task/list";
  }

  /**
   * add定时任务.
   * 
   * @param jobKey
   *          定时任务Key
   * @param cron
   *          定时任务表达式
   * @return 页面
   */
  @RequestMapping(value = "/add.htm", method = RequestMethod.POST)
  public String add(String jobKey, String jobGroup, String cron, String url, String description) {
    try {
      if(!addTask(jobKey, jobGroup, cron, url, description)){
          throw new SchedulerException("task is existed or CRON is faul!");
      }
    } catch (SchedulerException err) {
      logger.error("add task异常{}", err);
    }
    return "redirect:list.htm";
  }
  /**
   * 接口添加任务
   * @param jonKey
   * @param cron
   * @param uri
   * @param description
   * @return
   */
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
          if(!addTask(jobKey, "weixin", cron, uri, description)){
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

  /**
   * 添加一个定时任务
   * @param jobKey
   * @param jobGroup
   * @param cron
   * @param url
   * @param description
   * @throws SchedulerException
   */
  protected boolean addTask(String jobKey, String jobGroup, String cron, String url, String description) throws SchedulerException {
      boolean result = true;
      boolean jobKeyExists = scheduler.checkExists(new JobKey(jobKey, jobGroup));
      boolean cronValid = CronExpression.isValidExpression(cron);
      if (!jobKeyExists && cronValid) {

        ScheduleTask scheduleTask = new ScheduleTask(jobKey, jobGroup);
        scheduleTask.setCronExpression(cron);
        scheduleTask.setInvokeUrl(url);
        scheduleTask.setDescription(description);
        int id = scheduleTaskService.insert(scheduleTask);
        if (id != 1) {
          logger.error("插入数据失败");
          result = false;
        }
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JobHttpModel.SCHEDULE_TASK_ID, scheduleTask.getId());
        JobDetail job = JobBuilder.newJob(JobHttpModel.class).withIdentity(jobKey, jobGroup).withDescription(description)
            .usingJobData(jobDataMap).build();

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobKey, jobGroup).withSchedule(cronScheduleBuilder).withPriority(1)
            .withDescription(description).forJob(job).build();
        scheduler.startDelayed(10);
        scheduler.scheduleJob(job, trigger);

      }else{
          result = false;
      }
      return result;
  }

  /**
   * 更新.
   * 
   * @param jobKey
   * @param jobGroup
   * @param cron
   * @param invokeUrl
   * @param description
   * @return
   */
  @RequestMapping(value = "/edit.htm", method = RequestMethod.POST)
  public String edit(String jobKey, String jobGroup, String cron, String invokeUrl, String description) {
    try {
      TriggerKey triggerKey = new TriggerKey(jobKey, jobGroup);
      JobKey jobKeyObject = new JobKey(jobKey, jobGroup);

      boolean jobKeyExists = scheduler.checkExists(jobKeyObject);
      boolean triggerExists = scheduler.checkExists(triggerKey);
      boolean cronValid = CronExpression.isValidExpression(cron);
      if (triggerExists && jobKeyExists && cronValid) {
        CronTrigger newTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        newTrigger = newTrigger.getTriggerBuilder().withSchedule(cronScheduleBuilder).withDescription(description).build();

        JobDetail jobDetail = scheduler.getJobDetail(jobKeyObject);
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDetail = jobDetail.getJobBuilder().withDescription(description).usingJobData(jobDataMap).build();

        JobService.update(jobDetail);
        scheduler.rescheduleJob(triggerKey, newTrigger);

        int scheduleTaskId = jobDataMap.getInt(JobHttpModel.SCHEDULE_TASK_ID);
        ScheduleTask scheduleTask = ScheduleTaskService.load(scheduleTaskId);
        if (scheduleTask != null) {
          scheduleTask.setCronExpression(cron);
          scheduleTask.setName(jobKey);
          scheduleTask.setGroup(jobGroup);
          scheduleTask.setDescription(description);
          scheduleTask.setInvokeUrl(invokeUrl);
          ScheduleTaskService.update(scheduleTask);
        }
      }
    } catch (Exception err) {
      logger.error("异常{}", err);
    }
    return "redirect:list.htm";
  }

  /**
   * 页面跳转.
   * 
   * @param path
   *          指定路径
   * @return
   */
  @RequestMapping("to-{path}")
  public String to(@PathVariable String path, HttpServletRequest request, Model model) {
    if ("edit".equals(path)) {
      String scheduleTaskId = request.getParameter("scheduleTaskId");
      ScheduleTask scheduleTask = ScheduleTaskService.load(Integer.valueOf(scheduleTaskId));
      scheduleTask = scheduleTaskService.load(new JobKey(scheduleTask.getName(), scheduleTask.getGroup()));
      model.addAttribute("jobDetail", scheduleTask);
    }else if("list".equals(path)){
        List<ScheduleTask> taskList = scheduleTaskService.loadAll();
        model.addAttribute("taskList", taskList);
        return "task/list";
    }
    return "/task/" + path;
  }

  /**
   * 定时任务操作.
   * 
   * @param jobKey
   * @param jobGroup
   * @param redirectAttributes
   * @return
   */
  @RequestMapping("/operate.htm")
  public String operate(String operate, String jobKey, String jobGroup, RedirectAttributes redirectAttributes) {
    try {
      JobKey jobKeyObject = new JobKey(jobKey, jobGroup);
      if ("delete".equals(operate)) {
        ScheduleTask scheduleTask = scheduleTaskService.load(jobKeyObject);
        boolean flag = scheduler.deleteJob(jobKeyObject);
        if (flag && scheduleTask != null) {
          scheduleTaskService.delete(scheduleTask);
          redirectAttributes.addFlashAttribute("deleteSuccess", "ok");
        }
      } else if ("pause".equals(operate)) {
        scheduler.pauseJob(jobKeyObject);
      } else if ("resume".equals(operate)) {
        scheduler.resumeJob(jobKeyObject);
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
    return "redirect:list.htm";
  }
  /**
   * 定时任务回调处理
   * 
   * @param jobKey
   * @param jobGroup
   * @param redirectAttributes
   * @return
   */
  @RequestMapping("callback.json")
  @ResponseBody
  public Map<String,Object> callback(HttpServletRequest requet
          ,String taskToken
          ,String token
          ,String result
          ) {
      Map<String,Object> resultMap = new HashMap<String, Object>();
      resultMap.put("code", SystemConfig.SUCCESS);
      resultMap.put("msg", "处理成功");
      try {
          //验证加密信息是否通过
          result = URLDecoder.decode(result, "utf-8");
          if(AESTool.decodeAES(token, SystemConfig.TASK_KEY_TWO).equals(AESTool.decodeAES(taskToken, SystemConfig.TASK_KEY_ONE))){
              HashMap<String,Object> param = JSONObject.parseObject(result, HashMap.class);
              param.put("logType", 1);
              param.put("isCallBack", 1);
              Integer count = scheduleLogService.modifyScheduleLogDetail(param);
              if(count > 0){
                  resultMap.put("code", SystemConfig.SUCCESS);
                  resultMap.put("msg", "处理成功!");
              }else{
                  resultMap.put("code", SystemConfig.ERROR);
                  resultMap.put("msg", "处理失败,未知的执行任务!");
              }
          }else{
              resultMap.put("code", SystemConfig.ERROR_AES);
              resultMap.put("msg", "处理失败,token失效!");
          }
      } catch (Exception e) {
          e.printStackTrace();
          resultMap.put("code", SystemConfig.ERROR);
          resultMap.put("msg", "处理失败!");
      }
      return resultMap;
  }
  /**
   * 定时任务处理日志
   * 
   * @param jobKey
   * @param jobGroup
   * @param redirectAttributes
   * @return
   */
  @RequestMapping("logSave.json")
  @ResponseBody
  public Map<String,Object> logSave(HttpServletRequest requet
          ,String taskToken
          ,String token
          ,String logContent
  ) {
      Map<String,Object> resultMap = new HashMap<String, Object>();
      resultMap.put("code", SystemConfig.SUCCESS);
      resultMap.put("msg", "处理成功");
      try {
          //验证加密信息是否通过
          logContent = URLDecoder.decode(logContent, "utf-8");
          if(AESTool.decodeAES(token, SystemConfig.TASK_KEY_TWO).equals(AESTool.decodeAES(taskToken, SystemConfig.TASK_KEY_ONE))){
              Map<String,Object> param = new HashMap<String, Object>();
              param.put("logContent", logContent);
              param.put("excuteToken", AESTool.decodeAES(taskToken, SystemConfig.TASK_KEY_ONE));
              param.put("logType", 2);
              Integer count = scheduleLogService.modifyScheduleLogDetail(param);
              if(count > 0){
                  resultMap.put("code", SystemConfig.SUCCESS);
                  resultMap.put("msg", "处理成功!");
              }else{
                  resultMap.put("code", SystemConfig.ERROR);
                  resultMap.put("msg", "处理失败,未知的执行任务!");
              }
          }else{
              resultMap.put("code", SystemConfig.ERROR_AES);
              resultMap.put("msg", "处理失败,token失效!");
          }
      } catch (Exception e) {
          e.printStackTrace();
          resultMap.put("code", SystemConfig.ERROR);
          resultMap.put("msg", "处理失败!");
      }
      return resultMap;
  }


}
