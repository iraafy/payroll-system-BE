package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.user.ChangePasswordReqDto;
import com.lawencon.pss.dto.user.CreateUserReqDto;
import com.lawencon.pss.dto.user.LoginReqDto;
import com.lawencon.pss.dto.user.LoginResDto;
import com.lawencon.pss.dto.user.UserResDto;
//import com.lawencon.pss.model.File;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.CompanyRepository;
import com.lawencon.pss.repository.RoleRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.JwtService;
import com.lawencon.pss.service.PrincipalService;
import com.lawencon.pss.service.UserService;
import com.lawencon.pss.util.GeneratorUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PrincipalService principalService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
            final User result = user.get();
			return new org.springframework.security.core.userdetails.User(result.getEmail(), result.getPassword(), new ArrayList<>());
		}
		throw new UsernameNotFoundException("Invalid Login");
    }

    @Override
    public LoginResDto login(LoginReqDto request) {
        final var email = request.getEmail();
		final var result = userRepository.findByEmail(email);
        final var user = result.get();
		final var response = new LoginResDto();
		Map<String, Object> claim = new HashMap<>();
		claim.put("id", user.getId());
		final var token = jwtService.generateJwt(claim);
		
		response.setId(user.getId());
		response.setFullName(user.getFullName());
		response.setRoleCode(user.getRole().getRoleCode());
		response.setPath(user.getPicture().getPath());
		response.setToken(token);
		
		return response;
    }

    @Override
    public InsertResDto createUser(CreateUserReqDto request) {
        final var user = new User();
		final var userEmail = request.getEmail();
		final var userFullName = request.getFullName();
		final var roleId = request.getRoleId();
		final var companyId = request.getCompanyId();
//		 final var filePath = request.getPath();
		final var roleOpt = roleRepository.findById(roleId);
        final var role = roleOpt.get();
        final var companyOpt = companyRepository.findById(companyId);
        final var company = companyOpt.get();
		final var password = GeneratorUtil.randomString();
		final var createdBy = principalService.getUserId();
		
		final var encodedPassword = passwordEncoder.encode(password);
		
//		final File fileToInsert = new File();
//		fileToInsert.setPath(request.getPath());
//		final var file = fileDao.insertFile(fileToInsert);
		
		user.setEmail(userEmail);
		user.setPassword(encodedPassword);
		user.setFullName(userFullName);
		user.setRole(role);
		user.setCompany(company);
		// user.setPicture(file);
		user.setCreatedBy(createdBy);
		
		final var result = userRepository.save(user);
		
		final Runnable runnable = () -> {
			final var subjectEmail = "Selamat Datang di Aplikasi TMS!";
			final var bodyEmail = "Akun anda telah berhasil di buat!\n\n"
					+ "email : " + userEmail
					+ "\npassword : " + password
					+ "\n\nSelamat mencoba aplikasi kami! :D";
			
			emailService.sendEmail(userEmail, subjectEmail, bodyEmail);			
        };
        
        final Thread mailThread = new Thread(runnable);
        mailThread.start();
		
		final var response = new InsertResDto();
		response.setId(result.getId());
		response.setMessage("Berhasil menambahkan user");
		
		return response;
    }

    @Override
    public List<UserResDto> getAllUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserResDto> getAllPs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserResDto> getAllClient() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserResDto> getClientsByPS(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserResDto> getAvailableClients() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UpdateResDto updatePassword(ChangePasswordReqDto request) {
        // TODO Auto-generated method stub
        return null;
    }

    


}
