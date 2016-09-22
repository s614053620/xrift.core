package sunkey.frameworks.xrift.invoke;

import org.apache.thrift.protocol.TProtocol;
/**
 * 带有协议的Invoke元数据
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午5:09:01
 *
 */
public interface ProtocolInvoke extends Invoke {

  TProtocol getProtocol();
  
}
