package com.px.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.board.command.AddUserCommand;
import com.px.board.command.LoginCommand;
import com.px.board.dtos.CalDto;
import com.px.board.service.CalServiceImp;
import com.px.board.service.MemService;
import com.px.board.utils.Util;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class MemController {

	@Autowired
	private MemService memService;
	@Autowired
	private CalServiceImp calServiceImp;
	
	@GetMapping(value = "/addUser")
	public String addUserForm(Model model) {
		System.out.println("회원가입폼 이동");
		
		//회원가입폼에서 addUserCommand객체를 사용하는 코드가 작성되어 있어서
		//null일 경우 오류가 발생하기 때문에 보내줘야 함
		model.addAttribute("addUserCommand", new AddUserCommand());
		
		return "mem/addUserForm";
	}
	
	@PostMapping(value = "/addUser")
	public String addUser(@Validated AddUserCommand addUserCommand,BindingResult result, Model model) {
		System.out.println("회원가입하기");
		
		if(result.hasErrors()) {
			System.out.println("회원가입 유효값 오류");
			return "mem/addUserForm";
		}
		
		try {
			memService.addUser(addUserCommand);
			System.out.println("회원가입 성공");
			return "redirect:/";
		} catch (Exception e) {
			System.out.println("회원가입실패");
			e.printStackTrace();
			return "redirect:addUser";
		}
		
	}
	
	@ResponseBody
	@GetMapping(value = "/idChk")
	public Map<String, String> idChk(String id){
		System.out.println("ID중복체크");
		String resultId=memService.idChk(id);
		//json객체로 보내기 위해 Map에 담아서 응답
		//text라면 그냥 String으로 보내도 됨
		Map<String, String>map=new HashMap<>();
		map.put("id", resultId);
		return map;
	}
	
	//로그인 폼 이동
	@GetMapping(value = "/login")
	public String loginForm(Model model) {
		model.addAttribute("loginCommand",new LoginCommand());
		return "login";
	}
	
	//로그인 실행
	@PostMapping(value = "/login")
	public String login(@Validated LoginCommand loginCommand, BindingResult result, Model model, HttpServletRequest request) {
		if(result.hasErrors()) {
			System.out.println("로그인 유효값 오류");
			return "login";
		}
		
		String path=memService.login(loginCommand, request, model);
		
		// makeCalendar 가져오기
		Map<String, Integer>map=calServiceImp.makeCalendar(request); 
		model.addAttribute("calMap",map);
		
		//clist 가져오기
		String clist = Util.getCalViewList(i, clist);
		model.addAttribute("clist", clist);
		
		return path;
	}
	
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		System.out.println("로그아웃");
		request.getSession().invalidate();
		return "redirect:/";
	}
	
	//나의 정보 조회
	
	//나의 정보 수정
	
	//회원목록 전체 조회
}
