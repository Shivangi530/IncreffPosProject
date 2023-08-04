package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertArrayEquals;

public class orderDtoTest extends AbstractUnitTest {

    @Autowired
    private orderDto dto;

    @Test
    public void testAdd() throws ApiException {
        int id = dto.add();
        LocalDateTime expectedDateTime = LocalDateTime.now();
        assertEquals(expectedDateTime.getMonthValue(), dto.get(id).getDateTime().getMonthValue());
        assertEquals("CREATED",dto.get(id).getStatus());
    }

    @Test
    public void testUpdate() throws ApiException {
        int id = dto.add();
        dto.update(id,"INVOICED");
        assertEquals("INVOICED",dto.get(id).getStatus());
    }

    @Test
    public void testGetAll() throws ApiException {
        int id1 = dto.add();
        int id2 = dto.add();
        List<OrderData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void testGetInvoicePDF() throws Exception {
        int id= dto.add();
        ResponseEntity<byte[]> response = dto.getInvoicePDF(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
