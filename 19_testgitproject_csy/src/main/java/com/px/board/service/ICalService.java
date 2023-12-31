package com.px.board.service;

import java.util.List;
import java.util.Map;

import com.px.board.command.InsertCalCommand;
import com.px.board.command.UpdateCalCommand;
import com.px.board.dtos.CalDto;

import jakarta.servlet.http.HttpServletRequest;

public interface ICalService {

		// 달력생성시 필요한 값 구하는 메서드
		public Map<String, Integer> makeCalendar(HttpServletRequest request);
	
		// 일정 추가
	    public boolean insertCalBoard(InsertCalCommand insertCalCommand)throws Exception;
		// 일정 목록
		public List<CalDto> getcalBoardList(String id, String yyyyMMdd);
		// 일정 상세조회
		public CalDto calBoardDetail(int number);
		// 일정 수정하기
		public boolean CalBoardUpdate(UpdateCalCommand updateCalCommand);
		// 일정 삭제하기
		public boolean calMulDel(Map<String,String[]>map);
		// 일일의 일정 보여주기
		public List<CalDto> calViewList(String yyyyMM);
	
}