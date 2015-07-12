package com.enhide.repositories;

import com.enhide.models.persistent.Captcha;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author edwinhere@gmail.com
 */
public interface CaptchaRepository extends CrudRepository<Captcha, Long> {

	Captcha findByHashEquals(String hash);
}
