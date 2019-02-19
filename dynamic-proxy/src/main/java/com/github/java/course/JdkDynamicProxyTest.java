package com.github.java.course;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicProxyTest {
	
	public static void main(String[] args) {
		
		Animal animal = new AnimalProxy(new Dog()).getProxy(); // 获取代理对象
		System.out.println(animal.getClass());
		System.out.println(animal.name());
		
		Animal tomcat = new AnimalProxy(new Tomcat()).getProxy();
		System.out.println(tomcat.name());
		
		System.out.println(AnimalProxyFactory.getProxy("dog").name());
		System.out.println(AnimalProxyFactory.getProxy("tomcat").name());
		System.out.println(AnimalProxyFactory.getProxy("pick").name());
	}

}

class AnimalProxy implements InvocationHandler {
	
	private Animal target; // 真实接口实现对象
	
	private Animal proxy;

	public AnimalProxy(Animal target) {
		super();
		this.target = target;
		proxy = (Animal) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}

	public Animal getProxy() {
		return proxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(this + " InvocationHandler#invoke ...");
		return method.invoke(this.target, args);
	}
	
}

class AnimalProxyFactory {
	
	public static Animal getProxy(String animal) {
		if ("tomcat".equalsIgnoreCase(animal)) {
			return new AnimalProxy(new Tomcat()).getProxy();
		} else if ("dog".equalsIgnoreCase(animal)) {
			return new AnimalProxy(new Dog()).getProxy();
		}
		// ServiceLoader
		// SpringContext
		// etc.
		return new AnimalProxy(() -> "undefined").getProxy();
	}
}

interface Animal {
	
	String name();
	
}

class Dog implements Animal {
	
	@Override
	public String name() {
		return "Dog";
	}
	
}

class Tomcat implements Animal {

	@Override
	public String name() {
		return "tomcat";
	}
	
}
