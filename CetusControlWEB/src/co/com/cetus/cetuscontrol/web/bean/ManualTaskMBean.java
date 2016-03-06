package co.com.cetus.cetuscontrol.web.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.AttachDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.dto.PersonGroupDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.TraceTaskDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.EWeekDay;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.mail.SendMail;
import co.com.cetus.common.util.UtilCommon;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualTaskMBean.
 *
 * @author Andres Herrera - Cetus Technology
 * @version CetusControlWEB (2/02/2016)
 */
@ManagedBean
@RequestScoped
public class ManualTaskMBean extends GeneralManagedBean {
  
  /** The Constant serialVersionUID. */
  private static final long      serialVersionUID        = -3255236201554716426L;
                                                         
  /** The list files task. */
  ArrayList< UploadedFile >      listFilesTask           = null;
                                                         
  /** The user portal dto. */
  private UserPortalDTO          userPortalDTO           = null;
                                                         
  /** The list register. */
  private List< TaskDTO >        listRegister            = null;
  private List< AttachDTO >      listAttachFiles         = null;
                                                         
  /** The list register group. */
  private List< PersonGroupDTO > listRegisterGroup       = null;
                                                         
  /** The list area. */
  private List< AreaDTO >        listArea                = null;
                                                         
  /** The list priority. */
  private List< PriorityDTO >    listPriority            = null;
                                                         
  /** The list task type. */
  private List< TaskTypeDTO >    listTaskType            = null;
                                                         
  /** The list person. */
  private List< PersonDTO >      listPerson              = null;
                                                         
  /** The list person g. */
  private List< PersonGroupDTO > listPersonG             = null;
                                                         
  /** The list status. */
  private List< StatusDTO >      listStatus              = null;
                                                         
  /** The list area item. */
  private List< SelectItem >     listAreaItem            = null;
                                                         
  /** The list priority item. */
  private List< SelectItem >     listPriorityItem        = null;
                                                         
  /** The list task type item. */
  private List< SelectItem >     listTaskTypeItem        = null;
                                                         
  /** The list status item. */
  private List< SelectItem >     listStatusItem          = null;
                                                         
  /** The add object. */
  private TaskDTO                addObject               = null;
                                                         
  /** The selected object. */
  private TaskDTO                selectedObject          = null;
                                                         
  /** The selected object person. */
  private PersonGroupDTO         selectedObjectPerson    = null;
                                                         
  /** The show confirm add. */
  private boolean                showConfirmAdd          = false;
                                                         
  /** The show confirm mod. */
  private boolean                showConfirmMod          = false;
                                                         
  /** The show dialog confirm update. */
  private boolean                showDialogConfirmUpdate = false;
                                                         
  /** The show confirm delete. */
  private boolean                showConfirmDelete       = false;
                                                         
  /** The show alert select row. */
  private boolean                showAlertSelectRow      = false;
                                                         
  /** The show view detail. */
  private boolean                showViewDetail          = false;
                                                         
  /** The show add dialog. */
  private boolean                showAddDialog           = false;
                                                         
  private boolean                showConfirmCancel       = false;
                                                         
  private boolean                showConfirmCompleted    = false;
  private boolean                showConfirmSuspended    = false;
                                                         
  /** The show menu. */
  private boolean                showMenu                = false;
                                                         
  /** The approved. */
  private boolean                approved                = false;
                                                         
  /** The group tdto selected. */
  private PersonGroupDTO         groupTDTOSelected       = null;
                                                         
  /** The visible buttons. */
  private boolean                visibleButtons          = false;
                                                         
  /** The code. */
  private String                 code                    = null;
                                                         
  /** The send mail. */
  private SendMail               sendMail                = null;
                                                         
  /** The note task. */
  private String                 noteTask                = null;
                                                         
  /** The status. */
  private int                    status;
                                 
  /** The percentage. */
  private double                 percentage;
                                 
  /** The percentage selected. */
  private double                 percentageSelected;
                                 
  /** The percentage current. */
  private double                 percentageCurrent;
                                 
  /** The delivery date. */
  private Date                   deliveryDate            = null;
                                                         
  /** The id usuario. */
  private String                 idUsuario;
                                 
  /** The destination. */
  private String                 destination             = ConstantWEB.PATH_FILE_TASK;
                                                         
  /** The separador. */
  private String                 separador               = System.getProperty( "file.separator" );
                                                         
  private AttachDTO              attachDTOSelected       = null;
                                                         
  private int                    indexTab                = 0;
                                                         
  private List< String >         listStatusFilter;
                                 
  private StreamedContent        fileTemplate            = null;
                                                         
