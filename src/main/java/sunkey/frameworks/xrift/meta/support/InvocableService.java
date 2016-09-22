package sunkey.frameworks.xrift.meta.support;

import java.lang.reflect.Method;

import sunkey.frameworks.xrift.meta.Service;

public interface InvocableService<T> extends Service<T>{

  Object handle(Method method, Object ... params);
  
}
