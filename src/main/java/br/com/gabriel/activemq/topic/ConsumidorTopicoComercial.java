package br.com.gabriel.activemq.topic;

import br.com.gabriel.activemq.modelo.Pedido;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import java.util.Scanner;

public class ConsumidorTopicoComercial {

    public static void main(String[] args) throws Exception {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.setClientID("comercial");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) context.lookup("loja");

        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");

        consumer.setMessageListener(message -> {
            ObjectMessage objectMessage  = (ObjectMessage) message;
            try {
                Pedido pedido = (Pedido) objectMessage.getObject();
                System.out.println(pedido.getCodigo());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        new Scanner(System.in).next();

        session.close();
        connection.close();
        context.close();
    }
}
