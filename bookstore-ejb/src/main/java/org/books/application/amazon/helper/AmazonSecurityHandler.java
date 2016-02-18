/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.amazon.helper;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import static javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class AmazonSecurityHandler implements SOAPHandler<SOAPMessageContext> {
    
    private static final Logger LOGGER = Logger.getLogger(AmazonSecurityHandler.class.getName());
    private static final String AWS_NAMESPACE = "http://security.amazonaws.com/doc/2007-01-01/";
    private static final String AWS_NAMESPACE_PREFIX = "aws";

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        LOGGER.log(Level.INFO, "handleMessage: " + context.getMessage().toString());
        if (!(Boolean) context.get(MESSAGE_OUTBOUND_PROPERTY)) {
         /*   try {
                context.getMessage().writeTo(System.out);
            } catch (SOAPException | IOException ex) {
                Logger.getLogger(AmazonSecurityHandler.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            return true;
        }
    
        try {
            QName operation = (QName) context.get(MessageContext.WSDL_OPERATION);
            AmazonSecurityHelper amazonSecurityHelper = new AmazonSecurityHelper(operation.getLocalPart());            

            SOAPMessage message = context.getMessage();
            SOAPHeader header = message.getSOAPHeader();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            if (header == null) {
                header = envelope.addHeader();
            }
            
            QName qNameAWSAccessKeyId = new QName(AWS_NAMESPACE, "AWSAccessKeyId");
            SOAPHeaderElement aWSAccessKeyId = header.addHeaderElement(qNameAWSAccessKeyId );
            aWSAccessKeyId.addTextNode(AmazonSecurityHelper.ACCESS_KEY);
            
            QName qNameTimestamp = new QName(AWS_NAMESPACE, "Timestamp");
            SOAPHeaderElement timestamp = header.addHeaderElement(qNameTimestamp);
            timestamp.addTextNode(amazonSecurityHelper.getTimestamp());

            QName qNameSignature = new QName(AWS_NAMESPACE, "Signature");
            SOAPHeaderElement signature = header.addHeaderElement(qNameSignature);
            signature.addTextNode(amazonSecurityHelper.getSignature());

            message.saveChanges();
            
            //context.getMessage().writeTo(System.out);
        } catch (SOAPException | NoSuchAlgorithmException | InvalidKeyException ex) {
            LOGGER.log(Level.SEVERE, "Setting security header failed:" + ex.getMessage());
        }
        
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }

}
