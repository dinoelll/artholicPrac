package kr.co.two.mypage.controller;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import kr.co.two.mypage.dto.EventDataDTO;
import kr.co.two.mypage.service.MypageService;

@Controller
public class MypageController {

	private final MypageService service;
	
	public MypageController(MypageService service) {
		this.service = service;
	}
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	// @Value 를 사용하여 C:/upload 위치를 root 에 담는다.
	@Value("${spring.servlet.multipart.location}") private String root;
	
	@RequestMapping(value="/pwchange")
	public String pwChange() {
		
		return "pwChange";
	}
	
	@RequestMapping(value="/myfolder")
	public String myFolder() {
		
		return "myFolder";	
	}
	
	@RequestMapping(value="/mycalendar")
	public String myCalendar() {
		
		return "myCalendar";
	}
	



	@RequestMapping(value="/createFolder")
	public String myFolderCreate(@RequestParam String folderName, HttpServletResponse response) {
		
		logger.info("createFolder Controller");
		logger.info("folderName"+"/"+folderName);
		
		service.myFolderCreate(folderName);
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Refresh", "0;url=/myfolder");
		
		return "redirect:/myfolder";
	}
	
	@RequestMapping(value="/uploadFile")
	public String fileUpload(@RequestParam("file") MultipartFile[] formData, @RequestParam String folder_id, HttpServletResponse response) {
		
		logger.info("uploadFile Controller");
		logger.info("formData :"+formData);
		
		int folderId = Integer.parseInt(folder_id);
		
		service.upload(formData,folderId);
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Refresh", "0;url=/myfolder");
	    
		return "redirect:/myfolder";
	}
	
	@GetMapping(value="/list.ajax")
	@ResponseBody
	public HashMap<String, Object> folderList(){
		
		logger.info("folderList Controller");
		
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    ArrayList<String> list = service.folderList();
	    map.put("folder-list", list);
	    
	    return map;
	}
	
	@PostMapping(value="/filelist.ajax")
	@ResponseBody
	public HashMap<String, Object> fileList(@RequestParam String folder_id, HttpServletResponse response){
		
		logger.info("fileList Controller");
		
		int folderId = Integer.parseInt(folder_id);
		
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    ArrayList<String> fileList = service.fileList(folderId);
	    map.put("file-list", fileList);
	    
	    response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Refresh", "0;url=/myfolder");
	    
	    return map;
	}
	
	@RequestMapping(value="/updateFolder")
	public String myFolderUpdate(@RequestParam String folderName, @RequestParam String folder_id, HttpServletResponse response) {
		
		logger.info("updateFolder Controller");
		
		int folderId = Integer.parseInt(folder_id);
		 	 
		logger.info("folderName"+"/"+folderName+"folder_id"+"/"+folderId);
		 
		service.myFolderUpdate(folderName,folderId);
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Refresh", "0;url=/myfolder");
		
		return "redirect:/myfolder";
	}
	
	@RequestMapping(value="/deleteFolder")
	@ResponseBody
	public String myFolderDelete(@RequestParam String folder_id, HttpServletResponse response) {
		
		logger.info("deleteFolder Controller");
		logger.info("folder_id : " + folder_id);
		
		int folderId = Integer.parseInt(folder_id);
	 	 
		logger.info("folder_id"+"/"+folderId);
		
		  service.myFolderDelete(folderId);
		 
		  response.setHeader("Cache-Control", "no-cache");
		  response.setHeader("Refresh", "0;url=/myfolder");
		 
		return "redirect:/myfolder";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value="/calendarUpdate.ajax")
	@ResponseBody
	public String calendarUpdate(@RequestBody ArrayList<EventDataDTO> eventDataList) {
		
		service.calendarUpdate(eventDataList);
		
	
		
		return "success";
	}
	
	@RequestMapping(value="/calendarUpdate2.ajax")
	@ResponseBody
	public String calendarUpdate2(@RequestBody EventDataDTO requestData) {
		
		logger.info("requestdata : "+requestData.getMember_id() );
		
		service.calendarUpdate2(requestData);

		return "success";
	}
	
    @GetMapping("/getEvent.ajax")
    @ResponseBody
    public List<EventDataDTO> getEvents() {
        // 이벤트 데이터를 가져와서 리스트 형태로 반환
        List<EventDataDTO> events = service.getEvents();
        
        for (EventDataDTO eventDataDTO : events) {
			logger.info("start:"+eventDataDTO.getStart_date());
		}
        
        return events;
    }
    
    @PostMapping("/eventDelete.ajax")
    @ResponseBody
    public HashMap<String, String> eventDelete(@RequestParam String id ) {
        logger.info("id:"+id);
        
        int row = service.eventDelete(id);
        HashMap<String, String>map = new HashMap<String, String>();
        
        
        if (row ==1) {
        	
            map.put("data", "삭제성공");
		}
        
        
        map.put("data", "삭제실패");
        
        
        
        return map;
    }
	
	

}
