package sunkey.frameworks.xrift.protocol.provider;

import org.apache.thrift.protocol.TProtocol;
/**
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午9:55:24
 *
 */
public interface ProtocolProvider {

  // NOT Multiplexed.
  TProtocol getProtocol();

}
