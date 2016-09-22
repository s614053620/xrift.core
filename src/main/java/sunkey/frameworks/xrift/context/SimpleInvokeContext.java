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
import sunkey.frameworks.xrift.filter.support.FilterContext;
import sunkey.frameworks.xrift.invoke.ProtocolInvoke;
import sunkey.frameworks.xrift.invoke.ProtocolInvoker;
import sunkey.frameworks.xrift.invoke.ProtocolResult;
import sunkey.frameworks.xrift.invoke.support.InvokeImpl;
import sunkey.frameworks.xrift.invoke.support.InvokePostProcessor;
import sunkey.frameworks.xrift.invoke.support.sync.SyncProtocolInvoker;
import sunkey.frameworks.xrift.meta.DefaultServiceName;
import sunkey.frameworks.xrift.meta.support.InvocableService;
import sunkey.frameworks.xrift.meta.support.InvocableServiceImpl;
import sunkey.frameworks.xrift.meta.support.InvocationHandler;
import sunkey.frameworks.xrift.pool.support.GenericProtocolPool;
import sunkey.frameworks.xrift.pool.support.PooledProtocolFilter;
import sunkey.frameworks.xrift.protocol.provider.ClientProtocolProvider;
import sunkey.frameworks.xrift.protocol.provider.ProtocolProvider;
import sunkey.frameworks.xrift.proxy.ServiceProxyFactory;
import sunkey.frameworks.xrift.proxy.support.JDKProxyFactory;

public class SimpleInvokeContext implements InvokeContext {

  private static final Logger loger = LoggerFactory.getLogger(SimpleInvokeContext.class);
  
  private List<InvocableService<?>> services = new ArrayList<>();
  private JDKProxyFactory factory = new JDKProxyFactory();
  private FilterContext filterCtx = new FilterContext();
  private SyncProtocolInvoker invoker = new SyncProtocolInvoker();
  private ConverterChain cvt = new ConverterChain();
  private ClientProtocolProvider provider = new ClientProtocolProvider(get1());
  
  GenericProtocolPool pool = new GenericProtocolPool(get2(provider));
  private PooledProtocolFilter processor = new PooledProtocolFilter(pool);
  private volatile long invokingStep = 0;

  public static ClientProtocolProvider.Args get1(){
    ClientProtocolProvider.Args arg = new ClientProtocolProvider.Args();
    arg.setAddress("localhost:9876");
    arg.setProtocol("json");
    return arg;
  }
  
  public static GenericProtocolPool.Args get2(ProtocolProvider pv) {
    GenericProtocolPool.Args arg = new GenericProtocolPool.Args();
    arg.setProvider(pv);
    arg.setCapacity(10);
    arg.setFair(true);
    arg.setAvaliableTime(100000L);
    return arg;
  }

  public SimpleInvokeContext(){
    filterCtx.registerFilter(processor);
  }
  
  @Override
  public void registerObject(final Object obj) {
    
  }

  @Override
  public void close() throws IOException {
    loger.info("closed");
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

  public <T> InvocableService<T> registerService(final String serviceName, Class<T> service) {
    InvocableService<T> proxy = factory.getProxy(service, new InvocationHandler() {
      public Object handle(Object target, Method method, Object... params) {
        //调用元数据
        InvokeImpl inv = new InvokeImpl();
        ProtocolInvoke inv_ = inv;
        inv.setInvokeId(++invokingStep);
        inv.setInvokeParams(params);
        inv.setInvokeTarget(target);
        inv.setProtocol(pool.getObject());
        inv.setReturnType(method.getReturnType());
        DefaultServiceName sn = new DefaultServiceName();
        sn.setSign(serviceName + ":" + method.getName());
        inv.setServiceName(sn);
        //调用过滤链
        FilterChain<ProtocolInvoke> fc = filterCtx.getFilterChainByType(ProtocolInvoke.class);
        try {
          inv_ = fc.doFilter(inv);
          processor.preProcessInvoke(inv_, target);
          ProtocolResult res_ = invoker.invoke(inv_);
          processor.postProcessInvoke(inv_, target, res_);
          return res_.getResult();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
    ((InvocableServiceImpl<T>) proxy).setName(serviceName);
    services.add(proxy);
    return proxy;
  }
  
  
  @Override
  public <T> T getObject(Class<T> type) {
    return null;
  }

  @Override
  public InvokePostProcessor getInvokePostProcessor() {
    return processor;
  }

}
