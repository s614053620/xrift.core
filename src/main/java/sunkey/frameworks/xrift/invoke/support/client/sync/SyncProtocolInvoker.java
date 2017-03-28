package sunkey.frameworks.xrift.invoke.support.client.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunkey.frameworks.xrift.invoke.ProtocolInvoke;
import sunkey.frameworks.xrift.invoke.ProtocolInvoker;
import sunkey.frameworks.xrift.invoke.ProtocolResult;

public class SyncProtocolInvoker implements ProtocolInvoker {

  private static final Logger loger = LoggerFactory.getLogger(SyncProtocolInvoker.class);

  public SyncProtocolInvoker() {}

  @Override
  public ProtocolResult invoke(ProtocolInvoke invoke) {
    loger.debug("invoking method:{}", invoke.getServiceName().getSign());
    ProtocolResult res = null;
    try {
      new RequestProcessor(invoke.getProtocol()).callMethod(invoke.getServiceName().getSign(),
          invoke.getInvokeParams());
      Object result =
          new ResponseProcessor(invoke.getProtocol()).processResult(invoke.getReturnType());
      res = new ProtocolResult(invoke.getInvokeId(), result);
    } catch (Exception e) {
      loger.error(e.getMessage(), e);
      res = new ProtocolResult(invoke.getInvokeId(), e);
    }
    return res;
  }

  public static class Factory
      implements
        sunkey.frameworks.xrift.common.Factory<SyncProtocolInvoker> {
    private final SyncProtocolInvoker INSTANCE = new SyncProtocolInvoker();

    public SyncProtocolInvoker get() {
      return INSTANCE;
    }
  }

}
