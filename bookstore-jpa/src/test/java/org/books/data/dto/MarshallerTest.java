package org.books.data.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

public class MarshallerTest {

    @Test
    public void testMarshallOneItem() throws JAXBException {

        String rsXml = "<orderRequest>\n" +
                "  <customerNr>C-1</customerNr>\n" +
                "  <items>\n" +
                "    <bookInfo>\n" +
                "      <isbn>1935182994</isbn>\n" +
                "      <title>Java EE</title>\n" +
                "      <price>15.80</price>\n" +
                "    </bookInfo>\n" +
                "    <quantity>3</quantity>\n" +
                "  </items>\n" +
                "</orderRequest>";

        JAXBContext jaxbContext = JAXBContext.newInstance(OrderRequest.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        OrderRequest response = (OrderRequest) unmarshaller.unmarshal(new ByteArrayInputStream(rsXml.getBytes()));

        Assert.assertEquals("1935182994", response.getItems().get(0).getBook().getIsbn());
    }

    @Test
    public void testMarshallMultipleItems() throws JAXBException {

        String rsXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<orderRequest>\n" +
                "   <customerNr>C-1</customerNr>\n" +
                "   <items>\n" +
                "      <bookInfo>\n" +
                "         <isbn>1935182994</isbn>\n" +
                "         <title>Java EE</title>\n" +
                "         <price>15.00</price>\n" +
                "      </bookInfo>\n" +
                "      <quantity>3</quantity>\n" +
                "   </items>\n" +
                "   <items>\n" +
                "      <bookInfo>\n" +
                "         <isbn>1935182994</isbn>\n" +
                "         <title>Java EE</title>\n" +
                "         <price>15.00</price>\n" +
                "      </bookInfo>\n" +
                "      <quantity>3</quantity>\n" +
                "   </items>\n" +
                "</orderRequest>";

        JAXBContext jaxbContext = JAXBContext.newInstance(OrderRequest.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        OrderRequest response = (OrderRequest) unmarshaller.unmarshal(new ByteArrayInputStream(rsXml.getBytes()));

        Assert.assertEquals(2, response.getItems().size());
        Assert.assertEquals("1935182994", response.getItems().get(0).getBook().getIsbn());
    }



}
