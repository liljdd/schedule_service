/**
 * 
 */
package cn.dianzhi.task.business.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.dianzhi.task.business.model.ScheduleLog;
import cn.dianzhi.task.business.model.ScheduleLogDetail;

/**
 * @author lee
 *
 */
@Repository
public interface ScheduleLogDao {

  public void insert(ScheduleLog logInfo);

  public List<ScheduleLog> load(ScheduleLog logInfo);

  /**
   * @param logInfo
   */
  public void update(ScheduleLog logInfo);
  
  //更新日志内容
  public Integer updateCodeByExcuteToken(ScheduleLog scheduleLog);
  
  //查询日志内容
  public ScheduleLogDetail selectLogDetailByIdAndTypeById(ScheduleLogDetail scheduleLogDetail);
  //查询日志内容
  public ScheduleLogDetail selectLogDetailByIdAndTypeByExcuteToken(ScheduleLogDetail scheduleLogDetail);
  //更新日志内容
  public Integer updateSchedultLogDetailByExcuteToken(ScheduleLogDetail scheduleLogDetail);
  //插入日志内容
  public Integer insertSchedultLogDetail(ScheduleLogDetail scheduleLogDetail);

}
