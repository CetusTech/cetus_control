package co.com.cetus.cetuscontrol.web.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * The Class UtilWEB.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (10/12/2014)
 */
public class UtilWEB {
  
  /**
   * </p> Gets the property. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pFileNameProperties the p file name properties
   * @param pKey the p key
   * @return el string
   * @since CetusControlWEB (10/12/2014)
   */
  public static String getProperty ( String pFileNameProperties, String pKey ) {
    ResourceBundle rb = null;
    String valueProperty = null;
    try {
      rb = ResourceBundle.getBundle( pFileNameProperties );
      if ( rb != null ) {
        valueProperty = rb.getString( pKey );
      }
    } catch ( Exception e ) {
      try {
        throw new Exception( "Error obteniendo la propiedad " + pKey + " del properties " + pFileNameProperties );
      } catch ( Exception e1 ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
    return valueProperty;
  }
  
  //  public static boolean validateResponseSuccess ( ResponseDTO pResponse ) {
  //    if ( pResponse != null && pResponse.getCode() != null && !pResponse.getCode().isEmpty() && pResponse.getCode() != null && !pResponse.getCode().isEmpty()
  //         && pResponse.getCode().equals( ConstantCommon.WSResponse.CODE_ONE )
  //         && pResponse.getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) ) {
  //      return true;
  //    }
  //    return false;
  //  }
  //  
  //  public static boolean validateResponseNoResult ( ResponseDTO pResponse ) {
  //    if ( pResponse != null && pResponse.getCode() != null && !pResponse.getCode().isEmpty() && pResponse.getCode() != null && !pResponse.getCode().isEmpty()
  //         && pResponse.getCode().equals( ConstantCommon.WSResponse.CODE_ZERO )
  //         && pResponse.getType().equals( ConstantCommon.WSResponse.TYPE_NORESULT ) ) {
  //      return true;
  //    }
  //    return false;
  //  }
  //  
  //  public static boolean validateResponseSuccessXMLOutput ( ResponseDTO pResponse ) {
  //    if ( pResponse != null && pResponse.getCode() != null && !pResponse.getCode().isEmpty() && pResponse.getCode() != null && !pResponse.getCode().isEmpty()
  //         && pResponse.getCode().equals( ConstantCommon.WSResponse.CODE_ONE )
  //         && pResponse.getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) && pResponse.getDataResponseXML() != null && !pResponse.getDataResponseXML().isEmpty() ) {
  //      return true;
  //    }
  //    return false;
  //  }
  
  /**
   * </p> Format date pattern. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pFecha the p fecha
   * @param pPattern the p pattern
   * @return el date
   * @since CetusControlWEB (10/12/2014)
   */
  public static Date formatDatePattern ( Date pFecha, String pPattern ) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat( pPattern );
      String formattedDate = formatter.format( pFecha );
      return ( Date ) formatter.parse( formattedDate );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Conver date to xml gregorian calendar. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pFecha the p fecha
   * @return el XML gregorian calendar
   * @throws DatatypeConfigurationException the datatype configuration exception
   * @since CetusControlWEB (10/12/2014)
   */
  public static XMLGregorianCalendar converDateToXMLGregorianCalendar ( Date pFecha ) throws DatatypeConfigurationException {
    GregorianCalendar c = null;
    XMLGregorianCalendar xmlGregorianCalendar = null;
    
    try {
      c = new GregorianCalendar();
      c.setTime( pFecha );
      xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar( c );
    } catch ( DatatypeConfigurationException e ) {
      throw e;
    }
    return xmlGregorianCalendar;
  }
  
  /**
   * </p> Gets the date sql. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pFecha the p fecha
   * @return el timestamp
   * @since CetusControlWEB (10/12/2014)
   */
  public static Timestamp getDateSql ( Date pFecha ) {
    Date fechaActual = null;
    Timestamp momentoTimestamp = null;
    try {
      if ( pFecha != null ) {
        momentoTimestamp = new Timestamp( pFecha.getTime() );
      } else {
        fechaActual = new Date();
        momentoTimestamp = new Timestamp( fechaActual.getTime() );
      }
      return momentoTimestamp;
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
}
