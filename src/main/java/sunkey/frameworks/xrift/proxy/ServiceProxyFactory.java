package sunkey.frameworks.xrift.proxy;

import sunkey.frameworks.xrift.meta.support.InvocableService;
import sunkey.frameworks.xrift.meta.support.InvocationHandler;

public interface ServiceProxyFactory {

  <T> InvocableService<T> getProxy(Class<T> type, InvocationHandler handler);
  
  Class<?> getUserClass(Object obj);
  
}
