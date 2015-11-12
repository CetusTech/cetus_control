package co.com.cetus.cetuscontrol.web.bean;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.dto.PersonGroupDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.TraceTaskDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.mail.SendMail;
import co.com.cetus.common.util.UtilCommon;

@ManagedBean
@RequestScoped
public class ManualTaskMBean extends GeneralManagedBean {
  
  private static final long      serialVersionUID        = -3255236201554716426L;
  
  private UserPortalDTO          userPortalDTO           = null;
  private List< TaskDTO >        listRegister            = null;
  private List< PersonGroupDTO > listRegisterGroup       = null;
  private List< AreaDTO >        listArea                = null;
  private List< PriorityDTO >    listPriority            = null;
  private List< TaskTypeDTO >    listTaskType            = null;
  private List< PersonDTO >      listPerson              = null;
  private List< PersonGroupDTO > listPersonG             = null;
  private List< StatusDTO >      listStatus              = null;
  private List< SelectItem >     listAreaItem            = null;
  private List< SelectItem >     listPriorityItem        = null;
  private List< SelectItem >     listTaskTypeItem        = null;
  private List< SelectItem >     listStatusItem          = null;
  private TaskDTO                addObject               = null;
  private TaskDTO                selectedObject          = null;
  private PersonGroupDTO         selectedObjectPerson    = null;
  private boolean                showConfirmAdd          = false;
  private boolean                showConfirmMod          = false;
  private boolean                showDialogConfirmUpdate = false;
  private boolean                showConfirmDelete       = false;
  private boolean                showAlertSelectRow      = false;
  private boolean                showViewDetail          = false;
  private boolean                showAddDialog           = false;
  private boolean                showMenu                = false;
  private boolean                approved                = false;
  private PersonGroupDTO         groupTDTOSelected       = null;
  private boolean                visibleButtons          = false;
  private String                 code                    = null;
  private SendMail               sendMail                = null;
  private String                 noteTask                = null;
  private int                   status;
  private double                 percentage;
  private double                 percentageSelected;
  private double                 percentageCurrent;
  private Date                   deliveryDate            = null;
  
  public ManualTaskMBean () {
    super();
    addObject = new TaskDTO();
    addObject.setArea( new AreaDTO() );
    addObject.setPriority( new PriorityDTO() );
    addObject.setStatus( new StatusDTO() );
    addObject.setTaskType( new TaskTypeDTO() );
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
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  private void getPercentageNow () {
    percentage = generalDelegate.getPercentageCurrent( userPortalDTO.getPerson().getId() );
  }
  
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
            
            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
              sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
                                       replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
              sendMail.start();
            }
            
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
  
  @SuppressWarnings ( "unchecked" )
  public void completeTask () {
    RequestContext context = null;
    ResponseDTO response = null;
    String duration = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
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
            
            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
              if ( resParamSubjectTaskAdd != null && resParamHtmlMsgTaskAdd != null ) {
                sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
                                         replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
                
                sendMail.start();
              }
            }
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
  
  @SuppressWarnings ( "unchecked" )
  public void cancelTask () {
    RequestContext context = null;
    //Proceso para dar inicio a la tarea
    ResponseDTO response = null;
    String duration = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null && noteTask != null & !noteTask.isEmpty() ) {
        
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
            String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
            String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
            String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
            String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
            String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
            String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
            String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( "HTML_EMAIL_TASK_CANCEL" );
            String resParamSubjectTaskAdd = generalDelegate.getValueParameter( "SUBJECT_TASK_CANCEL" );
            
            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
              if ( resParamSubjectTaskAdd != null && resParamHtmlMsgTaskAdd != null ) {
                sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
                                         replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
                
                sendMail.start();
              }
            }
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
  
  @SuppressWarnings ( "unchecked" )
  public void suspendTask () {
    RequestContext context = null;
    //Proceso para dar inicio a la tarea
    ResponseDTO response = null;
    String duration = null;
    try {
      context = RequestContext.getCurrentInstance();
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
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
            
            //Enviar EMAIL temporalmente          
            String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
            String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
            String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
            String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
            String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
            String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
            String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( "HTML_EMAIL_TASK_SUPENDED" );
            String resParamSubjectTaskAdd = generalDelegate.getValueParameter( "SUBJECT_TASK_SUSPENDED" );
            
            if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
              sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
                                       replaceContentEmail( resParamHtmlMsgTaskAdd, selectedObject ), to, null );
              sendMail.start();
            }
            
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
  
  public void onTimeout () {
    addMessageInfo( null, "Nota:", "la tarea se ha vencido!" );
  }
  
  public void cleanVar () {
    //cleanObjectSession( "percentageSelected" );
    selectedObject.setPercentage( ( ( double ) getObjectSession( "percentageCurrent" ) ) );
  }
  
  public void closeDialogView () {
    selectedObject = new TaskDTO();
    selectedObject.setArea( new AreaDTO() );
    selectedObject.setPriority( new PriorityDTO() );
    selectedObject.setStatus( new StatusDTO() );
    selectedObject.setTaskType( new TaskTypeDTO() );
  }
  
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
  
