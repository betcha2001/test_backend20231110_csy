package com.px.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.px.board.command.AddUserCommand;
import com.px.board.command.DeleteCalCommand;
import com.px.board.command.LoginCommand;
import com.px.board.command.UpdateRoleCommand;
import com.px.board.command.UpdateUserCommand;
import com.px.board.dtos.MemDto;
import com.px.board.mapper.MemMapper;
import com.px.board.status.RoleStatus;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class MemService {
	
	@Autowired
	private MemMapper memMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public boolean addUser(AddUserCommand addUserCommand) {
		MemDto mdto=new MemDto();
		mdto.setId(addUserCommand.getId());
		mdto.setName(addUserCommand.getName());
		
		// password 암호화하여 저장
		mdto.setPassword(passwordEncoder.encode(addUserCommand.getPassword()));
			
		mdto.setEmail(addUserCommand.getEmail());
		mdto.setPhone(addUserCommand.getPhone());
		mdto.setGrade(RoleStatus.USER+"");
		return memMapper.addUser(mdto);
	}
	
	public String idChk(String id) {
		return memMapper.idChk(id);
	}
	
	public String login(LoginCommand loginCommand, HttpServletRequest request, 
			Model model) {
		MemDto dto = memMapper.loginUser(loginCommand.getId());
		String path="thymeleaf/cal/calendar";
		if(dto!=null) {
			//로그인 폼에서 입력받은 패스워드값과 DB에 암호화된 패스워드 비교
			if(passwordEncoder.matches(loginCommand.getPassword(), dto.getPassword())) {
				System.out.println("패스워드 같음: 회원이 맞음");
				//session객체에 로그인 정보 저장
				request.getSession().setAttribute("mdto", dto);
				return path;
			}else {
				System.out.println("패스워드 틀림");
				model.addAttribute("msg","패스워드를 확인하세요");
				path="login";
			}
		}else {
			System.out.println("회원이 아닙니다.");
			model.addAttribute("msg","아이디를 확인하세요");
			path="login";
		}
		return path;
	}
	
	
	// 나의 정보 조회
	public MemDto getmemInfo(String id, Model model, HttpServletRequest request) { // MemDto인지 String인지...
		
		HttpSession session = request.getSession();
	    MemDto mdto = (MemDto)session.getAttribute("mdto");
	    
		return memMapper.getmemInfo(mdto.getId());
	}
	
	
	// 나의 정보 수정
	public boolean updateMem(UpdateUserCommand updateUserCommand) {
		MemDto mdto=new MemDto();
		
		mdto.setId(updateUserCommand.getId());
		mdto.setName(updateUserCommand.getName());		
		mdto.setEmail(updateUserCommand.getEmail());
		mdto.setPhone(updateUserCommand.getPhone());
		
		return memMapper.updateMem(mdto);
	}
		
	
	// 회원목록 전체조회
	public List<MemDto> getAllMemList() {		
		List<MemDto> mdto = memMapper.getAllMemList();
		
		return mdto;
		
	}
	
	// 회원 정보 조회
	public MemDto getmemList(String id) { 
		
		MemDto mdto = memMapper.getmemList(id);
	    
		return memMapper.getmemInfo(mdto.getId());
	}
	
	// 회원 등급 수정
	public boolean memUpdateRole(UpdateRoleCommand updateRoleCommand) {
		MemDto mdto=new MemDto();
		
		mdto.setId(updateRoleCommand.getId());
		mdto.setName(updateRoleCommand.getName());		
		mdto.setGrade(updateRoleCommand.getGrade());
		
		return memMapper.memUpdateRole(mdto);		
	}
	
	
	// 회원 삭제
	public boolean delMem(Map<String, String[]> map) {			
		return memMapper.delMem(map);
	}

}


