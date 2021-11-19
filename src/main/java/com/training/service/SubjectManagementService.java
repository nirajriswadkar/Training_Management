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
import com.training.dto.SubjectDTO;
import com.training.dto.SuccessResponseDTO;
import com.training.dto.VideoDTO;
import com.training.entity.Course;
import com.training.entity.Subject;
import com.training.entity.Video;
import com.training.repository.CourseRepository;
import com.training.repository.SubjectRepository;

@Service
public class SubjectManagementService {

	@Lazy
	@Autowired
	SubjectRepository subjectRepository;
	
	@Lazy
	@Autowired
	CourseRepository courseRepository;
	
	public Object getDetailsOfSubject(String subject_id, String is_All) {
		List<SubjectDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_subject_id = null;
		try {
			if(null != is_All && is_All.equalsIgnoreCase("true")) {
				List<Subject> list = subjectRepository.findAll();
				dtoList = convertSubjectEntityListToSubjectDTOList(list);
			} else{
				try{
					long_subject_id = Long.parseLong(subject_id);
				}catch(Exception e) {
					System.out.println("Not able to parse subject-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
					errorResponseDTO.setError_code("SUBJECT_ID_PARSE_FAILED");
					errorResponseDTO.setError_descritpion("Parsing of subject ID failed");
					return errorResponseDTO;
				}
				Subject subject = subjectRepository.findById(long_subject_id).get();
				dtoList = new ArrayList<SubjectDTO>();
				dtoList.add(convertSubjectEntityToSubjectDTO(subject));
			}
		}catch(NoSuchElementException e) {
			System.out.println("subject with given id not found in database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("SUBJECT_NOT_FOUND_DB");
			errorResponseDTO.setError_descritpion("Subject is not found in Database");
			return errorResponseDTO;
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of subject. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object deleteSubjectFromDB(String subject_id) {
		SubjectDTO dto = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_subject_id = null;
		try {
			try{
				long_subject_id = Long.parseLong(subject_id);
			}catch(Exception e) {
				System.out.println("Not able to parse subject-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
				errorResponseDTO.setError_code("SUBJECT_ID_PARSE_FAILED");
				errorResponseDTO.setError_descritpion("Parsing of subject ID failed");
				return errorResponseDTO;
			}
			subjectRepository.deleteById(long_subject_id);
			return new SuccessResponseDTO();
		}
		catch(Throwable e) {
			System.out.println("Error in deleting subject from database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}

	public Object addSubjectToDB(SubjectDTO subjectDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != subjectDTO) {
				Subject alreadPresent = subjectRepository.findByName(subjectDTO.getName());
				if(null != alreadPresent) {
					System.out.println("Error in adding subject to database since already present");
					errorResponseDTO.setError_code("SAME_SUBJECT_ALREADY_PRESENT");
					errorResponseDTO.setError_descritpion("Same subject is already present");
					return errorResponseDTO;
				}
				else{
					boolean isValid = validateSubjectDetailsDTO(subjectDTO);
					if(isValid) {
						Subject entity = convertSubjectDTOToSubjectEntity(subjectDTO);
						subjectRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for subject while adding to database.");
						errorResponseDTO.setError_code("SUBJECT_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Subject validation failed");
						return errorResponseDTO;
					}
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding subject to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object updateSubjectInDB(SubjectDTO subjectDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != subjectDTO) {
				Subject subject = subjectRepository.findById(subjectDTO.getId()).get();
				if(null != subject) {
					boolean isValid = validateSubjectDetailsDTO(subjectDTO);
					if(isValid) {
						Subject entity = convertSubjectDTOToSubjectEntity(subjectDTO);
						subjectRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for subject while updating in database.");
						errorResponseDTO.setError_code("SUBJECT_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Subject validation failed");
						return errorResponseDTO;
					}
					
				}
				else{
					System.out.println("Subject Not found in database");
					errorResponseDTO.setError_code("SUBJECT_NOT_FOUND");
					errorResponseDTO.setError_descritpion("Subject Not found in database");
					return errorResponseDTO;
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding subject to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Subject convertSubjectDTOToSubjectEntity(SubjectDTO subjectDTO) {
		Subject sub = null;
		if(null != subjectDTO) {
			sub = new Subject();
			sub.setId(subjectDTO.getId());
			sub.setDescription(subjectDTO.getDescription());
			sub.setName(subjectDTO.getName());
			sub.setTitle(subjectDTO.getTitle());
			sub.setCourses(convertCoursesDtoToCoursesList(subjectDTO.getCourses()));
		}
		
		return sub;
	}
	
	public SubjectDTO convertSubjectEntityToSubjectDTO(Subject subject) {
		SubjectDTO dto = null;
		if(null != subject) {
			dto = new SubjectDTO();
			dto.setId(subject.getId());
			dto.setDescription(subject.getDescription());
			dto.setName(subject.getName());
			dto.setTitle(subject.getTitle());
			dto.setCourses(convertCoursesListToCoursesDto(subject.getCourses()));
		}
		return dto;
	}
	
	public List<SubjectDTO> convertSubjectEntityListToSubjectDTOList(List<Subject> list) {
		List<SubjectDTO> dtoList = null;
		if(null != list) {
			dtoList = new ArrayList<SubjectDTO>();
			for(Subject subject: list) {
				SubjectDTO dto = new SubjectDTO();
				dto.setId(subject.getId());
				dto.setDescription(subject.getDescription());
				dto.setName(subject.getName());
				dto.setTitle(subject.getTitle());
				dto.setCourses(convertCoursesListToCoursesDto(subject.getCourses()));
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

	//whatever criteria is important for subject validation can be added here.
	boolean validateSubjectDetailsDTO(SubjectDTO subjectDTO) {	
		if(subjectDTO.getName() != null)
			return true;
		return false;
	}
}

