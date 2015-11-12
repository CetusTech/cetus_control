package co.com.cetus.cetuscontrol.web.util;

import java.math.BigDecimal;

/**
 * The Enum EstatusUser.
 *
 * @author Andres Herrera - Cetus Technology
 * @version CetusControlEJB (19/02/2015)
 */
public enum StatusUser {
  
  /** The active. */
  ACTIVE ( "ACTIVO", BigDecimal.ONE.intValue() ),
  
  /** The inactive. */
  INACTIVE ( "INACTIVO", BigDecimal.ZERO.intValue() );
  
  /** The value str. */
  private String     valueStr;
  
  /** The value num. */
  private int valueNum;
  
  /**
   * </p> Instancia un nuevo estatus user. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param valueStr the value str
   * @param valueNum the value num
   * @since CetusControlEJB (19/02/2015)
   */
  StatusUser ( String valueStr, int valueNum ) {
    this.valueStr = valueStr;
    this.valueNum = valueNum;
  }
  
  /**
   * </p> Gets the value str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param value the value
   * @return el string
   * @since CetusControlEJB (19/02/2015)
   */
  public static String getValueStr ( int value ) {
    String valueStr = null;
    try {
      for ( StatusUser status: StatusUser.values() ) {
        if ( status.getValueNum() == value ) {
          valueStr = status.getValueStr();
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return valueStr;
  }
  
  /**
   * </p> Gets the value num. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param value the value
   * @return el integer
   * @since CetusControlEJB (19/02/2015)
   */
  public static int getValueNum ( String value ) {
    int valueStr=0;
    try {
      for ( StatusUser status: StatusUser.values() ) {
        if ( status.getValueStr() == value ) {
          valueStr = status.getValueNum();
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return valueStr;
  }
  
  /**
   * </p> Gets the value str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlEJB (19/02/2015)
   */
  public String getValueStr () {
    return valueStr;
  }
  
  /**
   * </p> Sets the value str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param valueStr the value str
   * @since CetusControlEJB (19/02/2015)
   */
  public void setValueStr ( String valueStr ) {
    this.valueStr = valueStr;
  }
  
  /**
   * </p> Gets the value num. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el integer
   * @since CetusControlEJB (19/02/2015)
   */
  public int getValueNum () {
    return valueNum;
  }
  
  /**
   * </p> Sets the value num. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param valueNum the value num
   * @since CetusControlEJB (19/02/2015)
   */
  public void setValueNum ( int valueNum ) {
    this.valueNum = valueNum;
  }
  
}
