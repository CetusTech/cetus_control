package co.com.cetus.cetuscontrol.ejb.util;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import co.com.cetus.common.dto.ParameterDTO;

/**
 * The Class ConstantEJB.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
public class ConstantEJB {
  
  public static Logger                    CETUS_CONTROL_EJB_LOG              = Logger.getLogger( "CetusControlEJB" );
  
  public static PropertiesLoader          properties                         = PropertiesLoader.getInstance();
  
  public static String                    WSDL_CETUS_CUSTOMER_SERVICE        = properties.getProperty( "WSDL_CETUS_CUSTOMER_SERVICE" );
  public static String                    USER_WS_CETUS                      = properties.getProperty( "USER_WS_CETUS" );
  public static String                    PASSWORD_WS_CETUS                  = properties.getProperty( "PASSWORD_WS_CETUS" );
  public static int                       ID_APPLICATION_CETUS_CONTROL       = Integer.parseInt( properties.getProperty( "ID_APPLICATION_CETUS_CONTROL",
                                                                                                                         "0" ) );
  public static int                       ID_APPLICATION_CETUS_VORTAL        = Integer.parseInt( properties.getProperty( "ID_APPLICATION_CETUS_VORTAL",
                                                                                                                         "0" ) );
  
  public final static String              NAME_BUNDLE_NEGOCIO                = "CetusControlEJB";
  public static String                    PCK_CLASS_DTO                      = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "PCK_CLASS_DTO" );
  public static String                    PCK_CLASS_JPA                      = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "PCK_CLASS_JPA" );
  public static String                    ACRONYM_DTO                        = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "ACRONYM_DTO" );
  public static String                    CETUS_CONTROL_EJB_COMPONENT        = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO,
                                                                                                    "CETUS_CONTROL_EJB_COMPONENT" );
  public static String                    CETUS_VORTAL_EJB_COMPONENT         = properties.getProperty( "CETUS_VORTAL_EJB_COMPONENT" );
  public static String                    NO_PERMISSION_SERVICE              = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "NO_PERMISSION_SERVICE" );
  public static String                    NO_EXISTS_COMPONENT                = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "NO_EXISTS_COMPONENT" );
  public static String                    TIME_ZONE                          = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "TIME_ZONE" );
  public static boolean                   ENABLED_TIMER_CONTROLLER           = Boolean.parseBoolean( UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO,
                                                                                                                          "ENABLED_TIMER_CONTROLLER" ) );
  public static String                    MINUTE_FOR_EXECUTE_TIMER_BEFORE    = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO,
                                                                                                    "MINUTE_FOR_EXECUTE_TIMER_BEFORE" );
  
  public static String                    TASK_STATUS_FINAL                  = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "TASK_STATUS_FINAL" );
  public static String                    DES_NEXT_OVERCOME                  = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "DES_NEXT_OVERCOME" );
  public static String                    DES_FINISH                         = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "DES_FINISH" );
  public static String                    DES_RUN                            = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "DES_RUN" );
  public static String                    DES_EXPIRED                        = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "DES_EXPIRED" );
  public static String                    RESULT_SQL_OK                      = "OK";
  public static String                    RESULT_SQL_ERROR                   = "ERROR";
  
  public static String                    TIME_EXECUTE_TIMER_CONTROLLER_0    = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO,
                                                                                                    "TIME_EXECUTE_TIMER_CONTROLLER_0" );
  
  public static String                    NAME_TIMER_BEFORE_EXPIRATION_TASKS = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO,
                                                                                                    "NAME_TIMER_BEFORE_EXPIRATION_TASKS" );
  public static String                    EVENT_BEFORE_EXPIRATION            = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "EVENT_BEFORE_EXPIRATION" );
  
  public static String                    EVENT_EXPIRATION                   = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO, "EVENT_EXPIRATION" );
  
  public static String                    NAME_TIMER_EXPIRATION_TASKS        = UtilEJB.getProperty( NAME_BUNDLE_NEGOCIO,
                                                                                                    "NAME_TIMER_EXPIRATION_TASKS" );
  
  public static String                    TEMPLATE_EMAIL_BEFORE_EXPIRATION   = "TEMPLATE_EMAIL_BEFORE_EXPIRATION";
  public static String                    SMTP_HOST                          = "SMTP_HOST";
  public static String                    SMPT_PORT                          = "SMPT_PORT";
  public static String                    SMTP_FROM                          = "SMTP_FROM";
  public static String                    SMTP_PASS                          = "SMTP_PASS";
  public static String                    SMTP_USERNAME                      = "SMTP_USERNAME";
  public static String                    SUBJECT_BEFORE_EXPIRATION          = "SUBJECT_BEFORE_EXPIRATION";
  public static String                    WSDL_CETUS_MESSAGE_SERVICE         = "WSDL_CETUS_MESSAGE_SERVICE";
  public static String                    USER_WS_MESSAGE_SERVICE            = "USER_WS_MESSAGE_SERVICE";
  public static String                    PASSWORD_WS_MESSAGE_SERVICE        = "PASSWORD_WS_MESSAGE_SERVICE";
  
  public static String                    TIME_HOUR_EXECUTE_TIMER_CONTROLLER = "TIME_HOUR_EXECUTE_TIMER_CONTROLLER";
  public static String                    TASK_STATUS_IN_PROGRESS            = "TASK_STATUS_IN_PROGRESS";
  public static String                    THREAD_BEFORE_EXPIRATION_TASK      = "THREAD_BEFORE_EXPIRATION_TASK";
  public static String                    CONTEXT_TIMER_PROCESS              = "java:app/CetusControlEJB/TimerProcess!co.com.cetus.cetuscontrol.ejb.process.TimerProcess";
  public static String                    CONTEXT_CETUS_CONTROL_PROCESS      = "java:app/CetusControlEJB/CetusControlProcess!co.com.cetus.cetuscontrol.ejb.process.CetusControlProcess";
  public static String                    TEMPLATE_EMAIL_EXPIRATION          = "TEMPLATE_EMAIL_EXPIRATION";
  public static String                    SUBJECT_EXPIRATION_TASK            = "SUBJECT_EXPIRATION_TASK";
  public static String                    EVENT_EXPIRATION_TASK              = "EVENT_EXPIRATION_TASK";
  
  public static HashMap< String, String > parameter                          = null;
  
  /**
   * </p> Load parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param list the list
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (18/02/2015)
   */
  public static boolean loadParameter ( List< ParameterDTO > list ) {
    boolean result = false;
    try {
      if ( list != null ) {
        parameter = new HashMap< String, String >();
        if ( list != null && list.size() > 0 ) {
          for ( ParameterDTO parametro: list ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( parametro.toString() );
            parameter.put( parametro.getName(), parametro.getValue() );
          }
        }
        result = true;
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      result = false;
    }
    return result;
  }
  
  /**
   * </p> Gets the parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameParam the name param
   * @return el string
   * @since CetusControlEJB (18/02/2015)
   */
  public static String getParameter ( String nameParam ) {
    String valueParameter = null;
    try {
      if ( parameter != null ) {
        valueParameter = parameter.get( nameParam );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return valueParameter;
  }
  
}
