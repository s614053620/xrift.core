package sunkey.frameworks.xrift.invoke;

/**
 * 反射执行者
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午5:09:54
 *
 */
public interface Invoker<I extends Invoke, O> {

  O invoke(I invoke);
  
}
