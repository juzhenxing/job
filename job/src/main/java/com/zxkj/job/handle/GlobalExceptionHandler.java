package com.zxkj.job.handle;

import com.zxkj.job.exp.JobException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author juzhenxing 2018/3/31 20:00
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = JobException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, JobException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(ErrorInfo.ERROR);
        r.setUrl(req.getRequestURL().toString());
        return r;
    }
}