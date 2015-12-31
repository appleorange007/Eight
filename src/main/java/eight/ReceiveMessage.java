package eight;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiveMessage implements Runnable {
    private final String EXCHANGE_REC = "tableR";
    private final String EXCHANGE_SEND = "tableS";
    private MainInterface main = null;
    Image cardsPic = null;
    Graphics g = null;
    boolean running = false;

    ReceiveMessage(MainInterface mainI, Image cardsPic, Graphics g) {
        this.main = mainI;
        this.cardsPic = cardsPic;
        this.g = g;
    }

    public void setEnd() {
        this.running = false;
    }

    public void run() {
        // TODO Auto-generated method stub
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        String exchangeName;
        if (isBanker()) { // server
            exchangeName = EXCHANGE_REC;
        } else {
            exchangeName = EXCHANGE_SEND;
        }

        try {
            connection = factory.newConnection();
            Channel channel = (connection).createChannel();

            channel.exchangeDeclare(exchangeName, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, "");

            // System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            main.addMsg("Waiting for messages......isBanker:" + isBanker());
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);
            Gson gson = new Gson();
            Dealer deal = new Dealer(main, cardsPic, g);
            running = true;
            while (running) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String body = new String(delivery.getBody());
                Message msg = gson.fromJson(body, Message.class);
                // To handle message.
                deal.dealMessage(msg);
            }
            channel.close();
            connection.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ShutdownSignalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConsumerCancelledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("ReceiveMessage connection closed.");
    }

    private boolean isBanker() {
        return main.getTablePos() == 0;
    }

}
