package sunkey.frameworks.xrift.proxy.support;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.aop.support.AopUtils;

import sunkey.frameworks.xrift.meta.support.InvocableService;
import sunkey.frameworks.xrift.meta.support.InvocableServiceImpl;
import sunkey.frameworks.xrift.meta.support.InvocationHandler;

public class JDKProxyFactory extends AbstractServiceProxyFactory {

  @Override
  @SuppressWarnings("unchecked")
  public <T> InvocableService<T> getProxy(Class<T> type, final InvocationHandler handler) {
    InvocableServiceImpl<T> service = new InvocableServiceImpl<T>();
    service.setInvocationHandler(handler);
    service.setName(type.getName());
    service.setServiceType(type);
    T proxyObject = (T) Proxy.newProxyInstance(getClassLoader(), new Class<?>[]{type}, new java.lang.reflect.InvocationHandler() {
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return handler.handle(proxy, method, args);
      }
    });
    service.setService(proxyObject);
    return service;
  }

  @Override
  public Class<?> getUserClass(Object obj) {
    return AopUtils.getTargetClass(obj);
  }
  
}
