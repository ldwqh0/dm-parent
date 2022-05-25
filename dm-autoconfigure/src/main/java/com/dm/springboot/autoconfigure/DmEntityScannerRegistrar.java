package com.dm.springboot.autoconfigure;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.*;

public class DmEntityScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private final Environment environment;

    DmEntityScannerRegistrar(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<String> packages = new ArrayList<>(getPackagesToScan(importingClassMetadata));
        EntityScanPackages.register(registry, packages);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
            .fromMap(metadata.getAnnotationAttributes(DmEntityScan.class.getName()));
        Set<String> packagesToScan = new LinkedHashSet<>();
        if (Objects.nonNull(attributes)) {
            for (String basePackage : attributes.getStringArray("basePackages")) {
                addResolvedPackage(basePackage, packagesToScan);
            }
        }
        return packagesToScan;
    }

    private void addResolvedPackage(String packageName, Set<String> packagesToScan) {
        packagesToScan.add(this.environment.resolvePlaceholders(packageName));
    }
}
