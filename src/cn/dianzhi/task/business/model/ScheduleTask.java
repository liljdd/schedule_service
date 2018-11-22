/**
 * 
 */
package cn.dianzhi.task.business.model;

import java.io.Serializable;
import java.util.Date;

/**
 * ScheduleTask.
 * 
 * @author lee
 *
 */
public class ScheduleTask implements Serializable {
    private static final long serialVersionUID = -1403054430213443146L;

    public ScheduleTask() {
    }

    /**
     * @param name
     * @param group
     */
    public ScheduleTask(String name, String group) {
        super();
        this.name = name;
        this.group = group;
    }

    private int id;
    private String name;
    private String group;
    private String cronExpression;// 表达式
    private String invokeUrl;
    private int invokeCount;
    private Date previousFireTime;
    private Date nextFireTime;
    private String description;
    private long costTime;
    private Integer priority;// 优先级
    private String state;// 状态
    private boolean delete;

    private String taskName;    //className|methodName 需要配置为这种格式

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *          the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *          the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group
     *          the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the cronExpression
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression
     *          the cronExpression to set
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * @return the invokeUrl
     */
    public String getInvokeUrl() {
        return invokeUrl;
    }

    /**
     * @param invokeUrl
     *          the invokeUrl to set
     */
    public void setInvokeUrl(String invokeUrl) {
        this.invokeUrl = invokeUrl;
    }

    /**
     * @return the invokeCount
     */
    public int getInvokeCount() {
        return invokeCount;
    }

    /**
     * @param invokeCount
     *          the invokeCount to set
     */
    public void setInvokeCount(int invokeCount) {
        this.invokeCount = invokeCount;
    }

    /**
     * @return the previousFireTime
     */
    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    /**
     * @param previousFireTime
     *          the previousFireTime to set
     */
    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    /**
     * @return the nextFireTime
     */
    public Date getNextFireTime() {
        return nextFireTime;
    }

    /**
     * @param nextFireTime
     *          the nextFireTime to set
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *          the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the costTime
     */
    public long getCostTime() {
        return costTime;
    }

    /**
     * @param costTime
     *          the costTime to set
     */
    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    /**
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @param priority
     *          the priority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *          the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the delete
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


}
