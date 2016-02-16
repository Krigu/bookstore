package org.books.application;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import org.books.application.amazon.helper.AmazonSecurityHelper;
import org.books.application.amazon.soap.AWSECommerceService;
import org.books.application.amazon.soap.AWSECommerceServicePortType;
import org.books.application.amazon.soap.Item;
import org.books.application.amazon.soap.ItemAttributes;
import org.books.application.amazon.soap.ItemLookup;
import org.books.application.amazon.soap.ItemLookupRequest;
import org.books.application.amazon.soap.ItemLookupResponse;
import org.books.application.amazon.soap.ItemSearch;
import org.books.application.amazon.soap.ItemSearchRequest;
import org.books.application.amazon.soap.ItemSearchResponse;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book.Binding;

@Stateless(name = "AmazonCatalog")
public class AmazonCatalogBean implements AmazonCatalog {

    private static final Logger LOGGER = Logger.getLogger(AmazonCatalogBean.class.getName());
    private static final String ID_TYPE = "ISBN";
    private static final String RESPONSE_GROUP = "ItemAttributes";
    private static final String SEARCH_INDEX = "Books";

    @Override
    public BookDTO findBook(String isbn) throws BookNotFoundException {
        LOGGER.log(Level.INFO, "AmazonCatalogBean findBook"); 
        AWSECommerceService amazonService = new AWSECommerceService();
        AWSECommerceServicePortType amazonWS = amazonService.getAWSECommerceServicePort();
        
        ItemLookupResponse itemLookupResponse = amazonWS.itemLookup(getItemLookup(isbn));
        
        if (!bookFound(itemLookupResponse)) {
            throw new BookNotFoundException();
        }
                
        return getBookFromResponse(itemLookupResponse);
    }
    
    @Override
    public List<BookInfo> searchBooks(String keywords) {
        LOGGER.log(Level.INFO, "AmazonCatalogBean searchBooks"); 
        BigInteger pageIndex = BigInteger.ONE;
        BigInteger totalPages = BigInteger.TEN;
        List<BookInfo> books = new ArrayList<>();

        AWSECommerceService amazonService = new AWSECommerceService();
        AWSECommerceServicePortType amazonWS = amazonService.getAWSECommerceServicePort();

        while (pageIndex.compareTo(totalPages) != 1) {
        
            ItemSearchResponse itemSearchResponse = amazonWS.itemSearch(getItemSearch(keywords, pageIndex));

            for (Item item: itemSearchResponse.getItems().get(0).getItem()) {
                if (isValidBook(item.getItemAttributes())) {
                    books.add(new BookInfo(item.getItemAttributes().getISBN(), 
                                           item.getItemAttributes().getTitle(), 
                                           new BigDecimal(item.getItemAttributes().getListPrice().getAmount()).divide(BigDecimal.valueOf(100))));
                }
            }

            totalPages = (itemSearchResponse.getItems().get(0).getTotalPages().compareTo(BigInteger.TEN) == -1) ? itemSearchResponse.getItems().get(0).getTotalPages() : BigInteger.TEN;
                       
            pageIndex = pageIndex.add(BigInteger.ONE);
        }

        return books;
    }

    private boolean isValidBook(ItemAttributes itemAttributes) {
        if (itemAttributes.getBinding().equals("Kindle Edition")) {
            itemAttributes.setBinding("Ebook");
        }
        try {
            Binding.valueOf(itemAttributes.getBinding());
            
            if (itemAttributes.getAuthor() == null) {
                return false;
            }
            if (itemAttributes.getNumberOfPages() == null) {
                return false;
            }
            if (itemAttributes.getListPrice() == null || itemAttributes.getListPrice().getAmount() == null) {
                return false;
            }
            if (itemAttributes.getPublicationDate() == null) {
                return false;
            }
            if (itemAttributes.getPublisher() == null) {
                return false;
            }
            if (itemAttributes.getTitle() == null) {
                return false;
            }
            
            return true;
        }
        catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, "Binding does not exist: " + ex.getMessage());
            return false;
        }

    }
    
    private boolean bookFound(ItemLookupResponse itemLookupResponse) {
        if (itemLookupResponse.getItems().get(0).getRequest().getErrors() != null) {
            if ("AWS.ECommerceService.ItemNotAccessible".equals(itemLookupResponse.getItems().get(0).getRequest().getErrors().getError().get(0).getCode()) ||
                    "AWS.InvalidParameterValue".equals(itemLookupResponse.getItems().get(0).getRequest().getErrors().getError().get(0).getCode())) {
                return false;
            }
            else {
                throw new EJBException(itemLookupResponse.getOperationRequest().getErrors().getError().get(0).getMessage());
            }
        }

        if (!isValidBook(itemLookupResponse.getItems().get(0).getItem().get(0).getItemAttributes())) {
            return false;
        }

        return true;
    }

    private BookDTO getBookFromResponse(ItemLookupResponse itemLookupResponse) {
        ItemAttributes itemAttributes = itemLookupResponse.getItems().get(0).getItem().get(0).getItemAttributes();
        if (itemAttributes.getBinding().equals("Kindle Edition")) {
            itemAttributes.setBinding("Ebook");
        }
        
        return new BookDTO(String.join(",", itemAttributes.getAuthor()),
                                   Binding.valueOf(itemAttributes.getBinding()),
                                   itemAttributes.getISBN(),
                                   itemAttributes.getNumberOfPages().intValue(),
                                   (new BigDecimal(itemAttributes.getListPrice().getAmount()).divide(BigDecimal.valueOf(100))),
                                   Integer.parseInt(itemAttributes.getPublicationDate().substring(0,4)),
                                   itemAttributes.getPublisher(),
                                   itemAttributes.getTitle());
    }
    
    private ItemLookup getItemLookup(String isbn) {
        ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
        itemLookupRequest.getItemId().add(isbn);
        itemLookupRequest.setIdType(ID_TYPE);
        itemLookupRequest.getResponseGroup().add(RESPONSE_GROUP);
        itemLookupRequest.setSearchIndex(SEARCH_INDEX);

        ItemLookup itemLookup = new ItemLookup();
        itemLookup.setAssociateTag(AmazonSecurityHelper.ASSOCIATE_TAG);
        itemLookup.getRequest().add(itemLookupRequest);
        
        return itemLookup;
    }
    
    private ItemSearch getItemSearch(String keywords, BigInteger pageIndex) {
        ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
        itemSearchRequest.setItemPage(pageIndex);
        itemSearchRequest.setKeywords(keywords);
        itemSearchRequest.getResponseGroup().add(RESPONSE_GROUP);
        itemSearchRequest.setSearchIndex(SEARCH_INDEX);

        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setAssociateTag(AmazonSecurityHelper.ASSOCIATE_TAG);
        itemSearch.getRequest().add(itemSearchRequest);
        
        return itemSearch;
    }
}
