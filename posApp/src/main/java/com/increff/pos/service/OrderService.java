package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.InvoiceForm;
import com.increff.pos.model.OrderItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
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
import java.time.format.DateTimeFormatter;
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

	@Value("${invoice.url}")
	private String invoiceUrl;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderPojo p) throws ApiException {
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<OrderPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id) throws ApiException {
		OrderPojo existingPojo = getCheck(id);
		existingPojo.setStatus(true);
		System.out.println("existingPojo.setStatus: "+existingPojo.getStatus());
		dao.update(existingPojo);
	}

	@Transactional
	public OrderPojo getCheck(int id) throws ApiException {
		OrderPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Order with given ID does not exit, id: " + id);
		}
		return p;
	}
	@Transactional
	public List<OrderPojo> getOrderDates(LocalDateTime startDate, LocalDateTime endDate) {
		return dao.selectDates(startDate,endDate);
	}

	public List<OrderItemPojo> selectByOrderId(Integer orderId) {
		return orderItemService.selectByOrderId(orderId);
	}
	public ResponseEntity<byte[]> getInvoicePDF(Integer id) throws Exception {
		InvoiceForm invoiceForm = generateInvoiceForOrder(id);
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("invoice url: "+invoiceUrl);
		String url=invoiceUrl;
		System.out.println("invoice url: "+url);
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
		OrderPojo orderPojo = get(orderId);
		invoiceForm.setOrderId(orderPojo.getId());
		invoiceForm.setPlacedDate(orderPojo.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")).toString());
		List<OrderItemPojo> orderItemPojoList = selectByOrderId(orderPojo.getId());
		List<OrderItem> orderItemList = new ArrayList<>();
		for(OrderItemPojo p: orderItemPojoList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(p.getId());
			String productName = productService.getCheck(p.getProductId()).getName();
			orderItem.setProductName(productName);
			orderItem.setQuantity(p.getQuantity());
			orderItem.setSellingPrice(p.getSellingPrice());
			orderItemList.add(orderItem);
		}
		invoiceForm.setOrderItemList(orderItemList);
		return invoiceForm;
	}
}
