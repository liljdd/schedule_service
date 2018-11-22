
package cn.dianzhi.task.business.plugin;

import java.text.MessageFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.quartz.plugins.history.LoggingTriggerHistoryPlugin;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dianzhi.task.business.model.ScheduleLog;
import cn.dianzhi.task.business.service.ScheduleLogService;
import cn.dianzhi.task.business.util.SpringContextUtils;

/**
 * 记录日志.
 * 
 * @author lee
 * @since 2016年5月20日
 */
public class LoggingPlugin extends LoggingTriggerHistoryPlugin implements SchedulerPlugin, TriggerListener {

  private static final Logger logger = LoggerFactory.getLogger(LoggingPlugin.class);
  @Autowired
  private ScheduleLogService scheduleLogService;

  /**
   * triggerFired.
   */
  public void triggerFired(Trigger trigger, JobExecutionContext context) {
    if (!getLog().isInfoEnabled()) {
      return;
    }

    Object[] args = { trigger.getKey().getName(), trigger.getKey().getGroup(), trigger.getPreviousFireTime(), trigger.getNextFireTime(),
        new java.util.Date(), context.getJobDetail().getKey().getName(), context.getJobDetail().getKey().getGroup(),
        Integer.valueOf(context.getRefireCount()) };

    getLog().info(MessageFormat.format(getTriggerFiredMessage(), args));

    // insertLogInfo(trigger, context);
  }

  /**
   * triggerMisfired.
   */
  public void triggerMisfired(Trigger trigger) {

    Object[] args = { trigger.getKey().getName(), trigger.getKey().getGroup(), trigger.getPreviousFireTime(), trigger.getNextFireTime(),
        new java.util.Date(), trigger.getJobKey().getName(), trigger.getJobKey().getGroup() };

    getLog().info(MessageFormat.format(getTriggerMisfiredMessage(), args));
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
    if (!getLog().isInfoEnabled()) {
      return;
    }

    String instrCode = "UNKNOWN";
    if (triggerInstructionCode == CompletedExecutionInstruction.DELETE_TRIGGER) {
      instrCode = "DELETE TRIGGER";
    } else if (triggerInstructionCode == CompletedExecutionInstruction.NOOP) {
      instrCode = "DO NOTHING";
    } else if (triggerInstructionCode == CompletedExecutionInstruction.RE_EXECUTE_JOB) {
      instrCode = "RE-EXECUTE JOB";
    } else if (triggerInstructionCode == CompletedExecutionInstruction.SET_ALL_JOB_TRIGGERS_COMPLETE) {
      instrCode = "SET ALL OF JOB'S TRIGGERS COMPLETE";
    } else if (triggerInstructionCode == CompletedExecutionInstruction.SET_TRIGGER_COMPLETE) {
      instrCode = "SET THIS TRIGGER COMPLETE";
    }

    Object[] args = { trigger.getKey().getName(), trigger.getKey().getGroup(), trigger.getPreviousFireTime(), trigger.getNextFireTime(),
        new java.util.Date(), context.getJobDetail().getKey().getName(), context.getJobDetail().getKey().getGroup(),
        Integer.valueOf(context.getRefireCount()), triggerInstructionCode.toString(), instrCode };

    getLog().info(MessageFormat.format(getTriggerCompleteMessage(), args));

    // updateLogInfo(trigger, context, triggerInstructionCode);
  }

  /**
   * 插入日志.
   * 
   * @param trigger trigger
   * @param context context
   */
  private void insertLogInfo(Trigger trigger, JobExecutionContext context) {
    try {
      if (scheduleLogService == null) {
        scheduleLogService = SpringContextUtils.getBean("logInfoService", ScheduleLogService.class);
      }
      ScheduleLog logInfo = new ScheduleLog();
      logInfo.setTriggerGroup(trigger.getKey().getGroup());
      logInfo.setTriggerKey(trigger.getKey().getName());
      logInfo.setJobGroup(context.getJobDetail().getKey().getGroup());
      logInfo.setJobKey(context.getJobDetail().getKey().getName());
      logInfo.setFireTime(new Date());
      logInfo.setPreviousFireTime(trigger.getPreviousFireTime());
      logInfo.setNextFireTime(trigger.getNextFireTime());
      logInfo.setUrl(context.getJobDetail().getJobDataMap().getString("url"));
      ScheduleLogService.insert(logInfo);

    } catch (Exception err) {
      logger.error("插入LogInfo异常{}", err);
    }
  }

  /**
   * 更新日志.
   * 
   * @param trigger trigger
   * @param context context
   * @param triggerInstruction triggerInstruction
   */
  private void updateLogInfo(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstruction) {

    try {
      ScheduleLog logInfo = new ScheduleLog();
      logInfo.setTriggerGroup(trigger.getKey().getGroup());
      logInfo.setTriggerKey(trigger.getKey().getName());
      logInfo.setJobGroup(context.getJobDetail().getKey().getGroup());
      logInfo.setJobKey(context.getJobDetail().getKey().getName());
      logInfo = scheduleLogService.load(logInfo);
      if (logInfo != null) {
        Date completeTime = new Date();
        logInfo.setCompleteTime(completeTime);
        logInfo.setCostTime(completeTime.getTime() - logInfo.getFireTime().getTime());
        scheduleLogService.update(logInfo);
      }
    } catch (Exception err) {
      logger.error("更新LogInfo异常{}", err);
    }
  }
}
