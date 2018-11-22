package cn.dianzhi.task.business.util;

import java.util.Random;

/**
 * tools.
 * 
 * @author lee
 * @since 2016年5月25日
 */
public final class StringUtils {

  /**
   * 随机字符串.
   * 
   * @param length
   *          字符串长度
   * @return 生成的字符串
   */
  public static String getInvokeToken(int length) {
    String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#%()[]";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(str.length());
      sb.append(str.charAt(number));
    }
    return sb.toString();
  }

}
