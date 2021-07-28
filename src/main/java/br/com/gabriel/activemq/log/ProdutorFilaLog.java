package br.com.gabriel.activemq.log;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import java.util.Scanner;

public class ProdutorFilaLog {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("LOG");

        MessageProducer producer = session.createProducer(fila);

//        for(int i = 0; i < 1000; i ++) {
//            System.out.println(i);
//            Message message = session.createTextMessage("<pedido><id>" + i + "</id></pedido>");
//            producer.send(message);
//        }

        Message message = session.createTextMessage("ERROR | Listening for connections at ws://THE-ROCK-NITRO:61614?maximumConnections=1000&wireFormat.maxFrameSize=104857600");
        //prioridade e tempo de vida da mensagem
        producer.send(message, DeliveryMode.NON_PERSISTENT, 9, 50000);

        new Scanner(System.in).next();

        session.close();
        connection.close();
        context.close();
    }
}
