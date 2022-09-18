package workqueue;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class Customer2 {
    public static void main(String [] args) throws IOException {
        //获取连接
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("work",true,false,false,null);

        channel.basicConsume("work",true,new DefaultConsumer(channel){

            @Override//最后一个参数从消息队列中取出消息
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者-2"+new String(body));
            }
        });

    }
}
