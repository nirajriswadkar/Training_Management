package com.training.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.training.dto.CourseDTO;
import com.training.dto.ErrorResponseDTO;
import com.training.dto.LessonDTO;
import com.training.dto.SuccessResponseDTO;
import com.training.dto.VideoDTO;
import com.training.entity.Course;
import com.training.entity.Lesson;
import com.training.entity.Video;
import com.training.repository.CourseRepository;
import com.training.repository.LessonRepository;
import com.training.repository.VideoRepository;

@Service
public class LessonManagementService {

	
	@Lazy
	@Autowired
	LessonRepository lessonRepository;
	
	@Lazy
	@Autowired
	CourseRepository courseRepository;
	
	@Lazy
	@Autowired
	VideoRepository videoRepository;
	
	
	public Object getDetailsOfLesson(String lesson_id, String is_All) {
		
		List<LessonDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_lesson_id = null;
		try {
			if(null != is_All && is_All.equalsIgnoreCase("true")) {
				List<Lesson> list = lessonRepository.findAll();
				dtoList = convertLessonEntityListToLessonDTOList(list);
			} else{
				try{
					long_lesson_id = Long.parseLong(lesson_id);
				}catch(Exception e) {
					System.out.println("Not able to parse lesson-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
					errorResponseDTO.setError_code("LESSON_ID_PARSE_FAILED");
					errorResponseDTO.setError_descritpion("Parsing of lesson ID failed");
					return errorResponseDTO;
				}
				Lesson lesson = lessonRepository.findById(long_lesson_id).get();
				dtoList = new ArrayList<LessonDTO>();
				dtoList.add(convertLessonEntityToLessonDTO(lesson));
			}
			
		}catch(NoSuchElementException e) {
			System.out.println("lesson with given id not found in database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("LESSON_NOT_FOUND_DB");
			errorResponseDTO.setError_descritpion("Lesson is not found in Database");
			return errorResponseDTO;
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of lesson. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object deleteLessonFromDB(String lesson_id) {
		LessonDTO dto = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_lesson_id = null;
		try {
			try{
				long_lesson_id = Long.parseLong(lesson_id);
			}catch(Exception e) {
				System.out.println("Not able to parse lesson-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
				errorResponseDTO.setError_code("LESSON_ID_PARSE_FAILED");
				errorResponseDTO.setError_descritpion("Parsing of lesson ID failed");
				return errorResponseDTO;
			}
			lessonRepository.deleteById(long_lesson_id);
			return new SuccessResponseDTO();
		}
		catch(Throwable e) {
			System.out.println("Error in deleting lesson from database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}

	public Object addLessonToDB(LessonDTO lessonDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != lessonDTO) {
				Lesson alreadPresent = lessonRepository.findByName(lessonDTO.getName());
				if(null != alreadPresent) {
					System.out.println("Error in adding lesson to database since already present");
					errorResponseDTO.setError_code("SAME_LESSON_ALREADY_PRESENT");
					errorResponseDTO.setError_descritpion("Same lesson is already present");
					return errorResponseDTO;
				}
				else{
					boolean isValid = validateLessonDetailsDTO(lessonDTO);
					if(isValid) {
						Lesson entity = convertLessonDTOToLessonEntity(lessonDTO);
						lessonRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for lesson while adding to database.");
						errorResponseDTO.setError_code("LESSON_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Lesson validation failed");
						return errorResponseDTO;
					}
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding lesson to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object updateLessonInDB(LessonDTO lessonDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != lessonDTO) {
				Lesson lesson = lessonRepository.findById(lessonDTO.getId()).get();
				if(null != lesson ) {
					boolean isValid = validateLessonDetailsDTO(lessonDTO);
					if(isValid) {
						Lesson entity = convertLessonDTOToLessonEntity(lessonDTO);
						lessonRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for lesson while updating in database.");
						errorResponseDTO.setError_code("LESSON_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Lesson validation failed");
						return errorResponseDTO;
					}
					
				}
				else{
					System.out.println("Lesson Not found in database");
					errorResponseDTO.setError_code("LESSON_NOT_FOUND");
					errorResponseDTO.setError_descritpion("Lesson Not found in database");
					return errorResponseDTO;
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding lesson to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object getDetailsOfActiveLessons(String course_id) {
		List<LessonDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<Lesson> list = lessonRepository.findByIsActiveAndCourses_id(true, course_id);
			dtoList = convertLessonEntityListToLessonDTOList(list);
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of active courses. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Lesson convertLessonDTOToLessonEntity(LessonDTO lessonDTO) {
		Lesson ls = null;
		if(null != lessonDTO) {
			ls = new Lesson();
			ls.setId(lessonDTO.getId());
			ls.setDescription(lessonDTO.getDescription());
			ls.setName(lessonDTO.getName());
			ls.setCourses(convertCoursesDtoToCoursesList(lessonDTO.getCourses()));
			ls.setVideos(convertVideosDtoToVideosList(lessonDTO.getVideos()));
			ls.setActive(lessonDTO.isActive());
		}
		return ls;
	}
	
	public LessonDTO convertLessonEntityToLessonDTO(Lesson lesson) {
		LessonDTO dto = null;
		if(null != lesson) {
			dto = new LessonDTO();
			dto.setId(lesson.getId());
			dto.setDescription(lesson.getDescription());
			dto.setName(lesson.getName());
			dto.setCourses(convertCoursesListToCoursesDto(lesson.getCourses()));
			dto.setVideos(convertVideosListToVideosDto(lesson.getVideos()));
			dto.setActive(lesson.isActive());
		}
		return dto;
	}
	
	public List<LessonDTO> convertLessonEntityListToLessonDTOList(List<Lesson> list) {
		List<LessonDTO> dtoList = null;
		if(null != list) {
			dtoList = new ArrayList<LessonDTO>();
			for(Lesson lesson: list) {
				LessonDTO dto = new LessonDTO();
				dto.setId(lesson.getId());
				dto.setDescription(lesson.getDescription());
				dto.setName(lesson.getName());
				dto.setCourses(convertCoursesListToCoursesDto(lesson.getCourses()));
				dto.setVideos(convertVideosListToVideosDto(lesson.getVideos()));
				dto.setActive(lesson.isActive());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	public Set<VideoDTO> convertVideosListToVideosDto(Set<Video> videos){
		Set<VideoDTO> dto = null;
		if(null != videos) {
			dto = new HashSet<VideoDTO> (); 
			for(Video vd: videos) {
				VideoDTO vdDto = new VideoDTO();
				vdDto.setDescription(vd.getDescription());
				vdDto.setId(vd.getId());
				vdDto.setLink(vd.getLink());
				vdDto.setTitle(vd.getTitle());
				vdDto.setActive(vd.isActive());
				dto.add(vdDto);
			}
		}
		return dto;
	}
	
	public Set<CourseDTO> convertCoursesListToCoursesDto(Set<Course> courses){
		Set<CourseDTO> dto = null;
		if(null != courses) {
			dto = new HashSet<CourseDTO> (); 
			for(Course cs: courses) {
				CourseDTO csDto = new CourseDTO();
				csDto.setDescription(cs.getDescription());
				csDto.setId(cs.getId());
				csDto.setName(cs.getName());
				csDto.setTitle(cs.getTitle());
				csDto.setActive(cs.isActive());
				dto.add(csDto);
			}
		}
		return dto;
	}
	
	public Set<Video> convertVideosDtoToVideosList(Set<VideoDTO> videos){
		Set<Video> vdSet = null;
		if(null != videos) {
			vdSet = new HashSet<Video>(); 
			for(VideoDTO vddto: videos) {
				Video vd = videoRepository.findById(vddto.getId()).get();
				if(null != vd)
					vdSet.add(vd);
			}
		}
		return vdSet;
	}
	
	public Set<Course> convertCoursesDtoToCoursesList(Set<CourseDTO> courses){
		Set<Course> csSet = null;
		if(null != courses) {
			csSet = new HashSet<Course> (); 
			for(CourseDTO csdto: courses) {
				Course cs = courseRepository.findById(csdto.getId()).get(); 
				if(null != cs)
					csSet.add(cs);
			}
		}
		return csSet;
	}

	//whatever criteria is important for lesson validation can be added here.
	boolean validateLessonDetailsDTO(LessonDTO lessonDTO) {	
		if(lessonDTO.getName() != null)
			return true;
		return false;
	}
}

