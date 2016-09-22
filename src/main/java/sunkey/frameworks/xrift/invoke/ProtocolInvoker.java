package sunkey.frameworks.xrift.invoke;

public interface ProtocolInvoker extends Invoker<ProtocolInvoke, ProtocolResult> {

  ProtocolResult invoke(ProtocolInvoke invoke);
  
}
