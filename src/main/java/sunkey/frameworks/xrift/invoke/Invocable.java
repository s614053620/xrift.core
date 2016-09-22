package sunkey.frameworks.xrift.invoke;

import sunkey.frameworks.xrift.meta.ServiceName;

public interface Invocable {

  Object invoke(ServiceName name, Object ... params);
  
}
