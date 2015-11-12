package co.com.cetus.cetuscontrol.web.util;

import org.apache.log4j.Logger;

/**
 * The Class ConstantWEB.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (10/12/2014)
 */
public class ConstantWEB {
  
  /** The cetus logs. */
  public static Logger           WEB_LOG                            = Logger.getLogger( "CetusControlWEB" );
  
  /** The Constant NAME_BUNDLE_WEB. */
  public final static String     NAME_BUNDLE_WEB                    = "message_es";
  
  /** The Constant PATTERN_DATE. */
  public final static String     PATTERN_DATE                       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_DATE" );
  public final static String     AUDIT_DELETE                       = "DELETE";
  
  public static PropertiesLoader properties                         = PropertiesLoader.getInstance();
  
  /** The Constant WSDL_AGARTHI_CORE_SERVICE. */
  public final static String     JNDI_PROPERTIE                     = properties.getProperty( "JNDI_PROPERTIE" );
  public final static String     CONTEXT_PROPERTIE                  = properties.getProperty( "CONTEXT_PROPERTIE" );
  public final static String     USER_PROPERTIE                     = properties.getProperty( "USER_PROPERTIE" );
  public final static String     PASSWORD_PROPERTIE                 = properties.getProperty( "PASSWORD_PROPERTIE" );
  public final static String     DESC_APP                           = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DESC_APP" );
  public final static String     DESC_USER_DTO                      = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DESC_USER_DTO" );
  public final static String     DESC_ACRONYM                       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DESC_ACRONYM" );
  public final static String     DESC_IP_REQUEST                    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DESC_IP_REQUEST" );
  public final static String     DESC_HOST_REQUEST                  = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DESC_HOST_REQUEST" );
  public final static String     URL_PAGE_USER_NOVALID              = UtilWEB.getProperty( NAME_BUNDLE_WEB, "URL_PAGE_USER_NOVALID" );
  public final static String     URL_PAGE_ERROR                     = UtilWEB.getProperty( NAME_BUNDLE_WEB, "URL_PAGE_ERROR" );
  public final static String     REAL_PATH                          = UtilWEB.getProperty( NAME_BUNDLE_WEB, "REAL_PATH" );
  public final static String     SEPARATOR_UNDERSCORE               = UtilWEB.getProperty( NAME_BUNDLE_WEB, "SEPARATOR_UNDERSCORE" );
  public final static String     FIELD_ORDER_NAME                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "FIELD_ORDER_NAME" );
  public final static String     MESSAGE_SUCCES_DELETE              = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_SUCCES_DELETE" );
  public final static String     MESSAGE_ERROR                      = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR" );
  public final static String     MESSAGE_ERROR_DELETE_MASTER        = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_DELETE_MASTER" );
  public final static String     MESSAGE_SUCCES                     = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_SUCCES" );
  public final static String     MESSAGE_ERROR_CREATE               = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_CREATE" );
  public final static String     MESSAGE_SUCCES_UPDATE              = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_SUCCES_UPDATE" );
  public final static String     MESSAGE_ERROR_UPDATE               = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_UPDATE" );
  public final static String     MSG_DETAIL_SUCCESS                 = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MSG_DETAIL_SUCCESS" );
  public final static String     MSG_DETAIL_ERROR                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MSG_DETAIL_ERROR" );
  public final static String     MSG_DETAIL_ERROR_LIMIT_TASK        = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MSG_DETAIL_ERROR_LIMIT_TASK" );
  public final static String     MESSAGE_ERROR_DELETE_DETAIL        = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_DELETE_DETAIL" );
  public final static String     NAME_CLIENT                        = "name";
  public final static String     NAME_PERSON                        = "names";
  public final static String     MESSAGE_WARNING                    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_WARNING" );
  public final static String     MSG_WARNING_CLIENT_NOT_EMPTY       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MSG_WARNING_CLIENT_NOT_EMPTY" );
  public final static String     FIELD_ORDER_DESCRIPTIION           = UtilWEB.getProperty( NAME_BUNDLE_WEB, "FIELD_ORDER_DESCRIPTIION" );
  public final static String     VALIDATION_ONLY_NUMBER             = UtilWEB.getProperty( NAME_BUNDLE_WEB, "VALIDATION_ONLY_NUMBER" );
  public final static String     DESC_PARAMETER_GENERAL_DTO         = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DESC_PARAMETER_GENERAL_DTO" );
  public final static String     PATTERN_DATE_12H                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_DATE_12H" );
  public final static String     PATTERN_DATE_24H                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_DATE_24H" );
  public final static String     PATTERN_HOUR_12H                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_HOUR_12H" );
  public final static String     PATTERN_HOUR_24H                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_HOUR_24H" );
  public final static String     LABEL_MONDAY                       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_MONDAY" );
  public final static String     LABEL_TUESDAY                      = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_TUESDAY" );
  public final static String     LABEL_WEDNESDAY                    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_WEDNESDAY" );
  public final static String     LABEL_THRUSDAY                     = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_THRUSDAY" );
  public final static String     LABEL_FRYDAY                       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_FRYDAY" );
  public final static String     LABEL_SATURDAY                     = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_SATURDAY" );
  public final static String     LABEL_SUNDAY                       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LABEL_SUNDAY" );
  public final static String[]   LIST_JORNADAS                      = UtilWEB.getProperty( NAME_BUNDLE_WEB, "LIST_JORNADAS" ).split( "," );
  public final static String     ERROR_MIN_JORNADA                  = UtilWEB.getProperty( NAME_BUNDLE_WEB, "ERROR_MIN_JORNADA" );
  public final static String     ERROR_TIME_START_HIGHER_END        = UtilWEB.getProperty( NAME_BUNDLE_WEB, "ERROR_TIME_START_HIGHER_END" );
  public final static String     ERROR_TIME_SEQUENCE                = UtilWEB.getProperty( NAME_BUNDLE_WEB, "ERROR_TIME_SEQUENCE" );
  public final static String     DESC_ALL_DAY                       = UtilWEB.getProperty( NAME_BUNDLE_WEB, "all_day" );
  public final static String     ERROR_EXISTS_EXCEPTION_ALL_DAY     = UtilWEB.getProperty( NAME_BUNDLE_WEB, "ERROR_EXISTS_EXCEPTION_ALL_DAY" );
  public final static String     ERROR_EXISTS_EXCEPTION_ALL_JORNADA = UtilWEB.getProperty( NAME_BUNDLE_WEB, "ERROR_EXISTS_EXCEPTION_ALL_JORNADA" );
  public final static String     MESSAGE_ERROR_CHEK_NOTACTIONS      = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_CHEK_NOTACTIONS" );
  public final static String     MESSAGE_ERROR_DELETE_NOTACTIONS    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_DELETE_NOTACTIONS" );
  
  public final static String     STATUS_GROUP_CREATED               = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_created" );
  public final static String     STATUS_GROUP_NEW                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_new" );
  public final static String     STATUS_GROUP_CLOSED                = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_closed" );
  public final static String     STATUS_GROUP_IN_PROGRESS           = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_in_progress" );
  public final static String     STATUS_GROUP_CREATED_ES            = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_created_es" );
  public final static String     STATUS_GROUP_NEW_ES                = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_new_es" );
  public final static String     STATUS_GROUP_CLOSED_ES             = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_closed_es" );
  public final static String     STATUS_GROUP_IN_PROGRESS_ES        = UtilWEB.getProperty( NAME_BUNDLE_WEB, "status_in_progress_es" );
  
  public final static String     MESSAGE_ERROR_DELETE_GROUP_PERSON  = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_DELETE_GROUP_PERSON" );
  public final static String     MESSAGE_ERROR_CREATE_GROUP_PERSON  = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_CREATE_GROUP_PERSON" );
  public final static String     NAME_TEMPLATE_TASK_MASSIVE         = UtilWEB.getProperty( NAME_BUNDLE_WEB, "NAME_TEMPLATE_TASK_MASSIVE" );
  public final static String     MESSAGE_ERROR_CREATE_TEMPLATE_TM   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_ERROR_CREATE_TEMPLATE_TM" );
  
  public final static int        STATUS_ASSIGNED                    = Integer.parseInt( UtilWEB.getProperty( NAME_BUNDLE_WEB, "STATUS_ASSIGNED" ) );
  public final static int        STATUS_INPROGRESS                  = Integer.parseInt( UtilWEB.getProperty( NAME_BUNDLE_WEB, "STATUS_INPROGRESS" ) );
  public final static int        STATUS_COMPLETED                   = Integer.parseInt( UtilWEB.getProperty( NAME_BUNDLE_WEB, "STATUS_COMPLETED" ) );
  public final static int        STATUS_CANCELED                    = Integer.parseInt( UtilWEB.getProperty( NAME_BUNDLE_WEB, "STATUS_CANCELED" ) );
  public final static int        STATUS_SUSPENDED                   = Integer.parseInt( UtilWEB.getProperty( NAME_BUNDLE_WEB, "STATUS_SUSPENDED" ) );
  public final static String     MESSAGE_SUCCESS_UPLOAD_FILE        = UtilWEB.getProperty( NAME_BUNDLE_WEB, "MESSAGE_SUCCESS_UPLOAD_FILE" );
  public final static String     PATTERN_DATE_WRITE_FILE            = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_DATE_WRITE_FILE" );
  public final static String     NAME_FILE_RESULT_PROCESS           = UtilWEB.getProperty( NAME_BUNDLE_WEB, "NAME_FILE_RESULT_PROCESS" );
  public final static String     PATTERN_DATE_TEMPLATE_XLS          = UtilWEB.getProperty( NAME_BUNDLE_WEB, "PATTERN_DATE_TEMPLATE_XLS" );
  public final static String     MSG_STARTED_TASK                   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "msg_started_task" );
  public final static String     MESSAGE_TASK_CREATED               = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_task_created" );
  public final static int        STATUS_ASSIGNED_VAL                = 1;
  public final static int        STATUS_INPROGRESS_VAL              = 2;
  public final static int        STATUS_COMPLETED_VAL               = 3;
  public final static int        STATUS_CANCELED_VAL                = 4;
  public final static int        STATUS_SUSPENDED_VAL               = 5;
  public final static String     SUSPENDED                          = "S";
  public final static String     NO_SUSPENDED                       = "N";
  public final static String     MYTASK_MY_PRIORITY_DIGRAM_TITLE    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "myTask_my_priority_digram_title" );
  public final static String     MYTASK_MY_PRIORITY_DIGRAM_DETAIL   = UtilWEB.getProperty( NAME_BUNDLE_WEB, "myTask_my_priority_digram_detail" );
  public final static String     MYTASK_MY_PRIORITY_V_LABEL         = UtilWEB.getProperty( NAME_BUNDLE_WEB, "myTask_my_priority_V_label" );
  public final static String     MYTASK_MY_PRIORITY_H_LABEL         = UtilWEB.getProperty( NAME_BUNDLE_WEB, "myTask_my_priority_H_label" );
  public final static String     MYTASK_MY_PROGRESS_DIGRAM_TITLE    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "myTask_my_prgress_digram_title" );
  public final static String     MESSAGE_CANCELED_TASK              = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_canceled_task" );
  public final static String     MESSAGE_STARTED_TASK               = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_started_task" );
  public final static String     MESSAGE_SUSPENDED_TASK             = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_suspended_task" );
  public final static String     MESSAGE_COMPLETED_TASK             = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_completed_task" );
  public final static String     MESSAGE_NOT_CANCELED_TASK          = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_not_canceled_task" );
  public final static String     MESSAGE_NOT_STARTED_TASK           = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_not_started_task" );
  public final static String     MESSAGE_NOT_SUSPENDED_TASK         = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_not_suspended_task" );
  public final static String     MESSAGE_NOT_COMPLETED_TASK         = UtilWEB.getProperty( NAME_BUNDLE_WEB, "message_not_completed_task" );
  
  public final static String     MESSAGE_DES_NEXT_OVERCOME          = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DES_NEXT_OVERCOME" );
  public final static String     MESSAGE_DES_FINISH                 = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DES_FINISH" );
  public final static String     MESSAGE_DES_RUN                    = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DES_RUN" );
  public final static String     MESSAGE_DES_EXPIRED                = UtilWEB.getProperty( NAME_BUNDLE_WEB, "DES_EXPIRED" );
  
}
