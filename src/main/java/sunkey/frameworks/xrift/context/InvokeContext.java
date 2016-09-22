package sunkey.frameworks.xrift.context;

import java.util.List;

import sunkey.frameworks.xrift.common.LifeCycle;
import sunkey.frameworks.xrift.converter.TypeConverter;
import sunkey.frameworks.xrift.filter.support.FilterContext;
import sunkey.frameworks.xrift.invoke.ProtocolInvoker;
import sunkey.frameworks.xrift.invoke.support.InvokePostProcessor;
import sunkey.frameworks.xrift.meta.support.InvocableService;
import sunkey.frameworks.xrift.protocol.provider.ProtocolProvider;
import sunkey.frameworks.xrift.proxy.ServiceProxyFactory;

public interface InvokeContext extends ObjectContext, LifeCycle {

  <T> InvocableService<T> registerService(String serviceName, Class<T> service);
  
  void start() throws Exception ;
  
  List<InvocableService<?>> getServices();
  
  FilterContext getFilterContext();
  
  ProtocolInvoker getInvoker();
  
  TypeConverter getTypeConverter();
  
  ServiceProxyFactory getProxyFactory();
  
  ProtocolProvider getProtocolProvider();
  
  InvokePostProcessor getInvokePostProcessor();
  
}
