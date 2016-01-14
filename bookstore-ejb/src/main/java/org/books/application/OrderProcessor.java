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
import javax.jms.JMSException;;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
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

    @Override
    public void onMessage(Message message) {
        LOGGER.info("Recieve message");;
        MapMessage msg = (MapMessage) message;

        try {
            Long orderId = msg.getLong("orderId");
            Order order = orderDAO.find(orderId);
            //5 seconds
            //use to simulate the work of a person
            timerService.createSingleActionTimer(5000, new TimerConfig(order.getId(), true));
        } catch (JMSException ex) {
            LOGGER.log(Level.WARNING, "Message error", ex);
        }
    }

    @Timeout
    public void shipperOrder(Timer timer) throws JMSException {
        LOGGER.info("Ship the order");
        Long orderId = (Long) timer.getInfo();
        Order order = orderDAO.find(orderId);
        if (order.getStatus().equals(Order.Status.accepted)) {
            order.setStatus(Order.Status.processing);
            orderDAO.update(order);
            //5 seconds
            timerService.createSingleActionTimer(5000, new TimerConfig(order.getId(), true));
        } else if (order.getStatus().equals(Order.Status.processing)) {
            order.setStatus(Order.Status.shipped);
            orderDAO.update(order);
        }
    }
}
