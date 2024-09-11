package com.example.uhf;

import com.rscja.deviceapi.UhfBase;

public class ErrorCodeManage {
    public static String getMessage(int errorCode){
         switch (errorCode){
             case UhfBase.ErrorCode.ERROR_NO_TAG:
                 return "找不到标签!";
             case UhfBase.ErrorCode.ERROR_INSUFFICIENT_PRIVILEGES:
                 return "没有权限访问!";
             case UhfBase.ErrorCode.ERROR_MEMORY_OVERRUN:
                 return "数据区超限!";
             case UhfBase.ErrorCode.ERROR_MEMORY_LOCK:
                 return "数据区被锁定!";
             case UhfBase.ErrorCode.ERROR_TAG_NO_REPLY:
                 return "标签没有应答!";
             case UhfBase.ErrorCode.ERROR_PASSWORD_IS_INCORRECT:
                 return "密码不正确!";
             case UhfBase.ErrorCode.ERROR_RESPONSE_BUFFER_OVERFLOW:
                 return "缓冲区溢出!";
             case UhfBase.ErrorCode.ERROR_NO_ENOUGH_POWER_ON_TAG:
                 return "标签能量不足!";
             case UhfBase.ErrorCode.ERROR_OPERATION_FAILED:
                 return "操作失败!";
                 default:
                     return "失败!";

         }
    }
}