  /**
   * </p> Instancia un nuevo manual task m bean. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  public ManualTaskMBean () {
    super();
    addObject = new TaskDTO();
    addObject.setArea( new AreaDTO() );
    addObject.setPriority( new PriorityDTO() );
    addObject.setStatus( new StatusDTO() );
    addObject.setTaskType( new TaskTypeDTO() );
    addObject.setAssingDate( new Date() );
    selectedObject = new TaskDTO();
    selectedObject.setArea( new AreaDTO() );
    selectedObject.setPriority( new PriorityDTO() );
    selectedObject.setStatus( new StatusDTO() );
    selectedObject.setTaskType( new TaskTypeDTO() );
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    userPortalDTO = getUserDTO();
    if ( userPortalDTO != null ) {
      listRegisterGroup( userPortalDTO.getPerson().getId() );
      listArea( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
      listPriority( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
      listStatus( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
      listTaskType( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
      getPercentageNow();
      //Obtener el ID del usuario que esa logueado con el fin de usarlo para almacenamiento de documentos asociados a la tarea
      idUsuario = String.valueOf( getUserDTO().getPerson().getClient().getClientCetus().getId() );
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  /**
   * </p> Obtiene el porcentaje actual de dedicacion del usuario logueado. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  private void getPercentageNow () {
    percentage = generalDelegate.getPercentageCurrent( userPortalDTO.getPerson().getId() );
  }
  
  /**
   * </p> Load. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void load ( ActionEvent event ) {
    try {
      if ( addObject != null ) {
        addObject.setApproved( approved ? BigDecimal.ONE.intValue() : BigDecimal.ZERO.intValue() );
        addObjectSession( addObject, "addObject" );
        this.showConfirmAdd = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void loadCancel ( ActionEvent event ) {
    if ( noteTask != null && !noteTask.isEmpty() ) {
      addObjectSession( noteTask, "noteTask" );
      //addObjectSession( selectedObject, "selectedObject" );
      showConfirmCancel = true;
      showConfirmCompleted = true;
    }
  }
  
  public void loadSuspended ( ActionEvent event ) {
    if ( noteTask != null && !noteTask.isEmpty() ) {
      addObjectSession( noteTask, "noteTask" );
      //addObjectSession( selectedObject, "selectedObject" );
      showConfirmSuspended = true;
    }
  }
  
  /**
   * </p> Load update. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void loadUpdate ( ActionEvent event ) {
    TaskDTO taskDTO = null;
    try {
      if ( selectedObject != null ) {
        taskDTO = ( TaskDTO ) getObjectSession( "selectedObject" );
        if ( taskDTO != null ) {
          selectedObject.setId( taskDTO.getId() );
          selectedObject.setApproved( approved ? BigDecimal.ONE.intValue() : BigDecimal.ZERO.intValue() );
          selectedObject.setDateInit( taskDTO.getDateInit() );
          addObjectSession( percentageCurrent, "percentageCurrent" );
          addObjectSession( ( 100 - percentageCurrent ), "percentageSelected" );
          selectedObject.setCode( taskDTO.getCode() );
          code = taskDTO.getCode();
          addObjectSession( code, "code" );
          addObjectSession( noteTask, "noteTask" );
          selectedObject.setPersonGroup( taskDTO.getPersonGroup() );
          selectedObject.setAssingDate( taskDTO.getAssingDate() );
          //selectedObject.setClientCetus( TaskDTO.getClientCetus() );
          selectedObject.setCreationUser( taskDTO.getCreationUser() );
          selectedObject.setCreationDate( taskDTO.getCreationDate() );
          if ( selectedObject.getStatus() == null || ( selectedObject.getStatus() != null && selectedObject.getStatus().getId() <= 0 ) ) {
            selectedObject.setStatus( new StatusDTO() );
            selectedObject.getStatus().setId( ( int ) getObjectSession( "status" ) );
          }
          addObjectSession( taskDTO.getStatus(), "statusOld" );//Guardo el estado anterior
        }
        addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmUpdate = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Start task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void startTask () {
    ResponseDTO responseDTO = null;
    TaskDTO taskDTO = null;
    int count = 0;
    RequestContext context = null;
    try {
      context = RequestContext.getCurrentInstance();
      //Se debe establecer la fecha de inicio de la tarea para poder calcular con la duracion la longitud de la misma
      this.showConfirmMod = false;
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObjectChanged" );
      if ( selectedObject != null ) {
        selectedObject.setPercentage( percentageSelected );
        selectedObject.setDeliveryDate( deliveryDate );
        listRegister = ( List< TaskDTO > ) getObjectSession( "listRegister" );
        //Validar capacidad para ejecutar otra tarea segun parametro general
        if ( listRegister != null ) {
          for ( TaskDTO task: listRegister ) {
            if ( task.getStatus().getId() == ConstantWEB.STATUS_INPROGRESS ) {
              count++;
            }
          }
        }
        ConstantWEB.WEB_LOG.info( "########## TAREAS EN PROGRESO  " + count );
        
        if ( count <= getParameterGeneralNumberTaskInProgress() ) {
          taskDTO = ( TaskDTO ) getObjectSession( "selectedObject" );
          if ( taskDTO != null ) {
            selectedObject.setId( taskDTO.getId() );
            selectedObject.setCode( taskDTO.getCode() );
            selectedObject.setSuspended( taskDTO.getSuspended() );
            selectedObject.setVLength( taskDTO.getVLength() );
            selectedObject.setDateInitCurrent( taskDTO.getDateInitCurrent() );
            selectedObject.setCreationDate( taskDTO.getCreationDate() );
            selectedObject.setCreationUser( taskDTO.getCreationUser() );
            selectedObject.setSuspended( taskDTO.getSuspended() );
            code = taskDTO.getCode();
            addObjectSession( code, "code" );
            selectedObject.setPersonGroup( taskDTO.getPersonGroup() );
            selectedObject.setAssingDate( taskDTO.getAssingDate() );
            selectedObject.setModificationUser( getUserInSession() );
            selectedObject.setApproved( ( Boolean ) getObjectSession( "approved" ) ? BigDecimal.ONE.intValue() : BigDecimal.ZERO.intValue() );
            selectedObject.setModificationDate( currentDate );
            selectedObject.setStatus( new StatusDTO() );
            selectedObject.getStatus().setId( ConstantWEB.STATUS_INPROGRESS_VAL );//Se coloca en progreso la tarea
            selectedObject.setDateInit( currentDate );//Fecha de Inicio de la Tarea      
            responseDTO = generalDelegate.edit( selectedObject );
            
            //LIstar Tareas
            responseDTO = generalDelegate.findTaskByPersonGroup( selectedObject.getPersonGroup().getGroupT().getId(), userPortalDTO.getPerson()
                                                                                                                                   .getId() );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              //Tiene Usuario portal la persona seleecionada 
              listRegister = ( ( List< TaskDTO > ) responseDTO.getObjectResponse() );
            } else {
              listRegister = new ArrayList< TaskDTO >();
            }
            addObjectSession( listRegister, "listRegister" );
            
            //Enviar EMAIL temporalmente          
            String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
            String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
            String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
            String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
            String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
            String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
            String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( "HTML_EMAIL_TASK_STARTED" );
            String resParamSubjectTaskAdd = generalDelegate.getValueParameter( "SUBJECT_TASK_STARTED" );
            
            //            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
            //              sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
            //                                       replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
            //              sendMail.start();
            //            }
            
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              ConstantWEB.WEB_LOG.info( "########## SE DA INICIO A LA TAREA  " + selectedObject.getCode() );
              //Insertar Trazabilidad 
              if ( ( Integer ) getObjectSession( "status" ) != selectedObject.getStatus().getId() ) {
                //Si el estado anterior y el nuevo es diferente debe realizar una insercion a la trazabilidad
                TraceTaskDTO traceTaskDTO = new TraceTaskDTO();
                traceTaskDTO.setTask( selectedObject );
                traceTaskDTO.setNote( ( String ) getObjectSession( "noteTask" ) );
                traceTaskDTO.setIdStatus( ( int ) getObjectSession( "status" ) );
                traceTaskDTO.setAssingDate( selectedObject.getAssingDate() );
                traceTaskDTO.setIdPerson( selectedObject.getPersonGroup().getPerson().getId() );
                traceTaskDTO.setCreationDate( currentDate );
                traceTaskDTO.setCreationUser( getUserInSession() );
                responseDTO = generalDelegate.create( traceTaskDTO );
                if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                  ConstantWEB.WEB_LOG.info( "########## SE CREAR EL REGISTRO DE TRAZABILIDAD PARA LA TAREA " + selectedObject.getCode() );
                }
              }
            }
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, MessageFormat.format( ConstantWEB.MSG_STARTED_TASK, selectedObject.getCode() ) );
            
            context.execute( "PF('dlgTaskInfo').hide();" );
            context.execute( "PF('dialogViewVar').hide();" );
          }
        } else {
          RequestContext.getCurrentInstance()
                        .showMessageInDialog( new FacesMessage( FacesMessage.SEVERITY_WARN, ConstantWEB.MESSAGE_WARNING,
                                                                MessageFormat.format( ConstantWEB.MSG_DETAIL_ERROR_LIMIT_TASK,
                                                                                      getParameterGeneralNumberTaskInProgress() ) ) );
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getCause().getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      context.execute( "PF('dlgTaskInfo').hide();" );
      context.execute( "PF('dialogViewVar').hide();" );
    }
    
  }
  
  /**
   * </p> Complete task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void completeTask () {
    RequestContext context = null;
    ResponseDTO response = null;
    String duration = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      noteTask = ( String ) getObjectSession( "noteTask" );
      if ( selectedObject != null && noteTask != null & !noteTask.isEmpty() ) {
        selectedObject.setStatus( new StatusDTO() );
        selectedObject.getStatus().setId( ConstantWEB.STATUS_COMPLETED_VAL );//Se coloca en COMPLETADA la tarea
        selectedObject.setDateEnd( getCurrentDateTime() );
        //Obtener la longitud de la tarea llamando al procedimiento almacenado
        response = generalDelegate.generateLengthTask( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          duration = ( String ) response.getObjectResponse();
          selectedObject.setVLength( duration );
          response = generalDelegate.edit( selectedObject );
          
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            //Listar Tareas
            response = generalDelegate.findTaskByPersonGroup( selectedObject.getPersonGroup().getGroupT().getId(), userPortalDTO.getPerson()
                                                                                                                                .getId() );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              listRegister = ( ( List< TaskDTO > ) response.getObjectResponse() );
            } else {
              listRegister = new ArrayList< TaskDTO >();
            }
            
            //Lista de tareas actualizadas
            addObjectSession( listRegister, "listRegister" );
            //Enviar EMAIL temporalmente          
            String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
            String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
            String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
            String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
            String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
            String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
            String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( "HTML_EMAIL_TASK_COMPLETED" );
            String resParamSubjectTaskAdd = generalDelegate.getValueParameter( "SUBJECT_TASK_COMPLETED" );
            
            //            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
            //              if ( resParamSubjectTaskAdd != null && resParamHtmlMsgTaskAdd != null ) {
            //                sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
            //                                         replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
            //                                         
            //                sendMail.start();
            //              }
            //            }
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              ConstantWEB.WEB_LOG.info( "########## TAREA COMPLETADA  " + selectedObject.getCode() );
              //Insertar Trazabilidad 
              if ( ( Integer ) getObjectSession( "status" ) != selectedObject.getStatus().getId() ) {
                //Si el estado anterior y el nuevo es diferente debe realizar una insercion a la trazabilidad
                TraceTaskDTO traceTaskDTO = new TraceTaskDTO();
                traceTaskDTO.setTask( selectedObject );
                traceTaskDTO.setNote( noteTask );
                traceTaskDTO.setIdStatus( ( int ) getObjectSession( "status" ) );
                traceTaskDTO.setAssingDate( selectedObject.getAssingDate() );
                traceTaskDTO.setIdPerson( selectedObject.getPersonGroup().getPerson().getId() );
                traceTaskDTO.setCreationDate( currentDate );
                traceTaskDTO.setCreationUser( getUserInSession() );
                response = generalDelegate.create( traceTaskDTO );
                if ( UtilCommon.validateResponseSuccess( response ) ) {
                  ConstantWEB.WEB_LOG.info( "########## SE CREA EL REGISTRO DE TRAZABILIDAD PARA LA TAREA " + selectedObject.getCode() );
                }
              }
            }
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_COMPLETED_TASK );
          } else {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, ConstantWEB.MESSAGE_NOT_COMPLETED_TASK );
          }
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, ConstantWEB.MESSAGE_NOT_COMPLETED_TASK );
        }
        context.execute( "PF('dlgTaskInfo').hide();" );
        context.execute( "PF('dialogViewVar').hide();" );
        //
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), e.getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      context.execute( "PF('dlgTaskInfo').hide();" );
      context.execute( "PF('dialogViewVar').hide();" );
    }
  }
  
  /**
   * </p> View form info. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  public void viewFormInfo () {
    RequestContext context = null;
    if ( selectedObject != null ) {
      context = RequestContext.getCurrentInstance();
      // Validar si ya alcanzo el porcentaje de dedicacion no puede permitir 
      if ( ( 100 - percentage ) > 0 ) {
        addObjectSession( noteTask, "noteTask" );
        addObjectSession( approved, "approved" );
        addObjectSession( selectedObject, "selectedObjectChanged" );
        context.execute( "PF('dlgTaskInfo').show();" );
      } else {
        context.execute( "PF('alertPercetageFullVar').show();" );
      }
    }
    
  }
  
  /**
   * </p> View applet assign. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  public void viewAppletAssign () {
    RequestContext context = null;
    selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
    if ( selectedObject != null ) {
      context = RequestContext.getCurrentInstance();
      // Validar si ya alcanzo el porcentaje de dedicacion no puede permitir      
      addObjectSession( noteTask, "noteTask" );
      addObjectSession( approved, "approved" );
      addObjectSession( selectedObject, "selectedObjectChanged" );
      context.execute( "PF('appletAssignDialogVar').show();" );
      if ( selectedObject.getPersonGroup() != null && selectedObject.getPersonGroup().getGroupT() != null ) {
        listPersonByGroup( selectedObject.getPersonGroup().getGroupT().getId() );
      }
      
    }
    
  }
  
  /**
   * </p> Cancel task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void cancelTask () {
    RequestContext context = null;
    //Proceso para dar inicio a la tarea
    ResponseDTO response = null;
    String duration = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      noteTask = ( String ) getObjectSession( "noteTask" );
      if ( selectedObject != null && noteTask != null && !noteTask.isEmpty() ) {
        selectedObject.setStatus( new StatusDTO() );
        selectedObject.getStatus().setId( ConstantWEB.STATUS_CANCELED_VAL );//Se coloca en cancelada la tarea  
        response = generalDelegate.generateLengthTask( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          duration = ( String ) response.getObjectResponse();
          selectedObject.setVLength( duration );
          response = generalDelegate.edit( selectedObject );
          
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            //LIstar Tareas
            response = generalDelegate.findTaskByPersonGroup( selectedObject.getPersonGroup().getGroupT().getId(), userPortalDTO.getPerson()
                                                                                                                                .getId() );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              //Tiene Usuario portal la persona seleecionada 
              listRegister = ( ( List< TaskDTO > ) response.getObjectResponse() );
            } else {
              listRegister = new ArrayList< TaskDTO >();
            }
            addObjectSession( listRegister, "listRegister" );
            //Enviar EMAIL temporalmente          
            //            String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
            //            String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
            //            String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
            //            String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
            //            String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
            //            String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
            //            String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( "HTML_EMAIL_TASK_CANCEL" );
            //            String resParamSubjectTaskAdd = generalDelegate.getValueParameter( "SUBJECT_TASK_CANCEL" );
            
            //            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
            //              if ( resParamSubjectTaskAdd != null && resParamHtmlMsgTaskAdd != null ) {
            //                sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
            //                                         replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
            //                                         
            //                sendMail.start();
            //              }
            //            }
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              ConstantWEB.WEB_LOG.info( "########## TAREA CANCELADA  " + selectedObject.getCode() );
              //Insertar Trazabilidad 
              if ( ( int ) getObjectSession( "status" ) != selectedObject.getStatus().getId() ) {
                //Si el estado anterior y el nuevo es diferente debe realizar una insercion a la trazabilidad
                TraceTaskDTO traceTaskDTO = new TraceTaskDTO();
                traceTaskDTO.setTask( selectedObject );
                traceTaskDTO.setNote( noteTask );
                traceTaskDTO.setIdStatus( ( int ) getObjectSession( "status" ) );
                traceTaskDTO.setAssingDate( selectedObject.getAssingDate() );
                traceTaskDTO.setIdPerson( selectedObject.getPersonGroup().getPerson().getId() );
                traceTaskDTO.setCreationDate( currentDate );
                traceTaskDTO.setCreationUser( getUserInSession() );
                response = generalDelegate.create( traceTaskDTO );
                if ( UtilCommon.validateResponseSuccess( response ) ) {
                  ConstantWEB.WEB_LOG.info( "########## SE CREA EL REGISTRO DE TRAZABILIDAD PARA LA TAREA " + selectedObject.getCode() );
                }
              }
            }
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_CANCELED_TASK );
          } else {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, ConstantWEB.MESSAGE_NOT_CANCELED_TASK );
          }
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, ConstantWEB.MESSAGE_NOT_CANCELED_TASK );
        }
        context.execute( "PF('dlgTaskInfo').hide();" );
        context.execute( "PF('dialogViewVar').hide();" );
        //
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), e.getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      context.execute( "PF('dlgTaskInfo').hide();" );
      context.execute( "PF('dialogViewVar').hide();" );
    }
    
  }
  
  /**
   * </p> Suspend task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void suspendTask () {
    RequestContext context = null;
    //Proceso para dar inicio a la tarea
    ResponseDTO response = null;
    String duration = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      noteTask = ( String ) getObjectSession( "noteTask" );
      if ( selectedObject != null && noteTask != null & !noteTask.isEmpty() ) {
        selectedObject.setStatus( new StatusDTO() );
        selectedObject.getStatus().setId( ConstantWEB.STATUS_SUSPENDED_VAL );//Se coloca en suspendido la tarea
        selectedObject.setSuspended( ConstantWEB.SUSPENDED );
        selectedObject.setDateInitCurrent( selectedObject.getDateInit() );
        response = generalDelegate.generateLengthTask( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          duration = ( String ) response.getObjectResponse();
          selectedObject.setVLength( duration );
          selectedObject.setDateInit( null );
          response = generalDelegate.edit( selectedObject );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            //LIstar Tareas
            response = generalDelegate.findTaskByPersonGroup( selectedObject.getPersonGroup().getGroupT().getId(), userPortalDTO.getPerson()
                                                                                                                                .getId() );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              //Tiene Usuario portal la persona seleecionada 
              listRegister = ( ( List< TaskDTO > ) response.getObjectResponse() );
            } else {
              listRegister = new ArrayList< TaskDTO >();
            }
            addObjectSession( listRegister, "listRegister" );
            
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              ConstantWEB.WEB_LOG.info( "########## TAREA SUSPENDIDA  " + selectedObject.getCode() );
              //Insertar Trazabilidad 
              if ( ( int ) getObjectSession( "status" ) != selectedObject.getStatus().getId() ) {
                //Si el estado anterior y el nuevo es diferente debe realizar una insercion a la trazabilidad
                TraceTaskDTO traceTaskDTO = new TraceTaskDTO();
                traceTaskDTO.setTask( selectedObject );
                traceTaskDTO.setNote( noteTask );
                traceTaskDTO.setIdStatus( ( int ) getObjectSession( "status" ) );
                traceTaskDTO.setAssingDate( selectedObject.getAssingDate() );
                traceTaskDTO.setIdPerson( selectedObject.getPersonGroup().getPerson().getId() );
                traceTaskDTO.setCreationDate( currentDate );
                traceTaskDTO.setCreationUser( getUserInSession() );
                response = generalDelegate.create( traceTaskDTO );
                if ( UtilCommon.validateResponseSuccess( response ) ) {
                  ConstantWEB.WEB_LOG.info( "########## SE CREA EL REGISTRO DE TRAZABILIDAD PARA LA TAREA " + selectedObject.getCode() );
                }
              }
            }
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUSPENDED_TASK );
          } else {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, ConstantWEB.MESSAGE_NOT_SUSPENDED_TASK );
          }
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, ConstantWEB.MESSAGE_NOT_SUSPENDED_TASK );
        }
        context.execute( "PF('dlgTaskInfo').hide();" );
        context.execute( "PF('dialogViewVar').hide();" );
        //
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), e.getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      context.execute( "PF('dlgTaskInfo').hide();" );
      context.execute( "PF('dialogViewVar').hide();" );
    }
    
  }
  
  public boolean isDayValid ( String pDay ) {
    ResponseDTO response = null;
    boolean flag = false;
    try {
      //consultar si el día que esta ingresando es un dia laboral
      response = generalDelegate.existsJorndInDay( userPortalDTO.getPerson().getClient().getClientCetus().getId(), pDay );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        flag = ( boolean ) response.getObjectResponse();
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), e.getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return flag;
  }
  
  public boolean isTimeValid ( String pDay, Date pTime ) {
    ResponseDTO response = null;
    boolean flag = false;
    try {
      //consultar si el día que esta ingresando es un dia laboral
      response = generalDelegate.isTimeValid( userPortalDTO.getPerson().getClient().getClientCetus().getId(), pDay, pTime );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        flag = ( boolean ) response.getObjectResponse();
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), e.getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return flag;
  }
  
  /**
   * </p> Metodo llamado al cargar un archivo asociado a la tarea. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void handleFileUpload ( FileUploadEvent event ) {
    try {
      if ( event != null ) {
        if ( getObjectSession( "listFilesTask" ) == null ) {
          listFilesTask = new ArrayList< >();
          listFilesTask.add( event.getFile() );
        } else {
          listFilesTask.add( event.getFile() );
        }
        //copiar archivo a la ruta del usuario como archivo temporal
        copyFileTmp( event.getFile().getFileName(), event.getFile().getInputstream() );
        addObjectSession( listFilesTask, "listFilesTask" );
        //addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, event.getFile().getFileName() );
      }
    } catch ( IOException e ) {
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getCause().getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> On timeout. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  public void onTimeout () {
    addMessageInfo( null, "Nota:", "la tarea se ha vencido!" );
  }
  
  /**
   * </p> Clean var. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  public void cleanVar () {
    //cleanObjectSession( "percentageSelected" );
    selectedObject.setPercentage( ( ( double ) getObjectSession( "percentageCurrent" ) ) );
  }
  
  /**
   * </p> Close dialog view. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  public void closeDialogView () {
    selectedObject = new TaskDTO();
    selectedObject.setArea( new AreaDTO() );
    selectedObject.setPriority( new PriorityDTO() );
    selectedObject.setStatus( new StatusDTO() );
    selectedObject.setTaskType( new TaskTypeDTO() );
    removerFileUploads();
  }
  
  /**
   * </p> Remover file uploads. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  private void removerFileUploads () {
    cleanObjectSession( "listFilesTask" );
    if ( destination != null && idUsuario != null ) {
      
      File archivos = new File( destination + separador + idUsuario );
      if ( archivos != null ) {
        File[] lista = archivos.listFiles();
        if ( archivos.listFiles() != null && archivos.listFiles().length > 0 ) {
          for ( File file: lista ) {
            if ( !file.isDirectory() ) {
              file.delete();
            }
          }
        }
      }
    }
    
  }
  
  /**
   * </p> List register group. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idPerson the id person
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listRegisterGroup ( int idPerson ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPersonGroupByPerson( idPerson );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listRegisterGroup = ( List< PersonGroupDTO > ) response.getObjectResponse();
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegisterGroup = new ArrayList< PersonGroupDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> List task type. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listTaskType ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findTaskType( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listTaskType = ( List< TaskTypeDTO > ) response.getObjectResponse();
        listTaskTypeItem = new ArrayList< SelectItem >();
        for ( TaskTypeDTO objDTO: listTaskType ) {
          listTaskTypeItem.add( new SelectItem( objDTO.getId(), objDTO.getDescription() ) );
        }
        addObjectSession( listTaskTypeItem, "listTaskTypeItem" );
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listTaskType = new ArrayList< TaskTypeDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> List status. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listStatus ( int idClientCetus ) {
    ResponseDTO response = null;
    List< SelectItem > copy = null;
    try {
      response = generalDelegate.findStatus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listStatus = ( List< StatusDTO > ) response.getObjectResponse();
        listStatusItem = new ArrayList< SelectItem >();
        listStatusFilter = new ArrayList< >();
        for ( StatusDTO objDTO: listStatus ) {
          if ( objDTO.getId() != ConstantWEB.STATUS_ASSIGNED && objDTO.getId() != ConstantWEB.STATUS_COMPLETED
               && objDTO.getId() != ConstantWEB.STATUS_INPROGRESS && objDTO.getId() != ConstantWEB.STATUS_CANCELED
               && objDTO.getId() != ConstantWEB.STATUS_SUSPENDED ) {
            listStatusItem.add( new SelectItem( objDTO.getId(), objDTO.getDescription() ) );
            listStatusFilter.add( objDTO.getDescription() );
          }
        }
        if ( ( Integer ) getObjectSession( "status" ) != null ) {
          copy = new ArrayList< SelectItem >( listStatusItem );
          if ( listStatusItem != null ) {
            for ( SelectItem obj: listStatusItem ) {
              if ( ( int ) obj.getValue() == ( int ) getObjectSession( "status" ) ) {
                copy.remove( obj );
              }
            }
          }
        }
        addObjectSession( copy, "listStatusItem" );
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listStatus = new ArrayList< StatusDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> List priority. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listPriority ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPriorityByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listPriority = ( List< PriorityDTO > ) response.getObjectResponse();
        listPriorityItem = new ArrayList< SelectItem >();
        for ( PriorityDTO objDTO: listPriority ) {
          listPriorityItem.add( new SelectItem( objDTO.getId(), objDTO.getDescription() ) );
        }
        addObjectSession( listPriorityItem, "listPriorityItem" );
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listPriority = new ArrayList< PriorityDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> List area. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listArea ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findAreaByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listArea = ( List< AreaDTO > ) response.getObjectResponse();
        listAreaItem = new ArrayList< SelectItem >();
        for ( AreaDTO objDTO: listArea ) {
          listAreaItem.add( new SelectItem( objDTO.getId(), objDTO.getDescription() ) );
        }
        addObjectSession( listAreaItem, "listAreaItem" );
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listArea = new ArrayList< AreaDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> List person by group. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idGroup the id group
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listPersonByGroup ( int idGroup ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPersonByGroup( idGroup );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listPersonG = ( List< PersonGroupDTO > ) response.getObjectResponse();
        //        listPersonItem = new ArrayList< SelectItem >();
        //        for ( PersonDTO objDTO: listPerson ) {
        //          listPersonItem.add( new SelectItem( objDTO.getId(), objDTO.getNames() + " " + objDTO.getLastNames() ) );
        //        }
        addObjectSession( listPersonG, "listPersonG" );
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listPersonG = new ArrayList< PersonGroupDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      this.showConfirmDelete = false;
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        ConstantWEB.WEB_LOG.info( "Se procede a eliminar el area ::> " + selectedObject.toString() );
        responseDTO = generalDelegate.remove( selectedObject );
        ConstantWEB.WEB_LOG.info( "Respuesta despues de eliminar el area ::> " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Area eliminada exitosamente..." );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
        } else {
          ConstantWEB.WEB_LOG.error( "Error eliminado el area..." );
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#update()
   */
  @SuppressWarnings ( "unchecked" )
  @Override
  public String update () {
    ResponseDTO responseDTO = null;
    File directoryTmp = null;
    File[] contents = null;
    String path;
    File userUploads = null;
    AttachDTO attachDTO = null;
    try {
      this.showConfirmMod = false;
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        selectedObject.setModificationUser( getUserInSession() );
        selectedObject.setModificationDate( currentDate );
        //selectedObject.getPersonGroup().setId( groupTDTOSelected.getId() );
        selectedObject.setPercentage( ( double ) getObjectSession( "percentageCurrent" ) );
        ConstantWEB.WEB_LOG.info( "Se procede a actualizar el Area ::> " + selectedObject.toString() );
        
        if ( selectedObject.getStatus().getId() == ConstantWEB.STATUS_SUSPENDED_VAL ) {
          if ( getObjectSession( "selectedObjectPerson" ) != null ) {
            selectedObject.setPersonGroup( ( PersonGroupDTO ) getObjectSession( "selectedObjectPerson" ) );
          }
        }
        responseDTO = generalDelegate.edit( selectedObject );
        
        ConstantWEB.WEB_LOG.info( "Respuesta despues de actualizar el area ::> " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Tarea actualizada exitosamente..." );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
          
          //Tiene archivos cargados en memoria 
          if ( getObjectSession( "listFilesTask" ) != null ) {
            listFilesTask = ( ArrayList< UploadedFile > ) getObjectSession( "listFilesTask" );
            if ( listFilesTask.size() > 0 ) {
              //Tiene archivos para agregar! 
              ConstantWEB.WEB_LOG.debug( "############# TIENE ARCHIVOS PARA CARGAR " + listFilesTask.size() );
              path = destination + separador + idUsuario;
              directoryTmp = new File( path );
              for ( UploadedFile att: listFilesTask ) {
                if ( directoryTmp != null ) {
                  contents = directoryTmp.listFiles();
                  interno: for ( File file: contents ) {
                    if ( !file.isDirectory() && file.getName().substring( 0, file.getName().lastIndexOf( "#" ) )
                                                    .equals( att.getFileName().substring( 0, att.getFileName().lastIndexOf( "." ) ) ) ) {
                      attachDTO = new AttachDTO();
                      attachDTO.setTask( selectedObject );
                      attachDTO.setFileName( att.getFileName() );
                      userUploads = new File( path, String.valueOf( attachDTO.getTask().getCode() ) );
                      attachDTO.setPath( userUploads.getPath() );
                      attachDTO.setCreationDate( currentDate );
                      attachDTO.setCreationUser( getUserInSession() );
                      userUploads.mkdir();
                      //Crear el registro del archivo adjunto a la tarea 
                      responseDTO = generalDelegate.create( attachDTO );
                      
                      //Renombrar el archivo y mover a la carpeta asociada al ID de la tarea
                      if ( renameAndMoveFileTmp( att.getFileName(), file.getName(), userUploads.getPath() + separador, path ) ) {
                        //Archivo se movio correctamente
                        ConstantWEB.WEB_LOG.debug( "############# ARCHIVO MOVIDO CORRECTAMENTE " + att.getFileName() );
                        
                      } else {
                        //Archivo presento problemas al moverlo, se debe validar porque debio ser borrado por ser temporal
                        ConstantWEB.WEB_LOG.debug( "############# ERROR MOVIENDO EL ARCHIVO " + att.getFileName() );
                        
                      }
                      break interno;
                    }
                  }
                }
              }
            }
            
          }
          //LIstar Tareas
          responseDTO = generalDelegate.findTaskByPersonGroup( selectedObject.getPersonGroup().getGroupT().getId(),
                                                               userPortalDTO.getPerson().getId() );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            //Tiene Usuario portal la persona seleecionada 
            listRegister = ( ( List< TaskDTO > ) responseDTO.getObjectResponse() );
          } else {
            listRegister = new ArrayList< TaskDTO >();
          }
          addObjectSession( listRegister, "listRegister" );
          
          //Insertar Trazabilidad 
          if ( ( ( StatusDTO ) getObjectSession( "statusOld" ) ).getId() != selectedObject.getStatus().getId() ) {
            //Si el estado anterior y el nuevo es diferente debe realizar una insercion a la trazabilidad
            TraceTaskDTO traceTaskDTO = new TraceTaskDTO();
            traceTaskDTO.setTask( selectedObject );
            traceTaskDTO.setNote( ( String ) getObjectSession( "noteTask" ) );
            traceTaskDTO.setIdStatus( ( ( StatusDTO ) getObjectSession( "statusOld" ) ).getId() );
            traceTaskDTO.setAssingDate( selectedObject.getAssingDate() );
            traceTaskDTO.setIdPerson( selectedObject.getPersonGroup().getPerson().getId() );
            traceTaskDTO.setCreationDate( currentDate );
            traceTaskDTO.setCreationUser( getUserInSession() );
            responseDTO = generalDelegate.create( traceTaskDTO );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              ConstantWEB.WEB_LOG.info( "########## SE CREAR EL REGISTRO DE TRAZABILIDAD PARA LA TAREA " + selectedObject.getCode() );
            }
          }
          
        } else {
          ConstantWEB.WEB_LOG.error( "Error actualizando el Area..." );
          //Obetener codigo de error de oracle
          getMessageError( responseDTO );
        }
      }
    } catch (
    
    Exception e )
    
    {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
    
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @SuppressWarnings ( "unchecked" )
  @Override
  public String add () {
    ResponseDTO responseDTO = null;
    AttachDTO attachDTO = null;
    TaskDTO tmpTask;
    File directoryTmp = null;
    File[] contents = null;
    String path;
    File userUploads = null;
    try {
      this.showConfirmAdd = false;
      addObject = ( TaskDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        groupTDTOSelected = ( PersonGroupDTO ) getObjectSession( "groupTDTOSelected" );
        
        responseDTO = generalDelegate.generateCodeTask();//Generar el codigo de la tarea
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          addObject.setCode( ( String ) responseDTO.getObjectResponse() );
        } else {
          addObject.setCode( null );
        }
        addObject.setStatus( new StatusDTO() );
        addObject.getStatus().setId( ConstantWEB.STATUS_ASSIGNED );
        addObject.setPersonGroup( new PersonGroupDTO() );
        addObject.getPersonGroup().setId( groupTDTOSelected.getId() );
        addObject.setCreationUser( getUserInSession() );
        addObject.setCreationDate( currentDate );
        addObject.setSuspended( ConstantWEB.NO_SUSPENDED );
        responseDTO = generalDelegate.create( addObject );
        tmpTask = ( TaskDTO ) responseDTO.getObjectResponse();
        
        //Tarea creada Exitosamente
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.debug( "############# TAREA CREADA EXITOSAMENTE " + tmpTask.getCode() );
          this.initElement();
          //Tiene archivos cargados en memoria 
          if ( getObjectSession( "listFilesTask" ) != null ) {
            listFilesTask = ( ArrayList< UploadedFile > ) getObjectSession( "listFilesTask" );
            if ( listFilesTask.size() > 0 ) {
              //Tiene archivos para agregar! 
              ConstantWEB.WEB_LOG.debug( "############# TIENE ARCHIVOS PARA CARGAR " + listFilesTask.size() );
              path = destination + separador + idUsuario;
              directoryTmp = new File( path );
              for ( UploadedFile att: listFilesTask ) {
                if ( directoryTmp != null ) {
                  contents = directoryTmp.listFiles();
                  interno: for ( File file: contents ) {
                    if ( !file.isDirectory() && file.getName().substring( 0, file.getName().lastIndexOf( "#" ) )
                                                    .equals( att.getFileName().substring( 0, att.getFileName().lastIndexOf( "." ) ) ) ) {
                      attachDTO = new AttachDTO();
                      attachDTO.setTask( tmpTask );
                      attachDTO.setFileName( att.getFileName() );
                      userUploads = new File( path, String.valueOf( attachDTO.getTask().getCode() ) );
                      attachDTO.setPath( userUploads.getPath() );
                      attachDTO.setCreationDate( currentDate );
                      attachDTO.setCreationUser( getUserInSession() );
                      userUploads.mkdir();
                      //Crear el registro del archivo adjunto a la tarea 
                      responseDTO = generalDelegate.create( attachDTO );
                      
                      //Renombrar el archivo y mover a la carpeta asociada al ID de la tarea
                      if ( renameAndMoveFileTmp( att.getFileName(), file.getName(), userUploads.getPath() + separador, path ) ) {
                        //Archivo se movio correctamente
                        ConstantWEB.WEB_LOG.debug( "############# ARCHIVO MOVIDO CORRECTAMENTE " + att.getFileName() );
                        
                      } else {
                        //Archivo presento problemas al moverlo, se debe validar porque debio ser borrado por ser temporal
                        ConstantWEB.WEB_LOG.debug( "############# ERROR MOVIENDO EL ARCHIVO " + att.getFileName() );
                        
                      }
                      break interno;
                    }
                  }
                }
              }
            }
          }
          
          //TODO Envio de correo temporalmente          
          //sendMailTmp( "HTML_EMAIL_TASK_NEW", "SUBJECT_TASK_NEW", addObject );
          
          //Buscar las tareas actualizando el registro nuevo
          responseDTO = generalDelegate.findTaskByPersonGroup( groupTDTOSelected.getGroupT().getId(), userPortalDTO.getPerson().getId() );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            //Tiene Usuario portal la persona seleecionada 
            listRegister = ( ( List< TaskDTO > ) responseDTO.getObjectResponse() );
          } else {
            listRegister = new ArrayList< TaskDTO >();
          }
          addObjectSession( listRegister, "listRegister" );
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES,
                          MessageFormat.format( ConstantWEB.MESSAGE_TASK_CREATED, addObject.getCode() ) );
                          
        } else {
          ConstantWEB.WEB_LOG.error( ConstantWEB.MESSAGE_ERROR_CREATE );
          //Obetener codigo de error de oracle
          getMessageError( responseDTO, ConstantWEB.MESSAGE_ERROR_CREATE );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    return null;
  }
  
  @SuppressWarnings ( "unchecked" )
  public void removeAttachInTask () {
    ResponseDTO responseDTO = null;
    attachDTOSelected = ( AttachDTO ) getObjectSession( "attachDTOSelected" );
    if ( attachDTOSelected != null ) {
      //Obtener PATH para abrir el archivo
      indexTab = 1;
      File archivos = new File( attachDTOSelected.getPath() + separador + attachDTOSelected.getFileName() );
      if ( archivos != null ) {
        if ( archivos.isFile() ) {
          if ( archivos.delete() ) {
            responseDTO = generalDelegate.remove( attachDTOSelected );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
              responseDTO = generalDelegate.findAttachmentFilesByTaskId( selectedObject.getId() );
              if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                //Tiene archivos la tarea 
                listAttachFiles = ( ( List< AttachDTO > ) responseDTO.getObjectResponse() );
              } else {
                listAttachFiles = new ArrayList< AttachDTO >();
              }
              addObjectSession( listAttachFiles, "listAttachFiles" );
            }
          } else {
            indexTab = 1;
            addMessageWarning( null, MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE, "Archivo" ), null );
          }
        } else {
          responseDTO = generalDelegate.remove( attachDTOSelected );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
            responseDTO = generalDelegate.findAttachmentFilesByTaskId( selectedObject.getId() );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              //Tiene archivos la tarea 
              listAttachFiles = ( ( List< AttachDTO > ) responseDTO.getObjectResponse() );
            } else {
              listAttachFiles = new ArrayList< AttachDTO >();
            }
            addObjectSession( listAttachFiles, "listAttachFiles" );
          }
        }
      }
      
    } else {
      indexTab = 1;
      addMessageWarning( null, MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE, "Archivo" ), null );
    }
    selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
    cleanObjectSession( "attachDTOSelected" );
  }
  
  public void validedDateInit ( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
    selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
    Date initTask;
    Date endTask;
    if ( selectedObject != null ) {
      initTask = selectedObject.getDateInit();
      if ( initTask != null ) {
        endTask = ( Date ) value;
        if ( !initTask.before( endTask ) ) {
          throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.MESSAGE_ERROR,
                                                          ConstantWEB.MESSAGE_DATE_END_VALIDATION ) );
        } else {
          
          if ( !isDayValid( EWeekDay.getValue( getDayOfTheWeek( endTask ) ) ) ) {
            //No es valido Generar Excepcion
            throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.MESSAGE_ERROR,
                                                            ConstantWEB.MSG_DATE_NOT_JORND ) );
          }
          if ( !isTimeValid( EWeekDay.getValue( getDayOfTheWeek( endTask ) ), endTask ) ) {
            //No es valido Generar Excepcion
            throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.MESSAGE_ERROR,
                                                            ConstantWEB.MSG_TIME_NOT_JORND ) );
          }
        }
        
      }
    }
  }
  
  public void validedDateAssign ( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
    selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
    Date assignTask;
    Date endTask;
    if ( selectedObject != null ) {
      assignTask = selectedObject.getAssingDate();
      if ( assignTask != null ) {
        endTask = ( Date ) value;
        if ( !assignTask.before( endTask ) ) {
          throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.MESSAGE_ERROR,
                                                          ConstantWEB.MESSAGE_DATE_END_VALIDATION ) );
        } else {
          
          if ( !isDayValid( EWeekDay.getValue( getDayOfTheWeek( endTask ) ) ) ) {
            //No es valido Generar Excepcion
            throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.MESSAGE_ERROR,
                                                            ConstantWEB.MSG_DATE_NOT_JORND ) );
          }
          
          if ( !isTimeValid( EWeekDay.getValue( getDayOfTheWeek( endTask ) ), endTask ) ) {
            //No es valido Generar Excepcion
            throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.MESSAGE_ERROR,
                                                            ConstantWEB.MSG_TIME_NOT_JORND ) );
          }
        }
      }
    }
  }
  
  private void sendMailTmp ( String pParamMsg, String pParamSubject, TaskDTO pObj ) {
    //Enviar EMAIL temporalmente          
    String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
    String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
    String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
    String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
    String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
    String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
    String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( pParamMsg );
    String resParamSubjectTaskAdd = generalDelegate.getValueParameter( pParamSubject );
    
    if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
      sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
                               replaceContentEmail( resParamHtmlMsgTaskAdd, pObj ), to, null );
      sendMail.start();
    }
  }
  
  /**
   *   
   * @author Andres Herrera - Cetus Technology
   * @param pFileNameReal Nombre real del archivo cargado
   * @param pFileNameTmp Nombre del archivo con extension de temporales
   * @param pTargetDir Directorio destino donde se almacenara el archivo
   * @param pPathTmp Directorio temporal donde estan los archivos cargados 
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean renameAndMoveFileTmp ( String pFileNameReal, String pFileNameTmp, String pTargetDir, String pPathTmp ) {
    File filesTmp = null;//Archivo Temporal existente en la ruta especificada
    File dir = null;
    String newName = null;
    try {
      if ( pPathTmp != null && pFileNameTmp != null ) {
        filesTmp = new File( pPathTmp + separador, pFileNameTmp );
        dir = new File( pTargetDir );
        int j = pFileNameReal.lastIndexOf( "." );
        if ( pFileNameReal != null && !pFileNameReal.isEmpty() ) {
          newName = pFileNameReal.substring( 0, j ) + pFileNameReal.substring( j, pFileNameReal.length() );
          return filesTmp.renameTo( new File( dir, newName ) );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return false;
  }
  
  /**
   * </p> Copy file. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pFileName the file name
   * @param in the in
   * @throws IOException 
   * @since CetusControlWEB (2/02/2016)
   */
  public void copyFileTmp ( String pFileName, InputStream in ) throws IOException {
    File userUploads = null;
    int j = 0;
    File tmp = null;
    OutputStream out = null;
    int read = 0;
    byte[] bytes = new byte[1024];
    try {
      userUploads = new File( destination, String.valueOf( idUsuario ) );
      userUploads.mkdir();//crear el directorio del usuario que esta logueado
      if ( pFileName != null ) {
        j = pFileName.lastIndexOf( "." );
        //Crear un archivo temporal que sera borrado cuando se termine la operacion
        //Se le agrega un numeral para diferenciar de los numeros que generar java temporalmente asociados al archivo
        tmp = File.createTempFile( pFileName.substring( 0, j ) + "#", "." + pFileName.substring( j, pFileName.length() ), userUploads );
        tmp.deleteOnExit();//Indicar que el archivo sera borrado
        out = new FileOutputStream( tmp );
        while ( ( read = in.read( bytes ) ) != -1 ) {
          out.write( bytes, 0, read );
        }
        in.close();
        out.flush();
        out.close();
        System.out.println( "New file created!" );
      }
    } catch ( IOException e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      throw e;
    } finally {
      try {
        in.close();
        out.flush();
        out.close();
      } catch ( IOException e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
        throw e;
      }
      
    }
  }
  
  /**
   * </p> Replace content email. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param resParamHtmlMsgTaskAdd the res param html msg task add
   * @param pTaskDTO the p task dto
   * @return el string
   * @since CetusControlWEB (2/02/2016)
   */
  private String replaceContentEmail ( String resParamHtmlMsgTaskAdd, TaskDTO pTaskDTO ) {
    
    if ( resParamHtmlMsgTaskAdd != null && !resParamHtmlMsgTaskAdd.isEmpty() ) {
      if ( resParamHtmlMsgTaskAdd.contains( "{0}" ) ) {
        resParamHtmlMsgTaskAdd = resParamHtmlMsgTaskAdd.replace( "{0}", pTaskDTO.getCode() );
      }
      if ( resParamHtmlMsgTaskAdd.contains( "{1}" ) ) {
        resParamHtmlMsgTaskAdd = resParamHtmlMsgTaskAdd.replace( "{1}", pTaskDTO.getDescription() );
      }
      if ( resParamHtmlMsgTaskAdd.contains( "{2}" ) ) {
        resParamHtmlMsgTaskAdd = resParamHtmlMsgTaskAdd.replace( "{2}", pTaskDTO.getUserFunctional() );
      }
      if ( resParamHtmlMsgTaskAdd.contains( "{3}" ) ) {
        resParamHtmlMsgTaskAdd = resParamHtmlMsgTaskAdd.replace( "{3}", String.valueOf( pTaskDTO.getVDuration() ) );
      }
      if ( resParamHtmlMsgTaskAdd.contains( "{4}" ) ) {
        resParamHtmlMsgTaskAdd = resParamHtmlMsgTaskAdd.replace( "{4}", userPortalDTO.getPerson().getNames() );
      }
      
    }
    return resParamHtmlMsgTaskAdd;
  }
  
  /**
   * </p> Validate selected record. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void validateSelectedRecord ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        this.showAlertSelectRow = false;
        this.showViewDetail = true;
        this.showConfirmMod = true;
        this.showConfirmDelete = true;
      } else {
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showConfirmMod = false;
        this.showConfirmDelete = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate selected record group selected. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void validateSelectedRecordGroupSelected ( ActionEvent event ) {
    try {
      if ( groupTDTOSelected != null ) {
        addObjectSession( groupTDTOSelected, "groupTDTOSelected" );
        this.showAlertSelectRow = false;
        this.showAddDialog = true;
        this.showConfirmMod = true;
        this.showConfirmDelete = true;
      } else {
        this.showAlertSelectRow = true;
        this.showAddDialog = false;
        this.showConfirmMod = false;
        this.showConfirmDelete = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> On row select. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void onRowSelect ( SelectEvent event ) {
    ResponseDTO response = null;
    try {
      groupTDTOSelected = ( ( PersonGroupDTO ) event.getObject() );
      if ( groupTDTOSelected != null ) {
        response = generalDelegate.findTaskByPersonGroup( groupTDTOSelected.getGroupT().getId(), userPortalDTO.getPerson().getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          //Tiene Usuario portal la persona seleecionada 
          listRegister = ( ( List< TaskDTO > ) response.getObjectResponse() );
        } else {
          listRegister = new ArrayList< TaskDTO >();
        }
        addObjectSession( listRegister, "listRegister" );
      }
      addObjectSession( groupTDTOSelected, "groupTDTOSelected" );
      //FacesContext.getCurrentInstance().addMessage( null, msg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  /**
   * </p> On row select task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void onRowSelectTask ( SelectEvent event ) {
    ResponseDTO response = null;
    try {
      selectedObject = ( ( TaskDTO ) event.getObject() );
      if ( selectedObject != null ) {
        if ( selectedObject.getPersonGroup() != null ) {
          selectedObjectPerson = selectedObject.getPersonGroup();
          addObjectSession( selectedObjectPerson, "selectedObjectPerson" );
        }
        //al 100% se le debe restar lo que ya el responsable tiene asignado
        addObjectSession( ( 100 - percentage ), "percentageSelected" );
        visibleButtons = true;
        showViewDetail = true;
        if ( ( selectedObject.getStatus().getId() == ConstantWEB.STATUS_COMPLETED_VAL )
             || selectedObject.getStatus().getId() == ConstantWEB.STATUS_CANCELED_VAL ) {
          showMenu = true;
          addObjectSession( true, "showMenu" );
        } else {
          cleanObjectSession( "showMenu" );
        }
        addObjectSession( selectedObject.getPercentage(), "percentageCurrent" );
        addObjectSession( selectedObject.getCode(), "code" );
        approved = selectedObject.getApproved() == 1 ? true : false;
        addObjectSession( approved, "approved" );
        
        //validar si tiene archivos adjuntos, consultarlos y traerlos
        if ( selectedObject.getId() > 0 ) {
          response = generalDelegate.findAttachmentFilesByTaskId( selectedObject.getId() );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            //Tiene archivos la tarea 
            listAttachFiles = ( ( List< AttachDTO > ) response.getObjectResponse() );
          } else {
            listAttachFiles = new ArrayList< AttachDTO >();
          }
          addObjectSession( listAttachFiles, "listAttachFiles" );
        }
        
      }
      
      addObjectSession( selectedObject.getStatus().getId(), "status" );
      addObjectSession( selectedObject, "selectedObject" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  public void onRowSelectAttachment ( SelectEvent event ) {
    ResponseDTO response = null;
    try {
      attachDTOSelected = ( ( AttachDTO ) event.getObject() );
      addObjectSession( attachDTOSelected, "attachDTOSelected" );
      indexTab = 1;
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  /**
   * </p> On row select person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void onRowSelectPerson ( SelectEvent event ) {
    
    RequestContext context = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObjectPerson = ( ( PersonGroupDTO ) event.getObject() );
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      addObjectSession( selectedObjectPerson, "selectedObjectPerson" );
      context.execute( "PF('appletAssignDialogVar').hide();" );
      showViewDetail = true;
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  public void generateReportViewTask () {
    ResponseDTO response = null;
    ByteArrayInputStream byteArrayInputStream = null;
    try {
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      response = generalDelegate.generateReportViewTask( selectedObject.getId(), getPatterDate() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        byteArrayInputStream = new ByteArrayInputStream( ( byte[] ) response.getObjectResponse() );
        fileTemplate = new DefaultStreamedContent( byteArrayInputStream, "application/pdf", ConstantWEB.NAME_FILE_PDG_REPORT );
      } else {
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_ERROR_GEN_REPORT_TASK );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  /**
   * </p> On row unselect. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void onRowUnselect ( UnselectEvent event ) {
    FacesMessage msg = new FacesMessage( "tarea deseleccionada", ( ( TaskDTO ) event.getObject() ).getCode() );
    FacesContext.getCurrentInstance().addMessage( null, msg );
  }
  
  /**
   * </p> Gets the list register. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< TaskDTO > getListRegister () {
    listRegister = ( List< TaskDTO > ) ( getObjectSession( "listRegister" ) != null ? getObjectSession( "listRegister" ) : null );
    return listRegister;
  }
  
  /**
   * </p> Sets the list register. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listRegister the list register
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListRegister ( List< TaskDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  /**
   * </p> Gets the add object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el task dto
   * @since CetusControlWEB (2/02/2016)
   */
  public TaskDTO getAddObject () {
    return addObject;
  }
  
  /**
   * </p> Sets the add object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param addObject the adds the object
   * @since CetusControlWEB (2/02/2016)
   */
  public void setAddObject ( TaskDTO addObject ) {
    this.addObject = addObject;
  }
  
  /**
   * </p> Gets the selected object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el task dto
   * @since CetusControlWEB (2/02/2016)
   */
  public TaskDTO getSelectedObject () {
    return selectedObject;
  }
  
  /**
   * </p> Sets the selected object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param selectedObject the selected object
   * @since CetusControlWEB (2/02/2016)
   */
  public void setSelectedObject ( TaskDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  /**
   * </p> Checks if is show confirm add. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  /**
   * </p> Sets the show confirm add. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showConfirmAdd the show confirm add
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  /**
   * </p> Checks if is show confirm mod. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  /**
   * </p> Sets the show confirm mod. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showConfirmMod the show confirm mod
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  /**
   * </p> Checks if is show dialog confirm update. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  /**
   * </p> Sets the show dialog confirm update. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showDialogConfirmUpdate the show dialog confirm update
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  /**
   * </p> Checks if is show confirm delete. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  /**
   * </p> Sets the show confirm delete. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showConfirmDelete the show confirm delete
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
  }
  
  /**
   * </p> Checks if is show alert select row. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  /**
   * </p> Sets the show alert select row. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showAlertSelectRow the show alert select row
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  /**
   * </p> Checks if is show view detail. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  /**
   * </p> Sets the show view detail. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showViewDetail the show view detail
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  /**
   * </p> Gets the user portal dto. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el user portal dto
   * @since CetusControlWEB (2/02/2016)
   */
  public UserPortalDTO getUserPortalDTO () {
    return userPortalDTO;
  }
  
  /**
   * </p> Sets the user portal dto. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param userPortalDTO the user portal dto
   * @since CetusControlWEB (2/02/2016)
   */
  public void setUserPortalDTO ( UserPortalDTO userPortalDTO ) {
    this.userPortalDTO = userPortalDTO;
  }
  
  /**
   * </p> Gets the list area item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  public List< SelectItem > getListAreaItem () {
    //listAreaItem = ( List< SelectItem > ) ( getObjectSession( "listAreaItem" ) != null ? getObjectSession( "listAreaItem" ) : null );
    return listAreaItem;
  }
  
  /**
   * </p> Sets the list area item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listAreaItem the list area item
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListAreaItem ( List< SelectItem > listAreaItem ) {
    this.listAreaItem = listAreaItem;
  }
  
  /**
   * </p> Gets the list priority item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListPriorityItem () {
    listPriorityItem = ( List< SelectItem > ) ( getObjectSession( "listPriorityItem" ) != null ? getObjectSession( "listPriorityItem" ) : null );
    return listPriorityItem;
  }
  
  /**
   * </p> Sets the list priority item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listPriorityItem the list priority item
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListPriorityItem ( List< SelectItem > listPriorityItem ) {
    this.listPriorityItem = listPriorityItem;
  }
  
  /**
   * </p> Gets the list task type item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListTaskTypeItem () {
    listTaskTypeItem = ( List< SelectItem > ) ( getObjectSession( "listTaskTypeItem" ) != null ? getObjectSession( "listTaskTypeItem" ) : null );
    return listTaskTypeItem;
  }
  
  /**
   * </p> Sets the list task type item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listTaskTypeItem the list task type item
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListTaskTypeItem ( List< SelectItem > listTaskTypeItem ) {
    this.listTaskTypeItem = listTaskTypeItem;
  }
  
  /**
   * </p> Gets the list status item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListStatusItem () {
    listStatusItem = ( List< SelectItem > ) ( getObjectSession( "listStatusItem" ) != null ? getObjectSession( "listStatusItem" ) : null );
    return listStatusItem;
  }
  
  /**
   * </p> Sets the list status item. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listStatusItem the list status item
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListStatusItem ( List< SelectItem > listStatusItem ) {
    this.listStatusItem = listStatusItem;
  }
  
  /**
   * </p> Gets the serialversionuid. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el long
   * @since CetusControlWEB (2/02/2016)
   */
  public static long getSerialversionuid () {
    return serialVersionUID;
  }
  
  /**
   * </p> Checks if is approved. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isApproved () {
    approved = ( Boolean ) ( getObjectSession( "approved" ) != null ? getObjectSession( "approved" ) : false );
    return approved;
  }
  
  /**
   * </p> Sets the approved. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param approved the approved
   * @since CetusControlWEB (2/02/2016)
   */
  public void setApproved ( boolean approved ) {
    this.approved = approved;
  }
  
  /**
   * </p> Gets the list register group. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  public List< PersonGroupDTO > getListRegisterGroup () {
    return listRegisterGroup;
  }
  
  /**
   * </p> Sets the list register group. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listRegisterGroup the list register group
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListRegisterGroup ( List< PersonGroupDTO > listRegisterGroup ) {
    this.listRegisterGroup = listRegisterGroup;
  }
  
  /**
   * </p> Gets the group tdto selected. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el person group dto
   * @since CetusControlWEB (2/02/2016)
   */
  public PersonGroupDTO getGroupTDTOSelected () {
    return groupTDTOSelected;
  }
  
  /**
   * </p> Sets the group tdto selected. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param groupTDTOSelected the group tdto selected
   * @since CetusControlWEB (2/02/2016)
   */
  public void setGroupTDTOSelected ( PersonGroupDTO groupTDTOSelected ) {
    this.groupTDTOSelected = groupTDTOSelected;
  }
  
  /**
   * </p> Checks if is show add dialog. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowAddDialog () {
    return showAddDialog;
  }
  
  /**
   * </p> Sets the show add dialog. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showAddDialog the show add dialog
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowAddDialog ( boolean showAddDialog ) {
    this.showAddDialog = showAddDialog;
  }
  
  /**
   * </p> Checks if is visible buttons. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isVisibleButtons () {
    return visibleButtons;
  }
  
  /**
   * </p> Sets the visible buttons. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param visibleButtons the visible buttons
   * @since CetusControlWEB (2/02/2016)
   */
  public void setVisibleButtons ( boolean visibleButtons ) {
    this.visibleButtons = visibleButtons;
  }
  
  /**
   * </p> Gets the code. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (2/02/2016)
   */
  public String getCode () {
    code = ( String ) ( getObjectSession( "code" ) != null ? getObjectSession( "code" ) : null );
    return code;
  }
  
  /**
   * </p> Sets the code. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param code the code
   * @since CetusControlWEB (2/02/2016)
   */
  public void setCode ( String code ) {
    this.code = code;
  }
  
  /**
   * </p> Gets the note task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlWEB (2/02/2016)
   */
  public String getNoteTask () {
    return noteTask;
  }
  
  /**
   * </p> Sets the note task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param noteTask the note task
   * @since CetusControlWEB (2/02/2016)
   */
  public void setNoteTask ( String noteTask ) {
    this.noteTask = noteTask;
  }
  
  /**
   * </p> Gets the status. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el int
   * @since CetusControlWEB (2/02/2016)
   */
  public int getStatus () {
    status = getObjectSession( "status" ) != null ? ( int ) getObjectSession( "status" ) : 0;
    return status;
  }
  
  /**
   * </p> Sets the status. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param status the status
   * @since CetusControlWEB (2/02/2016)
   */
  public void setStatus ( int status ) {
    this.status = status;
  }
  
  /**
   * </p> Gets the percentage. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el double
   * @since CetusControlWEB (2/02/2016)
   */
  public double getPercentage () {
    return percentage;
  }
  
  /**
   * </p> Sets the percentage. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param percentage the percentage
   * @since CetusControlWEB (2/02/2016)
   */
  public void setPercentage ( double percentage ) {
    this.percentage = percentage;
  }
  
  /**
   * </p> Gets the percentage selected. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el double
   * @since CetusControlWEB (2/02/2016)
   */
  public double getPercentageSelected () {
    percentageSelected = getObjectSession( "percentageSelected" ) != null ? ( double ) getObjectSession( "percentageSelected" ) : 0;
    return percentageSelected;
  }
  
  /**
   * </p> Sets the percentage selected. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param percentageSelected the percentage selected
   * @since CetusControlWEB (2/02/2016)
   */
  public void setPercentageSelected ( double percentageSelected ) {
    this.percentageSelected = percentageSelected;
  }
  
  /**
   * </p> Gets the delivery date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el date
   * @since CetusControlWEB (2/02/2016)
   */
  public Date getDeliveryDate () {
    return deliveryDate;
  }
  
  /**
   * </p> Sets the delivery date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param deliveryDate the delivery date
   * @since CetusControlWEB (2/02/2016)
   */
  public void setDeliveryDate ( Date deliveryDate ) {
    this.deliveryDate = deliveryDate;
  }
  
  /**
   * </p> Sets the percentage current. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param percentageCurrent the percentage current
   * @since CetusControlWEB (2/02/2016)
   */
  public void setPercentageCurrent ( double percentageCurrent ) {
    this.percentageCurrent = percentageCurrent;
  }
  
  /**
   * </p> Gets the percentage current. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el double
   * @since CetusControlWEB (2/02/2016)
   */
  public double getPercentageCurrent () {
    percentageCurrent = getObjectSession( "percentageCurrent" ) != null ? ( double ) getObjectSession( "percentageCurrent" ) : 0;
    return percentageCurrent;
  }
  
  /**
   * </p> Gets the selected object person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el person group dto
   * @since CetusControlWEB (2/02/2016)
   */
  public PersonGroupDTO getSelectedObjectPerson () {
    selectedObjectPerson = ( PersonGroupDTO ) ( getObjectSession( "selectedObjectPerson" ) != null
                                                                                                   ? getObjectSession( "selectedObjectPerson" )
                                                                                                   : null );
    return selectedObjectPerson;
  }
  
  /**
   * </p> Sets the selected object person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param selectedObjectPerson the selected object person
   * @since CetusControlWEB (2/02/2016)
   */
  public void setSelectedObjectPerson ( PersonGroupDTO selectedObjectPerson ) {
    this.selectedObjectPerson = selectedObjectPerson;
  }
  
  /**
   * </p> Gets the list person g. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< PersonGroupDTO > getListPersonG () {
    listPersonG = ( List< PersonGroupDTO > ) ( getObjectSession( "listPersonG" ) != null ? getObjectSession( "listPersonG" ) : null );
    return listPersonG;
  }
  
  /**
   * </p> Sets the list person g. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listPersonG the list person g
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListPersonG ( List< PersonGroupDTO > listPersonG ) {
    this.listPersonG = listPersonG;
  }
  
  /**
   * </p> Checks if is show menu. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean isShowMenu () {
    showMenu = getObjectSession( "showMenu" ) != null ? ( boolean ) getObjectSession( "showMenu" ) : false;
    return showMenu;
  }
  
  /**
   * </p> Sets the show menu. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showMenu the show menu
   * @since CetusControlWEB (2/02/2016)
   */
  public void setShowMenu ( boolean showMenu ) {
    this.showMenu = showMenu;
  }
  
  /**
   * </p> Gets the list files task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el array list
   * @since CetusControlWEB (2/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public ArrayList< UploadedFile > getListFilesTask () {
    listFilesTask = ( ArrayList< UploadedFile > ) ( getObjectSession( "listFilesTask" ) != null ? getObjectSession( "listFilesTask" ) : null );
    return listFilesTask;
  }
  
  /**
   * </p> Sets the list files task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listFilesTask the list files task
   * @since CetusControlWEB (2/02/2016)
   */
  public void setListFilesTask ( ArrayList< UploadedFile > listFilesTask ) {
    this.listFilesTask = listFilesTask;
  }
  
  public List< AttachDTO > getListAttachFiles () {
    listAttachFiles = ( List< AttachDTO > ) ( getObjectSession( "listAttachFiles" ) != null ? getObjectSession( "listAttachFiles" ) : null );
    return listAttachFiles;
  }
  
  public void setListAttachFiles ( List< AttachDTO > listAttachFiles ) {
    this.listAttachFiles = listAttachFiles;
  }
  
  private DefaultStreamedContent download;
  
  public void setDownload ( DefaultStreamedContent download ) {
    this.download = download;
  }
  
  public DefaultStreamedContent getDownload () throws Exception {
    return download;
  }
  
  public void prepDownload ( AttachDTO in ) throws Exception {
    File file = new File( in.getPath() + separador + in.getFileName() );
    InputStream input = new FileInputStream( file );
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    setDownload( new DefaultStreamedContent( input, externalContext.getMimeType( file.getName() ), file.getName() ) );
  }
  
  public String getSeparador () {
    return separador;
  }
  
  public void setSeparador ( String separador ) {
    this.separador = separador;
  }
  
  public AttachDTO getAttachDTOSelected () {
    return attachDTOSelected;
  }
  
  public void setAttachDTOSelected ( AttachDTO attachDTOSelected ) {
    this.attachDTOSelected = attachDTOSelected;
  }
  
  public int getIndexTab () {
    return indexTab;
  }
  
  public void setIndexTab ( int indexTab ) {
    this.indexTab = indexTab;
  }
  
  public boolean isShowConfirmCancel () {
    return showConfirmCancel;
  }
  
  public void setShowConfirmCancel ( boolean showConfirmCancel ) {
    this.showConfirmCancel = showConfirmCancel;
  }
  
  public boolean isShowConfirmCompleted () {
    return showConfirmCompleted;
  }
  
  public void setShowConfirmCompleted ( boolean showConfirmCompleted ) {
    this.showConfirmCompleted = showConfirmCompleted;
  }
  
  public boolean isShowConfirmSuspended () {
    return showConfirmSuspended;
  }
  
  public void setShowConfirmSuspended ( boolean showConfirmSuspended ) {
    this.showConfirmSuspended = showConfirmSuspended;
  }
  
  public StreamedContent getFileTemplate () {
    return fileTemplate;
  }
  
  public void setFileTemplate ( StreamedContent fileTemplate ) {
    this.fileTemplate = fileTemplate;
  }
  
}
