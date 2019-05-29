package grape.learn.springboot.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;

import grape.learn.springboot.validation.common.ValidationUtils;
import grape.learn.springboot.validation.service.HelloBService;
import grape.learn.springboot.validation.web.dto.HelloDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootValidationApplicationTests {

	@Autowired
	private HelloBService helloBService;

	@Test
	public void contextLoads() {
		HelloDTO helloDTO = new HelloDTO();
		helloBService.insertHello(helloDTO);
	}

	@Test
	public void testValidationUtils() {
		HelloDTO helloDTO = new HelloDTO();
		Set<ConstraintViolation<HelloDTO>> constraintViolations =  ValidationUtils.validate(helloDTO);
		Assert.assertNotNull(constraintViolations);
	}

}
