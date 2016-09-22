package sunkey.frameworks.xrift.converter;

public interface TypeConverter {

  /**
   * if can't convert, return null;
   * @param sourceType
   * @param source
   * @param needType
   * @return
   */
  Object convert(Class<?> sourceType, Object source, Class<?> needType);
  
}
