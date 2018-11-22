/**
 * 
 */
package cn.dianzhi.task.business.model;

import java.util.Date;

/**
 * 日志.
 * 
 * @author lee
 * 
 */
public class ScheduleLogDetail {

    private int id;
    private Integer scheduleLogId;
    private String result;
    private String logContent;
    private String excuteToken;
    private Date lastModifyTime;
    private Date createTime;
    
    private Integer logType;
    
    public Integer getScheduleLogId() {
        return scheduleLogId;
    }
    public void setScheduleLogId(Integer scheduleLogId) {
        this.scheduleLogId = scheduleLogId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getExcuteToken() {
        return excuteToken;
    }
    public void setExcuteToken(String excuteToken) {
        this.excuteToken = excuteToken;
    }
    public Date getLastModifyTime() {
        return lastModifyTime;
    }
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getLogType() {
        return logType;
    }
    public void setLogType(Integer logType) {
        this.logType = logType;
    }
    public String getLogContent() {
        return logContent;
    }
    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
    
}
