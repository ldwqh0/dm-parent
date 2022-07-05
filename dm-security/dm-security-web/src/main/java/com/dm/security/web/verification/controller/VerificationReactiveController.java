package com.dm.security.web.verification.controller;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeGenerator;
import com.dm.security.verification.VerificationCodeStorage;
import com.google.code.kaptcha.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("verificationCode")
public class VerificationReactiveController {

    private final Producer producer;

    private final VerificationCodeGenerator validateCodeGenerator;

    private final VerificationCodeStorage codeStorage;

    public VerificationReactiveController(Producer producer, VerificationCodeGenerator validateCodeGenerator, VerificationCodeStorage codeStorage) {
        this.producer = producer;
        this.validateCodeGenerator = validateCodeGenerator;
        this.codeStorage = codeStorage;
    }

    /**
     * 生成验证码，将验证码数据以Base64格式输出
     */
    @GetMapping(produces = {
        TEXT_PLAIN_VALUE,
        APPLICATION_JSON_VALUE
    })
    public Mono<VerificationCode> generate() {
        return Mono.defer(() -> {
            VerificationCode code = validateCodeGenerator.generate(6);
            BufferedImage img = generateImage(code.getCode());
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(img, "png", os);
                String imgData = Base64.getEncoder().encodeToString(os.toByteArray());
                code = code.withImageData(imgData);
                codeStorage.save(code);
                return Mono.just(code);
            } catch (IOException e) {
                return Mono.error(e);
            }
        });
    }

    private BufferedImage generateImage(String code) {
        return producer.createImage(code);
    }
}
