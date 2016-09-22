package sunkey.frameworks.xrift.invoke.support;

import sunkey.frameworks.xrift.invoke.InvokeResult;
import sunkey.frameworks.xrift.invoke.ProtocolInvoke;

public interface InvokePostProcessor {

  void preProcessInvoke(ProtocolInvoke invoke, Object invokeContext);
  
  void postProcessInvoke(ProtocolInvoke invoke, Object invokeContext, InvokeResult result);
  
}
