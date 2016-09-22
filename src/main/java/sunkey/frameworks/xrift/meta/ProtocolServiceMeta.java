package sunkey.frameworks.xrift.meta;

import org.apache.thrift.protocol.TProtocol;

public interface ProtocolServiceMeta extends ServiceMeta {

  TProtocol getProtocol();
  
}
