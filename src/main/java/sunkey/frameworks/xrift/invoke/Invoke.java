package sunkey.frameworks.xrift.invoke;

import sunkey.frameworks.xrift.meta.ServiceName;
/**
 * 一个Invoke调用元信息
 * 
 * @author Sunkey
 *
 */
public interface Invoke {

  /**
   * Service Name Infos
   * @return
   */
  ServiceName getServiceName();
  /**
   * service version info
   * @return
   */
  String getServiceVersion();
  /**
   * invoke params
   * @return
   */
  Object[] getInvokeParams();
  /**
   * method return type
   * @return
   */
  Class<?> getReturnType();
  /**
   * return object?
   * @return
   */
  //Object get();
  /**
   * service target object
   * @return
   */
  Object getInvokeTarget();
  /**
   * invoke identity key;
   * @return
   */
  long getInvokeId();
  
}
