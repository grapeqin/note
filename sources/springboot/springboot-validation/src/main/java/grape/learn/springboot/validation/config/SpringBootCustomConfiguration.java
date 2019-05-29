package grape.learn.springboot.validation.config;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author grape
 * @date 2019-05-28
 */
@ConfigurationProperties(value = "custom")
@Validated
@Component
@Getter
@Setter
public class SpringBootCustomConfiguration {

	@NotBlank(message = "item must not be blank!")
	private String item;
}
