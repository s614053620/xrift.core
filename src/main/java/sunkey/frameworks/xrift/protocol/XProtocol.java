package sunkey.frameworks.xrift.protocol;

import java.io.Closeable;
import java.io.IOException;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolDecorator;

import sunkey.frameworks.xrift.meta.ServiceName;
import sunkey.frameworks.xrift.protocol.provider.ProtocolProvider;

/**
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午8:02:58
 *
 */
@SuppressWarnings("unused")
public class XProtocol extends TProtocolDecorator implements Closeable {

  public static XProtocol create(ProtocolProvider provider){
    TProtocol prot = provider.getProtocol();
    if(prot != null)
      return new XProtocol(prot);
    throw new NullPointerException();
  }
  
  private final TProtocol protocol;
  private ServiceName usingService;
  private boolean hasClosed = false;
  
  private int seqid = 0;
  
  private XProtocol(TProtocol protocol) {
    super(protocol);
    this.protocol = protocol;
  }

  public TProtocol getTargetProtocol(){
    return protocol;
  }
  
  public synchronized int nextSeqid(){
    return ++ seqid;
  }
  
 /* public synchronized XProtocol useFor(ServiceName service){
    if(hasClosed)
      throw new IllegalStateException("has closed");
    if(usingService == null){
      usingService = service;
      return this;
    }
    return null;
  }
  
  public void over(){
    usingService = null;
  }*/
  
  @Override
  public void close() throws IOException {
    hasClosed = true;
    protocol.getTransport().close();
  }

  
}
