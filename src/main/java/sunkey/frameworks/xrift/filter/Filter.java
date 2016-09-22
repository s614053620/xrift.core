package sunkey.frameworks.xrift.filter;

public interface Filter<T> {

  T doFilter(T t) throws Exception;
  
}
