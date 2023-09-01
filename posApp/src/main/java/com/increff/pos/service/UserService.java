package com.increff.pos.service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.model.EnumData;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		isValidPassword(p.getPassword());
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	public UserPojo get(int id){
		return dao.selectById(id);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) throws ApiException{
		UserPojo pojo= get(id);
		if(pojo.getRole() == EnumData.Role.valueOf("SUPERVISOR")){
			throw new ApiException("Cannot delete supervisor");
		}
		dao.delete(id);
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
	}

	public static boolean isValidPassword(String password) throws ApiException {
		String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
		// Check for minimum length
		if (password.length() < 8) {
			throw new ApiException("Password must be at least 8 characters long.");
		}

		// Check for complexity
		boolean hasUppercase = false;
		boolean hasLowercase = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;

		for (char ch : password.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				hasUppercase = true;
			} else if (Character.isLowerCase(ch)) {
				hasLowercase = true;
			} else if (Character.isDigit(ch)) {
				hasDigit = true;
			} else if (SPECIAL_CHARACTERS.contains(Character.toString(ch))) {
				hasSpecialChar = true;
			}
		}
		if (!hasUppercase) {
			throw new ApiException("Password must contain at least one uppercase letter.");
		}
		if (!hasLowercase) {
			throw new ApiException("Password must contain at least one lowercase letter.");
		}
		if (!hasDigit) {
			throw new ApiException("Password must contain at least one digit.");
		}
		if (!hasSpecialChar) {
			throw new ApiException("Password must contain at least one special character (" + SPECIAL_CHARACTERS + ").");
		}
		return true;
	}

}
