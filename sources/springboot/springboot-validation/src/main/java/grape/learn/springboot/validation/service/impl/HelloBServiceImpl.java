package grape.learn.springboot.validation.service.impl;

import javax.validation.Valid;

import grape.learn.springboot.validation.service.HelloBService;
import grape.learn.springboot.validation.web.dto.HelloDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author grape
 * @date 2019-05-29
 */
@Service
@Slf4j
@Validated
public class HelloBServiceImpl implements HelloBService {

	@Override
	public int insertHello(@Valid HelloDTO helloDTO) {
		log.info("insertHello method execute....");
		return 0;
	}
}
