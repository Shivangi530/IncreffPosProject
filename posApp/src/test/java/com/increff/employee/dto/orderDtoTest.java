package com.increff.employee.dto;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.model.*;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static com.increff.employee.helper.helper.*;
import static junit.framework.TestCase.*;

public class orderDtoTest extends AbstractUnitTest {

    @Autowired
    private orderDto dto;

    @Test
    public void testAdd() throws ApiException {
        int id = dto.add();
        LocalDateTime expectedDateTime = LocalDateTime.now();
        assertEquals(expectedDateTime.getMonthValue(), dto.get(id).getDateTime().getMonthValue());
        assertFalse(dto.get(id).isStatus());
    }

    @Test
    public void testUpdate() throws ApiException {
        int id = dto.add();
        dto.update(id);
        OrderData data = dto.get(id);
        assertTrue(data.isStatus());
    }

    @Test
    public void testGetAll() throws ApiException {
        int id1 = dto.add();
        int id2 = dto.add();
        List<OrderData> list = dto.getAll();
        assertEquals(2, list.size());
    }

}