  @SuppressWarnings ( "unchecked" )
  private void listStatus ( int idClientCetus ) {
    ResponseDTO response = null;
    List< SelectItem > copy = null;
    try {
      response = generalDelegate.findStatus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listStatus = ( List< StatusDTO > ) response.getObjectResponse();
        listStatusItem = new ArrayList< SelectItem >();
        for ( StatusDTO objDTO: listStatus ) {
          if ( objDTO.getId() != ConstantWEB.STATUS_ASSIGNED && objDTO.getId() != ConstantWEB.STATUS_COMPLETED
               && objDTO.getId() != ConstantWEB.STATUS_INPROGRESS && objDTO.getId() != ConstantWEB.STATUS_CANCELED
               && objDTO.getId() != ConstantWEB.STATUS_SUSPENDED ) {
            listStatusItem.add( new SelectItem( objDTO.getId(), objDTO.getDescription() ) );
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
          
          //LIstar Tareas
          responseDTO = generalDelegate.findTaskByPersonGroup( selectedObject.getPersonGroup().getGroupT().getId(), userPortalDTO.getPerson().getId() );
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
    } catch ( Exception e ) {
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
    try {
      this.showConfirmAdd = false;
      addObject = ( TaskDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        groupTDTOSelected = ( PersonGroupDTO ) getObjectSession( "groupTDTOSelected" );
        
        responseDTO = generalDelegate.generateCodeTask();
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
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "TAREA CREADA EXITOSAMENTE" );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, MessageFormat.format( ConstantWEB.MESSAGE_TASK_CREATED, addObject.getCode() ) );
          //Enviar EMAIL temporalmente          
          String to[] = { getUserDTO().getPerson().getEmail() };// Se debe validar donde se estan almacenando los correos asociados al usuario que esta logueado
          String resParamHost = generalDelegate.getValueParameter( "SMTP_HOST" );
          String resParamPort = generalDelegate.getValueParameter( "SMPT_PORT" );
          String resParamFrom = generalDelegate.getValueParameter( "SMTP_FROM" );
          String resParamPass = generalDelegate.getValueParameter( "SMTP_PASS" );
          String resParamUserName = generalDelegate.getValueParameter( "SMTP_USERNAME" );
          String resParamHtmlMsgTaskAdd = generalDelegate.getValueParameter( "HTML_EMAIL_TASK_NEW" );
          String resParamSubjectTaskAdd = generalDelegate.getValueParameter( "SUBJECT_TASK_NEW" );
          
          if ( resParamHost != null && resParamFrom != null && resParamPass != null && resParamPort != null && resParamUserName != null ) {
            sendMail = new SendMail( resParamUserName, resParamHost, resParamPort, resParamFrom, resParamPass, resParamSubjectTaskAdd,
                                     replaceContentEmail( resParamHtmlMsgTaskAdd, addObject ), to, null );
            sendMail.start();
          }
          responseDTO = generalDelegate.findTaskByPersonGroup( groupTDTOSelected.getGroupT().getId(), userPortalDTO.getPerson().getId() );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            //Tiene Usuario portal la persona seleecionada 
            listRegister = ( ( List< TaskDTO > ) responseDTO.getObjectResponse() );
          } else {
            listRegister = new ArrayList< TaskDTO >();
          }
          addObjectSession( listRegister, "listRegister" );
          
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
  
  public void onRowSelectTask ( SelectEvent event ) {
    
    try {
      selectedObject = ( ( TaskDTO ) event.getObject() );
      if ( selectedObject != null ) {
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
        
      }
      
      addObjectSession( selectedObject.getStatus().getId(), "status" );
      addObjectSession( selectedObject, "selectedObject" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
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
  
  public void onRowUnselect ( UnselectEvent event ) {
    FacesMessage msg = new FacesMessage( "tarea deseleccionada", ( ( TaskDTO ) event.getObject() ).getCode() );
    FacesContext.getCurrentInstance().addMessage( null, msg );
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< TaskDTO > getListRegister () {
    listRegister = ( List< TaskDTO > ) ( getObjectSession( "listRegister" ) != null ? getObjectSession( "listRegister" ) : null );
    return listRegister;
  }
  
  public void setListRegister ( List< TaskDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public TaskDTO getAddObject () {
    return addObject;
  }
  
  public void setAddObject ( TaskDTO addObject ) {
    this.addObject = addObject;
  }
  
  public TaskDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( TaskDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
  }
  
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public UserPortalDTO getUserPortalDTO () {
    return userPortalDTO;
  }
  
  public void setUserPortalDTO ( UserPortalDTO userPortalDTO ) {
    this.userPortalDTO = userPortalDTO;
  }
  
  public List< SelectItem > getListAreaItem () {
    //listAreaItem = ( List< SelectItem > ) ( getObjectSession( "listAreaItem" ) != null ? getObjectSession( "listAreaItem" ) : null );
    return listAreaItem;
  }
  
  public void setListAreaItem ( List< SelectItem > listAreaItem ) {
    this.listAreaItem = listAreaItem;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListPriorityItem () {
    listPriorityItem = ( List< SelectItem > ) ( getObjectSession( "listPriorityItem" ) != null ? getObjectSession( "listPriorityItem" ) : null );
    return listPriorityItem;
  }
  
  public void setListPriorityItem ( List< SelectItem > listPriorityItem ) {
    this.listPriorityItem = listPriorityItem;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListTaskTypeItem () {
    listTaskTypeItem = ( List< SelectItem > ) ( getObjectSession( "listTaskTypeItem" ) != null ? getObjectSession( "listTaskTypeItem" ) : null );
    return listTaskTypeItem;
  }
  
  public void setListTaskTypeItem ( List< SelectItem > listTaskTypeItem ) {
    this.listTaskTypeItem = listTaskTypeItem;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListStatusItem () {
    listStatusItem = ( List< SelectItem > ) ( getObjectSession( "listStatusItem" ) != null ? getObjectSession( "listStatusItem" ) : null );
    return listStatusItem;
  }
  
  public void setListStatusItem ( List< SelectItem > listStatusItem ) {
    this.listStatusItem = listStatusItem;
  }
  
  public static long getSerialversionuid () {
    return serialVersionUID;
  }
  
  public boolean isApproved () {
    approved = ( Boolean ) ( getObjectSession( "approved" ) != null ? getObjectSession( "approved" ) : false );
    return approved;
  }
  
  public void setApproved ( boolean approved ) {
    this.approved = approved;
  }
  
  public List< PersonGroupDTO > getListRegisterGroup () {
    return listRegisterGroup;
  }
  
  public void setListRegisterGroup ( List< PersonGroupDTO > listRegisterGroup ) {
    this.listRegisterGroup = listRegisterGroup;
  }
  
  public PersonGroupDTO getGroupTDTOSelected () {
    return groupTDTOSelected;
  }
  
  public void setGroupTDTOSelected ( PersonGroupDTO groupTDTOSelected ) {
    this.groupTDTOSelected = groupTDTOSelected;
  }
  
  public boolean isShowAddDialog () {
    return showAddDialog;
  }
  
  public void setShowAddDialog ( boolean showAddDialog ) {
    this.showAddDialog = showAddDialog;
  }
  
  public boolean isVisibleButtons () {
    return visibleButtons;
  }
  
  public void setVisibleButtons ( boolean visibleButtons ) {
    this.visibleButtons = visibleButtons;
  }
  
  public String getCode () {
    code = ( String ) ( getObjectSession( "code" ) != null ? getObjectSession( "code" ) : null );
    return code;
  }
  
  public void setCode ( String code ) {
    this.code = code;
  }
  
  public String getNoteTask () {
    return noteTask;
  }
  
  public void setNoteTask ( String noteTask ) {
    this.noteTask = noteTask;
  }
  
  public int getStatus () {
    status = getObjectSession( "status" ) != null ? ( int ) getObjectSession( "status" ) : 0;
    return status;
  }
  
  public void setStatus ( int status ) {
    this.status = status;
  }
  
  public double getPercentage () {
    return percentage;
  }
  
  public void setPercentage ( double percentage ) {
    this.percentage = percentage;
  }
  
  public double getPercentageSelected () {
    percentageSelected = getObjectSession( "percentageSelected" ) != null ? ( double ) getObjectSession( "percentageSelected" ) : 0;
    return percentageSelected;
  }
  
  public void setPercentageSelected ( double percentageSelected ) {
    this.percentageSelected = percentageSelected;
  }
  
  public Date getDeliveryDate () {
    return deliveryDate;
  }
  
  public void setDeliveryDate ( Date deliveryDate ) {
    this.deliveryDate = deliveryDate;
  }
  
  public void setPercentageCurrent ( double percentageCurrent ) {
    this.percentageCurrent = percentageCurrent;
  }
  
  public double getPercentageCurrent () {
    percentageCurrent = getObjectSession( "percentageCurrent" ) != null ? ( double ) getObjectSession( "percentageCurrent" ) : 0;
    return percentageCurrent;
  }
  
  public PersonGroupDTO getSelectedObjectPerson () {
    selectedObjectPerson = ( PersonGroupDTO ) ( getObjectSession( "selectedObjectPerson" ) != null
                                                                                                  ? getObjectSession( "selectedObjectPerson" )
                                                                                                  : null );
    return selectedObjectPerson;
  }
  
  public void setSelectedObjectPerson ( PersonGroupDTO selectedObjectPerson ) {
    this.selectedObjectPerson = selectedObjectPerson;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< PersonGroupDTO > getListPersonG () {
    listPersonG = ( List< PersonGroupDTO > ) ( getObjectSession( "listPersonG" ) != null ? getObjectSession( "listPersonG" ) : null );
    return listPersonG;
  }
  
  public void setListPersonG ( List< PersonGroupDTO > listPersonG ) {
    this.listPersonG = listPersonG;
  }
  
  public boolean isShowMenu () {
    showMenu = getObjectSession( "showMenu" ) != null ? ( boolean ) getObjectSession( "showMenu" ) : false;
    return showMenu;
  }
  
  public void setShowMenu ( boolean showMenu ) {
    this.showMenu = showMenu;
  }
  
}
