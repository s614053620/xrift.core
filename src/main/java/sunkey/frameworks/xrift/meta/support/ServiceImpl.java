package sunkey.frameworks.xrift.meta.support;

import sunkey.frameworks.xrift.meta.Service;

public class ServiceImpl<T> implements Service<T>{

  private String name;
  private T serviceObject;
  private Class<T> serviceType;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public T getService() {
    return serviceObject;
  }
  public void setService(T serviceObject) {
    this.serviceObject = serviceObject;
  }
  public Class<T> getServiceType() {
    return serviceType;
  }
  public void setServiceType(Class<T> serviceType) {
    this.serviceType = serviceType;
  }
  @Override
  public String toString() {
    return "ServiceImpl [name=" + name + ", serviceObject=" + serviceObject + ", serviceType="
        + serviceType + "]";
  }
  
}
