package cn.dianzhi.task.business.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dianzhi.task.business.model.ScheduleTask;
import cn.dianzhi.task.business.service.ScheduleTaskService;

/**
 * 
 */
@Controller
@RequestMapping("/user")
public class UserAction {

  private static final Logger logger = LoggerFactory.getLogger(UserAction.class);
  
  @Value("${login.username}")
  private String userName;
  @Value("${login.password}")
  private String password;

  /**
   * 用户登录
   * @return
 * @throws Exception 
   */
  @RequestMapping("/tologin")
  public String toLogin(
		  HttpSession session
          ) throws Exception{
	  if(session.getAttribute("isLogin")!=null && (Integer)session.getAttribute("isLogin") == 1){
		  return "redirect:/task/list.do";
	  }
      return "/user/login";
  }
  
  @ResponseBody
  @RequestMapping("/login.json")
  public Map<String,Object> login(
          HttpSession session
          ,@RequestParam(value="userName",defaultValue="")String userName
          ,@RequestParam(value="password",defaultValue="")String password
  ) throws Exception{
      Map<String,Object> result = new HashMap<String, Object>();
      result.put("isSuccess", true);
      if(!userName.equals(this.userName) || !password.equals(this.password)){
          result.put("isSuccess", false);
          result.put("msg", "用户名或密码错误");
      }else{
          session.setAttribute("isLogin", 1);
      }
      return result;
  }
}
