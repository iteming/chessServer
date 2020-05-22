package com.example.chess.utils;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationResult {

    // 校验结果是否有错
    private boolean isErrors;

    // 校验错误信息
    private Map<String, String> errorMsg;
}
