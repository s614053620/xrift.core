package sunkey.frameworks.xrift.meta;

public interface ServiceName {

  String getServiceClass();
  
  String getMethodName();
  
  String getVersion();
  
  /**
   * 方法唯一签名
   * @return
   */
  String getSign();
  
}
