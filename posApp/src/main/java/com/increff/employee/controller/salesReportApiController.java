package com.increff.employee.controller;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class salesReportApiController {

	@Autowired
	private OrderItemService service;
	@Autowired
	private OrderService orderService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProductDao productDao;
	LocalDate startDate,endDate;

	int findBrandCategory(List<SalesReportData> list,SalesReportData s){
		String a,b,c,d;
		b=s.getBrand();
		d=s.getCategory();
		System.out.println(s.getId() + " " +b+ " " +d);
		for(SalesReportData e: list){
			a=e.getBrand();
			c=e.getCategory();
			if(a.equals(b) && c.equals(d)){
				System.out.println("found "+ e.getId());
				return list.indexOf(e);
			}
		}return -1;
	}
	@ApiOperation(value = "Get list of all orders")
	@RequestMapping(path = "/api/report/salesReport/relevent", method = RequestMethod.GET)
	public List<SalesReportData> getReleventAll(LocalDate startDate1,LocalDate endDate1){//,String brand,String category) {
		System.out.println("GetRelevent in api!!!!!!!!!!!!!!!!!!!");
		List<SalesReportData> list = new ArrayList<SalesReportData>();
		LocalDateTime startDate = startDate1.atStartOfDay();
		LocalDateTime endDate = endDate1.atTime(LocalTime.MAX);
		List<OrderPojo> list1 = orderService.getOrderDates(startDate,endDate);
		for (OrderPojo p : list1) {
			System.out.println("The list is:");
			System.out.println(p.getDateTime());
		}
//		if (brand != "" || category != "") {
//
//		}
		List<OrderItemPojo> orderList= service.getRelevantAll(list1);
		for (OrderItemPojo p : orderList) {
			System.out.println("The list is:");
			System.out.println(p.getId());
		}
//
		for (OrderItemPojo p : orderList) {
			SalesReportData s= convert(p);
			int i=findBrandCategory(list,s);
			if(i==-1) {
				list.add(convert(p));
			}
			else{
				list.set(i,convert3(list.get(i),p));
			}
		}
		return list;
	}
	@ApiOperation(value = "Get list of all orders")
	@RequestMapping(path = "/api/report/salesReport", method = RequestMethod.GET)
	public List<SalesReportData> getAll() {
		System.out.println("Hey there!!!!!!!!!!!!!!!!!!!");
		List<OrderItemPojo> orderList = service.getAll();
		List<SalesReportData> list = new ArrayList<SalesReportData>();
//		LocalDateTime startDate = startDate1.atStartOfDay();
//		LocalDateTime endDate = endDate1.atTime(LocalTime.MAX);
//		List<OrderPojo> list1 = orderService.getOrderDates(startDate,endDate);
//		for (OrderPojo p : list1) {
//			System.out.println("The list is:");
//			System.out.println(p.getDateTime());
//		}
//		List<OrderItemPojo> orderList= service.getRelevantAll(list1);
//		for (OrderItemPojo p : orderList) {
//			System.out.println("The list is:");
//			System.out.println(p.getId());
//		}
		for (OrderItemPojo p : orderList) {
			SalesReportData s= convert(p);
			int i=findBrandCategory(list,s);
			if(i==-1) {
				list.add(convert(p));
			}
			else{
				list.set(i,convert3(list.get(i),p));
			}
		}
		return list;
	}
	@ApiOperation(value = "Show date time")
	@RequestMapping(path = "/api/report/salesReport", method = RequestMethod.POST)
	public List<SalesReportData> add(@RequestBody SalesReportData form) throws ApiException {
		startDate= form.getStartDate();
		endDate= form.getEndDate();
		String brand= form.getBrand();
		String category= form.getCategory();
		System.out.println("startDate= "+startDate+" + endDate= " + endDate);
		List<SalesReportData> l=getReleventAll(startDate,endDate);//,brand,category);
		return l;
	}

	private SalesReportData convert(OrderItemPojo p) {
		SalesReportData d = new SalesReportData();
		int brand_category=productDao.select(p.getProductId()).getBrand_category();
		d.setId(p.getId());
		d.setDate((orderDao.select(p.getOrderId()).getDateTime()).toLocalDate());
		d.setBrand(brandDao.select(brand_category).getBrand());
		d.setCategory(brandDao.select(brand_category).getCategory());
		d.setQuantity(p.getQuantity());
		d.setRevenue(p.getSellingPrice()*p.getQuantity());
		return d;
	}
	private SalesReportData convert3(SalesReportData d,OrderItemPojo p) {
		d.setQuantity(d.getQuantity()+p.getQuantity());
		d.setRevenue(d.getRevenue()+(p.getSellingPrice()*p.getQuantity()));
		return d;
	}
}
