package org.books.application;

import org.books.data.dao.OrderDAOLocal;
import org.books.data.entity.Order;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.books.data.entity.OrderItem;
import org.books.application.Messages;

@MessageDriven(name = "OrderProcessor",
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
    
    //the number of milliseconds that must elapse before the timer expires.
    @Resource(name = "duration")
    private int duration;
    @EJB
    private MailService mailService;

    
    @Override
    public void onMessage(Message message) {
        LOGGER.info("Recieve message");;
        MapMessage msg = (MapMessage) message;

        try {
            Long orderId = msg.getLong("orderId");
            Order order = orderDAO.find(orderId);
            sendConfirmationMail(order);
            //use to simulate the work of a person
            timerService.createSingleActionTimer(duration, new TimerConfig(order.getId(), true));
        } catch (JMSException ex) {
            LOGGER.log(Level.WARNING, "Message error", ex);
        }
    }

    @Timeout
    public void changeOrderState(Timer timer) throws JMSException {
        LOGGER.info("Ship the order");
        Long orderId = (Long) timer.getInfo();
        Order order = orderDAO.find(orderId);
        if (order.getStatus().equals(Order.Status.accepted)) {
            order.setStatus(Order.Status.processing);
            orderDAO.update(order);
            timerService.createSingleActionTimer(duration, new TimerConfig(order.getId(), true));
        } else if (order.getStatus().equals(Order.Status.processing)) {
            order.setStatus(Order.Status.shipped);
            orderDAO.update(order);
            sendShippingMail(order);
        }
    }
    
    private void sendConfirmationMail(Order order) {
        String itemList = "";
        for (OrderItem item : order.getItems()) {
            itemList += item.getBook().getAuthors() + ": " + item.getBook().getTitle() + ", CHF " + item.getPrice().toString() + " (" + item.getQuantity().toString() + "x)\n";
        }
        String emailAdr = order.getCustomer().getEmail();
        String subject = Messages.getString("CONFIRMATION_MAIL_SUBJECT");
        String body = Messages.getString("CONFIRMATION_MAIL_BODY", order.getNumber(), itemList, order.getAmount());

        mailService.sendMail(emailAdr, subject, body);

    }
        
    private void sendShippingMail(Order order) {
        String adrStr = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName() + "\n" + 
                        order.getAddress().getStreet() + "\n" + 
                        order.getAddress().getPostalCode() + " " + order.getAddress().getCity() + "\n" + 
                        order.getAddress().getCountry();
        String emailAdr = order.getCustomer().getEmail();
        String subject = Messages.getString("SHIPPING_MAIL_SUBJECT");
        String body = Messages.getString("SHIPPING_MAIL_BODY", order.getNumber(), adrStr);
        
        mailService.sendMail(emailAdr, subject, body);
    }
    
}
