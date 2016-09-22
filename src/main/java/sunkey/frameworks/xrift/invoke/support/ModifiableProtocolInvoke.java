package sunkey.frameworks.xrift.invoke.support;

import org.apache.thrift.protocol.TProtocol;

import sunkey.frameworks.xrift.invoke.ProtocolInvoke;

public interface ModifiableProtocolInvoke extends ProtocolInvoke {

  void setProtocol(TProtocol prot);
  
}
