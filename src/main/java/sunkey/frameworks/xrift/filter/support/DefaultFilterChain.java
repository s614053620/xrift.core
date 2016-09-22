package sunkey.frameworks.xrift.filter.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunkey.frameworks.xrift.common.Ordered;
import sunkey.frameworks.xrift.filter.FilterChain;
import sunkey.frameworks.xrift.filter.TypedFilter;

public class DefaultFilterChain<T> implements FilterChain<T> {

  private static final Logger loger = LoggerFactory.getLogger(DefaultFilterChain.class);

  private final List<TypedFilter<T>> filters = new ArrayList<>();
  private final Class<T> filterType;

  public DefaultFilterChain(Class<T> type) {
    this.filterType = type;
  }

  @Override
  public Class<T> getFilterType() {
    return filterType;
  }

  @Override
  public T doFilter(T t) throws Exception {
    Iterator<TypedFilter<T>> iterator = filters.iterator();
    while (iterator.hasNext()) {
      TypedFilter<T> filter = iterator.next();
      t = filter.doFilter(t);
    }
    return t;
  }

  @Override
  public List<TypedFilter<T>> getFilters() {
    return filters;
  }

  @Override
  public synchronized boolean registerFilter(TypedFilter<T> filter) {
    if (filter == null) throw new NullPointerException();
    if (!filters.contains(filter)) {
      filters.add(filter);
      loger.info("filter registered:{}", filter);
    } else {
      loger.debug("filter register failed[exists]:{}", filter);
      return false;
    }
    // register succeed, sort it.
    Collections.sort(filters, F_C);
    return true;
  }

  private static final Comparator<TypedFilter<?>> F_C = new Comparator<TypedFilter<?>>() {
    public int compare(TypedFilter<?> o1, TypedFilter<?> o2) {
      int o1v = Ordered.DEFAULT_PRIORITY;
      int o2v = Ordered.DEFAULT_PRIORITY;
      if(o1 instanceof Ordered){
        o1v = ((Ordered) o1).getOrder();
      }
      if(o2 instanceof Ordered){
        o2v = ((Ordered) o2).getOrder();
      }
      return o1v - o2v;
    }
  };

  @Override
  public boolean deregisterFilter(TypedFilter<T> filter) {
    loger.info("filter removed:{}", filter);
    return filters.remove(filter);
  }

  @Override
  public void removeAllFilters() {
    loger.info("all filter removed");
    filters.removeAll(filters);
  }

  @Override
  public boolean isEmpty() {
    return filters.isEmpty();
  }

}
