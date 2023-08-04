package com.increff.pos.service;

import com.increff.pos.model.EnumData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.UserPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceTest extends AbstractUnitTest {

	@Autowired
	private UserService service;

	@Test
	public void testAdd() throws ApiException{
		UserPojo p= new UserPojo();
		p.setEmail("abc@gmail.com");
		p.setPassword("Increff@123");
		p.setRole(EnumData.Role.valueOf("SUPERVISOR"));

		service.add(p);

		String expectedEmail= "abc@gmail.com";
		String expectedPassword= "Increff@123";
		String expectedRole = "SUPERVISOR";

		UserPojo q= service.get(expectedEmail);
		assertEquals(expectedPassword,q.getPassword());
		assertEquals(expectedRole,q.getRole().toString());
	}

	@Test
	public void testAddExistingUser() throws ApiException {
		UserPojo p= new UserPojo();
		p.setEmail("abc@gmail.com");
		p.setPassword("Increff@123");
		p.setRole(EnumData.Role.valueOf("SUPERVISOR"));
		service.add(p);

		UserPojo q= new UserPojo();
		q.setEmail("abc@gmail.com");
		q.setPassword("Increff@123");
		q.setRole(EnumData.Role.valueOf("SUPERVISOR"));

		try {
			service.add(q);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "User with given email already exists", e.getMessage());
		}
	}

	@Test
	public void testGetAll() throws ApiException {
		UserPojo p= new UserPojo();
		p.setEmail("abc@gmail.com");
		p.setPassword("Increff@123");
		p.setRole(EnumData.Role.valueOf("SUPERVISOR"));
		service.add(p);

		UserPojo q= new UserPojo();
		q.setEmail("abcdef@gmail.com");
		q.setPassword("PosProj@123");
		q.setRole(EnumData.Role.valueOf("OPERATOR"));
		service.add(q);

		List<UserPojo> list = service.getAll();

		assertEquals(2,list.size());
		UserPojo userPojo1= list.get(0);
		UserPojo userPojo2= list.get(1);

		String expectedEmail1= "abc@gmail.com";
		String expectedPassword1= "Increff@123";
		String expectedRole1 = "SUPERVISOR";

		String expectedEmail2= "abcdef@gmail.com";
		String expectedPassword2= "PosProj@123";
		String expectedRole2 = "OPERATOR";
		assertEquals(expectedEmail1,userPojo1.getEmail());
		assertEquals(expectedPassword1,userPojo1.getPassword());
		assertEquals(expectedRole1,userPojo1.getRole().toString());

		assertEquals(expectedEmail2,userPojo2.getEmail());
		assertEquals(expectedPassword2,userPojo2.getPassword());
		assertEquals(expectedRole2,userPojo2.getRole().toString());
	}

	@Test
	public void testDelete() throws ApiException{
		UserPojo p= new UserPojo();
		p.setEmail("abc@gmail.com");
		p.setPassword("Increff@123");
		p.setRole(EnumData.Role.valueOf("SUPERVISOR"));

		service.add(p);

		List<UserPojo> list = service.getAll();
		assertEquals(1,list.size());

		UserPojo q= service.get("abc@gmail.com");
		service.delete(q.getId());

		List<UserPojo> list1 = service.getAll();
		assertEquals(0,list1.size());
	}

	@Test
	public void testIsValidPasswordLength() throws ApiException {
		String password= "123";

		try {
			service.isValidPassword(password);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Password must be at least 8 characters long.", e.getMessage());
		}
	}
	@Test
	public void testIsValidPasswordNoUpperCase() throws ApiException {
		String password= "sfafewdsfds";

		try {
			service.isValidPassword(password);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Password must contain at least one uppercase letter.", e.getMessage());
		}
	}
	@Test
	public void testIsValidPasswordNoLowerCase() throws ApiException {
		String password= "DSFHBDSHFBE";

		try {
			service.isValidPassword(password);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Password must contain at least one lowercase letter.", e.getMessage());
		}
	}

	@Test
	public void testIsValidPasswordNoDigit() throws ApiException {
		String password= "DSFdsfsdfs";

		try {
			service.isValidPassword(password);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Password must contain at least one digit.", e.getMessage());
		}
	}
	@Test
	public void testIsValidPasswordNoSpecialCharacter() throws ApiException {
		String password= "DSFdsfs134";

		try {
			service.isValidPassword(password);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Password must contain at least one special character (!@#$%^&*()-_=+[]{}|;:,.<>?).", e.getMessage());
		}
	}
}
