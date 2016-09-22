package sunkey.frameworks.xrift.invoke.support.sync;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMap;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSet;
import org.apache.thrift.protocol.TStruct;
import org.slf4j.Logger;

import sunkey.frameworks.xrift.protocol.XProtocol;
import sunkey.frameworks.xrift.utils.JavaBean;
import sunkey.frameworks.xrift.utils.JavaBean.Getter;
import sunkey.frameworks.xrift.utils.THelper;
import static sunkey.frameworks.xrift.utils.THelper.forThriftType;

public class RequestProcessor {

  private static final Logger loger = org.slf4j.LoggerFactory.getLogger(RequestProcessor.class);

  private final TProtocol prot;

  public RequestProcessor(TProtocol prot) {
    this.prot = prot;
  }

  public void callMethod(String method, Object... args)
      throws TException, IllegalArgumentException, IllegalAccessException {
    int nextSeqId = 1;
    if (prot instanceof XProtocol) {
      nextSeqId = ((XProtocol) prot).nextSeqid();
    }
    TMessage tmsg = new TMessage(method, TMessageType.CALL, nextSeqId);
    loger.trace("writeMessageBegin:{}", THelper.toString(tmsg));
    prot.writeMessageBegin(tmsg);
    writeParam(prot, args);
    loger.trace("writeMessageEnd:{}", THelper.toString(tmsg));
    prot.writeMessageEnd();
    prot.getTransport().flush();
  }

  private void writeParam(TProtocol prot, Object... param)
      throws TException, IllegalArgumentException, IllegalAccessException {
    loger.trace("writeStructBegin:args");
    prot.writeStructBegin(new TStruct("args"));
    for (int i = 0; i < param.length; i++) {
      writeField(prot, i + 1, param[i]);
    }
    loger.trace("writeFieldStop[PARAM]");
    prot.writeFieldStop();
    loger.trace("writeStructEnd[PARAM]");
    prot.writeStructEnd();
  }

  private void writeStruct(TProtocol prot, Object struct)
      throws TException, IllegalArgumentException, IllegalAccessException {
    Class<?> type = struct.getClass();
    String simpleName = type.getSimpleName();
    loger.trace("writeStructBegin:{}", simpleName);
    prot.writeStructBegin(new TStruct(simpleName));
    JavaBean jb = JavaBean.forType(type);
    for (Getter g : jb.getters().list()) {
      writeField(prot, g.getIndex() + 1, g.get(struct));
    }
    loger.trace("writeFieldStop:{}", simpleName);
    prot.writeFieldStop();
    loger.trace("writeStructEnd:{}", simpleName);
    prot.writeStructEnd();
  }

  private void writeObject(TProtocol prot, Object obj)
      throws IllegalArgumentException, IllegalAccessException, TException {
    // switch type (list, map, set, struct_, isPrimitive, string,..)
    if (obj == null) {
      writeNull(prot);
    } else {
      Class<?> type = obj.getClass();
      if (type == String.class) {
        writeString(prot, (String) obj);
      } else if (List.class.isAssignableFrom(type)) {
        writeList(prot, (List<?>) obj);
      } else if (Map.class.isAssignableFrom(type)) {
        writeMap(prot, (Map<?, ?>) obj);
      } else if (Set.class.isAssignableFrom(type)) {
        writeSet(prot, (Set<?>) obj);
      } else if (tryWritePrimitive(prot, obj)) {} else if (tryWriteCustom(prot, type)) {} else {
        writeStruct(prot, obj);
      }
    }
  }

  private boolean tryWriteCustom(TProtocol prot, Object obj) throws TException {
    if (obj.getClass() == Date.class) {
      tryWritePrimitive(prot, ((Date) obj).getTime());
      return true;
    }
    return false;
  }

  private void writeList(TProtocol prot, List<?> list)
      throws TException, IllegalArgumentException, IllegalAccessException {
    Class<?> type = THelper.getCollectionGenericType(list);
    byte ttype = forThriftType(type);
    TList tlist = new TList(ttype, list.size());
    prot.writeListBegin(tlist);
    for (Object obj : list) {
      writeObject(prot, obj);
    }
    prot.writeListEnd();
  }

  private void writeMap(TProtocol prot, Map<?, ?> map)
      throws TException, IllegalArgumentException, IllegalAccessException {
    Class<?>[] typeA = THelper.getMapGenericTypes(map);
    byte ttypeK = forThriftType(typeA[0]);
    byte ttypeV = forThriftType(typeA[1]);
    TMap tmap = new TMap(ttypeK, ttypeV, map.size());
    prot.writeMapBegin(tmap);
    for (Map.Entry<?, ?> e : map.entrySet()) {
      writeObject(prot, e.getKey());
      writeObject(prot, e.getValue());
    }
    prot.writeMapEnd();
  }

  private void writeSet(TProtocol prot, Set<?> set)
      throws TException, IllegalArgumentException, IllegalAccessException {
    Class<?> type = THelper.getCollectionGenericType(set);
    byte ttype = forThriftType(type);
    TSet tset = new TSet(ttype, set.size());
    prot.writeSetBegin(tset);
    for (Object obj : set) {
      writeObject(prot, obj);
    }
    prot.writeSetEnd();
  }

  private void writeString(TProtocol prot, String str) throws TException {
    loger.trace("writeString");
    prot.writeString(str);
  }

  private void writeNull(TProtocol prot) {
    // UNDO
  }

  private boolean tryWritePrimitive(TProtocol prot, Object obj) throws TException {
    Class<?> type = obj.getClass();
    if (type == Integer.class || type == Integer.TYPE) {
      prot.writeI32(((Integer) obj).intValue());
    } else if (type == Long.class || type == Long.TYPE) {
      prot.writeI64(((Long) obj).longValue());
    } else if (type == Short.class || type == Short.TYPE) {
      prot.writeI16(((Short) obj).shortValue());
    } else if (type == Byte.class || type == Byte.TYPE) {
      prot.writeByte(((Byte) obj).byteValue());
    } else if (type == Float.class || type == Float.TYPE) {
      prot.writeDouble(((Float) obj).doubleValue());
    } else if (type == Double.class || type == Double.TYPE) {
      prot.writeDouble(((Float) obj).doubleValue());
    } else if (type == Character.class || type == Character.TYPE) {
      // character use i32? or i16 or custom or not_support
      prot.writeI32((int) ((Character) obj).charValue());
    } else if (type == Boolean.class || type == Boolean.TYPE) {
      prot.writeBool(((Boolean) obj).booleanValue());
    } else {
      return false;
    }
    return true;
  }

  private void writeField(TProtocol prot, int index, Object obj)
      throws TException, IllegalArgumentException, IllegalAccessException {
    byte ttype = forThriftType(obj.getClass());
    TField tf = new TField("nouse", ttype, (short) index);
    loger.trace("writeFieldBegin:{}", THelper.toString(tf));
    prot.writeFieldBegin(tf);
    writeObject(prot, obj);
    loger.trace("writeFieldEnd:{}", THelper.toString(tf));
    prot.writeFieldEnd();
  }

}
