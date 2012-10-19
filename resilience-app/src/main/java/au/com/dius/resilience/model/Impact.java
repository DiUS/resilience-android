package au.com.dius.resilience.model;


public enum Impact {
  LOW(0, 25),
  MEDIUM(25, 75),
  HIGH(75, 100);
  
  private Integer lowerImpactBound;
  private Integer upperImpactBound;
  
  Impact(Integer lowerImpactBound, Integer upperImpactBound) {
    this.lowerImpactBound = lowerImpactBound;
    this.upperImpactBound = upperImpactBound;
  }
  
  public static Impact fromImpactScale(Integer scale) {
    Impact impact = LOW;
    if (scale >= MEDIUM.lowerImpactBound && scale <= MEDIUM.upperImpactBound) {
      impact = MEDIUM;
    }
    else if (scale > HIGH.lowerImpactBound && scale <= HIGH.upperImpactBound) {
      impact = HIGH;
    }
    
    return impact;
  }
}