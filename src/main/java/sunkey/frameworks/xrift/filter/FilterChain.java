package sunkey.frameworks.xrift.filter;

import java.util.List;

public interface FilterChain<T> {

  Class<T> getFilterType();
  
  T doFilter(T t) throws Exception;
  
  List<TypedFilter<T>> getFilters();
  
  boolean registerFilter(TypedFilter<T> filter);
  
  boolean deregisterFilter(TypedFilter<T> filter);
  
  void removeAllFilters();
  
  boolean isEmpty();

  public static interface Factory{
    <T> FilterChain<T> createFilterChain(Class<T> type);
  }
  
}
