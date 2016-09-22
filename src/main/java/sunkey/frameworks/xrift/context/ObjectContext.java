package sunkey.frameworks.xrift.context;

public interface ObjectContext {

  void registerObject(Object obj);

  <T> T getObject(Class<T> type);
  
}
