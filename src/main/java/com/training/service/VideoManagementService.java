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

import com.training.dto.ErrorResponseDTO;
import com.training.dto.LessonDTO;
import com.training.dto.SuccessResponseDTO;
import com.training.dto.TagDTO;
import com.training.dto.VideoDTO;
import com.training.entity.AnalyticsData;
import com.training.entity.Course;
import com.training.entity.Lesson;
import com.training.entity.Tag;
import com.training.entity.Video;
import com.training.repository.AnalyticsDataRepository;
import com.training.repository.LessonRepository;
import com.training.repository.TagRepository;
import com.training.repository.VideoRepository;

@Service
public class VideoManagementService {

	
	@Lazy
	@Autowired
	LessonRepository lessonRepository;
	
	@Lazy
	@Autowired
	TagRepository tagRepository;
	
	@Lazy
	@Autowired
	VideoRepository videoRepository;
	
	@Lazy
	@Autowired
	AnalyticsDataRepository analyticsDataRepository;
	
	public Object filterVideo(String video_title, List<String> tag_names) {
		List<VideoDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<Video> list = videoRepository.findByTitleAndTags_NameIn(video_title, tag_names);
			dtoList = convertVideoEntityListToVideoDTOList(list);
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of video. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object deleteVideoFromDB(String video_id) {
		VideoDTO dto = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_video_id = null;
		try {
			try{
				long_video_id = Long.parseLong(video_id);
			}catch(Exception e) {
				System.out.println("Not able to parse video-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
				errorResponseDTO.setError_code("VIDEO_ID_PARSE_FAILED");
				errorResponseDTO.setError_descritpion("Parsing of video ID failed");
				return errorResponseDTO;
			}
			videoRepository.deleteById(long_video_id);
			return new SuccessResponseDTO();
		}
		catch(Throwable e) {
			System.out.println("Error in deleting video from database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}

	public Object addVideoToDB(VideoDTO videoDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != videoDTO) {
				Video alreadPresent = videoRepository.findByTitle(videoDTO.getTitle());
				if(null != alreadPresent) {
					System.out.println("Error in adding video to database since already present");
					errorResponseDTO.setError_code("SAME_VIDEO_ALREADY_PRESENT");
					errorResponseDTO.setError_descritpion("Same video is already present");
					return errorResponseDTO;
				}
				else{
					boolean isValid = validateVideoDetailsDTO(videoDTO);
					if(isValid) {
						Video entity = convertVideoDTOToVideoEntity(videoDTO);
						videoRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for video while adding to database.");
						errorResponseDTO.setError_code("VIDEO_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Video validation failed");
						return errorResponseDTO;
					}
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding video to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}
	
	public Object updateVideoInDB(VideoDTO videoDTO){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			if(null != videoDTO) {
				Video video = videoRepository.findById(videoDTO.getId()).get();
				if(null != video ) {
					boolean isValid = validateVideoDetailsDTO(videoDTO);
					if(isValid) {
						Video entity = convertVideoDTOToVideoEntity(videoDTO);
						videoRepository.save(entity);
						return new SuccessResponseDTO();
					}
					else{
						System.out.println("Validation failed for video while updating in database.");
						errorResponseDTO.setError_code("VIDEO_VALIDATION_FAILED");
						errorResponseDTO.setError_descritpion("Video validation failed");
						return errorResponseDTO;
					}
					
				}
				else{
					System.out.println("Video Not found in database");
					errorResponseDTO.setError_code("VIDEO_NOT_FOUND");
					errorResponseDTO.setError_descritpion("Video Not found in database");
					return errorResponseDTO;
				}
				
			}
			return null;
		}
		catch(Throwable e) {
			System.out.println("Error in adding video to database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
	}

	public Object getDetailsOfActiveVideos(String lesson_id) {
		List<VideoDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<Video> list = videoRepository.findByIsActiveAndLessons_id(true, lesson_id);
			dtoList = convertVideoEntityListToVideoDTOList(list);
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of active courses. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}

	public Object getViewCountOfVideos() {
		List<VideoDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		try {
			List<AnalyticsData> analyticsData = analyticsDataRepository.findByEntitytype("Video", Sort.by(Sort.Direction.ASC, "viewcount"));
			List<Video> list = new ArrayList<Video>();
			if(null != analyticsData) {
				for(AnalyticsData data: analyticsData) {
					Video video = videoRepository.findById(data.getEntityid()).get();
					if(null != video)
						list.add(video);
				}
				dtoList = convertVideoEntityListToVideoDTOList(list);

			}
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of video analytics. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	public Object getDetailsOfVideo(String video_id, String is_All) {
		List<VideoDTO> dtoList = null;
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		Long long_video_id = null;
		try {
			if(null != is_All && is_All.equalsIgnoreCase("true")) {
				List<Video> list = videoRepository.findAll();
				dtoList = convertVideoEntityListToVideoDTOList(list);
			} else{
				try{
					long_video_id = Long.parseLong(video_id);
				}catch(Exception e) {
					System.out.println("Not able to parse video-ID. message:" + e.getMessage() + " cause:"  + e.getCause());
					errorResponseDTO.setError_code("VIDEO_ID_PARSE_FAILED");
					errorResponseDTO.setError_descritpion("Parsing of video ID failed");
					return errorResponseDTO;
				}
				Video video = videoRepository.findById(long_video_id).get();
				dtoList = new ArrayList<VideoDTO>();
				dtoList.add(convertVideoEntityToVideoDTO(video));
			}
		}catch(NoSuchElementException e) {
			System.out.println("video with given id not found in database. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("VIDEO_NOT_FOUND_DB");
			errorResponseDTO.setError_descritpion("Video is not found in Database");
			return errorResponseDTO;
			
		}catch(Throwable e) {
			System.out.println("Error in getting details of video. message:" + e.getMessage() + " cause:"  + e.getCause());
			errorResponseDTO.setError_code("INTENRAL_SERVER_ERROR");
			errorResponseDTO.setError_descritpion("Unknown error");
			return errorResponseDTO;
		}
		return dtoList;
	}
	
	
	public Video convertVideoDTOToVideoEntity(VideoDTO videoDTO) {
		Video vd = null;
		if(null != videoDTO) {
			vd = new Video();
			vd.setId(videoDTO.getId());
			vd.setDescription(videoDTO.getDescription());
			vd.setTitle(videoDTO.getTitle());
			vd.setTags(convertTagsDtoToTagsList(videoDTO.getTags()));
			vd.setLessons(convertLessonsDtoToLessonsList(videoDTO.getLessons()));
			vd.setActive(videoDTO.isActive());
		}
		return vd;
	}
	
	public VideoDTO convertVideoEntityToVideoDTO(Video video) {
		VideoDTO dto = null;
		if(null != video) {
			dto = new VideoDTO();
			dto.setId(video.getId());
			dto.setDescription(video.getDescription());
			dto.setTitle(video.getTitle());
			dto.setLink(video.getLink());
			dto.setLessons(convertLessonsListToLessonDto(video.getLessons()));
			dto.setTags(convertTagsListToTagsDto(video.getTags()));
			dto.setActive(video.isActive());
		}
		return dto;
	}
	
	
	public List<VideoDTO> convertVideoEntityListToVideoDTOList(List<Video> list) {
		List<VideoDTO> dtoList = null;
		if(null != list) {
			dtoList = new ArrayList<VideoDTO>();
			for(Video video: list) {
				VideoDTO dto = new VideoDTO();
				dto.setId(video.getId());
				dto.setDescription(video.getDescription());
				dto.setTitle(video.getTitle());
				dto.setLink(video.getLink());
				dto.setLessons(convertLessonsListToLessonDto(video.getLessons()));
				dto.setTags(convertTagsListToTagsDto(video.getTags()));
				dto.setActive(video.isActive());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	
	public Set<TagDTO> convertTagsListToTagsDto(Set<Tag> tags){
		Set<TagDTO> dto = null;
		if(null != tags) {
			dto = new HashSet<TagDTO>(); 
			for(Tag tag: tags) {
				TagDTO tagDto = new TagDTO();
				tagDto.setDescription(tag.getDescription());
				tagDto.setId(tag.getId());
				tagDto.setName(tag.getName());
				dto.add(tagDto);
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
	
	public Set<Tag> convertTagsDtoToTagsList(Set<TagDTO> tags){
		Set<Tag> tagSet = null;
		if(null != tags) {
			tagSet = new HashSet<Tag>(); 
			for(TagDTO tagdto: tags) {
				Tag tag = tagRepository.findById(tagdto.getId()).get();
				if(null != tag)
					tagSet.add(tag);
			}
		}
		return tagSet;
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

	//whatever criteria is important for video validation can be added here.
	boolean validateVideoDetailsDTO(VideoDTO videoDTO) {	
		if(videoDTO.getTitle() != null)
			return true;
		return false;
	}
}

