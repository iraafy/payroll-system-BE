package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lawencon.pss.constant.Roles;
import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.role.RoleResDto;
import com.lawencon.pss.dto.user.ChangePasswordReqDto;
import com.lawencon.pss.dto.user.ClientDropdownResDto;
import com.lawencon.pss.dto.user.CreateUserReqDto;
import com.lawencon.pss.dto.user.LoginReqDto;
import com.lawencon.pss.dto.user.LoginResDto;
import com.lawencon.pss.dto.user.UserResDto;
import com.lawencon.pss.model.File;
import com.lawencon.pss.model.Role;
//import com.lawencon.pss.model.File;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.repository.CompanyRepository;
import com.lawencon.pss.repository.FileRepository;
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
    private final FileRepository fileRepository;
    private final ClientAssignmentRepository clientAssignmentRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PrincipalService principalService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var user = userRepository.findByEmail(email);
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
		response.setEmail(user.getEmail());
		response.setRoleCode(user.getRole().getRoleCode());
		response.setCompanyName(user.getCompany().getCompanyName());
		response.setImageProfile(user.getFile().getId());
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
		final var fileContent = request.getFileContent();
		final var fileExt = request.getFileExt();
		final var roleOpt = roleRepository.findById(roleId);
        final var role = roleOpt.get();
        final var companyOpt = companyRepository.findById(companyId);
        final var company = companyOpt.get();
		final var password = GeneratorUtil.randomString();
		final var createdBy = principalService.getUserId();
		System.out.println(createdBy);
		
		final var encodedPassword = passwordEncoder.encode(password);
		
		final File file = new File();
		file.setFileContent(fileContent);
		file.setFileExt(fileExt);
		file.setFileName("Profile Picture");
		file.setCreatedBy(createdBy);
		final var fileResult = fileRepository.save(file);
		
		user.setEmail(userEmail);
		user.setPassword(encodedPassword);
		user.setFullName(userFullName);
		user.setRole(role);
		user.setCompany(company);
		user.setFile(fileResult);
		user.setCreatedBy(createdBy);
		
		final var result = userRepository.save(user);
		
		final Runnable runnable = () -> {
			final var subjectEmail = "Selamat Datang di Aplikasi Payroll Service System!";
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
    	final List<UserResDto> response = new ArrayList<>();
    	final var results = userRepository.findAll();
    	for (User u: results) {
    		final var user = new UserResDto();
    		user.setId(u.getId());
    		user.setFullName(u.getFullName());
    		user.setRoleName(u.getRole().getRoleName());
    		user.setEmail(u.getEmail());
    		user.setCompanyName(u.getCompany().getCompanyName());
    		if(u.getFile() != null) {
    			user.setPath(u.getFile().getStoredPath());
    		}
    		response.add(user);
    	}
        return response;
    }

    @Override
    public List<UserResDto> getAllPs() {
    	final List<UserResDto> response = new ArrayList<>();
    	final var results = userRepository.findByRoleRoleCode(Roles.PS.getCode());
    	for (User u: results) {
    		final var user = new UserResDto();
    		user.setId(u.getId());
    		user.setFullName(u.getFullName());
    		user.setRoleName(u.getRole().getRoleName());
    		user.setCompanyName(u.getCompany().getCompanyName());
    		if(u.getFile() != null) {
    			user.setPath(u.getFile().getStoredPath());
    		}
    		response.add(user);
    	}
        return response;
    }

    @Override
    public List<ClientDropdownResDto> getAllClient() {
    	final List<ClientDropdownResDto> response = new ArrayList<>();
    	final var results = userRepository.findByRoleRoleCode(Roles.CL.getCode());
    	for (User u: results) {
    		final var user = new ClientDropdownResDto();
    		user.setId(u.getId());
    		user.setClientName(u.getFullName());
    		final var clientAssignmentOpt = clientAssignmentRepository.findByClientId(u.getId());
    		
    		if (clientAssignmentOpt.isPresent()) {    			
    			final var clientAssignment = clientAssignmentOpt.get();
    			final var psName = clientAssignment.getPs().getFullName();
    			user.setPsName(psName);
    		}
    		
    		response.add(user);
    	}
        return response;
    }

    @Override
    public List<UserResDto> getAvailableClients() {
    	final List<UserResDto> response = new ArrayList<>();
    	final var results = userRepository.findAvailableClient(Roles.CL.getCode());
    	for (User u: results) {
    		final var user = new UserResDto();
    		user.setId(u.getId());
    		user.setFullName(u.getFullName());
    		user.setCompanyName(u.getCompany().getCompanyName());
    		
    		response.add(user);
    	}
    	return response;
    }

    @Override
    public UpdateResDto updatePassword(ChangePasswordReqDto request) {
    	final var response = new UpdateResDto();
    	final var updatedBy = principalService.getUserId();
    	final var userFound = userRepository.findById(updatedBy);
    	
    	if (userFound.isPresent()) {
    		final var user = userFound.get();
    		
    		final var oldPwd = request.getOldPassword();
    		final var oldPwdHash = user.getPassword();
    		
    		if (passwordEncoder.matches(oldPwd, oldPwdHash)) {
    			final var newPwd = passwordEncoder.encode(request.getNewPassword());
    			user.setPassword(newPwd);
    			final var result = userRepository.save(user);
    			
    			response.setVer(result.getVer());
    			response.setMessage("Berhasil mengubah kata sandi!");
    			
    		} else {
    			response.setMessage("Gagal mengubah kata sandi :(");
    		}
    	}    	
    	
        return response;
    }

	@Override
	public List<RoleResDto> getAllRoles() {
		List<Role> roles = roleRepository.findAll();
		List<RoleResDto> rolesRes = new ArrayList<RoleResDto>();
		for(Role role : roles) {
			final RoleResDto roleRes = new RoleResDto();
			roleRes.setId(role.getId());
			roleRes.setRoleCode(role.getRoleCode());
			roleRes.setRoleName(role.getRoleName());
			rolesRes.add(roleRes);
		}
		return rolesRes;
	}

    


}
