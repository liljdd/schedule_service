package cn.dianzhi.task.business.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dianzhi.task.business.model.ScheduleLog;
import cn.dianzhi.task.business.model.ScheduleLogDetail;
import cn.dianzhi.task.business.service.ScheduleLogService;

/**
 * 
 * @author lee
 * @since 2016年5月23日
 */
@Controller
@RequestMapping("/log/")
public class ScheduleLogAction {

  private static final Logger logger = LoggerFactory.getLogger(ScheduleLogAction.class);
  @Autowired
  private ScheduleLogService scheduleLogService;

  /**
   * 
   * .
   * 
   * @param triggerGroup
   * @param triggerKey
   * @param jobGroup
   * @param jobKey
   * @return
   */
  @RequestMapping("/list.htm")
  public String list(Integer scheduleTaskId, Model model) {
    ScheduleLog logInfo = new ScheduleLog();
    logInfo.setScheduleTaskId(scheduleTaskId);

    List<ScheduleLog> scheduleLogs = scheduleLogService.query(logInfo);

    model.addAttribute("scheduleLogs", scheduleLogs);
    return "/log/list";
  }
  /**
   * 异步加载日志
   * @param id
   * @param logType
   * @return
   */
  @ResponseBody
  @RequestMapping("/loadLog.json")
  public String loadLog(
          Integer id,
          Integer logType
          ) {
      String result= "暂无日志";
      try{
          ScheduleLogDetail detail = new ScheduleLogDetail();
          detail.setId(id);
          detail.setLogType(logType);
          
          detail = scheduleLogService.selectLogDetailByIdAndTypeById(detail);
          if(detail!=null){
              result = logType==1?detail.getResult():detail.getLogContent();
              if(StringUtils.isBlank(result)){
                  result = "暂无日志";
              }
          }
      }catch (Exception e) {
          e.printStackTrace();
      }
      return result;
  }
}
