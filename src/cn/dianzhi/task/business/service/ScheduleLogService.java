/**
 * 
 */
package cn.dianzhi.task.business.service;

import cn.dianzhi.task.business.dao.ScheduleLogDao;
import cn.dianzhi.task.business.job.JobHttpModel;
import cn.dianzhi.task.business.model.ScheduleLog;
import cn.dianzhi.task.business.model.ScheduleLogDetail;
import cn.dianzhi.task.business.util.SystemConfig;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ScheduleLogService.
 * 
 * @author lee
 *
 */
@Service
public class ScheduleLogService {

  private static final Logger logger = LoggerFactory.getLogger(ScheduleLogService.class);
  
  
  private static ScheduleLogDao scheduleLogDao;

  @Autowired
  public void setScheduleLogDao(ScheduleLogDao scheduleLogDao) {
    ScheduleLogService.scheduleLogDao = scheduleLogDao;
  }

  public static void insert(ScheduleLog logInfo) {
    scheduleLogDao.insert(logInfo);
  }

  /**
   * 查询日志.
   * 
   * @param scheduleLog scheduleLog
   * @return scheduleLog
   */
  public ScheduleLog load(ScheduleLog scheduleLog) {
    List<ScheduleLog> list = scheduleLogDao.load(scheduleLog);
    if (list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  public List<ScheduleLog> query(ScheduleLog logInfo) {
    return scheduleLogDao.load(logInfo);
  }

  public void update(ScheduleLog logInfo) {
    scheduleLogDao.update(logInfo);
  }

  /**
   * 插入执行日志.
   * 
   * @param context
   *          执行context
   */
  public static synchronized void insertLogInfo(JobExecutionContext context, JSONObject result) {
    Trigger trigger = context.getTrigger();
    try {
      int scheduleTaskId = context.getJobDetail().getJobDataMap().getInt(JobHttpModel.SCHEDULE_TASK_ID);

      ScheduleLog scheduleLog = new ScheduleLog();
      scheduleLog.setTriggerGroup(trigger.getKey().getGroup());
      scheduleLog.setTriggerKey(trigger.getKey().getName());
      scheduleLog.setJobGroup(context.getJobDetail().getKey().getGroup());
      scheduleLog.setJobKey(context.getJobDetail().getKey().getName());
      scheduleLog.setFireTime(context.getFireTime());
      scheduleLog.setUrl(context.getJobDetail().getJobDataMap().getString("url"));
      scheduleLog.setPreviousFireTime(trigger.getPreviousFireTime());
      scheduleLog.setNextFireTime(trigger.getNextFireTime());
      
      scheduleLog.setScheduleTaskId(scheduleTaskId);
      scheduleLog.setExcuteToken(result.getString("excuteToken"));
//      scheduleLog.setResult(result.toJSONString());

      scheduleLog.setCode(result.getInteger("code"));

      Date completeTime = new Date();
      scheduleLog.setCompleteTime(completeTime);
      scheduleLog.setCostTime(completeTime.getTime() - scheduleLog.getFireTime().getTime());
      insert(scheduleLog);

      //插入logDetail
      ScheduleLogDetail detail = new ScheduleLogDetail();
      detail.setCreateTime(new Date());
      detail.setLastModifyTime(new Date());
      detail.setResult(result.toJSONString());
      detail.setExcuteToken(result.getString("excuteToken"));
      detail.setScheduleLogId(scheduleLog.getId());
      
      scheduleLogDao.insertSchedultLogDetail(detail);
      
    } catch (Exception err) {
      logger.error("插入LogInfo异常{}", err);
    }
  }

  /**
   *更新日志的详细内容
   * @param excuteToken
   * @return
   */
  public Integer modifyScheduleLogDetail(Map<String,Object> param){
      
      //如果是callBack回调//修改scheduleLog 的 code值
      if(param.get("isCallBack")!=null && ((Integer)param.get("isCallBack")) == 1){
          ScheduleLog scheduleLog = new ScheduleLog();
          scheduleLog.setExcuteToken(param.get("excuteToken").toString());
          scheduleLog.setCode((Integer)param.get("code"));
          scheduleLogDao.updateCodeByExcuteToken(scheduleLog);
      }
      
      //修改scheduleDetail的内容 将日志内容追加进去
      ScheduleLogDetail scheduleLogDetail = new ScheduleLogDetail();
      scheduleLogDetail.setExcuteToken(param.get("excuteToken").toString());
      scheduleLogDetail = scheduleLogDao.selectLogDetailByIdAndTypeByExcuteToken(scheduleLogDetail);
      if(scheduleLogDetail == null){
          return 0;
      }
      //修改result内容
      if(((Integer)param.get("logType"))==1){
          
          if(param.get("msg").toString().contains("201")){
              param.put("msg", param.get("msg").toString().replace("201", "类不存在"));
          }else if(param.get("msg").toString().contains("202")){
              param.put("msg", param.get("msg").toString().replace("202", "方法不存在"));
          }else if(param.get("msg").toString().contains("203")){
              param.put("msg", param.get("msg").toString().replace("203", "加密信息错误"));
          }else if(param.get("msg").toString().contains("204")){
              param.put("msg", param.get("msg").toString().replace("204", "参数为空"));
          }
          
          scheduleLogDetail.setResult(scheduleLogDetail.getResult()+"<h6 class=\"text-info text-center\">执行回调结果</h6>"+JSONObject.toJSONString(param));
      }else if(((Integer)param.get("logType"))==2){
          //修改logContent内容
          if(StringUtils.isNotBlank(scheduleLogDetail.getLogContent())){
              scheduleLogDetail.setLogContent(scheduleLogDetail.getLogContent()+JSONObject.toJSONString(param.get("logContent").toString()));
          }else{
              scheduleLogDetail.setLogContent(JSONObject.toJSONString(param.get("logContent").toString()));
          }
      }
      scheduleLogDetail.setLastModifyTime(new Date());
      Integer count = scheduleLogDao.updateSchedultLogDetailByExcuteToken(scheduleLogDetail);
      return count;
  }
  
  
  //查询日志内容
  public ScheduleLogDetail selectLogDetailByIdAndTypeById(ScheduleLogDetail scheduleLogDetail){
      return scheduleLogDao.selectLogDetailByIdAndTypeById(scheduleLogDetail);
  }
  //更新日志内容
  public Integer updateSchedultLogDetailByExcuteToken(ScheduleLogDetail scheduleLogDetail){
      return null;
  }
  //插入日志内容
  public Integer insertSchedultLogDetail(ScheduleLogDetail scheduleLogDetail){
      return scheduleLogDao.insertSchedultLogDetail(scheduleLogDetail);
  }
}
