package co.com.cetus.cetuscontrol.ejb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The Class PropertiesLoader.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
public class PropertiesLoader {
  
  /** The instance. */
  private static PropertiesLoader instance;
  
  /** The properties. */
  private static Properties       properties = new Properties();
  
  /**
   * </p> Instancia un nuevo properties loader. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (18/02/2015)
   */
  public PropertiesLoader () {
    load();
  }
  
  /**
   * </p> Obtiene una instancia de PropertiesLoader. </p>
   *
   * @return una instancia de PropertiesLoader
   * @since CetusControlEJB (18/02/2015)
   * @Author Jose David Salcedo M. - Cetus Technology
   */
  public static PropertiesLoader getInstance () {
    if ( instance == null ) synchronized ( PropertiesLoader.class ) {
      if ( instance == null ) instance = new PropertiesLoader();
    }
    return instance;
  }
  
  /**
   * </p> Load. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el properties
   * @since CetusControlEJB (18/02/2015)
   */
  public Properties load () {
    try {
      
      InputStream in = this.getClass().getClassLoader().getResourceAsStream( "CetusControlBackend.properties" );
      properties.load( in );
    } catch ( IOException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error cargando archivo de propiedades " + e.getMessage() );
    }
    return properties;
  }
  
  /**
   * </p> Gets the property. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param prop the prop
   * @return el string
   * @since CetusControlEJB (18/02/2015)
   */
  public String getProperty ( String prop ) {
    String pro = properties.getProperty( prop );
    if ( pro == null ) {
      System.err.println( "error cargando propiedad " + prop );
    }
    return pro;
  }
  
  /**
   * </p> Gets the property. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param prop the prop
   * @param vDefalult the v defalult
   * @return el string
   * @since CetusControlEJB (18/02/2015)
   */
  public String getProperty ( String prop, String vDefalult ) {
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Obteniendo propiedad prop=" + prop + " vDefalult=" + vDefalult );
    String value = "";
    try {
      value = properties.getProperty( prop, vDefalult );
      if ( value == null ) {
        System.err.println( "ERROR propiedad NULA " + prop );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "ERROR propiedad NULA " + prop );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Asignando variable por defecto a la propiedad " + prop + " vDefalult=" + vDefalult );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "ERROR propiedad NULA " + prop, e );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Asignando variable por defecto a la propiedad " + prop + " vDefalult=" + vDefalult );
    }
    return value;
    
  }
  
}