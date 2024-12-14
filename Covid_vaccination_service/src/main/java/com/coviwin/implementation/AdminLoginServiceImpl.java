package com.coviwin.implementation;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coviwin.exception.LoginException;
import com.coviwin.model.Admin;
import com.coviwin.model.CurrentAdminSession;
import com.coviwin.model.LoginDTO;
import com.coviwin.repo.AdminDao;
import com.coviwin.repo.CurrentAdminSessionDao;
import com.coviwin.service.AdminLoginService;

import net.bytebuddy.utility.RandomString;

@Slf4j
@Service
public class AdminLoginServiceImpl implements AdminLoginService{
	@Autowired
	private AdminDao customerdao;
	
	@Autowired
	private CurrentAdminSessionDao currentdao;

	
	
	@Override
	public String loginAccount(LoginDTO dto) throws LoginException {
		log.info("Attempting login for mobile number: {}", dto.getMobileNo());

		Admin existingCustomer = customerdao.findByMobileNo(dto.getMobileNo());
		if(existingCustomer==null) {
			log.error("Admin not found for mobile number: {}", dto.getMobileNo());
			throw new LoginException("Please Enter valid mobile Number");
		}
		Optional<CurrentAdminSession> validCustomerSession= currentdao.findById(existingCustomer.getCustomerId());
		if(validCustomerSession.isPresent()) {
			log.warn("Multiple login attempt detected for admin ID: {}", existingCustomer.getCustomerId());
			throw new LoginException("User already logedin on this number ");
		}
		if(existingCustomer.getPassword().equals(dto.getPassword())) {
			String key=RandomString.make(6); //make unique key
			
			//object bnaya currentUserSession ka aur usme customer ka id and key and localdatetime ko store kraya
			CurrentAdminSession currentUserSession=new CurrentAdminSession(existingCustomer.getCustomerId(),key,LocalDateTime.now());
			currentdao.save(currentUserSession);

			log.info("Admin successfully logged in with ID: {}", existingCustomer.getCustomerId());
			return currentUserSession.toString();
		}
		else {
			log.error("Invalid password attempt for admin ID: {}", existingCustomer.getCustomerId());
			throw new LoginException("Please Enter a valid password");
		}
	}

	@Override
	public String logoutAccount(String key) throws LoginException {
		CurrentAdminSession validCustomerSession = currentdao.findByUuid(key);
		if(validCustomerSession==null) {
			throw new LoginException("User not Loged In with this Number");
		}
		currentdao.delete(validCustomerSession);
		return "Logged Out";
	}

	@Override
	public Boolean authenthicate(String key) throws LoginException {
		
		CurrentAdminSession cdSession = currentdao.findByUuid(key);
		
		if(cdSession==null) throw new LoginException("Admin not active");
		
		return true;
	}

}
