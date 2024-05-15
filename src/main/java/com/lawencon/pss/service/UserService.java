package com.lawencon.pss.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.user.ChangePasswordReqDto;
import com.lawencon.pss.dto.user.CreateUserReqDto;
import com.lawencon.pss.dto.user.LoginReqDto;
import com.lawencon.pss.dto.user.LoginResDto;
import com.lawencon.pss.dto.user.UserResDto;

public interface UserService extends UserDetailsService {

    LoginResDto login(LoginReqDto request);
    InsertResDto createUser(CreateUserReqDto request);
    List<UserResDto> getAllUser();
    List<UserResDto> getAllPs();
    List<UserResDto> getAllClient();
    List<UserResDto> getAvailableClients();
    UpdateResDto updatePassword(ChangePasswordReqDto request);

}
