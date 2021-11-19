package com.training.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.training.dto.CourseDTO;
import com.training.dto.ErrorResponseDTO;
import com.training.dto.LessonDTO;
import com.training.dto.SubjectDTO;
import com.training.dto.SuccessResponseDTO;
import com.training.dto.TagDTO;
import com.training.dto.VideoDTO;
import com.training.entity.UserDetails;
import com.training.service.CourseManagementService;
import com.training.service.LessonManagementService;
import com.training.service.SubjectManagementService;
import com.training.service.TagManagementService;
import com.training.service.UserManagementService;
import com.training.service.VideoManagementService;

@Controller
@RequestMapping("/training/")
public class TrainingController {

	
	@Lazy
	@Autowired
	LessonManagementService lessonManagementService;
	
	@Lazy
	@Autowired
	CourseManagementService courseManagementService;
	
	@Lazy
	@Autowired
	VideoManagementService videoManagementService;
	
	@Lazy
	@Autowired
	TagManagementService tagManagementService;
	
	@Lazy
	@Autowired
	SubjectManagementService subjectManagementService;
	
	@Lazy
	@Autowired
	UserManagementService userManagementService;
	
	@RequestMapping(method = RequestMethod.GET, value = "lesson/{lesson_id}/{is_all}")
    public ResponseEntity<?> getDetailsOfLesson(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String lesson_id, @PathVariable String is_all) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = lessonManagementService.getDetailsOfLesson(lesson_id, is_all);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "lesson")
    public ResponseEntity<?> addLessonToInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody LessonDTO lessonDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}		
		Object responseEntity = lessonManagementService.addLessonToDB(lessonDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "lesson")
    public ResponseEntity<?> updateLessonInInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody LessonDTO lessonDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}		
		Object responseEntity = lessonManagementService.updateLessonInDB(lessonDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, value = "lesson/{lesson_id}")
    public ResponseEntity<?> deleteLessonFromInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String lesson_id) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = lessonManagementService.deleteLessonFromDB(lesson_id);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "course/{course_id}/{is_all}")
    public ResponseEntity<?> getDetailsOfCourse(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String course_id, @PathVariable String is_all) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = courseManagementService.getDetailsOfCourse(course_id, is_all);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "course")
    public ResponseEntity<?> addCourseToInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody CourseDTO courseDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = courseManagementService.addCourseToDB(courseDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "course")
    public ResponseEntity<?> updateCourseInInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody CourseDTO courseDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = courseManagementService.updateCourseInDB(courseDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "course/{course_id}")
    public ResponseEntity<?> deleteCourseFromInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String course_id) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = courseManagementService.deleteCourseFromDB(course_id);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "filtercourse/{subject_name}")
    public ResponseEntity<?> filterCourse(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String subject_name) {
		Object responseEntity = courseManagementService.filterCourse(subject_name);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "video/{video_id}/{is_all}")
    public ResponseEntity<?> getDetailsOfVideo(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String video_id, @PathVariable String is_all) {
		Object responseEntity = videoManagementService.getDetailsOfVideo(video_id, is_all);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "video")
    public ResponseEntity<?> addVideoToInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody VideoDTO videoDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = videoManagementService.addVideoToDB(videoDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "video")
    public ResponseEntity<?> updateVideoInInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody VideoDTO videoDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = videoManagementService.updateVideoInDB(videoDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "video/{video_id}")
    public ResponseEntity<?> deleteVideoFromInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String video_id) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = videoManagementService.deleteVideoFromDB(video_id);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "filtervideo/{video_title}")
    public ResponseEntity<?> filterVideos(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String video_title, @RequestBody List<String> tag_names) {
		Object responseEntity = videoManagementService.filterVideo(video_title, tag_names);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "tag/{tag_id}/{is_all}")
    public ResponseEntity<?> getDetailsOfTag(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String tag_id, @PathVariable String is_all) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = tagManagementService.getDetailsOfTag(tag_id, is_all);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "tag")
    public ResponseEntity<?> addTagToInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody TagDTO tagDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = tagManagementService.addTagToDB(tagDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "tag")
    public ResponseEntity<?> updateTagInInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody TagDTO tagDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = tagManagementService.updateTagInDB(tagDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "tag/{tag_id}")
    public ResponseEntity<?> deleteTagFromInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String tag_id) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = tagManagementService.deleteTagFromDB(tag_id);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "subject/{subject_id}/{is_all}")
    public ResponseEntity<?> getDetailsOfSubject(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String subject_id, @PathVariable String is_all) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = subjectManagementService.getDetailsOfSubject(subject_id, is_all);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "subject")
    public ResponseEntity<?> addSubjectToInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody SubjectDTO subjectDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = subjectManagementService.addSubjectToDB(subjectDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "subject")
    public ResponseEntity<?> updateSubjectInInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @RequestBody SubjectDTO subjectDTO) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = subjectManagementService.updateSubjectInDB(subjectDTO);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "subject/{subject_id}")
    public ResponseEntity<?> deleteSubjectFromInventory(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String subject_id) {
		//some logic using session map to detect wheter request is from
		//valid instructor or not
		if(!userdetails.isIs_instructor()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Object responseEntity = subjectManagementService.deleteSubjectFromDB(subject_id);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "course/subscribe/{course_id}")
    public ResponseEntity<?> subscribeCourse(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String course_id) {
		Object responseEntity = userManagementService.subscribeCourse(course_id, userdetails);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "course/unsubscribe/{course_id}")
    public ResponseEntity<?> unsubscribeCourse(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String course_id) {
		Object responseEntity = userManagementService.unsubscribeCourse(course_id, userdetails);
		if(responseEntity instanceof SuccessResponseDTO)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "activeEntities/{entity_type}/{addtional_info}")
    public ResponseEntity<?> getActiveEntites(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String entity_type, @PathVariable String additonal_info) {
		Object responseEntity = userManagementService.getActiveEntites(entity_type, userdetails, additonal_info);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getviewcount/{entity_type}/")
    public ResponseEntity<?> getViewCount(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDetails userdetails, @PathVariable String entity_type) {
		Object responseEntity = userManagementService.getViewAnalytics(entity_type, userdetails);
		if(responseEntity instanceof List<?>)
			return ResponseEntity.ok().body(responseEntity);
		else if(responseEntity instanceof ErrorResponseDTO)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntity);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
