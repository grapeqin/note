package grape.learn.springboot.validation.service;

import grape.learn.springboot.validation.web.dto.HelloDTO;

/**
 *
 * @author grape
 * @date 2019-05-29
 */
public interface HelloAService {

	/**
	 * @TODO
	 * @param helloDTO
	 * @return
	 */
	int insertHello(HelloDTO helloDTO);
}
