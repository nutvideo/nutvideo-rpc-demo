package cn.nutvideo.demo;

import cn.nutvideo.demo.service.UserService;
import org.nutvideo.rpc.core.client.GRPCClient;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "这是消费者=====" );

        UserService userService=GRPCClient.
                getRemoteProxy(UserService.class,new InetSocketAddress("127.0.0.1",12345));

        String result=userService.addUserName("nutvideo===111111");

        System.out.println( result );
    }
}
