package sunkey.frameworks.xrift.protocol.provider;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.transport.TTransport;
/**
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午9:55:24
 *
 */

import sunkey.frameworks.xrift.protocol.XProtocol;
public abstract class AbstractProtocolProvider implements ProtocolProvider {

  protected static final Map<String, Class<?>> PROTOCOL_CLASS = new HashMap<>();

  static {
    PROTOCOL_CLASS.put("compact", TCompactProtocol.class);
    PROTOCOL_CLASS.put("binary", TBinaryProtocol.class);
    PROTOCOL_CLASS.put("json", TJSONProtocol.class);
    PROTOCOL_CLASS.put("simple-json", TSimpleJSONProtocol.class);
  }

  public static final Class<TCompactProtocol> DEFAULT_PROTOCOL = TCompactProtocol.class;

  @SuppressWarnings("unchecked")
  public static <T extends TProtocol> Class<T> fromType(String type) {
    Class<T> cz = (Class<T>) PROTOCOL_CLASS.get(type);
    if (cz == null) return (Class<T>) DEFAULT_PROTOCOL;
    return cz;
  }

  private Class<TProtocol> protType;
  private Constructor<TProtocol> const_;

  protected abstract TTransport getTransport();

  protected void setProtocolType(String type) {
    protType = fromType(type);
    try {
      const_ = protType.getConstructor(TTransport.class);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public Class<TProtocol> getProtocolType() {
    return protType;
  }

  @Override
  public TProtocol getProtocol() {
    try {
      return const_.newInstance(getTransport());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public XProtocol createProtocol(){
    return XProtocol.create(this);
  }
  
}
