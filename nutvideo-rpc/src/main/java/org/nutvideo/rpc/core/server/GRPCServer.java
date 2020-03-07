package org.nutvideo.rpc.core.server;

import org.nutvideo.rpc.core.protocol.RequestProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * RPC 框架服务的核心实现
 *
 *
 * 实现步骤：
 *
 * 1.暴露调用服务接口
 * 2.启用服务端
 *
 */
public class GRPCServer {


    //定义存储暴露服务列表
    Map<String,Object> serverMap=new ConcurrentHashMap<>(32);


    //使用多线程
    //定义一个线程池
    // 8核 20线程 200 毫秒

    ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(8,
            20,
            200,
            TimeUnit.MICROSECONDS,
            new ArrayBlockingQueue<>(10));

    /**
     *
     * @param interfaceClass 类对象
     * @param instance  （具体的服务实现类实例）
     *
     *
     */
    public void publishServiceAPI(Class<?> interfaceClass,Object instance){
        this.serverMap.put(interfaceClass.getName(),instance);
    }


    /**
     * 发布服务的方法
     * @param port
     */

    public void start(int port){

        try{

            //创建服务端
            ServerSocket serverSocket=new ServerSocket();

            //绑定指定端口
            serverSocket.bind(new InetSocketAddress(port));

            System.out.println("==============nutvideo GRPC server Strarting....... ==============");

            while (true){
                poolExecutor.execute(new ServerTask(serverSocket.accept()));
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 创建客户端请求处理的线程类
     */

    private class ServerTask implements Runnable {

        private final Socket socket;

        public ServerTask(Socket socket){

            this.socket=socket;
        }

        @Override
        public void run(){

            try (
                    ObjectInputStream deSerializer=new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream serializer=new ObjectOutputStream(socket.getOutputStream());

                    ){
                //反序列化获取服务端获取的输入数据(RequestProtocol)

                RequestProtocol requestProtocol= (RequestProtocol) deSerializer.readObject();

                //获取接口全名称

                String interfaceClassName=requestProtocol.getInterfaceClassName();
                Object instance=serverMap.get(interfaceClassName);
                if (null==instance){
                    return;
                }

                //创建一个方法对象(利用反射方式)

                Method method=instance.getClass().
                        getDeclaredMethod(
                                requestProtocol.getMethodName(),
                                requestProtocol.getParameterTypes());

                //2.调用方法

                Object result=method.invoke(instance,requestProtocol.getParameterValues());

                //3.序列化调用的结果

                serializer.writeObject(result);


            } catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("执行run方法");
        }
    }


}
