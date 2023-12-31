package com.px.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.px.board.dtos.MemDto;

@Mapper
public interface MemMapper {

	public boolean addUser(MemDto dto);
	
	public String idChk(String id);
	
	public MemDto loginUser(String id);
	
	public MemDto getmemInfo(String id);
	
	public List<MemDto> getAllMemList();
	
	public MemDto getmemList(String id);
	
	public boolean updateMem(MemDto dto);
	
	public boolean memUpdateRole(MemDto dto);
	
	public boolean delMem(Map<String, String[]> map);
}


