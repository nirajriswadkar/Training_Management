package com.training.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.dto.CourseDTO;
import com.training.dto.ErrorResponseDTO;
import com.training.dto.SuccessResponseDTO;
import com.training.entity.Course;
import com.training.entity.UserDetails;
import com.training.repository.CourseRepository;
import com.training.repository.UserDetailsRepository;

@Service
public class UserManagementService {

	@Lazy
	@Autowired
	CourseRepository courseRepository;
	
	@Lazy
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Lazy
	@Autowired
	LessonManagementService lessonManagementService;
	
	@Lazy
	@Autowired
	VideoManagementService videoManagementService;
	
	@Lazy
	@Autowired
	CourseManagementService courseManagementService;
	
	public Object subscribeCourse(String course_id, UserDetails user) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_course_id = null;
		try {
			try{
				long_course_id = Long.parseLong(course_id);
			}catch(Exception e) {
				System.out.println("Not able to parse course-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
				errorResponseDTO.setError_code("COURSE_ID_PARSE_FAILED");
				errorResponseDTO.setError_descritpion("Parsing of course ID failed");
				return errorResponseDTO;
			}
			Course course = courseRepository.findById(long_course_id).get();
			if(null != course) {
				Set<UserDetails> users = course.getUsers();
				UserDetails dbUser = userDetailsRepository.findById(user.getId()).get();
				users.add(dbUser);
				course.setUsers(users);
				courseRepository.save(course);
				return new SuccessResponseDTO();
			}
		}
		catch(Throwable e) {
			System.out.println("Error in subscribing course to user. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return null;
	}
	
	
	public Object unsubscribeCourse(String course_id, UserDetails user) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_course_id = null;
		try {
			try{
				long_course_id = Long.parseLong(course_id);
			}catch(Exception e) {
				System.out.println("Not able to parse course-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
				errorResponseDTO.setError_code("COURSE_ID_PARSE_FAILED");
				errorResponseDTO.setError_descritpion("Parsing of course ID failed");
				return errorResponseDTO;
			}
			Course course = courseRepository.findById(long_course_id).get();
			if(null != course) {
				Set<UserDetails> users = course.getUsers();
				UserDetails userTobeUnsubscribed = null;
				for(UserDetails us: users) {
					if(us.getId().equals(user.getId()) && us.getName().equals(user.getName())) {
						userTobeUnsubscribed = us;
					}
				}
				users.remove(userTobeUnsubscribed);
				course.setUsers(users);
				courseRepository.save(course);
				return new SuccessResponseDTO();
			}
		}
		catch(Throwable e) {
			System.out.println("Error in unsubscribing course to user. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return null;
	}
	
	public Object getActiveEntites(String entity_type, UserDetails userDetials, String additional_info) {
		List<Object> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(entity_type.equalsIgnoreCase("Course")) {
				return courseManagementService.getDetailsOfActiveCourses();
			}else if(entity_type.equalsIgnoreCase("Lesson")) {
				return lessonManagementService.getDetailsOfActiveLessons(additional_info);
			}else if(entity_type.equalsIgnoreCase("Video")) {
				return videoManagementService.getDetailsOfActiveVideos(additional_info);
			} 
		}catch(Throwable e) {
			System.out.println("Error in getting details of active entites. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	
	public Object getViewAnalytics(String entity_type, UserDetails userDetials) {
		List<Object> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(entity_type.equalsIgnoreCase("Course")) {
				return courseManagementService.getViewCountOfCourses();
			}else if(entity_type.equalsIgnoreCase("Video")) {
				return videoManagementService.getViewCountOfVideos();
			} 
		}catch(Throwable e) {
			System.out.println("Error in getting details of active entites. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
}

