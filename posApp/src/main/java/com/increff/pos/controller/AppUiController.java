package com.increff.pos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}
//	@RequestMapping(value = "/ui/home")
//	public ModelAndView home() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String role = authentication.getAuthorities().iterator().next().getAuthority();
//		System.out.println("The role is : "+role);
//		ModelAndView modelAndView = new ModelAndView();
//		if (role.equals("ROLE_supervisor")) {
//			modelAndView.setViewName("home.html");
//		} else if (role.equals("ROLE_operator")) {
//			modelAndView.setViewName("homeOperator.html");
//		} else {
//			// Handle other roles if needed
//			modelAndView.setViewName("brand.html");
//		}
//
//		return modelAndView;
//	}
	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/order")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/ui/orderItem")
	public ModelAndView orderItem() {
		return mav("orderItem.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}
	@RequestMapping(value = "/ui/createOrder")
	public ModelAndView createOrder() {
		return mav("createOrder.html");
	}
	@RequestMapping(value = "/ui/report/brandReport")
	public ModelAndView brandReport() {
		return mav("brandReport.html");
	}
	@RequestMapping(value = "/ui/report/inventoryReport")
	public ModelAndView inventoryReport() {
		return mav("inventoryReport.html");
	}
	@RequestMapping(value = "/ui/report/salesReport")
	public ModelAndView salesReport() {
		return mav("salesReport.html");
	}
	@RequestMapping(value = "/ui/report/invoiceReport")
	public ModelAndView invoiceReport() { return mav("dayOnDaySalesReport.html"); }
}
