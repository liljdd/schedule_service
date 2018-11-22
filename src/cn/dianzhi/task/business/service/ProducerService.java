package cn.dianzhi.task.business.service;

import org.apache.activemq.ScheduledMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * @author lilj
 * @date 18/11/12
 */
@Service
public class ProducerService {
    @Resource(name = "jmsQueueTemplate")
    private JmsTemplate jmsQueueTemplate;
    @Resource(name = "jmsTopicTemplate")
    private JmsTemplate jmsTopicTemplate;

    public void sendMessage(Destination destination, final String msg) {
        System.out.println(Thread.currentThread().getName() + " 向队列" + destination.toString() + "发送消息---------------------->" + msg);
        //jmsQueueTemplate.setDeliveryDelay(5 * 1000); //报错
        jmsQueueTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(msg);
                //需要配置activemq.xml才能生效
                long delay = 3 * 1000;
                long period = 5 * 1000;
                int repeat = 2;
                //整点开始执行
                textMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "0 * * * *");
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
                textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
                return textMessage;
            }
        });
    }

    public void sendMessage(final String msg) {
        String destination = jmsQueueTemplate.getDefaultDestinationName();
        System.out.println(Thread.currentThread().getName() + " 向队列" + destination + "发送消息---------------------->" + msg);
        jmsQueueTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }


}
