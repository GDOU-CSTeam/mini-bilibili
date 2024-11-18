

package com.bili.common.exception;


/**
 * @description bili项目异常类
 */
public class BiliLikeException extends RuntimeException {

    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public BiliLikeException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new BiliLikeException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new BiliLikeException(errMessage);
    }
}
