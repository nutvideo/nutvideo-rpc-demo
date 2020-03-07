package org.nutvideo.rpc.core.protocol;


import java.io.Serializable;

/**
 * RPC框架请求协议类，由于这个类需要在网络上传输 需要进行序列化
 *
 */
public class RequestProtocol implements Serializable {


    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    private String interfaceClassName;//接口全称

    private String methodName;//方法名称

    private Class<?>[] parameterTypes;//参数类型列表

    private Object[] parameterValues;//参数值列表






}
