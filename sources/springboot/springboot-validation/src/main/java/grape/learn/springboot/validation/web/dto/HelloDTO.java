package grape.learn.springboot.validation.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 *
 * @author grape
 * @date 2019-05-28
 */
@Data
public class HelloDTO {

	@NotBlank
	private String name;

	@Min(value = 1)
	@Max(value = 200)
	private int age;
}
