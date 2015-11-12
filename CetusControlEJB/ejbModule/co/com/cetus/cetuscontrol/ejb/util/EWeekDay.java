package co.com.cetus.cetuscontrol.ejb.util;

/**
 * The Enum EWeekDay.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (26/07/2015)
 */
public enum EWeekDay {
  
  MONDAY ( "LUNES", 2 ),
  TUESDAY ( "MARTES", 3 ),
  WEDNESDAY ( "MIERCOLES", 4 ),
  THRUSDAY ( "JUEVES", 5 ),
  FRYDAY ( "VIERNES", 6 ),
  SATURDAY ( "SABADO", 7 ),
  SUNDAY ( "DOMINGO", 1 );
  
  private String value;
  private int    dayOfWeek;
  
  EWeekDay ( String value, int dayOfWeek ) {
    this.value = value;
    this.dayOfWeek = dayOfWeek;
  }
  
  /**
   * </p> Gets the value. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dayOfWeek the day of week
   * @return el string
   * @since CetusControlEJB (26/07/2015)
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
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return value;
  }
  
  public String getValue () {
    return value;
  }
  
  public void setValue ( String value ) {
    this.value = value;
  }
  
  public int getDayOfWeek () {
    return dayOfWeek;
  }
  
  public void setDayOfWeek ( int dayOfWeek ) {
    this.dayOfWeek = dayOfWeek;
  }
  
}
