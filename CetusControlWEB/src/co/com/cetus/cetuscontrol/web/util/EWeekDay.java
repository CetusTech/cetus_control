package co.com.cetus.cetuscontrol.web.util;

/**
 * The Enum EWeekDay.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (12/03/2015)
 */
public enum EWeekDay {
  
  MONDAY ( "LUNES", ConstantWEB.LABEL_MONDAY, 2 ),
  TUESDAY ( "MARTES", ConstantWEB.LABEL_TUESDAY, 3 ),
  WEDNESDAY ( "MIERCOLES", ConstantWEB.LABEL_WEDNESDAY, 4 ),
  THRUSDAY ( "JUEVES", ConstantWEB.LABEL_THRUSDAY, 5 ),
  FRYDAY ( "VIERNES", ConstantWEB.LABEL_FRYDAY, 6 ),
  SATURDAY ( "SABADO", ConstantWEB.LABEL_SATURDAY, 7 ),
  SUNDAY ( "DOMINGO", ConstantWEB.LABEL_SUNDAY, 1 );
  
  private String value;
  private String label;
  private int    dayOfWeek;
  
  EWeekDay ( String value, String label, int dayOfWeek ) {
    this.value = value;
    this.label = label;
    this.dayOfWeek = dayOfWeek;
  }
  
  /**
   * </p> Gets the value. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dayOfWeek the day of week
   * @return el string
   * @since CetusControlWEB (23/03/2015)
   */
  public static String getValue ( int dayOfWeek ) {
    String value = null;
    try {
      for ( EWeekDay eWeekDay: EWeekDay.values() ) {
        if ( eWeekDay.getDayOfWeek() == dayOfWeek ) {
          value = eWeekDay.getValue();
          break;
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return value;
  }
  
  public String getValue () {
    return value;
  }
  
  public void setValue ( String value ) {
    this.value = value;
  }
  
  public String getLabel () {
    return label;
  }
  
  public void setLabel ( String label ) {
    this.label = label;
  }
  
  public int getDayOfWeek () {
    return dayOfWeek;
  }
  
  public void setDayOfWeek ( int dayOfWeek ) {
    this.dayOfWeek = dayOfWeek;
  }
  
}
