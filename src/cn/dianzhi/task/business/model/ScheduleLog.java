/**
 * 
 */
package cn.dianzhi.task.business.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 日志.
 * 
 * @author lee
 * 
 */
public class ScheduleLog implements Serializable {

  private static final long serialVersionUID = -7402175830954329717L;
  private int id;
  private Integer scheduleTaskId;
  private String triggerGroup;
  private String triggerKey;
  private String jobGroup;
  private String jobKey;
  private Date fireTime;
  private Date previousFireTime;
  private Date nextFireTime;
  private Date completeTime;
  private String url;
  private long costTime;
  private String excuteToken;
  
  private Timestamp lastModifyTime;

  private String result;
  private Integer code;

  public ScheduleLog() {

  }

  /**
   * @param triggerGroup
   * @param triggerKey
   * @param jobGroup
   * @param jobKey
   */
  public ScheduleLog(String triggerGroup, String triggerKey, String jobGroup, String jobKey) {
    super();
    this.triggerGroup = triggerGroup;
    this.triggerKey = triggerKey;
    this.jobGroup = jobGroup;
    this.jobKey = jobKey;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * scheduleTaskId.
   * 
   * @return the scheduleTaskId
   */
  public Integer getScheduleTaskId() {
    return scheduleTaskId;
  }

  /**
   * the scheduleTaskId to set.
   * 
   * @param scheduleTaskId
   *          the scheduleTaskId to set
   */
  public void setScheduleTaskId(Integer scheduleTaskId) {
    this.scheduleTaskId = scheduleTaskId;
  }

  public String getTriggerGroup() {
    return triggerGroup;
  }

  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }

  public String getTriggerKey() {
    return triggerKey;
  }

  public void setTriggerKey(String triggerKey) {
    this.triggerKey = triggerKey;
  }

  public String getJobGroup() {
    return jobGroup;
  }

  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }

  public String getJobKey() {
    return jobKey;
  }

  public void setJobKey(String jobKey) {
    this.jobKey = jobKey;
  }

  public Date getFireTime() {
    return fireTime;
  }

  public void setFireTime(Date fireTime) {
    this.fireTime = fireTime;
  }

  public Date getPreviousFireTime() {
    return previousFireTime;
  }

  public void setPreviousFireTime(Date previousFireTime) {
    this.previousFireTime = previousFireTime;
  }

  public Date getNextFireTime() {
    return nextFireTime;
  }

  public void setNextFireTime(Date nextFireTime) {
    this.nextFireTime = nextFireTime;
  }

  public Date getCompleteTime() {
    return completeTime;
  }

  public void setCompleteTime(Date completeTime) {
    this.completeTime = completeTime;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getCostTime() {
    return costTime;
  }

  public void setCostTime(long costTime) {
    this.costTime = costTime;
  }

  /**
   * result.
   * 
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * result.
   * 
   * @param result
   *          the result to set
   */
  public void setResult(String result) {
    this.result = result;
  }


    public Integer getCode() {
    return code;
}

public void setCode(Integer code) {
    this.code = code;
}

    public String getExcuteToken() {
        return excuteToken;
    }
    
    public void setExcuteToken(String excuteToken) {
        this.excuteToken = excuteToken;
    }

    public Timestamp getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Timestamp lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
    
}
