package sunkey.frameworks.xrift.filter;

public interface TypedFilter<T> extends Filter<T>{

  Class<T> getFilterType();
  
}
