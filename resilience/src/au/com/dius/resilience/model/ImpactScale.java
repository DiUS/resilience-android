package au.com.dius.resilience.model;

import android.util.SparseArray;

public enum ImpactScale {
  LOW(0),
  MEDIUM(1),
  HIGH(2);
  
  private Integer code;
  
  private static final SparseArray<ImpactScale> enumMap = new SparseArray<ImpactScale>();
  
  static {
    for (ImpactScale scale : ImpactScale.values()) {
      enumMap.put(scale.getCode(), scale);
    }
  }
  
  ImpactScale(Integer code) {
    this.code = code;
  }
  
  public int getCode() {
    return code;
  }
  
  public static ImpactScale fromCode(Integer code) {
    return enumMap.get(code);
  }
}