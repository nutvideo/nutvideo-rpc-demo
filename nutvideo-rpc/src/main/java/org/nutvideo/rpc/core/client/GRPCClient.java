package org.nutvideo.rpc.core.client;


import org.nutvideo.rpc.core.protocol.RequestProtocol;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * RPC 客户端核心实现类
 */
public class GRPCClient {

    /**
     * 通过动态代理获取调用接口对应实例
     *
     * Class<T> interfaceClass 被代理的类接口
     *
     * @param interfaceClass
     * @param address
     * @param <T>
     * @return
     *
     */

    public static <T> T getRemoteProxy(Class<T> interfaceClass,InetSocketAddress address){

        /**
         *
         *   原理：对这个类接口进行代理，生成相应的实体类（T）返回
         *
         *   参数1:类加载器
         *   参数2:数组 把被代理的类放进去
         *   参数3: 实现的接口类（使用了匿名内部类）
         *
         *   最后：强转成T
         */


        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                //1.连接接网络
                try (Socket socket=new Socket()){
                    //通过网络连接服务端
                    socket.connect(address);
                    try (
                            //获取输出流程
                            ObjectOutputStream serlializer=new ObjectOutputStream(socket.getOutputStream());
                            //获取输入流程（用于获取服务端取到的数据）
                            ObjectInputStream deSerlializer=new ObjectInputStream(socket.getInputStream());
                    ){
                        //创建一个GRPC 协议对象
                        RequestProtocol requestProtocol=new RequestProtocol();
                        //填充属性
                        requestProtocol.setInterfaceClassName(interfaceClass.getName());
                        requestProtocol.setMethodName(method.getName());
                        requestProtocol.setParameterTypes(method.getParameterTypes());
                        requestProtocol.setParameterValues(args);

                        //进行序列化协议对象（放入到网络中）
                        serlializer.writeObject(requestProtocol);

                        //反序列化(把服务端放入的数据获取出来)
                        Object result=deSerlializer.readObject();

                        //返出结果
                        return result;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

}
