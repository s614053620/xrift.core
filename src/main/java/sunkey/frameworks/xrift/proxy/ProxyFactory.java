package sunkey.frameworks.xrift.proxy;

import sunkey.frameworks.xrift.meta.support.InvocationHandler;

public interface ProxyFactory<T> {

  T getProxy(Class<?> type, InvocationHandler handler);
  
}
