package grape.learn.springboot.validation.service;

import javax.validation.Valid;

import grape.learn.springboot.validation.web.dto.HelloDTO;

/**
 *
 * @author grape
 * @date 2019-05-29
 */
public interface HelloBService {

	/**
	 * @TODO
	 * @param helloDTO
	 * @return
	 */
	int insertHello(@Valid  HelloDTO helloDTO);
}
