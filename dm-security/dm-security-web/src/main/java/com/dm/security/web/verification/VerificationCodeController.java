package com.dm.security.web.verification;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeGenerator;
import com.dm.security.verification.VerificationCodeStorage;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.*;

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
     * @return 生成的验证码
     */
    @GetMapping(produces = {
        TEXT_PLAIN_VALUE,
        APPLICATION_JSON_VALUE
    })
    public VerificationCode generate() throws IOException {
        VerificationCode code = validateCodeGenerator.generate(6);
        BufferedImage img = generateImage(code.getCode());
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", os);
            String imgData = Base64.getEncoder().encodeToString(os.toByteArray());
            code = code.withImageData(imgData);
            codeStorage.save(code);
            return code;
        }
    }

    @GetMapping(produces = {
        IMAGE_GIF_VALUE,
        IMAGE_JPEG_VALUE,
        IMAGE_PNG_VALUE,
        "image/*"
    })
    public ResponseEntity<InputStreamResource> generate2() throws IOException {
        // TODO 这里也不对
        VerificationCode code = validateCodeGenerator.generate(6);
        BufferedImage img = generateImage(code.getCode());
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            codeStorage.save(code);
            return ResponseEntity.ok(new InputStreamResource(is));
        }
    }

    private BufferedImage generateImage(String code) {
        return producer.createImage(code);
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
        } else if (validate(verifyId, verifyCode)) {
            result.put("result", Boolean.TRUE);
        } else {
            result.put("result", Boolean.FALSE);
            result.put("message", "验证码错误");
        }
        return result;
    }

    private boolean validate(String verifyId, String verifyCode) {
        VerificationCode savedCode = codeStorage.get(verifyId);
        return (Objects.nonNull(savedCode))
            && StringUtils.isNotBlank(savedCode.getCode())
            && StringUtils.equals(savedCode.getCode(), verifyCode);
    }
}
