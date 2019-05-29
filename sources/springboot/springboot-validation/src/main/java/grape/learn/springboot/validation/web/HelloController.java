package grape.learn.springboot.validation.web;

import javax.validation.Valid;

import grape.learn.springboot.validation.web.dto.HelloDTO;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author grape
 * @date 2019-05-28
 */
@RestController
public class HelloController {

	@RequestMapping("/controllerValid")
	public String controllerValid(@RequestBody @Valid HelloDTO helloDTO) {
		return "ok";
	}
}
