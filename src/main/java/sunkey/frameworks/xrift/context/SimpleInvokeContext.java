package sunkey.frameworks.xrift.context;

import java.lang.reflect.Method;

import sunkey.frameworks.xrift.invoke.support.client.sync.SyncProtocolInvoker;
import sunkey.frameworks.xrift.meta.DefaultServiceName;
import sunkey.frameworks.xrift.meta.ServiceName;
import sunkey.frameworks.xrift.pool.support.GenericProtocolPool;
import sunkey.frameworks.xrift.protocol.provider.ClientProtocolProvider;
import sunkey.frameworks.xrift.protocol.provider.ProtocolProvider;

public class SimpleInvokeContext extends AbstractInvokeContext {

  public SimpleInvokeContext(){
    ProtocolProvider prov = new ClientProtocolProvider(get1());
    setProvider(prov);
    setProtocolPool(new GenericProtocolPool(get2(prov)));
    setInvoker(new SyncProtocolInvoker());
  }
  
  public static ClientProtocolProvider.Args get1() {
    ClientProtocolProvider.Args arg = new ClientProtocolProvider.Args();
    arg.setAddress("localhost:8091");
    arg.setProtocol("compact");
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

  @Override
  protected ServiceName getServiceName(String serviceName, Method method) {
    DefaultServiceName sn = new DefaultServiceName();
    sn.setMethodName(method.getName());
    sn.setServiceClass(method.getDeclaringClass().getName());
    sn.setSign(serviceName + ":" + method.getName());
    sn.setVersion("1.0.0");
    return sn;
  }

}
