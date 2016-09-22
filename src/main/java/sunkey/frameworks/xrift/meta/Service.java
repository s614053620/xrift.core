package sunkey.frameworks.xrift.meta;

public interface Service<T> {

  String getName();
  
  T getServiceObject();
  
  Class<T> getServiceType();
  
}
