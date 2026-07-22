package com.example.censor.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 脏话和谐服务：按配置映射表替换文本中的敏感词。
 */
@Service
public class CensorService {

    private final Map<String, String> censorWordMap;

    public CensorService(Map<String, String> censorWordMap) {
        this.censorWordMap = censorWordMap;
    }

    /**
     * 对输入文本执行和谐替换。
     * 映射表已按词长降序排列，长词优先匹配。
     *
     * @param text 原始文本，可为 null
     * @return 和谐后的文本；null/空串原样返回
     */
    public String censor(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String result = text;
        for (Map.Entry<String, String> entry : censorWordMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
