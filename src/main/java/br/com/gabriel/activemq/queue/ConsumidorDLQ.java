package br.com.gabriel.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import java.util.Scanner;

public class ConsumidorDLQ {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("DLQ");

        MessageConsumer consumer = session.createConsumer(fila);

        consumer.setMessageListener(System.out::println);

        new Scanner(System.in).next();

        session.close();
        connection.close();
        context.close();
    }
}
