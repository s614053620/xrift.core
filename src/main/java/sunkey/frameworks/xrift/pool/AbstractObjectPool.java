package sunkey.frameworks.xrift.pool;

import java.util.LinkedList;
import java.util.Queue;
/**
 * 
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午7:24:26
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class AbstractObjectPool<T> implements ObjectPool<T> {

  private static final Logger loger = LoggerFactory.getLogger(AbstractObjectPool.class);
  
  protected final Queue<T> queue = new LinkedList<>();

  @Override
  public T getObject() {
    loger.trace("get source from pool");
    T t = queue.poll();
    if (t == null) {
      loger.debug("source not exists, try init.");
      t = init();
    }else{
      loger.trace("geted return resource.");
    }
    return t;
  }

  @Override
  public void returnObject(T t) {
    loger.trace("return resource");
    if(t != null)
      queue.add(t);
  }

  protected abstract T init();

}
