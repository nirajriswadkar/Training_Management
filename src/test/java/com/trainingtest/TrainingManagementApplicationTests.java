package com.trainingtest;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.controller.TrainingController;
import com.training.entity.Course;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan("com.training")
@EntityScan("com.traiing")
@RunWith(SpringRunner.class)
class TrainingManagementApplicationTests {

	@Lazy
	@Autowired
	TrainingController trainingController;
	
	@Test
	public void getPrductDetails() {
		
		String actualNameofLesson = "";
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		ResponseEntity<?> responseEntity = trainingController.getDetailsOfCourse(request, response, null, "1255", "false");		
		Object body = responseEntity.getBody();
		if(body instanceof List<?>) {
			List<Course> csList = (List<Course>) body;
			actualNameofLesson = csList.get(0).getName();
		}
		assertEquals("Some lesson", actualNameofLesson, "Get lessonsdetails successful");
	}

}
