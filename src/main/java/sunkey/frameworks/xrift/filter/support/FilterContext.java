package sunkey.frameworks.xrift.filter.support;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunkey.frameworks.xrift.filter.FilterChain;
import sunkey.frameworks.xrift.filter.TypedFilter;
import sunkey.frameworks.xrift.filter.FilterChain.Factory;

public class FilterContext {

  private static final Logger loger = LoggerFactory.getLogger(FilterContext.class);
  
  private final Map<Class<?>, FilterChain<?>> typedFilters = new HashMap<>();
  
  private final Factory factory;
  
  public FilterContext(){
    this(new Factory() {
      public <T> FilterChain<T> createFilterChain(Class<T> type) {
        loger.debug("use default filter chain.");
        return new DefaultFilterChain<>(type);
      }
    });
  }
  
  public FilterContext(Factory factory){
    this.factory = factory;
  }
  
  @SuppressWarnings("unchecked")
  public final <T> boolean registerFilter(TypedFilter<T> filter){
    FilterChain<T> filterSet = (FilterChain<T>) typedFilters.get(filter.getFilterType());
    if(filterSet == null){
      filterSet = factory.createFilterChain(filter.getFilterType());
      typedFilters.put(filter.getFilterType(), filterSet);
      loger.debug("filter chain created by type:{}", filter.getFilterType());
    }
    return filterSet.registerFilter(filter);
  }
  
  @SuppressWarnings("unchecked")
  public <T> FilterChain<T> getFilterChainByType(Class<T> type){
    return (FilterChain<T>) typedFilters.get(type);
  }
  
}
