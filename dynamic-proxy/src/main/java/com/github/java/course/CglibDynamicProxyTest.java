package com.github.java.course;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibDynamicProxyTest {

	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Message.class);
		enhancer.setCallback(new MessageProxy(new Message()));
		Message proxyObj = (Message) enhancer.create(); // 创建代理对象
		proxyObj.send("Hello Message !");
		
		MessageProxyFactory.getProxy("SMS").send("现在正在开会稍后联系你");
		MessageProxyFactory.getProxy("WeChat").send("现在正在开会稍后联系你");
		MessageProxyFactory.getProxy("Email").send("现在正在开会稍后联系你");
		
	}
	
}

class MessageProxyFactory {
	
	public static Message getProxy(String type) {
		MessageProxy callback = null;
		if ("SMS".equalsIgnoreCase(type)) {
			// 短信
			callback = new MessageProxy(new Message());
		} else if ("WeChat".equalsIgnoreCase(type)) {
			// 微信
			callback = new MessageProxy(new WeChatMessage());
		} else if ("Email".equalsIgnoreCase(type)) {
			// 邮件
			callback = new MessageProxy(new EmailMessage());
		} else {
			callback = new MessageProxy(new Message());
		}
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Message.class);
		enhancer.setCallback(callback);
		
		return (Message) enhancer.create();
	}
}

class MessageProxy implements MethodInterceptor {
	
	private Message target; // 真实实现对象

	public MessageProxy(Message target) {
		super();
		this.target = target;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		System.out.println("Invoke by cglib MethodInterceptor#intercept");
		return method.invoke(this.target, args);
	}
	
}

class Message {
	
	public boolean send(String message) {
		System.out.println("Send SMS: " + message);
		return true;
	}
	
}

class WeChatMessage extends Message {
	
	@Override
	public boolean send(String message) {
		System.out.println("Send WeChat: " + message);
		return true;
	}
	
}

class EmailMessage extends Message {
	
	@Override
	public boolean send(String message) {
		System.out.println("Send Email: " + message);
		return true;
	}
	
}