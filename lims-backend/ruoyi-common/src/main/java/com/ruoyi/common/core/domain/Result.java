package com.ruoyi.common.core.domain;

import java.io.Serializable;
import com.ruoyi.common.constant.HttpStatus;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
public class Result<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = HttpStatus.SUCCESS;

    /** 失败 */
    public static final int FAIL = HttpStatus.ERROR;

    private int code;

    private String message;

    private T data;

    public static <T> Result<T> success()
    {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data)
    {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Result<T> fail()
    {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> Result<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> Result<T> fail(T data)
    {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> Result<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> Result<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg)
    {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMessage(msg);
        return apiResult;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public static <T> Boolean isError(Result<T> ret)
    {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(Result<T> ret)
    {
        return Result.SUCCESS == ret.getCode();
    }
}
