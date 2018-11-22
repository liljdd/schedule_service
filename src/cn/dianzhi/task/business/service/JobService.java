package cn.dianzhi.task.business.service;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * jobService.
 * 
 * @author lee
 * @since 2016年5月20日
 */
@Service
public class JobService {

  private static final Logger logger = LoggerFactory.getLogger(JobService.class);

  private static Scheduler scheduler;

  /**
   * 更新jobDetail.
   * 
   * @param jobDetail jobDetail
   */
  public static void update(JobDetail jobDetail) {
    try {
      scheduler.addJob(jobDetail, true, true);
    } catch (Exception err) {
      logger.error("更新job异常", err);
    }
  }

  @Autowired
  public void setScheduler(Scheduler scheduler) {
    JobService.scheduler = scheduler;
  }
}
