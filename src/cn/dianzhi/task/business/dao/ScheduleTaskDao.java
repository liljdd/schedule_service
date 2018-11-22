/**
 * 
 */
package cn.dianzhi.task.business.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.dianzhi.task.business.model.ScheduleTask;

/**
 * @author lee
 *
 */
public interface ScheduleTaskDao {

  public int update(ScheduleTask scheduleTask);

  /**
   * 更新
   * 
   * @param scheduleTask
   * @return 更新行数
   */
  public int insert(ScheduleTask scheduleTask);

  public List<ScheduleTask> loadAll();

  public ScheduleTask load(@Param("id") int id);
}
