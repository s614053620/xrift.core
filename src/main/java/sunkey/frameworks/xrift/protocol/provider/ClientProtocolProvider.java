package sunkey.frameworks.xrift.protocol.provider;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunkey.frameworks.xrift.common.Address;
import sunkey.frameworks.xrift.utils.INetUtils;
/**
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午9:55:24
 *
 */
public class ClientProtocolProvider extends AbstractProtocolProvider {

  private static final Logger loger = LoggerFactory.getLogger(ClientProtocolProvider.class);
  
  public static class Args {
    private String protocol;
    private String address;

    public String getProtocol() {
      return protocol;
    }

    public void setProtocol(String protocol) {
      this.protocol = protocol;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }
  }

  protected final Address address;

  public ClientProtocolProvider(Args args) {
    address = INetUtils.parseAddress(args.getAddress());
    setProtocolType(args.getProtocol());
  }

  @Override
  protected TTransport getTransport() {
    TSocket soc = new TSocket(address.getHost(), address.getPort());
    try {
      soc.open();
    } catch (TTransportException e) {
      loger.error(e.getMessage(), e);
    }
    return soc;
  }

}
