package br.com.gabriel.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import java.util.Enumeration;
import java.util.Scanner;

public class ConsumidorFila {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        //true ou false para comportamento transacional (com commit, rollback, parecido com banco de dados
        //Session.tipo de confirmacao da mensagem
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        Destination fila = (Destination) context.lookup("financeiro");

        MessageConsumer consumer = session.createConsumer(fila);

        consumer.setMessageListener(message -> {
            TextMessage textMessage  = (TextMessage) message;
            try {
//                message.acknowledge();
                session.commit();
                System.out.println(textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        new Scanner(System.in).next();

        session.close();
        connection.close();
        context.close();
    }

    //não consome a mensagem, apenas avisa que elas estão lá
    static void browseMessages(Session session, Destination fila) throws Exception {
        QueueBrowser browser = session.createBrowser((Queue) fila);

        Enumeration msgs = browser.getEnumeration();
        while (msgs.hasMoreElements()) {
            TextMessage msg = (TextMessage) msgs.nextElement();
            System.out.println("Message: " + msg.getText());
        }
    }
}
