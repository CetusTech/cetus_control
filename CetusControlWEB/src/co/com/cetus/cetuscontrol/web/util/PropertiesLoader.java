package co.com.cetus.cetuscontrol.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The Class PropertiesLoader.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (17/12/2014)
 */
public class PropertiesLoader {
  
  /**
   * </p> Instancia un nuevo properties loader. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (17/12/2014)
   */
  public PropertiesLoader () {
    load();
  }
  
  /**
   * </p> Obtiene una instancia de PropertiesLoader. </p>
   *
   * @return una instancia de PropertiesLoader
   * @since CetusControlWEB (17/12/2014)
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
   * @since CetusControlWEB (17/12/2014)
   */
  public Properties load () {
    try {
      
      InputStream in = this.getClass().getClassLoader().getResourceAsStream( "CetusControlFrontend.properties" );
      properties.load( in );
    } catch ( IOException e ) {
      ConstantWEB.WEB_LOG.error( "Error cargando archivo de propiedades " + e.getMessage() );
    }
    return properties;
  }
  
  /**
   * </p> Gets the property. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param prop the prop
   * @return el string
   * @since CetusControlWEB (17/12/2014)
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
   * @since CetusControlWEB (17/12/2014)
   */
  public String getProperty ( String prop, String vDefalult ) {
    ConstantWEB.WEB_LOG.debug( "Obteniendo propiedad prop=" + prop + " vDefalult=" + vDefalult );
    String value = "";
    try {
      value = properties.getProperty( prop, vDefalult );
      if ( value == null ) {
        System.err.println( "ERROR propiedad NULA " + prop );
        ConstantWEB.WEB_LOG.error( "ERROR propiedad NULA " + prop );
        ConstantWEB.WEB_LOG.error( "Asignando variable por defecto a la propiedad " + prop + " vDefalult=" + vDefalult );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( "ERROR propiedad NULA " + prop, e );
      ConstantWEB.WEB_LOG.error( "Asignando variable por defecto a la propiedad " + prop + " vDefalult=" + vDefalult );
    }
    return value;
    
  }
  
  /** The instance. */
  private static PropertiesLoader instance;
  
  /** The properties. */
  private static Properties       properties = new Properties();
}