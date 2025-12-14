package ru.lama.expertCookingSystem.entity;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StringArrayType implements UserType<List<String>> {

  @Override
  public int getSqlType() {
    return Types.ARRAY;
  }

  @Override
  public Class<List<String>> returnedClass() {
    return (Class<List<String>>) (Class<?>) List.class;
  }

  @Override
  public boolean equals(List<String> x, List<String> y) {
    return x != null && x.equals(y);
  }

  @Override
  public int hashCode(List<String> x) {
    return x != null ? x.hashCode() : 0;
  }

  @Override
  public List<String> nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
    Array array = rs.getArray(position);
    if (array == null) {
      return new ArrayList<>();
    }
    String[] stringArray = (String[]) array.getArray();
    return new ArrayList<>(List.of(stringArray));
  }

  @Override
  public void nullSafeSet(PreparedStatement st, List<String> value, int index, SharedSessionContractImplementor session) throws SQLException {
    if (value == null || value.isEmpty()) {
      st.setNull(index, Types.ARRAY);
    } else {
      String[] stringArray = value.toArray(new String[0]);
      Array array = st.getConnection().createArrayOf("text", stringArray);
      st.setArray(index, array);
    }
  }

  @Override
  public List<String> deepCopy(List<String> value) {
    return value != null ? new ArrayList<>(value) : new ArrayList<>();
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(List<String> value) {
    return (Serializable) deepCopy(value);
  }

  @Override
  public List<String> assemble(Serializable cached, Object owner) {
    return deepCopy((List<String>) cached);
  }
}