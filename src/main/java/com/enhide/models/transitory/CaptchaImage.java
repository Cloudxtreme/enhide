package com.enhide.models.transitory;

import org.apache.commons.codec.digest.DigestUtils;


/**
 *
 * @author edwin.palathinkal
 */
public class CaptchaImage {
  private String image;
  private String hash;

  public CaptchaImage() {
  }

  public CaptchaImage(String image) {
    this.image = image;
    this.hash = DigestUtils.sha256Hex(image);
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }
}
