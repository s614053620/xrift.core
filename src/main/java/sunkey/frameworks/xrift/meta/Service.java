package sunkey.frameworks.xrift.meta;

public interface Service<T> {

  String getName();
  
  T getService();
  
  Class<T> getServiceType();
  
}
