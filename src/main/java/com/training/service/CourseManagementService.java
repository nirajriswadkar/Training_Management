package com.training.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.training.dto.CourseDTO;
import com.training.dto.ErrorResponseDTO;
import com.training.dto.LessonDTO;
import com.training.dto.SubjectDTO;
import com.training.dto.SuccessResponseDTO;
import com.training.dto.VideoDTO;
import com.training.entity.AnalyticsData;
import com.training.entity.Course;
import com.training.entity.Lesson;
import com.training.entity.Subject;
import com.training.repository.AnalyticsDataRepository;
import com.training.repository.CourseRepository;
import com.training.repository.LessonRepository;
import com.training.repository.SubjectRepository;

@Service
public class CourseManagementService {

	
	@Lazy
	@Autowired
	LessonRepository lessonRepository;
	
	@Lazy
	@Autowired
	CourseRepository courseRepository;
	
	@Lazy
	@Autowired
	SubjectRepository subjectRepository;
	
	@Lazy
	@Autowired
	AnalyticsDataRepository analyticsDataRepository;
	
	public Object filterCourse(String subject_name) {
		List<CourseDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<Course> list = courseRepository.findBySubjects_Name(subject_name);
			dtoList = convertCourseEntityListToCourseDTOList(list);
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of course. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object getDetailsOfCourse(String course_id, String is_All) {
		List<CourseDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_course_id = null;
		try {
			if(null != is_All && is_All.equalsIgnoreCase("true")) {
				List<Course> list = courseRepository.findAll();
				dtoList = convertCourseEntityListToCourseDTOList(list);
			} else{
				try{
					long_course_id = Long.parseLong(course_id);
				}catch(Exception e) {
					System.out.println("Not able to parse course-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
					errorResponseDTO.setError_code("COURSE_ID_PARSE_FAILED");
					errorResponseDTO.setError_descritpion("Parsing of course ID failed");
					return errorResponseDTO;
				}
				Course course = courseRepository.findById(long_course_id).get();
				dtoList = new ArrayList<CourseDTO>();
				dtoList.add(convertCourseEntityToCourseDTO(course));
			}
		}catch(NoSuchElementException e) {
			System.out.println("course with given id not found in database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("COURSE_NOT_FOUND_DB");
			errorResponseDTO.setError_descritpion("COURSE is not found in Database");
			return errorResponseDTO;
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of course. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object deleteCourseFromDB(String course_id) {
		CourseDTO dto = null;
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
			courseRepository.deleteById(long_course_id);
			return new SuccessResponseDTO();
		}
		catch(Throwable e) {
			System.out.println("Error in deleting course from database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}

	public Object addCourseToDB(CourseDTO courseDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != courseDTO) {
				Course alreadPresent = courseRepository.findByName(courseDTO.getName());
				if(null != alreadPresent) {
					System.out.println("Error in adding course to database since already present");
					errorResponseDTO.setError_code("SAME_COURSE_ALREADY_PRESENT");
					errorResponseDTO.setError_descritpion("Same course is already present");
					return errorResponseDTO;
				}
				else{
					boolean isValid = validateCourseDetailsDTO(courseDTO);
					if(isValid) {
						Course entity = convertCourseDTOToCourseEntity(courseDTO);
						courseRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for course while adding to database.");
						errorResponseDTO.setError_code("COURSE_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Course validation failed");
						return errorResponseDTO;
					}
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding course to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object updateCourseInDB(CourseDTO courseDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != courseDTO) {
				Course course = courseRepository.findById(courseDTO.getId()).get();
				if(null != course ) {
					boolean isValid = validateCourseDetailsDTO(courseDTO);
					if(isValid) {
						Course entity = convertCourseDTOToCourseEntity(courseDTO);
						courseRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for course while updating in database.");
						errorResponseDTO.setError_code("COURSE_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Course validation failed");
						return errorResponseDTO;
					}
					
				}
				else{
					System.out.println("Course Not found in database");
					errorResponseDTO.setError_code("COURSE_NOT_FOUND");
					errorResponseDTO.setError_descritpion("Course Not found in database");
					return errorResponseDTO;
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding course to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object getDetailsOfActiveCourses() {
		List<CourseDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<Course> list = courseRepository.findByIsActive(true);
			dtoList = convertCourseEntityListToCourseDTOList(list);
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of active courses. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	
	public Object getViewCountOfCourses() {
		List<CourseDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<AnalyticsData> analyticsData = analyticsDataRepository.findByEntitytype("Course", Sort.by(Sort.Direction.ASC, "viewcount"));
			List<Course> list = new ArrayList<Course>();
			if(null != analyticsData) {
				for(AnalyticsData data: analyticsData) {
					Course course = courseRepository.findById(data.getEntityid()).get();
					if(null != course)
						list.add(course);
				}
				dtoList = convertCourseEntityListToCourseDTOList(list);

			}			
		}catch(Throwable e) {
			System.out.println("Error in getting details of courses analytics. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Course convertCourseDTOToCourseEntity(CourseDTO courseDTO) {
		Course cs = null;
		if(null != courseDTO) {
			cs = new Course();
			cs.setId(courseDTO.getId());
			cs.setDescription(courseDTO.getDescription());
			cs.setName(courseDTO.getName());
			cs.setSubjects(convertSubjectsDtoToSubjectsList(courseDTO.getSubjects()));
			cs.setLessons(convertLessonsDtoToLessonsList(courseDTO.getLessons()));
			cs.setActive(courseDTO.isActive());
		}
		return cs;
	}
	
	public CourseDTO convertCourseEntityToCourseDTO(Course course) {
		CourseDTO dto = null;
		if(null != course) {
			dto = new CourseDTO();
			dto.setId(course.getId());
			dto.setDescription(course.getDescription());
			dto.setName(course.getName());
			dto.setLessons(convertLessonsListToLessonDto(course.getLessons()));
			dto.setSubjects(convertSubjectsListToSubjectsDto(course.getSubjects()));
			dto.setActive(course.isActive());
		}
		return dto;
	}
	
	public List<CourseDTO> convertCourseEntityListToCourseDTOList(List<Course> list) {
		List<CourseDTO> dtoList = null;
		if(null != list) {
			dtoList = new ArrayList<CourseDTO>();
			for(Course cs: list) {
				CourseDTO dto = new CourseDTO();
				dto.setId(cs.getId());
				dto.setDescription(cs.getDescription());
				dto.setName(cs.getName());
				dto.setLessons(convertLessonsListToLessonDto(cs.getLessons()));
				dto.setSubjects(convertSubjectsListToSubjectsDto(cs.getSubjects()));
				dto.setActive(cs.isActive());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	public Set<SubjectDTO> convertSubjectsListToSubjectsDto(Set<Subject> subjects){
		Set<SubjectDTO> dto = null;
		if(null != subjects) {
			dto = new HashSet<SubjectDTO>(); 
			for(Subject sub: subjects) {
				SubjectDTO subDto = new SubjectDTO();
				subDto.setDescription(sub.getDescription());
				subDto.setId(sub.getId());
				subDto.setTitle(sub.getTitle());
				subDto.setName(sub.getName());
				dto.add(subDto);
			}
		}
		return dto;
	}
	
	public Set<LessonDTO> convertLessonsListToLessonDto(Set<Lesson> lessons){
		Set<LessonDTO> dto = null;
		if(null != lessons) {
			dto = new HashSet<LessonDTO>(); 
			for(Lesson ls: lessons) {
				LessonDTO lsDto = new LessonDTO();
				lsDto.setDescription(ls.getDescription());
				lsDto.setId(ls.getId());
				lsDto.setName(ls.getName());
				lsDto.setActive(ls.isActive());
				dto.add(lsDto);
			}
		}
		return dto;
	}
	
	public Set<Subject> convertSubjectsDtoToSubjectsList(Set<SubjectDTO> subjects){
		Set<Subject> subSet = null;
		if(null != subjects) {
			subSet = new HashSet<Subject>(); 
			for(SubjectDTO subdto: subjects) {
				Subject sub = subjectRepository.findById(subdto.getId()).get();
				if(null != sub)
					subSet.add(sub);
			}
		}
		return subSet;
	}
	
	public Set<Lesson> convertLessonsDtoToLessonsList(Set<LessonDTO> lessons){
		Set<Lesson> lsSet = null;
		if(null != lessons) {
			lsSet = new HashSet<Lesson>(); 
			for(LessonDTO lsdto: lessons) {
				Lesson ls = lessonRepository.findById(lsdto.getId()).get(); 
				if(null != ls)
					lsSet.add(ls);
			}
		}
		return lsSet;
	}

	//whatever criteria is important for course validation can be added here.
	boolean validateCourseDetailsDTO(CourseDTO courseDTO) {	
		if(courseDTO.getName() != null)
			return true;
		return false;
	}
}

