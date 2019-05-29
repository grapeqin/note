package grape.learn.springboot.validation.service.impl;

import grape.learn.springboot.validation.service.HelloAService;
import grape.learn.springboot.validation.service.HelloBService;
import grape.learn.springboot.validation.web.dto.HelloDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author grape
 * @date 2019-05-29
 */
@Service
public class HelloAServiceImpl implements HelloAService {

	@Autowired
	private HelloBService helloBService;

	@Override
	public int insertHello(HelloDTO helloDTO) {
		return helloBService.insertHello(helloDTO);
	}
}
