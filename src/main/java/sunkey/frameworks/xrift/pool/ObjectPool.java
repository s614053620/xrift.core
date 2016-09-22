package sunkey.frameworks.xrift.pool;
/**
 * 
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午7:24:26
 *
 */
public interface ObjectPool<T> {

  T getObject();

  void returnObject(T t);

}
