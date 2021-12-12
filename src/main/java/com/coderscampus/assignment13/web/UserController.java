package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.AddressService;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	@Autowired AccountService accountService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		
		model.put("user", new User());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		
		return "users";
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findByIdAndAccounts(userId);

		model.put("users", Arrays.asList(user));
		model.put("user", user);
		model.put("address", user.getAddress());
		model.put("accounts", user.getAccounts());
		
		return "users";
	}
	
	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getOneAccount (ModelMap model, @PathVariable Long userId, Long accountId) {
		model.put("user", userService.findByIdAndAccounts(userId));
		model.put("account", accountService.findAccount(accountId));
		return "account";
		
	}
	
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String PostOneAccount (Account account, @PathVariable Long userId, Long accountId) {
	
		accountService.save(account);
		return "redirect:/users/{userId}/accounts/"+account.getAccountId();
		
	}
	
	
	@PostMapping("/users/{userId}")
	public String postOneUser (@PathVariable Long userId, User user) {
//		User currentUser = userService.findByIdAndAccounts(userId);
//		user.setAccounts(currentUser.getAccounts());
		
		user.getAddress().setUserId(userId);
		user.getAddress().setUser(user);
		userService.saveUser(user);
	
		return "redirect:/users/"+user.getUserId();
		
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
	
	
}
