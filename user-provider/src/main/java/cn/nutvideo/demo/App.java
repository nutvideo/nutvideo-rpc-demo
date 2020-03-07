package cn.nutvideo.demo;

import cn.nutvideo.demo.service.UserService;
import cn.nutvideo.demo.service.UserServiceImpl;
import org.nutvideo.rpc.core.server.GRPCServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "这里是生产者模块，从这里将要创建----rpc服务端" );

        //创建一个rpc服务端
        GRPCServer grpcServer=new GRPCServer();

        //发布服务
        grpcServer.publishServiceAPI(UserService.class,new UserServiceImpl());

        //启动服务
        grpcServer.start(12345);
    }
}
