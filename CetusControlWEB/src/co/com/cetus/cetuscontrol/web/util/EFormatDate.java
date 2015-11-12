package co.com.cetus.cetuscontrol.web.util;

/**
 * The Enum EFormatDate.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (12/03/2015)
 */
public enum EFormatDate {
  
  FORMAT_12H ( "12H", ConstantWEB.PATTERN_DATE_12H, ConstantWEB.PATTERN_HOUR_12H ),
  FORMAT_24H ( "24H", ConstantWEB.PATTERN_DATE_24H, ConstantWEB.PATTERN_HOUR_24H );
  
  private String format;
  private String patternDate;
  private String patternHour;
  
  EFormatDate ( String format, String patternDate, String patternHour ) {
    this.format = format;
    this.patternDate = patternDate;
    this.patternHour = patternHour;
  }
  
  public String getFormat () {
    return format;
  }
  
  public void setFormat ( String format ) {
    this.format = format;
  }
  
  public String getPatternDate () {
    return patternDate;
  }
  
  public void setPatternDate ( String patternDate ) {
    this.patternDate = patternDate;
  }
  
  public String getPatternHour () {
    return patternHour;
  }
  
  public void setPatternHour ( String patternHour ) {
    this.patternHour = patternHour;
  }
  
  public static String getPatternDate ( String format ) {
    String pattern = null;
    try {
      for ( EFormatDate eFormatDate: EFormatDate.values() ) {
        if ( eFormatDate.getFormat().equals( format ) ) {
          pattern = eFormatDate.getPatternDate();
          break;
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return pattern;
  }
  
  public static String getPatternHour ( String format ) {
    String pattern = null;
    try {
      for ( EFormatDate eFormatDate: EFormatDate.values() ) {
        if ( eFormatDate.getFormat().equals( format ) ) {
          pattern = eFormatDate.getPatternHour();
          break;
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return pattern;
  }
  
}
