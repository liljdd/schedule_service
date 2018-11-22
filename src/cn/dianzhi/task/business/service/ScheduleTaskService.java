package cn.dianzhi.task.business.service;

import cn.dianzhi.task.business.dao.ScheduleTaskDao;
import cn.dianzhi.task.business.job.JobHttpModel;
import cn.dianzhi.task.business.model.ScheduleTask;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * ScheduleTaskService.
 * 
 * @author lee
 * @since 2016年5月20日
 */
@Service
public class ScheduleTaskService {

  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ScheduleTaskService.class);
  @Autowired
  private Scheduler scheduler;

  private static ScheduleTaskDao scheduleTaskDao;

  @Autowired
  public void setScheduleTaskDao(ScheduleTaskDao scheduleTaskDao) {
    ScheduleTaskService.scheduleTaskDao = scheduleTaskDao;
  }

  public int insert(ScheduleTask scheduleTask) {
    return scheduleTaskDao.insert(scheduleTask);
  }

  public static synchronized int update(ScheduleTask scheduleTask) {
    return scheduleTaskDao.update(scheduleTask);
  }

  public boolean delete(ScheduleTask scheduleTask) {
    if (scheduleTask != null) {
      int id = scheduleTask.getId();
      scheduleTask = new ScheduleTask();
      scheduleTask.setId(id);
      scheduleTask.setDelete(true);
    }
    return scheduleTaskDao.update(scheduleTask) > 0;
  }

  /**
   * 获取所有的定时任务 .
   * 
   * @return list
   */
  public List<ScheduleTask> loadAll() {
    List<ScheduleTask> result = new ArrayList<ScheduleTask>();
    try {
      GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
      Set<JobKey> jobKeys;
      jobKeys = scheduler.getJobKeys(matcher);
      logger.debug("jobKeys:" + jobKeys.size());
      Iterator<JobKey> it = jobKeys.iterator();
      while (it.hasNext()) {
        result.add(load(it.next()));
      }
    } catch (SchedulerException err) {
      logger.error("获取全部定时任务异常{}", err);
    }
    return result;
  }

  /**
   * load ScheduleTask.
   * 
   * @param jobKey jobKey
   * @return ScheduleTask
   */
  public ScheduleTask load(JobKey jobKey) {
    ScheduleTask scheduleTask = null;
    try {
      JobDetail jobDetail = scheduler.getJobDetail(jobKey);
      int id = jobDetail.getJobDataMap().getInt(JobHttpModel.SCHEDULE_TASK_ID);
      scheduleTask = scheduleTaskDao.load(id);

      @SuppressWarnings("unchecked")
      List<CronTrigger> list = (List<CronTrigger>) scheduler.getTriggersOfJob(jobKey);
      if (!list.isEmpty()) {
        CronTrigger trigger = list.get(0);
        TriggerState triggerState = scheduler.getTriggerState(new TriggerKey(jobKey.getName(), jobKey.getGroup()));
        scheduleTask.setState(triggerState.toString());
        scheduleTask.setPreviousFireTime(trigger.getPreviousFireTime());
        scheduleTask.setNextFireTime(trigger.getNextFireTime());
      }

    } catch (SchedulerException err) {
      logger.error("异常", err);
    }

    return scheduleTask;
  }

  public static ScheduleTask load(int id) {
    return scheduleTaskDao.load(id);
  }
}
