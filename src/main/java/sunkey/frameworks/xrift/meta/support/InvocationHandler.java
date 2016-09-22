package sunkey.frameworks.xrift.meta.support;

import java.lang.reflect.Method;

public interface InvocationHandler{

  Object handle(Object target, Method method, Object ... params);
  
}
