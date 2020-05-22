package com.example.chess.exception;

import com.example.chess.entity.base.Result;
import com.example.chess.protocol.ICode;
import com.example.chess.protocol.SysErrorEnum;
import com.example.chess.utils.ExceptionUtil;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 江斌  <br/>
 * @version 1.0  <br/>
 * @ClassName: GlobalExceptionHandler <br/>
 * @date: 2019/9/23 11:31 <br/>
 * @Description: 全局异常处理类 <br/>
 * @since JDK 1.8
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active:dev}")
    public String activeProfile;

    private static final String PROD_MODE = "prod";

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 构建返回错误
     *
     * @param code
     * @param exception
     * @return
     */
    private Result failure(ICode code, Exception exception) {
        String error = null;
        if (null != exception) {
            if (!activeProfile.equalsIgnoreCase(PROD_MODE)) {
                error = exception.getMessage() != null ? exception.getMessage() : code.getMessage();
            }
        }
        return Result.custom(code, error);
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SysException.class)
    public Result handleSysException(SysException e) {
        log.error("自定义系统异常:{}", ExceptionUtil.buildErrorMessage(e));
        return Result.custom(e.getCode(), e.getMessage());
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public Result handleBizException(BizException e) {
        log.debug("业务异常:{}", e.getMessage());
        return Result.custom(e.getCode(), e.getMessage());
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthException.class)
    public Result handleAuthException(AuthException e) {
        log.warn("权限异常:{}", e.getMessage());
        return Result.custom(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValid:({})", ExceptionUtil.buildErrorMessage(e));
        FieldError fieldError = e.getBindingResult().getFieldError();
        Map<String, Object> fields = new HashMap<>();
        fields.put(fieldError.getField(), fieldError.getDefaultMessage());
        return Result.custom(SysErrorEnum.SYS_ILLEGAL_ARGUMENT_TYPE, fields.toString());
    }


    /**
     * 参数效验
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleValidationException(ConstraintViolationException e) {
        log.debug("ConstraintViolationException:({})", ExceptionUtil.buildErrorMessage(e));
        List<Map<String, Object>> fields = new ArrayList<>();
        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
            String fieldName = ((PathImpl) cv.getPropertyPath()).getLeafNode().asString();
            String message = cv.getMessage();
            Map<String, Object> field = new HashMap<>();
            field.put("field", fieldName);
            field.put("message", message);
            fields.add(field);
        }
        return Result.custom(SysErrorEnum.SYS_ILLEGAL_ARGUMENT_TYPE, fields.toString());
    }

    /**
     * 参数类型
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        log.debug("BindException:({})", ExceptionUtil.buildErrorMessage(e));
        List<Map<String, Object>> fields = new ArrayList<>();
        for (FieldError error : e.getFieldErrors()) {
            Map<String, Object> field = new HashMap<>();
            field.put("field", error.getField());
            field.put("message", error.getDefaultMessage());
            fields.add(field);
        }
        return Result.custom(SysErrorEnum.SYS_ILLEGAL_ARGUMENT_TYPE, fields.toString());
    }

    /**
     * 参数格式不对
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug("IllegalArgumentException:({})", ExceptionUtil.buildErrorMessage(e));
        return failure(SysErrorEnum.SYS_ILLEGAL_DATA, e);
    }

    /**
     * 缺少参数
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        log.debug("MissingServletRequestParameterException:({})", ExceptionUtil.buildErrorMessage(e));
        return failure(SysErrorEnum.SYS_MISSING_ARGUMENT, e);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleMethodArgumentTypeMismatchExceptionException(
            MethodArgumentTypeMismatchException e) {
        log.debug("MethodArgumentTypeMismatchException:({})", ExceptionUtil.buildErrorMessage(e));
        return failure(SysErrorEnum.SYS_ILLEGAL_ARGUMENT_TYPE, e);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception e) {
        log.error("Exception:({})", ExceptionUtil.buildErrorMessage(e));
        return failure(SysErrorEnum.SYS_ERROR, e);
    }
}
