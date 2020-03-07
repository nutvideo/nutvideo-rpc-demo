package cn.nutvideo.demo.service;

public class UserServiceImpl implements UserService{

    @Override
    public String addUserName(String name) {
        return "生产者调用api模块的addUserName的接口-实现添加名称："+name;
    }
}
