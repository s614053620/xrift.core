package sunkey.frameworks.xrift.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Sunkey.
 *
 */
public class JavaBean {

  public static class ByFieldResolver implements BeanTypeResolver {
    public PropertyMap<Getter> getters(Class<?> type) {
      PropertyMap<Getter> gts = new PropertyMap<>();
      int currentIndex = 0;
      for (final Field f : type.getDeclaredFields()) {
        if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
          f.setAccessible(true);
          final String fName = f.getName();
          final Class<?> type_ = f.getType();
          final int index = currentIndex++;
          gts.put(fName, new Getter() {
            public String getName() {
              return fName;
            }

            public Object get(Object target) {
              try {
                return f.get(target);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }

            public int getIndex() {
              return index;
            }

            public Class<?> getType() {
              return type_;
            }
          });
        }
      }
      return gts;
    }

    public PropertyMap<Setter> setters(Class<?> type) {
      PropertyMap<Setter> gts = new PropertyMap<>();
      int currentIndex = 0;
      for (final Field f : type.getDeclaredFields()) {
        if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
          f.setAccessible(true);
          final String fName = f.getName();
          final Class<?> type_ = f.getType();
          final int index = currentIndex++;
          gts.put(fName, new Setter() {
            public void set(Object target, Object val) {
              try {
                f.set(target, val);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }

            public String getName() {
              return fName;
            }

            public int getIndex() {
              return index;
            }

            public Class<?> getType() {
              return type_;
            }
          });
        }
      }
      return gts;
    }
  };
  public static class ByMethodResolver implements BeanTypeResolver {
    static final Method GET_CLASS;
    static {
      try {
        GET_CLASS = Object.class.getMethod("getClass");
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    public PropertyMap<Getter> getters(Class<?> type) {
      PropertyMap<Getter> gts = new PropertyMap<>();
      int currentIndex = 0;
      for (final Method m : type.getMethods()) {
        if (m.getName().startsWith("get") && !m.equals(GET_CLASS)
            && !Modifier.isStatic(m.getModifiers())) {
          // this is a getter method.
          final String propName = getGetterName(m.getName());
          final Class<?> type_ = m.getReturnType();
          final int index = currentIndex++;
          gts.put(propName, new Getter() {
            public String getName() {
              return propName;
            }

            public Object get(Object target) {
              try {
                return m.invoke(target);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }

            public int getIndex() {
              return index;
            }

            public Class<?> getType() {
              return type_;
            }
          });
        }
      }
      return gts;
    }

    static String getGetterName(String name) {
      return firstToLower(name.substring(3));
    }

    static String getSetterName(String name) {
      return firstToLower(name.substring(3));
    }

    static String firstToLower(String str) {
      if (str == null) return null;
      if (str.isEmpty()) return str;
      String fst = String.valueOf(str.charAt(0)).toLowerCase();
      return fst + str.substring(1);
    }

    public PropertyMap<Setter> setters(Class<?> type) {
      PropertyMap<Setter> gts = new PropertyMap<>();
      int currentIndex = 0;
      for (final Method m : type.getMethods()) {
        if (m.getName().startsWith("set") && !m.equals(GET_CLASS)
            && !Modifier.isStatic(m.getModifiers())) {
          // this is a getter method.
          final String propName = getGetterName(m.getName());
          final Class<?> type_ = m.getReturnType();
          final int index = currentIndex++;
          gts.put(propName, new Setter() {
            public String getName() {
              return propName;
            }

            public void set(Object target, Object val) {
              try {
                m.invoke(target, val);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }

            public int getIndex() {
              return index;
            }

            public Class<?> getType() {
              return type_;
            }
          });
        }
      }
      return gts;
    }
  };

  private static BeanTypeResolver beanTypeResolver = new ByFieldResolver();

  public static final void setBeanTypeResolver(BeanTypeResolver resolver) {
    beanTypeResolver = resolver;
  }

  // STATICS
  private static final Map<Class<?>, JavaBean> cache = new HashMap<>();

  public static final JavaBean forType(Class<?> type) {
    JavaBean jb = cache.get(type);
    if (jb == null) {
      jb = new JavaBean(type);
      cache.put(type, jb);
    }
    return jb;
  }

  // UNSTATICS
  private final Class<?> jbType;
  private final PropertyMap<Getter> getters;
  private final PropertyMap<Setter> setters;

  private JavaBean(Class<?> jbType) {
    this.jbType = jbType;
    getters = beanTypeResolver.getters(getType()).unmodifiable();
    setters = beanTypeResolver.setters(getType()).unmodifiable();
  }

  public Class<?> getType() {
    return jbType;
  }

  public PropertyMap<Getter> getters() {
    return getters;
  }

  public PropertyMap<Setter> setters() {
    return setters;
  }

  public Object get(Object target, String name) {
    Getter getter = getters.get(name);
    if (getter == null) throw new NoSuchPropertyException(getType(), name);
    try {
      return getter.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void set(Object target, String name, Object value) {
    Setter setter = setters.get(name);
    if (setter == null) throw new NoSuchPropertyException(getType(), name);
    try {
      setter.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static interface Indexed {
    int getIndex();
  }

  public static interface Property extends Indexed {
    int getIndex();

    Class<?> getType();

    String getName();
  }

  public static interface Getter extends Property {
    public abstract Object get(Object target);
  }
  public static interface Setter extends Property {
    public abstract void set(Object target, Object val);
  }

  public static class PropertyMap<T extends Property> {
    private boolean unmodify = false;
    private final List<T> list = new ArrayList<>();
    private final Map<String, T> mapper = new HashMap<>();

    public synchronized void put(String name, T prop) {
      if (unmodify) throw new UnmodifiablePropertyMapperException();
      list.add(prop);
      mapper.put(name, prop);
    }

    public T get(int index) {
      return list.get(index);
    }

    public T get(String name) {
      return mapper.get(name);
    }

    public int size() {
      return list.size();
    }

    public List<T> list() {
      return list;
    }

    public Map<String, T> map() {
      return mapper;
    }

    public PropertyMap<T> unmodifiable() {
      unmodify = true;
      return this;
    }
  }

  public static interface BeanTypeResolver {
    PropertyMap<Getter> getters(Class<?> type);

    PropertyMap<Setter> setters(Class<?> type);
  }

  public static class UnmodifiablePropertyMapperException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  public static class NoSuchPropertyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String msg;

    public NoSuchPropertyException(Class<?> type, String name) {
      this.msg = type.getName() + "." + name;
    }

    @Override
    public String getMessage() {
      return msg;
    }
  }
}
