package workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;
import utils.RabbitMQUtils;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {
    //生产消息
    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        //通过工具类获取连接对象
        Connection connection = RabbitMQUtils.getConnection();

        //获取连接中通道
        Channel channel = connection.createChannel();

        //通道绑定对应消息队列
        //参数1:  队列名称 如果队列不存在自动创建
        //参数2:  用来定义队列特性是否要持久化 true 持久化队列   false 不持久化
        //参数3:  exclusive 是否独占队列  true 独占队列   false  不独占
        //参数4:  autoDelete: 是否在消费完成后自动删除队列  true 自动删除  false 不自动删除
        //参数5:  额外附加参数
        //发布消息
        channel.queueDeclare("work", true, false, false, null);
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", "work", null, ("====>:我是消息"+i).getBytes());
        }

        //调用工具类
        RabbitMQUtils.closeConnectionAndChanel(channel,connection);

    }
}


