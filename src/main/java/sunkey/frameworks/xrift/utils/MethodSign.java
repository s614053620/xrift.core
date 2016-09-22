package sunkey.frameworks.xrift.utils;

import java.lang.reflect.Method;

import sunkey.frameworks.xrift.meta.DefaultServiceName;
import sunkey.frameworks.xrift.meta.ServiceName;
/**
 * 自定协议，如使用需两端使用。
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月22日 下午3:41:52
 *
 */
public class MethodSign {

  public static final String METHOD_SPLIT = ".";
  public static final String VERSION_SPLIT = ":";

  public static String generateMethodSign(Method method, String version) {
    return method.getDeclaringClass().getName() + METHOD_SPLIT + method.getName() + VERSION_SPLIT
        + version;
  }

  public static ServiceName fromMethod(Method method, String version){
    DefaultServiceName dsn = new DefaultServiceName();
    dsn.setMethodName(method.getName());
    dsn.setServiceClass(method.getDeclaringClass().getName());
    dsn.setVersion(version);
    dsn.setSign(generateMethodSign(method, version));
    return dsn;
  }
  
}
