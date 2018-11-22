package cn.dianzhi.task.business.filter;

import cn.dianzhi.task.business.util.HttpUtils;
import org.apache.http.client.ClientProtocolException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lilj
 * @date 18/11/09
 */
public class QueueMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage tm = (TextMessage) message;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd  HH:mm:ss");
            System.out.println("QueueMessageListener监听到了文本消息 " + sdf.format(new Date()) + "：\t" + tm.getText());
            //do something ...
            //调用其他项目的url 去实际执行这个task
            String invokeUrl = "http://localhost:8082/message/list.do";
            Map<String, String> data = new HashMap<>();
            String result = HttpUtils.post(invokeUrl, data, 100000, 3);
            System.out.println("========result=" + result);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
