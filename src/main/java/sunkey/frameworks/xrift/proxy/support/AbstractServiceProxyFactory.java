package sunkey.frameworks.xrift.proxy.support;

import sunkey.frameworks.xrift.proxy.ServiceProxyFactory;

public abstract class AbstractServiceProxyFactory implements ServiceProxyFactory {

  protected ClassLoader getClassLoader(){
    return getClass().getClassLoader();
  }
  
}
