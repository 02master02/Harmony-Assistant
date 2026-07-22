package com.example.censor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 从 censor.properties 加载脏话和谐映射。
 * 使用 @PropertySource 支持后续在配置文件中动态扩展词条。
 */
@Configuration
@PropertySource(value = "classpath:censor.properties", encoding = "UTF-8", name = "censorProps")
public class CensorConfig {

    /**
     * 构建按「原始词长度降序」排列的映射表，避免短词抢先替换长词。
     * 例如先处理「你妈」再处理「妈」。
     */
    @Bean
    public Map<String, String> censorWordMap(ConfigurableEnvironment environment) {
        EnumerablePropertySource<?> enumerable =
                (EnumerablePropertySource<?>) environment.getPropertySources().get("censorProps");
        if (enumerable == null) {
            throw new IllegalStateException("未能加载 censor.properties，请检查 classpath 配置");
        }

        Map<String, String> raw = new LinkedHashMap<>();
        for (String name : enumerable.getPropertyNames()) {
            Object value = enumerable.getProperty(name);
            if (value != null && !name.isBlank()) {
                raw.put(name, value.toString());
            }
        }

        List<String> keys = new ArrayList<>(raw.keySet());
        keys.sort((a, b) -> Integer.compare(b.length(), a.length()));

        Map<String, String> ordered = new LinkedHashMap<>();
        for (String key : keys) {
            ordered.put(key, raw.get(key));
        }
        return Collections.unmodifiableMap(ordered);
    }
}
