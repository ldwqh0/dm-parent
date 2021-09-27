package com.dm.springboot.autoconfigure;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DmEntityScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private final Environment environment;

    DmEntityScannerRegistrar(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<String> existsPackages = EntityScanPackages.get((BeanFactory) registry).getPackageNames();
        List<String> basePackages = AutoConfigurationPackages.get((BeanFactory) registry);
        basePackages.forEach(packageName -> {
            if (!existsPackages.contains(packageName)) {
                EntityScanPackages.register(registry, packageName);
            }
        });
        Set<String> usePackage = getPackagesToScan(importingClassMetadata);
        usePackage.forEach(packageName -> {
            if (!existsPackages.contains(packageName)) {
                EntityScanPackages.register(registry, packageName);
            }
        });
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
            .fromMap(metadata.getAnnotationAttributes(DmEntityScan.class.getName()));
        Set<String> packagesToScan = new LinkedHashSet<>();
        for (String basePackage : attributes.getStringArray("basePackages")) {
            addResolvedPackage(basePackage, packagesToScan);
        }
        return packagesToScan;
    }

    private void addResolvedPackage(String packageName, Set<String> packagesToScan) {
        packagesToScan.add(this.environment.resolvePlaceholders(packageName));
    }
}
