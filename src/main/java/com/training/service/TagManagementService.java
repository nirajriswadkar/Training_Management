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
import com.training.dto.SuccessResponseDTO;
import com.training.dto.TagDTO;
import com.training.dto.VideoDTO;
import com.training.entity.Course;
import com.training.entity.Tag;
import com.training.entity.Video;
import com.training.repository.TagRepository;
import com.training.repository.VideoRepository;

@Service
public class TagManagementService {

	
	@Lazy
	@Autowired
	TagRepository tagRepository;
	
	@Lazy
	@Autowired
	VideoRepository videoRepository;
	
	
	public Object getDetailsOfTag(String tag_id, String is_All) {
		List<TagDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_tag_id = null;
		try {
			if(null != is_All && is_All.equalsIgnoreCase("true")) {
				List<Tag> list = tagRepository.findAll();
				dtoList = convertTagEntityListToTagDTOList(list);
			} else{
				try{
					long_tag_id = Long.parseLong(tag_id);
				}catch(Exception e) {
					System.out.println("Not able to parse tag-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
					errorResponseDTO.setError_code("TAG_ID_PARSE_FAILED");
					errorResponseDTO.setError_descritpion("Parsing of tag ID failed");
					return errorResponseDTO;
				}
				Tag tag = tagRepository.findById(long_tag_id).get();
				dtoList = new ArrayList<TagDTO>();
				dtoList.add(convertTagEntityToTagDTO(tag));
			}
		}catch(NoSuchElementException e) {
			System.out.println("tag with given id not found in database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("TAG_NOT_FOUND_DB");
			errorResponseDTO.setError_descritpion("Tag is not found in Database");
			return errorResponseDTO;
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of tag. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object deleteTagFromDB(String tag_id) {
		TagDTO dto = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_tag_id = null;
		try {
			try{
				long_tag_id = Long.parseLong(tag_id);
			}catch(Exception e) {
				System.out.println("Not able to parse tag-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
				errorResponseDTO.setError_code("TAG_ID_PARSE_FAILED");
				errorResponseDTO.setError_descritpion("Parsing of tag ID failed");
				return errorResponseDTO;
			}
			tagRepository.deleteById(long_tag_id);
			return new SuccessResponseDTO();
		}
		catch(Throwable e) {
			System.out.println("Error in deleting tag from database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}

	public Object addTagToDB(TagDTO tagDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != tagDTO) {
				Tag alreadPresent = tagRepository.findByName(tagDTO.getName());
				if(null != alreadPresent) {
					System.out.println("Error in adding tag to database since already present");
					errorResponseDTO.setError_code("SAME_TAG_ALREADY_PRESENT");
					errorResponseDTO.setError_descritpion("Same tag is already present");
					return errorResponseDTO;
				}
				else{
					boolean isValid = validateTagDetailsDTO(tagDTO);
					if(isValid) {
						Tag entity = convertTagDTOToTagEntity(tagDTO);
						tagRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for tag while adding to database.");
						errorResponseDTO.setError_code("TAG_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Tag validation failed");
						return errorResponseDTO;
					}
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding tag to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object updateTagInDB(TagDTO tagDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != tagDTO) {
				Tag tag = tagRepository.findById(tagDTO.getId()).get();
				if(null != tag ) {
					boolean isValid = validateTagDetailsDTO(tagDTO);
					if(isValid) {
						Tag entity = convertTagDTOToTagEntity(tagDTO);
						tagRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for tag while updating in database.");
						errorResponseDTO.setError_code("TAG_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Tag validation failed");
						return errorResponseDTO;
					}
					
				}
				else{
					System.out.println("Tag Not found in database");
					errorResponseDTO.setError_code("TAG_NOT_FOUND");
					errorResponseDTO.setError_descritpion("Tag Not found in database");
					return errorResponseDTO;
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding tag to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Tag convertTagDTOToTagEntity(TagDTO tagDTO) {
		Tag tag = null;
		if(null != tagDTO) {
			tag = new Tag();
			tag.setId(tagDTO.getId());
			tag.setDescription(tagDTO.getDescription());
			tag.setName(tagDTO.getName());
			tag.setVideos(convertVideosDtoToVideosList(tagDTO.getVideos()));
		}
		return tag;
	}
	
	public TagDTO convertTagEntityToTagDTO(Tag tag) {
		TagDTO dto = null;
		if(null != tag) {
			dto = new TagDTO();
			dto.setId(tag.getId());
			dto.setDescription(tag.getDescription());
			dto.setName(tag.getName());
			dto.setVideos(convertVideosListToVideosDto(tag.getVideos()));
		}
		return dto;
	}
	
	public List<TagDTO> convertTagEntityListToTagDTOList(List<Tag> list) {
		List<TagDTO> dtoList = null;
		if(null != list) {
			dtoList = new ArrayList<TagDTO>();
			for(Tag tag: list) {
				TagDTO dto = new TagDTO();
				dto.setId(tag.getId());
				dto.setDescription(tag.getDescription());
				dto.setName(tag.getName());
				dto.setVideos(convertVideosListToVideosDto(tag.getVideos()));
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
	
	//whatever criteria is important for tag validation can be added here.
	boolean validateTagDetailsDTO(TagDTO tagDTO) {	
		if(tagDTO.getName() != null)
			return true;
		return false;
	}
}

