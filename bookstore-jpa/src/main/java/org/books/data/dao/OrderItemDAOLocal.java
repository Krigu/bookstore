/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dao;

import javax.ejb.Local;
import org.books.data.dao.generic.GenericDAO;
import org.books.data.entity.OrderItem;

/**
 * <h1>OrderItemDAOLocal</h1>
 * <p>
 * Interface for the OrderItem DAO layer
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 **/
@Local
public interface OrderItemDAOLocal extends GenericDAO<OrderItem>{
    
}
