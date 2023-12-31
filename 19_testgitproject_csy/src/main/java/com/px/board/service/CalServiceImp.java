package com.px.board.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.px.board.command.InsertCalCommand;
import com.px.board.command.UpdateCalCommand;
import com.px.board.dtos.CalDto;
import com.px.board.mapper.CalMapper;
import com.px.board.utils.Util;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CalServiceImp implements ICalService {
   
   // Validated 유효값처리 라이브러리 사용
   // - command 클래스를 생성하여 유효값 확인
   // 화면 --> command --> 컨트롤러/서비스 (command -> dto) --> Mapper로 전달
   //          command에 저장된 값을 확인하여 오류가 있으면 화면으로 보내고
   //          오류가 없으면 서비스로 보냄
   
   @Autowired
   private CalMapper calMapper;
   
   public Map<String, Integer> makeCalendar(HttpServletRequest request) {
      Map<String, Integer> map = new HashMap<>();
      
      // 달력의 날짜를 바꾸기 위해 전달된 year와 month 파라미터를 받는 코드
      String paramYear = request.getParameter("year");
      String paramMonth = request.getParameter("month");
      
      // Calendar : new 사용 불가      
      // 오늘 날짜가 들어있음
      Calendar cal = Calendar.getInstance(); // 추상클래스이자 staic 메서드
      
      // null이면 오늘 날짜로, null이 아니면 요청된 년도로 저장!
      int year = (paramYear == null)?
                  cal.get(Calendar.YEAR):Integer.parseInt(paramYear);
      
      int month = (paramMonth == null)?
            cal.get(Calendar.MONTH)+1:Integer.parseInt(paramMonth);
      // calendar 객체에서 month는 0~11월이니까 +1 해줄 것!
      
      // 11월 12월 13월 14월 / -2월 -1월 0월 1월로 가는 오류 처리
      if(month > 12) {
         month = 1;
         year++;
      }
      
      if(month < 1) {
         month = 12;
         year--;
      }
      
      // 1. 월의 1일에 대한 요일 구하기
      cal.set(year, month-1,1); // 원하는 날짜로 셋팅
      int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); 
      // 1~7중에서 반환 => 1 : 일요일, 7 : 토요일
      
      // 2. 월의 마지막 날 구하기
      int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      
      map.put("year", year);
      map.put("month", month);
      map.put("dayOfWeek", dayOfWeek);
      map.put("lastDay", lastDay);
      return map;
   }
   
   
   @Override
   public boolean insertCalBoard(InsertCalCommand insertCalCommand) throws Exception {
      // command --> dto로 값 이동
      // DB에서는 mdate 컬럼, command에서는 year, month...
      // 12자리로 변환하는 작업 필요
      String startdate = insertCalCommand.getYears()
            + Util.isTwo(insertCalCommand.getMonths()+"")   
            + Util.isTwo(insertCalCommand.getDates()+"")
            + Util.isTwo(insertCalCommand.getHours()+"")
            + Util.isTwo(insertCalCommand.getMins()+"");
      
      
      String enddate = insertCalCommand.getYear()
              + Util.isTwo(insertCalCommand.getMonth()+"")   
              + Util.isTwo(insertCalCommand.getDate()+"")
              + Util.isTwo(insertCalCommand.getHour()+"")
              + Util.isTwo(insertCalCommand.getMin()+"");
      
      // 202311151335_12자리
      // 20231181110_11자리 => 이를 방지하기 위해 Util 클래스 생성
      
      // command --> dto 값 복사
      CalDto dto = new CalDto();
      dto.setId(insertCalCommand.getId());
      dto.setTitle(insertCalCommand.getTitle());
      dto.setContent(insertCalCommand.getContent());
      dto.setCategory(insertCalCommand.getCategory());
      dto.setStartdate(startdate); 
      dto.setEnddate(enddate);
      
      int count = calMapper.insertCalBoard(dto);
      
      return count>0?true:false;
   }

   @Override
   public List<CalDto> getcalBoardList(String id, String yyyyMMdd) {      
      return calMapper.getcalBoardList(id, yyyyMMdd);
   }

   @Override
   public CalDto calBoardDetail(int number) {
      return calMapper.calBoardDetail(number);
   }

   @Override
   public boolean CalBoardUpdate(UpdateCalCommand updateCalCommand) {
      // command : year, month, date.. --> dto : mdate
      String startdate = updateCalCommand.getYears()
            + Util.isTwo(updateCalCommand.getMonths()+"")   
            + Util.isTwo(updateCalCommand.getDates()+"")
            + Util.isTwo(updateCalCommand.getHours()+"")
            + Util.isTwo(updateCalCommand.getMins()+""); // 12자리
      
      String enddate = updateCalCommand.getYear()
              + Util.isTwo(updateCalCommand.getMonth()+"")   
              + Util.isTwo(updateCalCommand.getDate()+"")
              + Util.isTwo(updateCalCommand.getHour()+"")
              + Util.isTwo(updateCalCommand.getMin()+""); // 12자리
      
      CalDto dto = new CalDto();
      dto.setId(updateCalCommand.getId());
      dto.setNumber(updateCalCommand.getNumber());
      dto.setTitle(updateCalCommand.getTitle());
      dto.setContent(updateCalCommand.getContent());
      dto.setCategory(updateCalCommand.getCategory());
      dto.setStartdate(startdate);
      dto.setEnddate(enddate);
      
      return calMapper.CalBoardUpdate(dto);
   }

   @Override
   public boolean calMulDel(Map<String, String[]> map) {
      return calMapper.calMulDel(map);
      
   }

   @Override
   public List<CalDto> calViewList(String yyyyMM) {
      return calMapper.calViewList(yyyyMM);
   }

  
   
}
