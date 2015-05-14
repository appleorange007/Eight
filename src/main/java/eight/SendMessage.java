package eight;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public class SendMessage {
    private final String EXCHANGE_REC = "tableR";
    private final String EXCHANGE_SEND = "tableS";

    private Message message;

    private boolean running;
    private int tablePos;
    String exchangeName;
    Connection connection;
    Channel channelR;
    Channel channelS;

    private static SendMessage single = null;

    public static SendMessage getInstance() {
        if (single == null) {
            single = new SendMessage();
        }
        return single;
    }

    private SendMessage() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channelR = connection.createChannel();
            channelR.exchangeDeclare(EXCHANGE_REC, "fanout");

            channelS = connection.createChannel();
            channelS.exchangeDeclare(EXCHANGE_SEND, "fanout");
            message = new Message();
            message.setSerial(-1);

            runAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTablePos() {
        return tablePos;
    }

    public void setTablePos(int tablePos) {
        this.tablePos = tablePos;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean end) {
        this.running = end;
    }

    public void runAction() {
        if (isRunning()) {
            return;
        }

        running = true;

        Runnable actionThread = new Runnable() {
            public void run() {
                try {

                    while (running) {
                        popAction();
                    }

                    channelR.close();
                    channelS.close();
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
                }

            }
        };
        Thread t = new Thread(actionThread);
        t.start();
    }

    public synchronized void popAction() throws IOException {

        if (!message.isValid())
        {

            try {
                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // do with message
        String str = message.toString();
        System.out.println("popAction message:" + str);
        if (isBanker()) {
            if (message.isIncludeServer()) {
                channelR.basicPublish(EXCHANGE_REC, "", null, str.getBytes());
            }
            channelS.basicPublish(EXCHANGE_SEND, "", null, str.getBytes());
        } else {
            channelR.basicPublish(EXCHANGE_REC, "", null, str.getBytes());
        }
        message.clear();
        notify();
    }

    public synchronized void pushAction(Message another) {
        if (message.isValid()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        message = another;
        message.setPos(tablePos);
        System.out.println(message.toString());

        notify();
    }

    boolean isBanker() {
        return tablePos == 0;
    }

}
