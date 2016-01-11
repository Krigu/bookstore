/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.books.data.dao.BookDAOLocal;
import org.books.data.dao.OrderDAOLocal;
import org.books.data.entity.Order;

@MessageDriven(
        activationConfig = {
            @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/orderQueue"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
        }
)
public class OrderProcessor implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(OrderProcessor.class.getName());
    @EJB
    private OrderDAOLocal orderDAO;
    @Resource
    private TimerService timerService;
    @EJB
    private BookDAOLocal bookDAO;

    @Override
    public void onMessage(Message message) {
        LOGGER.info("Recieve message");
        MapMessage msg = (MapMessage) message;

        try {
            Long orderId = msg.getLong("orderId");
            Order order = orderDAO.find(orderId);
            if (order.getStatus().equals(Order.Status.accepted)) {
                order.setStatus(Order.Status.processing);
                order = orderDAO.update(order);
                LOGGER.info("Send a message to ship the order");
                //15 seconds
                timerService.createSingleActionTimer(15000, new TimerConfig(order.getId(), true));
            }
        } catch (JMSException ex) {
            LOGGER.log(Level.WARNING,"Message error", ex);
        }
    }

    /*@Override
    public void onMessage(Message message) {
        LOGGER.info("************************Recieve message");
        ObjectMessage msg = (ObjectMessage) message;

        try {
            Book book = msg.getBody(Book.class);
            book.setAuthors("new autors");
            book = bookDAO.update(book);
            LOGGER.info("************************book updated " + book.getAuthors());
            timerService.createSingleActionTimer(15000, new TimerConfig(book, true));
        } catch (JMSException ex) {
            LOGGER.error("Message error", ex);
        }
    }

    @Timeout
    public void shipperBook(Timer timer) {
        LOGGER.info("*******************333Ship the order");
        Book b = (Book) timer.getInfo();
        b = bookDAO.find(b.getId());
        b.setAuthors("old authors");
        b = bookDAO.update(b);
        LOGGER.info("****************3End time : " + b.getAuthors());
    }*/
    @Timeout
    public void shipperOrder(Timer timer) {
        LOGGER.info("Ship the order");
        Long orderId = (Long) timer.getInfo();
        Order order = orderDAO.find(orderId);
        if (order.getStatus().equals(Order.Status.processing)) {
            order.setStatus(Order.Status.shipped);
            orderDAO.update(order);
        }
    }
}
