package test.sunkey.frameworks.xrift.c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sunkey.frameworks.xrift.common.Ordered;

public class TestSort {

  public static void main(String[] args) {
    List<Object> ls = new ArrayList<>();
    T t = new T(1);
    T t2= new T(2);
    ls.add(t);
    ls.add(t2);
    Collections.sort(ls, F_C);
    System.out.println(ls);
  }

  static class T implements Ordered {
    private int order;
    public T(int order) {
      this.order = order;
    }
    @Override
    public int getOrder() {
      return order;
    }
    @Override
    public String toString() {
      return "T [order=" + order + "]";
    }
  }

  private static final Comparator<Object> F_C = new Comparator<Object>() {
    public int compare(Object o1, Object o2) {
      int o1v = Ordered.DEFAULT_PRIORITY;
      int o2v = Ordered.DEFAULT_PRIORITY;
      if (o1 instanceof Ordered) {
        o1v = ((Ordered) o1).getOrder();
      }
      if (o2 instanceof Ordered) {
        o2v = ((Ordered) o2).getOrder();
      }
      return o1v - o2v;
    }
  };

}
