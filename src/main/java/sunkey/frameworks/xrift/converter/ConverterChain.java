package sunkey.frameworks.xrift.converter;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunkey.frameworks.xrift.common.Ordered;
/**
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月22日 下午1:01:03
 *
 */
public class ConverterChain implements TypeConverter {

  private static final Logger loger = LoggerFactory.getLogger(ConverterChain.class);

  private final LinkedList<TypeConverter> cvts = new LinkedList<>();

  public void add(TypeConverter cvt) {
    cvts.add(cvt);
    Collections.sort(cvts, F_C);
    loger.info("converter registered:{}", cvt);
  }

  @Override
  public Object convert(Class<?> sourceType, Object source, Class<?> needType) {
    Object cvt_ = null;
    for (TypeConverter cvt : cvts) {
      cvt_ = cvt.convert(sourceType, source, needType);
      if (cvt_ != null)// convert succeed
        return cvt_;
    }
    loger.info("bean [{}] typed [{}] convert to type [{}] failed.", source, sourceType, needType);
    return null;
  }

  private static final Comparator<TypeConverter> F_C = new Comparator<TypeConverter>() {
    public int compare(TypeConverter o1, TypeConverter o2) {
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
  
}
