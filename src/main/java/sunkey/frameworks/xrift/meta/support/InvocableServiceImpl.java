package sunkey.frameworks.xrift.meta.support;

import java.lang.reflect.Method;

public class InvocableServiceImpl<T> extends ServiceImpl<T> implements InvocableService<T> {

  private InvocationHandler handler;
  
  public void setInvocationHandler(InvocationHandler handler){
    this.handler = handler;
  }
  
  public Object handle(Method method, Object ... params){
    return handler.handle(getService(), method, params);
  }
  
}
