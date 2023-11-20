package com.px.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.px.board.command.UpdateCalCommand;
import com.px.board.dtos.CalDto;

@Mapper
public interface CalMapper {

	//일정 추가
	public int insertCalBoard(CalDto dto);
	//일정 목록
	public List<CalDto> calBoardList(String id, String yyyyMMdd);
	//일정 상세조회
	public CalDto calBoardDetail(int member);
	//일정 수정하기
	public boolean calBoardUpdate(CalDto dto);
	//일정 삭제하기
	public boolean calMulDel(Map<String,String[]>map);
	//일일의 일정 보여주기
	public List<CalDto> calViewList(String id, String yyyyMM);
	//일일의 일정개수 보여주기
	public List<CalDto> calBoardSummary(String category, String title);
}