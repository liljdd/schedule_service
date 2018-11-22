package cn.dianzhi.task.business.util;

import org.springframework.stereotype.Component;

/**
 * ss.
 * 
 * @author lee
 * @since 2016年5月25日
 */
@Component
public final class SystemConfig {

    public final static String TASK_KEY_ONE = "54dSD4dSD32fdg5SA5SD32SF1S23484SHsdfSOgyIn5713841S81sEgRNsJNd5kCL-one";
    public final static String TASK_KEY_TWO = "48s7dfSZF1FDS318846SD31SSD17s5s2rg87S21gfd5g4IOUHJ24sdfBGYU654sdf-two";
      
    public static final String CALLBACK = Config.getInstance().getString("CALL_BACK");
    public static final String LOG_SAVE_URI = Config.getInstance().getString("LOG_SAVE_URI");
    
    public static final Integer EXCETUING = 300;                  //执行中
    public static final Integer ERROR = 200;                  //错误
    public static final Integer ERROR_CLASS = 201;            //错误  类名不对引起的错误
    public static final Integer ERROR_METHOD = 202;           //错误    方法名不对引起的错误
    public static final Integer ERROR_AES = 203;              //错误    加密的信息解密之后对不上
    public static final Integer ERROR_PARAMTER = 204;         //错误    参数为空
    public static final Integer SUCCESS = 100;                //成功


}
