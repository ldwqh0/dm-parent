package com.dm.security.verification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeGenerator;
import com.dm.security.verification.VerificationCodeStorage;
import com.google.code.kaptcha.Producer;

import static org.springframework.http.MediaType.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

@RestController
@RequestMapping("verificationCode")
public class VerificationCodeController {

    @Autowired
    private Producer producer;

    @Autowired
    private VerificationCodeGenerator validateCodeGenerator;

    @Autowired
    private VerificationCodeStorage codeStorage;

    /**
     * 生成验证码，将验证码数据以Base64格式输出
     * 
     * @return
     */
    @GetMapping(produces = {
            TEXT_PLAIN_VALUE,
            APPLICATION_JSON_VALUE
    })
    public VerificationCode generate() {
        VerificationCode code = validateCodeGenerator.generate(6);
        BufferedImage img = generateImage(code.getCode());
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", os);
            String imgData = Base64.getEncoder().encodeToString(os.toByteArray());
            code.setImgData(imgData);
            codeStorage.save(code);
            return code;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping(produces = {
            IMAGE_GIF_VALUE,
            IMAGE_JPEG_VALUE,
            IMAGE_PNG_VALUE,
            "image/*"
    })
    public ResponseEntity<InputStreamResource> generate2() {
        VerificationCode code = validateCodeGenerator.generate(6);
        code.setId(null);
        BufferedImage img = generateImage(code.getCode());
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            codeStorage.save(code);
            return ResponseEntity.ok(new InputStreamResource(is));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage generateImage(String code) {
        return producer.createImage(code);
//		BufferedImage img = new BufferedImage(140, 40, BufferedImage.TYPE_INT_RGB);
//		Font font = new Font("Fixedsys", Font.BOLD, 30);
//		Graphics gd = img.getGraphics();
//		gd.setColor(Color.RED);
//		gd.setFont(font);
//		gd.drawString(code, 10, 30);
//		gd.dispose();
//		return img;
    }

    @GetMapping("validation")
    public Map<String, Object> checkCode(
            @RequestParam(value = "id", required = false) String verifyId,
            @RequestParam("code") String verifyCode) {
        Map<String, Object> result = new HashMap<>();
        VerificationCode code = codeStorage.get(verifyId);
        if (Objects.isNull(code) || ZonedDateTime.now().isAfter(code.getInvalidateTime())) {
            result.put("result", false);
            result.put("message", "验证码已经失效");
        } else if (codeStorage.validate(verifyId, verifyCode)) {
            result.put("result", Boolean.TRUE);
        } else {
            result.put("result", Boolean.FALSE);
            result.put("message", "验证码错误");
        }
        return result;

    }

}
