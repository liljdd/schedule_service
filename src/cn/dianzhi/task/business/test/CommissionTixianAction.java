package cn.dianzhi.task.business.test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试.
 * 
 * @author lee
 * @since 2016年5月25日
 */
@Controller
@RequestMapping("/test/")
public class CommissionTixianAction extends BaseTaskAction {
  String tmpResult = "error";

  @RequestMapping("execute.htm")
  @ResponseBody
  @Override
  public String execute(HttpServletRequest request) {
    InvokeResult result = new InvokeResult("success");
    tmpResult = "error".equals(tmpResult) ? "success" : "error";
    result.setMsg(tmpResult);
    return result.toString();
  }

}
