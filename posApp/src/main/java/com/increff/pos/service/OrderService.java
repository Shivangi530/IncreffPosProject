package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.EnumData;
import com.increff.pos.model.InvoiceForm;
import com.increff.pos.model.OrderItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OutwardOrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class OrderService {

	@Autowired
	private OrderDao dao;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;

	@Autowired
	private InventoryService inventoryService;

	@Value("${invoice.url}")
	private String invoiceUrl;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OutwardOrderPojo pojo) throws ApiException {
		dao.insert(pojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OutwardOrderPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<OutwardOrderPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, String status) throws ApiException {
		OutwardOrderPojo existingPojo = getCheck(id);
		if(status.equals("INVOICED")){
			existingPojo.setStatus(EnumData.Status.INVOICED);
		}
		if(status.equals("CANCELED")){
			existingPojo.setStatus(EnumData.Status.CANCELED);
		}
	}

	@Transactional
	public OutwardOrderPojo getCheck(int id) throws ApiException {
		OutwardOrderPojo pojo = dao.select(id);
		if (pojo == null) {
			throw new ApiException("Order with given ID does not exit, id: " + id);
		}
		return pojo;
	}
	@Transactional
	public List<OutwardOrderPojo> getOrderDates(LocalDateTime startDate, LocalDateTime endDate) {
		return dao.selectDates(startDate,endDate);
	}

	public List<OrderItemPojo> selectByOrderId(Integer orderId) throws ApiException{
		return orderItemService.getOrderList(orderId);
	}

	public ResponseEntity<byte[]> getInvoicePDF(Integer id) throws Exception {
		InvoiceForm invoiceForm = generateInvoiceForOrder(id);
		RestTemplate restTemplate = new RestTemplate();
		byte[] contents = Base64.getDecoder().decode(restTemplate.postForEntity(invoiceUrl, invoiceForm, byte[].class).getBody());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		String filename = "invoice.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}
	public InvoiceForm generateInvoiceForOrder(Integer orderId) throws ApiException
	{
		InvoiceForm invoiceForm = new InvoiceForm();
		OutwardOrderPojo outwardOrderPojo = getCheck(orderId);
		invoiceForm.setOrderId(outwardOrderPojo.getId());
		invoiceForm.setPlacedDate(outwardOrderPojo.getDateTime().toString());
		List<OrderItemPojo> orderItemPojoList = selectByOrderId(outwardOrderPojo.getId());
		List<OrderItem> orderItemList = new ArrayList<>();
		for(OrderItemPojo pojo: orderItemPojoList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(pojo.getId());
			String productName = productService.getCheck(pojo.getProductId()).getName();
			orderItem.setProductName(productName);
			orderItem.setQuantity(pojo.getQuantity());
			orderItem.setSellingPrice(pojo.getSellingPrice());
			orderItemList.add(orderItem);
		}
		invoiceForm.setOrderItemList(orderItemList);
		return invoiceForm;
	}

}
