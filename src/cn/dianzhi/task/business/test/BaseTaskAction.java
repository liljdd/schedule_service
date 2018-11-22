package cn.dianzhi.task.business.test;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * super action.
 * 
 * @author lee
 * @since 2016年5月25日
 */
public abstract class BaseTaskAction {

  public abstract String execute(HttpServletRequest request);

  @RequestMapping("/test.htm")
  @ResponseBody
  public String test() {
    InvokeResult result = new InvokeResult("success");
    return result.toString();
  }

  class InvokeResult {
    private String result;
    private String msg;

    public InvokeResult(String result) {
      this.result = result;
    }

    public String getResult() {
      return result;
    }

    public void setResult(String result) {
      this.result = result;
    }

    public String getMsg() {
      return msg;
    }

    public void setMsg(String msg) {
      this.msg = msg;
    }

    @Override
    public String toString() {
      return JSONObject.toJSONString(this);
    }
  }

}
