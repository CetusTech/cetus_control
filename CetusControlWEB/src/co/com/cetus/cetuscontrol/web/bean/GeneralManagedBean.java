package co.com.cetus.cetuscontrol.web.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.com.cetus.cetuscontrol.dto.ParameterGeneralDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.delegate.GeneralDelegate;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.EFormatDate;
import co.com.cetus.cetuscontrol.web.util.UtilWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.DateUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class GeneralManagedBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (23/01/2015)
 */
@SuppressWarnings ( "serial" )
public abstract class GeneralManagedBean implements Serializable {
  
  /** The current date. */
  protected Date            currentDate     = new Date();
                                            
  /** The current date str. */
  protected String          currentDateStr  = formatDatePattern();
                                            
  /** The general delegate. */
  protected GeneralDelegate generalDelegate = GeneralDelegate.getInstance();
                                            
  /** The patter date. */
  private String            patterDate      = null;
                                            
  /** The patter hour. */
  private String            patterHour      = null;
                                            
  /** The locale. */
  private String            locale          = null;
                                            
  /**
   * </p> Gets the context path. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  public String getContextPath () {
    ServletContext sc = null;
    try {
      sc = ( ServletContext ) FacesContext.getCurrentInstance().getExternalContext().getContext();
      return sc.getContextPath();
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Gets the response. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el http servlet response
   * @since CetusControlWEB (23/01/2015)
   */
  public HttpServletResponse getResponse () {
    ExternalContext ex = null;
    HttpServletResponse response = null;
    try {
      ex = FacesContext.getCurrentInstance().getExternalContext();
      response = ( HttpServletResponse ) ex.getResponse();
      return response;
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Gets the request. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el http servlet request
   * @since CetusControlWEB (23/01/2015)
   */
  public HttpServletRequest getRequest () {
    ExternalContext ex = null;
    HttpServletRequest request = null;
    try {
      ex = FacesContext.getCurrentInstance().getExternalContext();
      request = ( HttpServletRequest ) ex.getRequest();
      return request;
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Adds the message. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param compId the comp id
   * @param msg the msg
   * @param detail the detail
   * @param severity the severity
   * @since CetusControlWEB (23/01/2015)
   */
  public static void addMessage ( String compId, String msg, String detail, FacesMessage.Severity severity ) {
    FacesContext ctx = null;
    FacesMessage fmsg = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      fmsg = new FacesMessage( severity, msg, detail );
      ctx.addMessage( compId, fmsg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Adds the message info. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param compId the comp id
   * @param msg the msg
   * @param detail the detail
   * @since CetusControlWEB (23/01/2015)
   */
  public static void addMessageInfo ( String compId, String msg, String detail ) {
    FacesContext ctx = null;
    FacesMessage fmsg = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      fmsg = new FacesMessage( FacesMessage.SEVERITY_INFO, msg, detail );
      ctx.addMessage( compId, fmsg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Adds the message error. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param compId the comp id
   * @param msg the msg
   * @param detail the detail
   * @since CetusControlWEB (23/01/2015)
   */
  public static void addMessageError ( String compId, String msg, String detail ) {
    FacesContext ctx = null;
    FacesMessage fmsg = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      fmsg = new FacesMessage( FacesMessage.SEVERITY_ERROR, msg, detail );
      ctx.addMessage( compId, fmsg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Adds the message fatal. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param compId the comp id
   * @param msg the msg
   * @param detail the detail
   * @since CetusControlWEB (23/01/2015)
   */
  public static void addMessageFatal ( String compId, String msg, String detail ) {
    FacesContext ctx = null;
    FacesMessage fmsg = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      fmsg = new FacesMessage( FacesMessage.SEVERITY_FATAL, msg, detail );
      ctx.addMessage( compId, fmsg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Adds the message warning. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param compId the comp id
   * @param msg the msg
   * @param detail the detail
   * @since CetusControlWEB (23/01/2015)
   */
  public static void addMessageWarning ( String compId, String msg, String detail ) {
    FacesContext ctx = null;
    FacesMessage fmsg = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      fmsg = new FacesMessage( FacesMessage.SEVERITY_WARN, msg, detail );
      ctx.addMessage( compId, fmsg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Limpiar mensajes. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (25/04/2015)
   */
  public String limpiarMensajes () {
    return null;
  }
  
  /**
   * </p> Gets the real path. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  public String getRealPath () {
    String path = "";
    ServletContext sc = null;
    try {
      sc = ( ServletContext ) FacesContext.getCurrentInstance().getExternalContext().getContext();
      path = sc.getRealPath( ConstantWEB.REAL_PATH );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return path;
  }
  
  /**
   * </p> Gets the list message. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el list
   * @since CetusControlWEB (23/01/2015)
   */
  public static List< FacesMessage > getListMessage () {
    FacesContext ctx = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      return ctx.getMessageList();
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Inits the element. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (23/01/2015)
   */
  @PostConstruct
  public abstract void initElement ();
  
  /**
   * </p> Removes the. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  public abstract String remove ();
  
  /**
   * </p> Update. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  public abstract String update ();
  
  /**
   * </p> Adds the. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  public abstract String add ();
  
  /**
   * </p> Gets the user creation. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  public String getUserInSession () {
    String login = null;
    String ip = null;
    String acronimo = null;
    //String hostName = null;
    String usuarioCreacion = null;
    UserPortalDTO userDTO = null;
    try {
      userDTO = ( UserPortalDTO ) getObjectSession( ConstantWEB.DESC_USER_DTO );
      if ( userDTO != null ) {
        ip = ( String ) getObjectSession( ConstantWEB.DESC_IP_REQUEST );
        login = userDTO.getLoginCetus();
        //hostName = ( String ) getObjectSession( ConstantWEB.hostRequest );
        acronimo = ( String ) getObjectSession( ConstantWEB.DESC_ACRONYM );
        usuarioCreacion = acronimo + ConstantWEB.SEPARATOR_UNDERSCORE + login + ConstantWEB.SEPARATOR_UNDERSCORE
                          + ip /*+ ConstantWEB.SEPARATOR_UNDERSCORE + hostName*/;
      } else {
        usuarioCreacion = null;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return usuarioCreacion;
  }
  
  /**
   * </p> Adds the object session. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param obj the obj
   * @param key the key
   * @since CetusControlWEB (23/01/2015)
   */
  public void addObjectSession ( Object obj, String key ) {
    try {
      if ( FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null ) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put( key, obj );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Gets the object session. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pKey the p key
   * @return el object
   * @since CetusControlWEB (23/01/2015)
   */
  public static Object getObjectSession ( String pKey ) {
    try {
      if ( FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null ) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get( pKey );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Clean object session. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pKey the p key
   * @since CetusControlWEB (23/01/2015)
   */
  public void cleanObjectSession ( String pKey ) {
    try {
      if ( FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null ) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove( pKey );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Format date pattern. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (23/01/2015)
   */
  private static String formatDatePattern () {
    try {
      return DateUtility.formatDatePattern( new Date(), ConstantWEB.PATTERN_DATE );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Gets the current date str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (25/04/2015)
   */
  public String getCurrentDateStr () {
    return currentDateStr;
  }
  
  /**
   * </p> Gets the user dto. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el user portal dto
   * @since CetusControlWEB (25/04/2015)
   */
  public static UserPortalDTO getUserDTO () {
    return ( UserPortalDTO ) getObjectSession( ConstantWEB.DESC_USER_DTO );
  }
  
  /**
   * </p> Gets the param general client cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el parameter general dto
   * @since CetusControlWEB (12/03/2015)
   */
  protected ParameterGeneralDTO getParamGeneralClientCetus () {
    return ( ParameterGeneralDTO ) getObjectSession( ConstantWEB.DESC_PARAMETER_GENERAL_DTO );
  }
  
  /**
   * </p> Gets the patter date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (25/04/2015)
   */
  public String getPatterDate () {
    ParameterGeneralDTO parameterGeneralDTO = null;
    parameterGeneralDTO = ( ParameterGeneralDTO ) getObjectSession( ConstantWEB.DESC_PARAMETER_GENERAL_DTO );
    if ( parameterGeneralDTO != null && parameterGeneralDTO.getHourFormat() != null ) {
      patterDate = EFormatDate.getPatternDate( parameterGeneralDTO.getHourFormat() );
    } else {
      patterDate = EFormatDate.getPatternDate( EFormatDate.FORMAT_24H.getFormat() );
    }
    return patterDate;
  }
  
  /**
   * </p> Gets the locale. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (25/04/2015)
   */
  public String getLocale () {
    //se debe inplementar la logica para obtener el Idiomas
    locale = "es";
    return locale;
  }
  
  /**
   * </p> Gets the patter hour. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (25/04/2015)
   */
  public String getPatterHour () {
    ParameterGeneralDTO parameterGeneralDTO = null;
    parameterGeneralDTO = ( ParameterGeneralDTO ) getObjectSession( ConstantWEB.DESC_PARAMETER_GENERAL_DTO );
    if ( parameterGeneralDTO != null ) {
      patterHour = EFormatDate.getPatternHour( parameterGeneralDTO.getHourFormat() );
    } else {
      patterHour = EFormatDate.getPatternHour( EFormatDate.FORMAT_24H.getFormat() );
    }
    return patterHour;
  }
  
  public int getParameterGeneralNumberTaskInProgress () {
    ParameterGeneralDTO parameterGeneralDTO = null;
    parameterGeneralDTO = ( ParameterGeneralDTO ) getObjectSession( ConstantWEB.DESC_PARAMETER_GENERAL_DTO );
    if ( parameterGeneralDTO != null ) {
      return parameterGeneralDTO.getColNumber1();
    }
    return 0;
  }
  
  /**
   * </p> Convert hour to date. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param hour the hour
   * @return el date
   * @since CetusControlWEB (13/03/2015)
   */
  public String formatHourForView ( int hour ) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat formatter = null;
    String formattedHour = null;
    try {
      cal.set( 0, 0, 0, ( hour / 100 ), ( hour % 100 ) );
      formatter = new SimpleDateFormat( getPatterHour() );
      formattedHour = formatter.format( cal.getTime() );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return formattedHour;
  }
  
  public void getMessageError ( ResponseDTO response ) {
    String code = "0";
    try {
      if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
        code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
        if ( code != null && !code.isEmpty() ) {
          code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
        }
      } else {
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void getMessageError ( ResponseDTO response, String... param ) {
    String code = "0";
    try {
      if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
        code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
        if ( code != null && !code.isEmpty() ) {
          code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, MessageFormat.format( code, ( Object[] ) param ) );
        }
      } else {
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public static String getMessageProperty ( String pLabel, String pParam ) {
    return MessageFormat.format( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, pLabel ), pParam );
  }
  
  public Timestamp getCurrentDateTime () {
    java.util.Date date = new java.util.Date();
    return new Timestamp( date.getTime() );
  }
  
  public static void main ( String... arg ) {
    java.util.Date date = new java.util.Date();
    System.out.println( date );
  }
  
  public String getIdSessionCurrent () {
    String sessionId = null;
    try {
      FacesContext fCtx = FacesContext.getCurrentInstance();
      HttpSession session = ( HttpSession ) fCtx.getExternalContext().getSession( false );
      sessionId = session.getId();
    } catch ( Exception e ) {
      throw e;
    }
    return sessionId;
  }
}
