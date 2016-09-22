package sunkey.frameworks.xrift.context;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunkey.frameworks.xrift.converter.ConverterChain;
import sunkey.frameworks.xrift.converter.TypeConverter;
import sunkey.frameworks.xrift.filter.FilterChain;
import sunkey.frameworks.xrift.filter.TypedFilter;
import sunkey.frameworks.xrift.filter.support.FilterContext;
import sunkey.frameworks.xrift.invoke.ProtocolInvoke;
import sunkey.frameworks.xrift.invoke.ProtocolInvoker;
import sunkey.frameworks.xrift.invoke.ProtocolResult;
import sunkey.frameworks.xrift.invoke.support.InvokeImpl;
import sunkey.frameworks.xrift.invoke.support.InvokePostProcessor;
import sunkey.frameworks.xrift.invoke.support.InvokeProcessorChain;
import sunkey.frameworks.xrift.meta.ServiceName;
import sunkey.frameworks.xrift.meta.support.InvocableService;
import sunkey.frameworks.xrift.meta.support.InvocableServiceImpl;
import sunkey.frameworks.xrift.meta.support.InvocationHandler;
import sunkey.frameworks.xrift.pool.ObjectPool;
import sunkey.frameworks.xrift.protocol.XProtocol;
import sunkey.frameworks.xrift.protocol.provider.ProtocolProvider;
import sunkey.frameworks.xrift.proxy.ServiceProxyFactory;
import sunkey.frameworks.xrift.proxy.support.JDKProxyFactory;

public abstract class AbstractInvokeContext implements InvokeContext {

  public static final ServiceProxyFactory DEFAULT_FACTORY = new JDKProxyFactory();
  private static final Logger loger = LoggerFactory.getLogger(AbstractInvokeContext.class);

  private List<InvocableService<?>> services = new ArrayList<>();
  private ServiceProxyFactory factory = DEFAULT_FACTORY;
  private FilterContext filterCtx = new FilterContext();
  private ProtocolInvoker invoker;
  private ConverterChain cvt = new ConverterChain();
  private ProtocolProvider provider;
  private InvokeProcessorChain processor = new InvokeProcessorChain();
  private ObjectPool<XProtocol> protocolPool;

  private final List<Object> objects = new ArrayList<>();
  private volatile long invokingStep = 0;

  @SuppressWarnings("unchecked")
  @Override
  public void registerObject(Object obj) {
    if (obj == null) return;
    if (obj instanceof ServiceProxyFactory) {
      setFactory((ServiceProxyFactory) obj);
    }
    if (obj instanceof ObjectPool) {
      setProtocolPool((ObjectPool<XProtocol>) obj);
    }
    if (obj instanceof InvokePostProcessor) {
      processor.registerProcessor((InvokePostProcessor) obj);
    }
    if(obj instanceof ProtocolProvider){
      setProvider((ProtocolProvider) obj);
    }
    if(obj instanceof TypeConverter){
      cvt.add((TypeConverter)obj);
    }
    if(obj instanceof ProtocolInvoker){
      setInvoker((ProtocolInvoker) obj);
    }
    if(obj instanceof TypedFilter){
      filterCtx.registerFilter((TypedFilter<?>) obj);
    }
    objects.add(obj);
  }

  protected void setFactory(ServiceProxyFactory factory) {
    this.factory = factory;
  }

  protected void setInvoker(ProtocolInvoker invoker) {
    this.invoker = invoker;
  }

  protected void setProvider(ProtocolProvider provider) {
    this.provider = provider;
  }

  protected void setProtocolPool(ObjectPool<XProtocol> protocolPool) {
    this.protocolPool = protocolPool;
  }

  @Override
  public void close() throws IOException {
    loger.info("stopping");
  }

  protected abstract ServiceName getServiceName(String serviceName, Method method);

  @Override
  public <T> InvocableService<T> registerService(final String serviceName, final Class<T> service) {
    InvocableService<T> proxy = factory.getProxy(service, new InvocationHandler() {
      public Object handle(Object target, Method method, Object... params) {
        // 调用元数据
        InvokeImpl inv = new InvokeImpl();
        ProtocolInvoke inv_ = inv;
        inv.setInvokeId(++invokingStep);
        inv.setInvokeParams(params);
        inv.setInvokeTarget(target);
        if (protocolPool == null) {
          inv.setProtocol(provider.getProtocol());
        } else {
          inv.setProtocol(protocolPool.getObject());
        }
        inv.setReturnType(method.getReturnType());
        inv.setServiceName(getServiceName(serviceName, method));
        // 调用过滤链
        FilterChain<ProtocolInvoke> fc = filterCtx.getFilterChainByType(ProtocolInvoke.class);
        if (fc != null) {
          try {
            inv_ = fc.doFilter(inv);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
        if (processor != null) {
          processor.preProcessInvoke(inv_, target);
        }
        ProtocolResult res_ = invoker.invoke(inv_);
        if (processor != null) {
          processor.postProcessInvoke(inv_, target, res_);
        }
        return res_.getResult();
      }
    });
    ((InvocableServiceImpl<T>) proxy).setName(serviceName);
    services.add(proxy);
    objects.add(proxy.getServiceObject());
    return proxy;
  }

  @Override
  public void start() throws Exception {
    loger.info("starting");
  }

  @Override
  public List<InvocableService<?>> getServices() {
    return services;
  }

  @Override
  public FilterContext getFilterContext() {
    return filterCtx;
  }

  @Override
  public ProtocolInvoker getInvoker() {
    return invoker;
  }

  @Override
  public TypeConverter getTypeConverter() {
    return cvt;
  }

  @Override
  public ServiceProxyFactory getProxyFactory() {
    return factory;
  }

  @Override
  public ProtocolProvider getProtocolProvider() {
    return provider;
  }

  @Override
  public InvokePostProcessor getInvokePostProcessor() {
    return processor;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(Class<T> type) {
    for(Object obj : objects){
      if(type.isAssignableFrom(getUserClass(obj))){
        return (T) obj;
      }
    }
    return null;
  }

  protected Class<?> getUserClass(Object obj){
    return getProxyFactory().getUserClass(obj);
  }
  
}
