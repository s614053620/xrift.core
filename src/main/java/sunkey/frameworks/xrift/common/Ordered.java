package sunkey.frameworks.xrift.common;

public interface Ordered {

  int getOrder();
  
  int MAX_PRIORITY = 1;
  int MIN_PRIORITY = Integer.MAX_VALUE;
  int DEFAULT_PRIORITY = 5000;
  
}
