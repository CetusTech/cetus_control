package co.com.cetus.cetuscontrol.ejb.process;

import static co.com.cetus.common.util.UtilCommon.createMessageFAILURE;
import static co.com.cetus.common.util.UtilCommon.createMessageNORESULT;
import static co.com.cetus.common.util.UtilCommon.createMessageSUCCESS;
import static co.com.cetus.common.util.UtilCommon.createMessageWRONG_PARAMETERS;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.AreaTypeTaskDTO;
import co.com.cetus.cetuscontrol.dto.AttachDTO;
import co.com.cetus.cetuscontrol.dto.ClientDTO;
import co.com.cetus.cetuscontrol.dto.CommentTaskDTO;
import co.com.cetus.cetuscontrol.dto.ExceptionWorkdayDTO;
import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.GroupTypeDTO;
import co.com.cetus.cetuscontrol.dto.NotificationGeneralDTO;
import co.com.cetus.cetuscontrol.dto.NotificationSettingDTO;
import co.com.cetus.cetuscontrol.dto.ParameterGeneralDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.dto.PersonGroupDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskHistoryDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.TraceTaskDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.dto.WorkdayDTO;
import co.com.cetus.cetuscontrol.ejb.delegate.CetusMessageServiceDelegate;
import co.com.cetus.cetuscontrol.ejb.delegate.CetusVortalDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.ejb.util.EstatusUser;
import co.com.cetus.cetuscontrol.ejb.validator.CetusControlEJBValidator;
import co.com.cetus.cetuscontrol.jpa.entity.Area;
import co.com.cetus.cetuscontrol.jpa.entity.AreaTypeTask;
import co.com.cetus.cetuscontrol.jpa.entity.Attach;
import co.com.cetus.cetuscontrol.jpa.entity.Client;
import co.com.cetus.cetuscontrol.jpa.entity.CommentTask;
import co.com.cetus.cetuscontrol.jpa.entity.ExceptionWorkday;
import co.com.cetus.cetuscontrol.jpa.entity.GroupT;
import co.com.cetus.cetuscontrol.jpa.entity.GroupType;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationGeneral;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationSetting;
import co.com.cetus.cetuscontrol.jpa.entity.ParameterGeneral;
import co.com.cetus.cetuscontrol.jpa.entity.Person;
import co.com.cetus.cetuscontrol.jpa.entity.PersonGroup;
import co.com.cetus.cetuscontrol.jpa.entity.Priority;
import co.com.cetus.cetuscontrol.jpa.entity.Status;
import co.com.cetus.cetuscontrol.jpa.entity.Task;
import co.com.cetus.cetuscontrol.jpa.entity.TaskHistory;
import co.com.cetus.cetuscontrol.jpa.entity.TaskType;
import co.com.cetus.cetuscontrol.jpa.entity.TraceTask;
import co.com.cetus.cetuscontrol.jpa.entity.UserPortal;
import co.com.cetus.cetuscontrol.jpa.entity.Workday;
import co.com.cetus.common.dto.ParameterDTO;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.exception.ProcessException;
import co.com.cetus.common.exception.ValidatorException;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.Converter;
import co.com.cetus.common.util.EFilterSearch;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;
import co.com.cetus.portal.ejb.service.CreateUserRequestDTO;
import co.com.cetus.portal.ejb.service.CreateUserResponseDTO;
import co.com.cetus.portal.ejb.service.DeleteUserRequestDTO;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationRequestDTO;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationResponseDTO;
import co.com.cetus.portal.ejb.service.FindRolsByLoginRequestDTO;
import co.com.cetus.portal.ejb.service.FindRolsByLoginResponseDTO;
import co.com.cetus.portal.ejb.service.ResponseWSDTO;
import co.com.cetus.portal.ejb.service.UpdateUserRequestDTO;
import co.com.cetus.portal.ejb.service.UserDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * The Class CetusControlProcess.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
@Singleton
@DependsOn ( "GeneralProcess" )
@Lock ( LockType.READ )
public class CetusControlProcess {
  
  /** The general process. */
  @EJB
  private GeneralProcess               generalProcess;
                                       
  @EJB
  private CetusControlParameterProcess parameterProcess;
                                       
  /** The converter. */
  private Converter                    converter;
                                       
  private CetusVortalDelegate          delegateVortal;
                                       
  /** The em. */
  @PersistenceContext ( unitName = "CetusControlJPA" )
  private EntityManager                em;
                                       
  public CetusControlProcess () {
  }
  
  @PostConstruct
  public void init () {
    converter = new Converter( ConstantEJB.PCK_CLASS_DTO, ConstantEJB.PCK_CLASS_JPA );
  }
  
  /**
   * </p> Metodo para crear registros en una entidad mediante el Objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param <T> the generic type
   * @param pDTO the p dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  @SuppressWarnings ( "unchecked" )
  public < T > ResponseDTO create ( Object pDTO ) {
    ResponseDTO lResponseDTO = null;
    Class< ? > lEntity = null;
    T lEntityT = null;
    try {
      CetusControlEJBValidator.validateDTO( pDTO );
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "dto= " + pDTO.toString() );
      
      lEntity = Class.forName( ConstantEJB.PCK_CLASS_JPA
                               + pDTO.getClass().getSimpleName().substring( 0, pDTO.getClass().getSimpleName().indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      lEntityT = ( T ) lEntity.newInstance();
      
      converter.convertDtoToEntity( pDTO, lEntityT );
      
      lEntityT = generalProcess.create( lEntityT );
      if ( lEntityT != null ) {
        lResponseDTO = createMessageSUCCESS();
        converter.convertEntityToDto( lEntityT, pDTO, false );
        lResponseDTO.setObjectResponse( pDTO );
        return lResponseDTO;
      } else {
        return createMessageFAILURE();
      }
    } catch ( ValidatorException ve ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + ve.getMessage(), ve );
      lResponseDTO = createMessageWRONG_PARAMETERS();
      lResponseDTO.setMessage( ve.getMessage() );
      return lResponseDTO;
    } catch ( ProcessException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + e.getMessage(), e );
      lResponseDTO = createMessageFAILURE();
      lResponseDTO.setMessage( lResponseDTO.getMessage() + "-" + e.getMessage() );
      return lResponseDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + e.getMessage(), e );
      lResponseDTO = createMessageFAILURE();
      lResponseDTO.setMessage( lResponseDTO.getMessage() + "-" + e.getMessage() );
      return lResponseDTO;
    }
  }
  
  /**
   * </p> Metodo para actualizar la informacion de un registro en base de datos mediante el objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param <T> the generic type
   * @param pDTO the p dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  @SuppressWarnings ( "unchecked" )
  @Lock ( LockType.WRITE )
  public < T > ResponseDTO edit ( Object pDTO ) {
    ResponseDTO responseWSDTO = null;
    boolean response = false;
    Class< ? > entity = null;
    T entityT = null;
    try {
      CetusControlEJBValidator.validateDTO( pDTO );
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "dto= " + pDTO.toString() );
      
      entity = Class.forName( ConstantEJB.PCK_CLASS_JPA
                              + pDTO.getClass().getSimpleName().substring( 0, pDTO.getClass().getSimpleName().indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      entityT = ( T ) entity.newInstance();
      
      converter.convertDtoToEntity( pDTO, entityT );
      
      response = generalProcess.edit( entityT );
      if ( response ) {
        return createMessageSUCCESS();
      } else {
        return createMessageFAILURE();
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = createMessageWRONG_PARAMETERS();
      responseWSDTO.setMessage( ve.getMessage() );
      return responseWSDTO;
    } catch ( ConstraintViolationException sqlE ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + sqlE.getMessage(), sqlE );
      responseWSDTO = new ResponseDTO();
      responseWSDTO.setCode( String.valueOf( sqlE.getCause() ) );
      responseWSDTO.setMessage( responseWSDTO.getMessage() + "-" + sqlE.getMessage() );
      return responseWSDTO;
    } catch ( SQLException sqlE ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + sqlE.getMessage(), sqlE );
      responseWSDTO = new ResponseDTO();
      responseWSDTO.setCode( String.valueOf( sqlE.getErrorCode() ) );
      responseWSDTO.setMessage( responseWSDTO.getMessage() + "-" + sqlE.getMessage() );
      return responseWSDTO;
    } catch ( ProcessException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + e.getMessage(), e );
      responseWSDTO = createMessageFAILURE();
      responseWSDTO.setMessage( responseWSDTO.getMessage() + "-" + e.getMessage() );
      return responseWSDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + e.getMessage(), e );
      responseWSDTO = createMessageFAILURE();
      responseWSDTO.setMessage( responseWSDTO.getMessage() + "-" + e.getMessage() );
      return responseWSDTO;
    }
  }
  
  /**
   * </p> Metodo para eliminar la informacion de un registro en base de datos mediante el objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param <T> the generic type
   * @param dto the dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  @SuppressWarnings ( "unchecked" )
  @Lock ( LockType.WRITE )
  public < T > ResponseDTO remove ( Object dto ) {
    ResponseDTO responseWSDTO = null;
    boolean response = false;
    Class< ? > entity = null;
    T entityT = null;
    try {
      init();
      CetusControlEJBValidator.validateDTO( dto );
      entity = Class.forName( ConstantEJB.PCK_CLASS_JPA
                              + dto.getClass().getSimpleName().substring( 0, dto.getClass().getSimpleName().indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      entityT = ( T ) entity.newInstance();
      converter.convertDtoToEntity( dto, entityT );
      response = generalProcess.remove( entityT );
      if ( response ) {
        return createMessageSUCCESS();
      } else {
        return createMessageFAILURE();
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = createMessageWRONG_PARAMETERS();
      responseWSDTO.setMessage( ve.getMessage() );
      return responseWSDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error ::> " + e.getMessage(), e );
      responseWSDTO = createMessageFAILURE();
      responseWSDTO.setMessage( e.getMessage() );
      return responseWSDTO;
    }
  }
  
  /**
   * </p> Metodo para consultar todos los registros de una entidad y ordenarlos mediante los parametros enviados. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param classDTO the class dto
   * @param fieldOrder the field order
   * @param typeOrder the type order
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO findAll ( String classDTO, String fieldOrder, String typeOrder ) {
    ResponseDTO responseWSDTO = null;
    List< ? > listEntity = null;
    Class< ? > lEntity = null;
    Class< ? > lClassDTO = null;
    Object lDTO = null;
    List< Object > listDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "classDTO=" + classDTO + ", fieldOrder=" + fieldOrder + ", typeOrder=" + typeOrder );
      CetusControlEJBValidator.classDTOIsNullOrEmpty( classDTO );
      CetusControlEJBValidator.fieldOrderIsNullOrEmpty( fieldOrder );
      CetusControlEJBValidator.typeOrderIsNullOrEmpty( typeOrder );
      
      lClassDTO = Class.forName( ConstantEJB.PCK_CLASS_DTO + classDTO );
      lEntity = Class.forName( ConstantEJB.PCK_CLASS_JPA + classDTO.substring( 0, classDTO.indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      listEntity = generalProcess.findAll( lEntity, fieldOrder, typeOrder );
      
      if ( listEntity != null && !listEntity.isEmpty() ) {
        listDTO = new ArrayList< Object >();
        
        for ( Object entity: listEntity ) {
          lDTO = lClassDTO.newInstance();
          converter.convertEntityToDto( entity, lDTO, false );
          listDTO.add( lDTO );
        }
        responseWSDTO = createMessageSUCCESS();
        responseWSDTO.setObjectResponse( listDTO );
        return responseWSDTO;
      } else {
        return createMessageNORESULT();
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = createMessageWRONG_PARAMETERS();
      responseWSDTO.setMessage( ve.getMessage() );
      return responseWSDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para consultar todos los registros de una entidad. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param classDTO the class dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO findAll ( String classDTO ) {
    ResponseDTO responseWSDTO = null;
    List< ? > listEntity = null;
    Class< ? > lEntity = null;
    Object lDTO = null;
    Class< ? > lClassDTO = null;
    List< Object > listDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "classDTO=" + classDTO );
      CetusControlEJBValidator.classDTOIsNullOrEmpty( classDTO );
      lClassDTO = Class.forName( ConstantEJB.PCK_CLASS_DTO + classDTO );
      lEntity = Class.forName( ConstantEJB.PCK_CLASS_JPA + classDTO.substring( 0, classDTO.indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      listEntity = generalProcess.findAll( lEntity );
      
      if ( listEntity != null && !listEntity.isEmpty() ) {
        listDTO = new ArrayList< Object >();
        for ( Object entity: listEntity ) {
          lDTO = lClassDTO.newInstance();
          converter.convertEntityToDto( entity, lDTO, false );
          listDTO.add( lDTO );
        }
        responseWSDTO = createMessageSUCCESS();
        responseWSDTO.setObjectResponse( listDTO );
        return responseWSDTO;
      } else {
        return createMessageNORESULT();
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = createMessageWRONG_PARAMETERS();
      responseWSDTO.setMessage( ve.getMessage() );
      return responseWSDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para buscar un registro de una entidad mediante el id enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param classDTO the class dto
   * @param id the id
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO find ( String classDTO, Object id ) {
    ResponseDTO responseWSDTO = null;
    Object entity = null;
    Class< ? > lEntity = null;
    Object lDTO = null;
    Class< ? > lClassDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "classDTO=" + classDTO + ", id=" + id );
      CetusControlEJBValidator.classDTOIsNullOrEmpty( classDTO );
      CetusControlEJBValidator.idIsNull( id );
      lClassDTO = Class.forName( ConstantEJB.PCK_CLASS_DTO + classDTO );
      lEntity = Class.forName( ConstantEJB.PCK_CLASS_JPA + classDTO.substring( 0, classDTO.indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      entity = generalProcess.find( lEntity, id );
      if ( entity != null ) {
        lDTO = lClassDTO.newInstance();
        converter.convertEntityToDto( entity, lDTO, false );
        responseWSDTO = createMessageSUCCESS();
        responseWSDTO.setObjectResponse( lDTO );
        return responseWSDTO;
      } else {
        return createMessageNORESULT();
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = createMessageWRONG_PARAMETERS();
      responseWSDTO.setMessage( ve.getMessage() );
      return responseWSDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metoddo para consultar un rango de registros de una entidad. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param classDTO the class dto
   * @param arrRange the arr range
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO findRange ( String classDTO, int[] arrRange ) {
    ResponseDTO responseWSDTO = null;
    List< ? > listEntity = null;
    Class< ? > lEntity = null;
    Object lDTO = null;
    Class< ? > lClassDTO = null;
    List< Object > listDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "classDTO=" + classDTO + ", arrRange=" + arrRange );
      CetusControlEJBValidator.classDTOIsNullOrEmpty( classDTO );
      CetusControlEJBValidator.arrRangeIsNull( arrRange );
      lClassDTO = Class.forName( ConstantEJB.PCK_CLASS_DTO + classDTO );
      lEntity = Class.forName( ConstantEJB.PCK_CLASS_JPA + classDTO.substring( 0, classDTO.indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      listEntity = generalProcess.findRange( lEntity, arrRange );
      
      if ( listEntity != null && listEntity.size() > 0 ) {
        listDTO = new ArrayList< Object >();
        for ( Object entity: listEntity ) {
          lDTO = lClassDTO.newInstance();
          converter.convertEntityToDto( entity, lDTO, false );
          listDTO.add( lDTO );
        }
        responseWSDTO = createMessageSUCCESS();
        responseWSDTO.setObjectResponse( listDTO );
        return responseWSDTO;
      } else {
        return createMessageNORESULT();
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = createMessageWRONG_PARAMETERS();
      responseWSDTO.setMessage( ve.getMessage() );
      return responseWSDTO;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para consultar los registros de una entidad. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param classDTO the class dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO count ( String classDTO ) {
    ResponseDTO responseWSDTO = null;
    Integer listEntity = null;
    Class< ? > lEntity = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "classDTO=" + classDTO );
      CetusControlEJBValidator.classDTOIsNullOrEmpty( classDTO );
      lEntity = Class.forName( ConstantEJB.PCK_CLASS_JPA + classDTO.substring( 0, classDTO.indexOf( ConstantEJB.ACRONYM_DTO ) ) );
      listEntity = generalProcess.count( lEntity );
      
      if ( listEntity != null ) {
        responseWSDTO = createMessageSUCCESS();
        responseWSDTO.setObjectResponse( listEntity );
        return responseWSDTO;
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  // ****  PROCESOS SOLICITADOS POR EL CLIENTE CETUS CONTROL **** //
  
  /**
  * </p> Metodo para buscar el usuario que se encuentra logueado en la aplicacion </p>
  *
  * @author Jose David Salcedo M. - Cetus Technology
  * @param userCetus the user cetus
  * @return el response dto
  * @since CetusControlEJB (12/12/2014)
  */
  public ResponseDTO searchUserLogged ( String userCetus ) {
    ResponseDTO responseWSDTO = null;
    TypedQuery< UserPortal > query = null;
    UserPortal user = null;
    UserPortalDTO userDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "userCetus= " + userCetus );
      query = em.createNamedQuery( "UserPortal.searchUserLogged", UserPortal.class );
      query.setParameter( "userCetus", userCetus );
      query.setParameter( "status", EstatusUser.ACTIVE.getValueNum() );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        user = query.getSingleResult();
        userDTO = new UserPortalDTO();
        responseWSDTO = createMessageSUCCESS();
        converter.convertEntityToDto( user, userDTO, false );
        
        responseWSDTO.setObjectResponse( userDTO );
        
      } else {
        return createMessageNORESULT();
      }
      
    } catch ( NoResultException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE( e );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE( e );
    }
    
    return responseWSDTO;
  }
  
  /**
   * </p> Metodo para consultar el valor de un parametro. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameParameter the name parameter
   * @return el response
   * @since CetusControlEJB (18/02/2015)
   */
  public String getValueParameter ( String nameParameter ) {
    String response = null;
    List< ParameterDTO > listParameter = null;
    try {
      if ( ConstantEJB.parameter == null ) {
        listParameter = parameterProcess.loadParameter();
        ConstantEJB.loadParameter( listParameter );
      }
      response = ConstantEJB.getParameter( nameParameter );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    
    return response;
  }
  
  /**
   * </p> Find client by client cetus. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClientCetus the p id client cetus
   * @return el response dto
   * @since CetusControlEJB (2/03/2015)
   */
  public ResponseDTO findClientByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Client > list = null;
    List< ClientDTO > listDto = null;
    TypedQuery< Client > query = null;
    ClientDTO dto = null;
    try {
      query = em.createNamedQuery( "Client.findClientByClientCetus", Client.class );
      query.setParameter( "idClientCetus", pIdClientCetus );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< ClientDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new ClientDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find person by client cetus. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClientCetus the p id client cetus
   * @return el response dto
   * @since CetusControlEJB (4/03/2015)
   */
  public ResponseDTO findPersonByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Person > list = null;
    List< PersonDTO > listDto = null;
    TypedQuery< Person > query = null;
    PersonDTO dto = null;
    try {
      init();
      query = em.createNamedQuery( "Person.findPersonByClientCetus", Person.class );
      query.setParameter( "idClientCetus", pIdClientCetus );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< PersonDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new PersonDTO();
          converter.convertEntityToDto( entity, dto, true );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findRolByApplication ( int idApp ) {
    ResponseDTO responseDTO = null;
    FindRolsByApplicationResponseDTO findRolsByApplicationResponseDTO = null;
    FindRolsByApplicationRequestDTO request = null;
    try {
      delegateVortal = new CetusVortalDelegate();
      request = new FindRolsByApplicationRequestDTO();
      request.setIdApplication( idApp );
      request.setUser( ConstantEJB.USER_WS_CETUS );
      request.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
      findRolsByApplicationResponseDTO = delegateVortal.findRolByApplication( request );
      if ( findRolsByApplicationResponseDTO != null ) {
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( findRolsByApplicationResponseDTO );
      } else {
        responseDTO = UtilCommon.createMessageFAILURE();
        responseDTO.setMessage( "Error al buscar los roles asociados a la Aplicaci�n. Para m�s informaci�n comunicarse con el Administrador del Sistemas" );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findStatus ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    List< Status > list = null;
    TypedQuery< Status > query = null;
    StatusDTO dto = null;
    List< StatusDTO > listDto = null;
    try {
      query = em.createNamedQuery( "Status.findStatus", Status.class ).setParameter( "idclientCetus", idclientCetus );
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< StatusDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new StatusDTO();
          converter.convertEntityToDto( entity, dto, true );
          listDto.add( dto );
        }
      }
      
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskType ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    List< TaskType > list = null;
    TypedQuery< TaskType > query = null;
    TaskTypeDTO dto = null;
    List< TaskTypeDTO > listDto = null;
    try {
      listDto = new ArrayList< TaskTypeDTO >();
      query = em.createNamedQuery( "TaskType.findTaskType", TaskType.class ).setParameter( "idclientCetus", idclientCetus );
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< TaskTypeDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new TaskTypeDTO();
          converter.convertEntityToDto( entity, dto, true );
          listDto.add( dto );
        }
      }
      
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Metodo para obtener los parametros generales que posee un cliente cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the p id client cetus
   * @return el response dto
   * @since CetusControlEJB (5/03/2015)
   */
  public ResponseDTO findParameterGeneralByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    ParameterGeneral parameterGeneral = null;
    ParameterGeneralDTO parameterGeneralDTO = null;
    TypedQuery< ParameterGeneral > query = null;
    try {
      query = em.createNamedQuery( "ParameterGeneral.findParameterGeneralByClientCetus", ParameterGeneral.class );
      query.setParameter( "idClientCetus", idClientCetus );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        parameterGeneral = query.getSingleResult();
        parameterGeneralDTO = new ParameterGeneralDTO();
        responseDTO = createMessageSUCCESS();
        converter.convertEntityToDto( parameterGeneral, parameterGeneralDTO, false );
        
        responseDTO.setObjectResponse( parameterGeneralDTO );
      } else {
        return createMessageNORESULT();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Metodo para consultar todas las jornadas laborales de un cliente cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlEJB (12/03/2015)
   */
  public ResponseDTO findWorkDayByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Workday > listWorkday = null;
    List< WorkdayDTO > listworkdayDTO = null;
    TypedQuery< Workday > query = null;
    WorkdayDTO workdayDTO = null;
    try {
      query = em.createNamedQuery( "Workday.findWorkDayByClientCetus", Workday.class );
      query.setParameter( "idClientCetus", idClientCetus );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listworkdayDTO = new ArrayList< WorkdayDTO >();
        listWorkday = query.getResultList();
        for ( Workday workday: listWorkday ) {
          workdayDTO = new WorkdayDTO();
          converter.convertEntityToDto( workday, workdayDTO, false );
          listworkdayDTO.add( workdayDTO );
        }
        
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listworkdayDTO );
      } else {
        return createMessageNORESULT();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO createUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol ) {
    UserDTO usuarioDTO = null;
    CreateUserResponseDTO response = null;
    CreateUserRequestDTO request;
    List< String > roles = null;
    ResponseDTO responseDTO = null;
    try {
      if ( selectedOptionsRol != null ) {
        roles = ( List< String > ) selectedOptionsRol;
      }
      if ( pUserPortalDTO != null ) {
        usuarioDTO = new UserDTO();
        usuarioDTO.setEmail( pUserPortalDTO.getPerson().getEmail() );
        usuarioDTO.setAddress( pUserPortalDTO.getPerson().getAddress() );
        usuarioDTO.setFechaCreacion( UtilCommon.converDateToXMLGregorianCalendar( pUserPortalDTO.getCreationDate() ) );
        usuarioDTO.setUsuarioCreacion( pUserPortalDTO.getCreationUser() );
        usuarioDTO.setIdentificacion( pUserPortalDTO.getPerson().getIdentity() );
        usuarioDTO.setLogin( pUserPortalDTO.getLoginCetus() );
        usuarioDTO.setPhone( pUserPortalDTO.getPerson().getPhone() );
        
        request = new CreateUserRequestDTO();
        request.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
        request.setUser( ConstantEJB.USER_WS_CETUS );
        request.setUsuarioDTO( usuarioDTO );
        request.setRol( UtilCommon.toXML( roles ) );
        response = delegateVortal.createUserCetus( request );
        if ( response != null && response.getResponse() != null
             && ( ( ResponseWSDTO ) response.getResponse() ).getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) ) {
          responseDTO = createMessageSUCCESS();
          responseDTO.setObjectResponse( response.getUsuarioDTO() );
        } else {
          responseDTO = createMessageFAILURE();
          responseDTO.setMessage( response.getResponse().getMessage() );
        }
        
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO deleteUserCetus ( UserPortalDTO pUserPortalDTO ) {
    ResponseWSDTO response = null;
    DeleteUserRequestDTO request;
    ResponseDTO responseDTO = null;
    try {
      if ( pUserPortalDTO != null ) {
        request = new DeleteUserRequestDTO();
        request.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
        request.setUser( ConstantEJB.USER_WS_CETUS );
        request.setIdUsuario( Integer.parseInt( "" + pUserPortalDTO.getId() ) );
        request.setLogin( pUserPortalDTO.getLoginCetus() );
        response = delegateVortal.deleteUserCetus( request );
        if ( response != null && ( ( ResponseWSDTO ) response ).getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) ) {
          responseDTO = createMessageSUCCESS();
        } else {
          if ( response.getType().equalsIgnoreCase( ConstantCommon.WSResponse.TYPE_NORESULT ) ) {
            responseDTO = createMessageNORESULT();
            responseDTO.setMessage( response.getMessage() );
          } else {
            responseDTO = createMessageFAILURE();
            responseDTO.setMessage( response.getMessage() );
          }
        }
        
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Generate code client. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el response dto
   * @since CetusControlEJB (1/04/2015)
   */
  public ResponseDTO generateCodeClient () {
    String codeClient = null;
    ResponseDTO responseDTO = null;
    CallableStatement cst = null;
    Connection conn = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "antes de conexion" );
      conn = getConnection();
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "despues de conexion : " + conn );
      if ( conn != null ) {
        // Llamada al procedimiento almacenado
        cst = conn.prepareCall( "{call GENERATE_CODE_CLIENT (?)}" );
        if ( cst != null ) {
          cst.registerOutParameter( 1, java.sql.Types.VARCHAR );
          cst.executeUpdate();
          codeClient = cst.getString( 1 );
          if ( codeClient != null ) {
            responseDTO = UtilCommon.createMessageSUCCESS();
            responseDTO.setObjectResponse( codeClient );
          }
          cst.close();
        }
        conn.close();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    } finally {
      try {
        conn.close();
        cst.close();
      } catch ( SQLException e ) {
        e.printStackTrace();
      }
      
    }
    return responseDTO;
  }
  
  /**
   * </p> Find excep workday by client cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlEJB (23/03/2015)
   */
  public ResponseDTO findExcepWorkdayByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    List< ExceptionWorkday > listException = null;
    List< ExceptionWorkdayDTO > listExceptionDTO = null;
    TypedQuery< ExceptionWorkday > query = null;
    ExceptionWorkdayDTO exceptionWorkdayDTO = null;
    try {
      query = em.createNamedQuery( "ExceptionWorkday.findExcepWorkdayByClientCetus", ExceptionWorkday.class );
      query.setParameter( "idClientCetus", idClientCetus );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listExceptionDTO = new ArrayList< ExceptionWorkdayDTO >();
        listException = query.getResultList();
        for ( ExceptionWorkday exceptionWorkday: listException ) {
          exceptionWorkdayDTO = new ExceptionWorkdayDTO();
          converter.convertEntityToDto( exceptionWorkday, exceptionWorkdayDTO, false );
          listExceptionDTO.add( exceptionWorkdayDTO );
        }
        
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listExceptionDTO );
      } else {
        return createMessageNORESULT();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  @SuppressWarnings ( "unchecked" )
  public ResponseDTO findEmailsFromPerson ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    List< String > list = null;
    Query query = null;
    try {
      query = em.createNamedQuery( "Person.findEmailsByClientCetus", String.class ).setParameter( "idClientCetus", idclientCetus );
      list = ( List< String > ) query.getResultList();
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( list );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find area by client cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlEJB (25/03/2015)
   */
  public ResponseDTO findAreaByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Area > listArea = null;
    List< AreaDTO > listAreaDTO = null;
    TypedQuery< Area > query = null;
    AreaDTO areaDTO = null;
    try {
      query = em.createNamedQuery( "Area.findAreaByClientCetus", Area.class );
      query.setParameter( "idClientCetus", idClientCetus );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listAreaDTO = new ArrayList< AreaDTO >();
        listArea = query.getResultList();
        for ( Area area: listArea ) {
          areaDTO = new AreaDTO();
          converter.convertEntityToDto( area, areaDTO, false );
          listAreaDTO.add( areaDTO );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listAreaDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Generate code person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el response dto
   * @since CetusControlEJB (1/04/2015)
   */
  public ResponseDTO generateCodePerson () {
    String codeClient = null;
    ResponseDTO responseDTO = null;
    CallableStatement cst = null;
    Connection conn = null;
    try {
      conn = getConnection();
      
      if ( conn != null ) {
        // Llamada al procedimiento almacenado
        cst = conn.prepareCall( "{call GENERATE_CODE_PERSON (?)}" );
        if ( cst != null ) {
          cst.registerOutParameter( 1, java.sql.Types.VARCHAR );
          cst.executeUpdate();
          codeClient = cst.getString( 1 );
          if ( codeClient != null ) {
            responseDTO = UtilCommon.createMessageSUCCESS();
            responseDTO.setObjectResponse( codeClient );
          }
          cst.close();
        }
        conn.close();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    } finally {
      try {
        conn.close();
        cst.close();
      } catch ( SQLException e ) {
        e.printStackTrace();
      }
      
    }
    return responseDTO;
  }
  
  /**
   * </p> Find user by person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param idPerson the id person
   * @return el response dto
   * @since CetusControlEJB (3/04/2015)
   */
  public ResponseDTO findUserByPerson ( int idPerson ) {
    ResponseDTO responseDTO = null;
    List< UserPortal > list = null;
    List< UserPortalDTO > listDTO = null;
    TypedQuery< UserPortal > query = null;
    UserPortalDTO dto = null;
    try {
      query = em.createNamedQuery( "Person.findUserByPerson", UserPortal.class );
      query.setParameter( "idPerson", idPerson );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDTO = new ArrayList< UserPortalDTO >();
        list = query.getResultList();
        for ( UserPortal item: list ) {
          dto = new UserPortalDTO();
          converter.convertEntityToDto( item, dto, false );
          listDTO.add( dto );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find rol by login. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param login the login
   * @return el response dto
   * @since CetusControlEJB (3/04/2015)
   */
  public ResponseDTO findRolByLogin ( String login ) {
    ResponseDTO responseDTO = null;
    FindRolsByLoginRequestDTO request = null;
    FindRolsByLoginResponseDTO response = null;
    try {
      delegateVortal = new CetusVortalDelegate();
      request = new FindRolsByLoginRequestDTO();
      request.setLogin( login );
      request.setUser( ConstantEJB.USER_WS_CETUS );
      request.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
      response = delegateVortal.findRolByLogin( request );
      if ( response != null ) {
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( response );
      } else {
        responseDTO = UtilCommon.createMessageFAILURE();
        responseDTO.setMessage( "Error al buscar los roles asociados a la Usuario. Para m�s informaci�n comunicarse con el Administrador del Sistemas" );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO updateUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol ) {
    UserDTO usuarioDTO = null;
    ResponseWSDTO response = null;
    UpdateUserRequestDTO request;
    List< String > roles = null;
    ResponseDTO responseDTO = null;
    try {
      if ( selectedOptionsRol != null ) {
        roles = ( List< String > ) selectedOptionsRol;
      }
      if ( pUserPortalDTO != null ) {
        usuarioDTO = new UserDTO();
        usuarioDTO.setEmail( pUserPortalDTO.getPerson().getEmail() );
        usuarioDTO.setAddress( pUserPortalDTO.getPerson().getAddress() );
        usuarioDTO.setFechaCreacion( UtilCommon.converDateToXMLGregorianCalendar( pUserPortalDTO.getCreationDate() ) );
        usuarioDTO.setUsuarioCreacion( pUserPortalDTO.getCreationUser() );
        usuarioDTO.setIdentificacion( pUserPortalDTO.getPerson().getIdentity() );
        usuarioDTO.setLogin( pUserPortalDTO.getLoginCetus() );
        usuarioDTO.setPhone( pUserPortalDTO.getPerson().getPhone() );
        usuarioDTO.setStatus( pUserPortalDTO.getStatus() );
        usuarioDTO.setLoginOld( pUserPortalDTO.getLoginOld() );
        
        request = new UpdateUserRequestDTO();
        request.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
        request.setUser( ConstantEJB.USER_WS_CETUS );
        request.setUsuarioDTO( usuarioDTO );
        request.setRol( UtilCommon.toXML( roles ) );
        response = delegateVortal.updateUserCetus( request );
        if ( response != null && response.getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) ) {
          responseDTO = createMessageSUCCESS();
        } else {
          responseDTO = createMessageFAILURE();
          responseDTO.setMessage( response.getMessage() );
        }
        
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find group by client cetus. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClientCetus the p id client cetus
   * @return el response dto
   * @since CetusControlEJB (20/04/2015)
   */
  public ResponseDTO findGroupByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    List< GroupT > list = null;
    List< GroupTDTO > listDto = null;
    TypedQuery< GroupT > query = null;
    GroupTDTO dto = null;
    try {
      query = em.createNamedQuery( "GroupT.findGroupByClientCetus", GroupT.class );
      query.setParameter( "idClientCetus", pIdClientCetus );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< GroupTDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new GroupTDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find person by client. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClient the p id client
   * @return el response dto
   * @since CetusControlEJB (20/04/2015)
   */
  public ResponseDTO findPersonByClient ( int pIdClient ) {
    ResponseDTO responseDTO = null;
    List< Person > list = null;
    List< PersonDTO > listDto = null;
    TypedQuery< Person > query = null;
    PersonDTO dto = null;
    try {
      System.out.println( "##### buscar persona por cliente" );
      query = em.createNamedQuery( "Person.findPersonByClient", Person.class );
      query.setParameter( "idClient", pIdClient );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< PersonDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new PersonDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findAllGroupTypeByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    List< GroupType > list = null;
    List< GroupTypeDTO > listDto = null;
    TypedQuery< GroupType > query = null;
    GroupTypeDTO dto = null;
    try {
      query = em.createNamedQuery( "GroupType.findAllGroupTypeByClientCetus", GroupType.class );
      query.setParameter( "idClientCetus", pIdClientCetus );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< GroupTypeDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new GroupTypeDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find group by client. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClient the p id client
   * @return el response dto
   * @since CetusControlEJB (15/06/2015)
   */
  public ResponseDTO findGroupByClient ( int pIdClient ) {
    ResponseDTO responseDTO = null;
    List< GroupT > list = null;
    List< GroupTDTO > listDto = null;
    TypedQuery< GroupT > query = null;
    GroupTDTO dto = null;
    try {
      query = em.createNamedQuery( "GroupT.findGroupByClient", GroupT.class );
      query.setParameter( "idClient", pIdClient );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< GroupTDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new GroupTDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find group by person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdPerson the p id person
   * @return el response dto
   * @since CetusControlEJB (15/06/2015)
   */
  public ResponseDTO findGroupByPerson ( int pIdPerson ) {
    ResponseDTO responseDTO = null;
    List< GroupT > list = null;
    List< GroupTDTO > listDto = null;
    TypedQuery< GroupT > query = null;
    GroupTDTO dto = null;
    try {
      query = em.createNamedQuery( "GroupT.findGroupByPerson", GroupT.class );
      query.setParameter( "idPerson", pIdPerson );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< GroupTDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new GroupTDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find task by client. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClient the p id client
   * @return el response dto
   * @since CetusControlEJB (17/06/2015)
   */
  public ResponseDTO findTaskByClient ( int pIdClient ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TypedQuery< Task > query = null;
    TaskDTO dto = null;
    try {
      query = em.createNamedQuery( "Task.findTaskByClient", Task.class );
      query.setParameter( "idClient", pIdClient );
      list = query.getResultList();
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< TaskDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new TaskDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findGroupType ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    List< GroupType > list = null;
    List< GroupTypeDTO > listDto = null;
    TypedQuery< GroupType > query = null;
    GroupTypeDTO dto = null;
    try {
      query = em.createNamedQuery( "GroupType.findTypeByClientCetus", GroupType.class ).setParameter( "idclientCetus", idclientCetus );
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< GroupTypeDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new GroupTypeDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find person by group. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @return el response dto
   * @since CetusControlEJB (18/06/2015)
   */
  public ResponseDTO findPersonByGroup ( int idGroup ) {
    ResponseDTO responseDTO = null;
    List< PersonGroup > listPG = null;
    List< PersonGroupDTO > listPGDTO = null;
    TypedQuery< PersonGroup > query = null;
    PersonGroupDTO personGroupDTO = null;
    try {
      query = em.createNamedQuery( "PersonGroup.findPersonByGroup", PersonGroup.class );
      query.setParameter( "idGroup", idGroup );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listPGDTO = new ArrayList< PersonGroupDTO >();
        listPG = query.getResultList();
        for ( PersonGroup personGroup: listPG ) {
          personGroupDTO = new PersonGroupDTO();
          converter.convertEntityToDto( personGroup, personGroupDTO, false );
          listPGDTO.add( personGroupDTO );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listPGDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find priority by client cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlEJB (19/06/2015)
   */
  public ResponseDTO findPriorityByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Priority > listPriority = null;
    List< PriorityDTO > listPriorityDTO = null;
    TypedQuery< Priority > query = null;
    PriorityDTO priorityDTO = null;
    try {
      query = em.createNamedQuery( "Priority.findPriorityByClientCetus", Priority.class );
      query.setParameter( "idClientCetus", idClientCetus );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listPriorityDTO = new ArrayList< PriorityDTO >();
        listPriority = query.getResultList();
        for ( Priority priority: listPriority ) {
          priorityDTO = new PriorityDTO();
          converter.convertEntityToDto( priority, priorityDTO, false );
          listPriorityDTO.add( priorityDTO );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listPriorityDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find person group by person. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdPerson the p id person
   * @return el response dto
   * @since CetusControlEJB (20/06/2015)
   */
  public ResponseDTO findPersonGroupByPerson ( int pIdPerson ) {
    ResponseDTO responseDTO = null;
    List< PersonGroup > list = null;
    List< PersonGroupDTO > listDto = null;
    TypedQuery< PersonGroup > query = null;
    PersonGroupDTO dto = null;
    try {
      query = em.createNamedQuery( "PersonGroup.findPersonGroupByPerson", PersonGroup.class );
      query.setParameter( "idPerson", pIdPerson );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< PersonGroupDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new PersonGroupDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find task by person group. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdGroup the p id group
   * @param pIdPerson the p id person
   * @return el response dto
   * @since CetusControlEJB (20/06/2015)
   */
  public ResponseDTO findTaskByPersonGroup ( int pIdGroup, int pIdPerson ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TypedQuery< Task > query = null;
    TaskDTO dto = null;
    try {
      query = em.createNamedQuery( "Task.findTaskByPersonGroup", Task.class );
      query.setParameter( "idGroup", pIdGroup );
      query.setParameter( "idPerson", pIdPerson );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< TaskDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new TaskDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Generate code task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el response dto
   * @since CetusControlEJB (20/06/2015)
   */
  public ResponseDTO generateCodeTask () {
    String code = null;
    ResponseDTO responseDTO = null;
    CallableStatement cst = null;
    Connection conn = null;
    try {
      conn = getConnection();
      
      if ( conn != null ) {
        // Llamada al procedimiento almacenado
        cst = conn.prepareCall( "{call GENERATE_CODE_TASK(?)}" );
        if ( cst != null ) {
          cst.registerOutParameter( 1, java.sql.Types.VARCHAR );
          cst.executeUpdate();
          code = cst.getString( 1 );
          if ( code != null ) {
            responseDTO = UtilCommon.createMessageSUCCESS();
            responseDTO.setObjectResponse( code );
          }
          cst.close();
        }
        conn.close();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    } finally {
      try {
        conn.close();
        cst.close();
      } catch ( SQLException e ) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }
    return responseDTO;
  }
  
  /**
   * </p> Gets the template task massive. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @param idClient the id client
   * @return el response dto
   * @since CetusControlEJB (5/07/2015)
   */
  public ResponseDTO getTemplateTaskMassive ( int idClientCetus, int idClient ) {
    ResponseDTO responseDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "idClientCetus=" + idClientCetus + ", idClient=" + idClient );
      InputStream reportJasper = this.getClass().getClassLoader().getResourceAsStream( "resource/massive_task_template.jasper" );
      Map< String, Object > parameters = new HashMap< String, Object >();
      parameters.put( "ID_CLIENT_CETUS", String.valueOf( idClientCetus ) );
      parameters.put( "ID_CLIENT", String.valueOf( idClient ) );
      
      JasperReport report = ( JasperReport ) JRLoader.loadObject( reportJasper );
      
      Connection connection = getConnection();
      
      JasperPrint jasperPrint = JasperFillManager.fillReport( report, parameters, connection );
      
      ByteArrayOutputStream fileReport = new ByteArrayOutputStream();
      
      //Creaci�n del XLS
      JRXlsExporter exporter = new JRXlsExporter();
      exporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );
      exporter.setExporterOutput( new SimpleOutputStreamExporterOutput( fileReport ) );
      exporter.exportReport();
      
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( fileReport.toByteArray() );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find person group. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @param personIdentity the person identity
   * @return el response dto
   * @since CetusControlEJB (7/07/2015)
   */
  public ResponseDTO findPersonGroup ( int idGroup, String personIdentity ) {
    ResponseDTO responseDTO = null;
    PersonGroup personGroup = null;
    PersonGroupDTO personGroupDTO = null;
    TypedQuery< PersonGroup > query = null;
    try {
      query = em.createNamedQuery( "PersonGroup.findPersonGroup", PersonGroup.class );
      query.setParameter( "idGroup", idGroup );
      query.setParameter( "personIdentity", personIdentity );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        personGroup = query.getSingleResult();
        personGroupDTO = new PersonGroupDTO();
        converter.convertEntityToDto( personGroup, personGroupDTO, false );
        
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( personGroupDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find task by person. </p>
   *
   * @author eChia - Cetus Technology
   * @param pIdPerson the p id person
   * @return el response dto
   * @since CetusControlEJB (16/07/2015)
   */
  public ResponseDTO findTaskByPerson ( int pIdPerson ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TypedQuery< Task > query = null;
    TaskDTO dto = null;
    try {
      query = em.createNamedQuery( "Task.findTaskByPerson", Task.class );
      query.setParameter( "idPerson", pIdPerson ).setParameter( "statusExclude", ConstantEJB.TASK_STATUS_FINAL );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< TaskDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new TaskDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonPriority ( int pIdPerson, String descripPriority ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TypedQuery< Task > query = null;
    TaskDTO dto = null;
    try {
      query = em.createNamedQuery( "Task.findTaskByPersonPriority", Task.class );
      query.setParameter( "idPerson", pIdPerson ).setParameter( "statusExclude", ConstantEJB.TASK_STATUS_FINAL )
           .setParameter( "prDescrip", descripPriority );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< TaskDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new TaskDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find progress task. </p>
   * Retorna los valores de propiedades del progreso de tareas de un usuario
   * @author eChia - Cetus Technology
   * @return Map<String, String> EJ: <"Completadas", "20">
   * @since CetusControlEJB (10/08/2015)
   */
  public ResponseDTO findProgressTaskByPerson ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    Map< String, Integer > progressTask = new HashMap< String, Integer >();
    Query query = null;
    try {
      /**Completadas:**/
      int valorItem;
      valorItem = ( ( Long ) em.createQuery(
                                             " SELECT"
                                             + " COUNT(t.id)"
                                             + " FROM Task t"
                                             + " JOIN t.status s"
                                             + " JOIN t.personGroup pg"
                                             + " JOIN pg.person p"
                                             + " WHERE p.id = :idPerson AND s.description = :statusDes" )
                               .setParameter( "idPerson", pIdPerson )
                               .setParameter( "statusDes", ConstantEJB.TASK_STATUS_FINAL )
                               .getSingleResult() ).intValue();
                               
      progressTask.put( ConstantEJB.DES_FINISH, valorItem );
      
      /**Proximas a vencer:**/
      query = em.createNativeQuery( "SELECT count(t.id) FROM Task t inner JOIN person_group  pg on pg.id = t.ID_GROUP_PERSON inner JOIN person p on p.id = pg.ID_PERSON inner JOIN client c on c.id = p.ID_CLIENT inner JOIN client_cetus cs on cs.id = c.ID_CLIENT_CETUS inner JOIN parameter_general pgral on pgral.ID_CLIENT_CETUS = cs.id WHERE p.id = ? AND cs.id = ? AND  ADDTIME(now(),SEC_TO_TIME(pgral.TIME_BEFORE_EXPIRATION*60)) >= t.DELIVERY_DATE and t.id_status not in (3,4) and now() <= t.DELIVERY_DATE" );
      query.setParameter( 1, pIdPerson );
      query.setParameter( 2, pidClientCetus );
      valorItem = ( ( BigInteger ) query.getSingleResult() ).intValue();
      
      progressTask.put( ConstantEJB.DES_NEXT_OVERCOME, valorItem );
      
      /**En Curso:**/
      valorItem = ( ( Long ) em.createQuery(
                                             " SELECT"
                                             + " COUNT(t.id)"
                                             + " FROM Task t"
                                             + " JOIN t.status s"
                                             + " JOIN t.personGroup pg"
                                             + " JOIN pg.person p"
                                             + " WHERE p.id = :idPerson AND t.status.id in (2)" )
                               .setParameter( "idPerson", pIdPerson )
                               .getSingleResult() ).intValue();
                               
      progressTask.put( ConstantEJB.DES_RUN, valorItem );
      
      /**Vencidas:**/
      valorItem = ( ( Long ) em.createQuery(
                                             " SELECT"
                                             + " COUNT(t.id)"
                                             + " FROM Task t"
                                             + " JOIN t.status s"
                                             + " JOIN t.personGroup pg"
                                             + " JOIN pg.person p"
                                             + " WHERE p.id = :idPerson AND t.status.id in (2) AND now() > t.deliveryDate " )
                               .setParameter( "idPerson", pIdPerson ).getSingleResult() ).intValue();
                               
      progressTask.put( ConstantEJB.DES_EXPIRED, valorItem );
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( progressTask );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public ResponseDTO findTaskByPersonCompleted ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TaskDTO dto = null;
    Query query = null;
    try {
      query = em.createQuery(
                              " SELECT"
                              + " t"
                              + " FROM Task t"
                              + " JOIN t.status s"
                              + " JOIN t.personGroup pg"
                              + " JOIN pg.person p"
                              + " WHERE p.id = :idPerson AND t.status.id = 3" );
      query.setParameter( "idPerson", pIdPerson );
      list = ( ArrayList< Task > ) query.getResultList();
      
      listDto = new ArrayList< TaskDTO >();
      
      for ( Object entity: list ) {
        dto = new TaskDTO();
        converter.convertEntityToDto( entity, dto, false );
        listDto.add( dto );
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    
    responseDTO = createMessageSUCCESS();
    responseDTO.setObjectResponse( listDto );
    
    return responseDTO;
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public ResponseDTO findTaskByPersonNexOvercome ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TaskDTO dto = null;
    Query query = null;
    try {
      query = em.createQuery(
                              " SELECT"
                              + " t"
                              + " FROM Task t"
                              + " JOIN t.personGroup pg"
                              + " JOIN pg.person p"
                              + " JOIN p.client c"
                              + " JOIN c.clientCetus cc"
                              + " JOIN cc.parameterGenerals pgral"
                              + " WHERE p.id = :idPerson AND pgral.clientCetus.id = :idClientCetus AND ADDTIME(now(),SEC_TO_TIME(pgral.timeBeforeExpiration*60)) >= t.deliveryDate and t.status.id not in (4,3) " );
                              
      query.setParameter( "idClientCetus", pidClientCetus );
      query.setParameter( "idPerson", pIdPerson );
      list = ( ArrayList< Task > ) query.getResultList();
      
      listDto = new ArrayList< TaskDTO >();
      
      for ( Object entity: list ) {
        dto = new TaskDTO();
        converter.convertEntityToDto( entity, dto, false );
        listDto.add( dto );
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    
    responseDTO = createMessageSUCCESS();
    responseDTO.setObjectResponse( listDto );
    
    return responseDTO;
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public ResponseDTO findTaskByPersonRun ( int pIdPerson, int pIdClient ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TaskDTO dto = null;
    Query query = null;
    try {
      
      query = em.createQuery( " SELECT"
                              + " t"
                              + " FROM Task t"
                              + " JOIN t.status s"
                              + " JOIN t.personGroup pg"
                              + " JOIN pg.person p"
                              + " WHERE p.id = :idPerson and p.client.id = :idClient AND t.status.id in (2)" );
      query.setParameter( "idPerson", pIdPerson );
      query.setParameter( "idClient", pIdClient );
      list = ( ArrayList< Task > ) query.getResultList();
      
      listDto = new ArrayList< TaskDTO >();
      
      for ( Object entity: list ) {
        dto = new TaskDTO();
        converter.convertEntityToDto( entity, dto, false );
        listDto.add( dto );
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    
    responseDTO = createMessageSUCCESS();
    responseDTO.setObjectResponse( listDto );
    
    return responseDTO;
  }
  
  @SuppressWarnings ( "unchecked" )
  public ResponseDTO findTaskByPersonExpired ( int pIdPerson, int pIdClient ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TaskDTO dto = null;
    Query query = null;
    try {
      
      query = em.createQuery( " SELECT"
                              + " t"
                              + " FROM Task t"
                              + " JOIN t.status s"
                              + " JOIN t.personGroup pg"
                              + " JOIN pg.person p"
                              + " WHERE p.id = :idPerson and p.client.id = :idClient AND t.status.id not in (4,3) AND now() > t.deliveryDate " );
      query.setParameter( "idPerson", pIdPerson );
      query.setParameter( "idClient", pIdClient );
      list = ( ArrayList< Task > ) query.getResultList();
      
      listDto = new ArrayList< TaskDTO >();
      
      for ( Object entity: list ) {
        dto = new TaskDTO();
        converter.convertEntityToDto( entity, dto, false );
        listDto.add( dto );
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    
    responseDTO = createMessageSUCCESS();
    responseDTO.setObjectResponse( listDto );
    
    return responseDTO;
    
  }
  
  public double getPercentageCurrent ( int idPerson ) {
    Query query = null;
    double result = 0;
    try {
      query = em.createNamedQuery( "Task.getPercentageCurrent" );
      query.setParameter( "idPerson", idPerson );
      result = ( double ) query.getSingleResult();
      return result;
    } catch ( Exception ex ) {
      return 0;
    }
    
  }
  
  public ResponseDTO generateLengthTask ( int idTask ) {
    String result = null;
    String duration = null;
    ResponseDTO responseDTO = null;
    CallableStatement cst = null;
    Connection conn = null;
    
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "### GENERAR TIEMPO DEDICADO PARA LA TAREA " + idTask );
      conn = getConnection();
      if ( conn != null ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "### CONEXION ESTABLECIDA  " );
        // Llamada al procedimiento almacenado
        cst = conn.prepareCall( "{call GEN_LENGTH_TASK (?,?,?)}" );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "### PL OK" + idTask );
        if ( cst != null ) {
          cst.setInt( 1, idTask );
          cst.registerOutParameter( 2, java.sql.Types.VARCHAR );
          cst.registerOutParameter( 3, java.sql.Types.VARCHAR );
          cst.executeUpdate();
          ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "### EXECUTION DONE " + idTask );
          result = cst.getString( 2 );
          duration = cst.getString( 3 );
          ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "### RESULTADO PL " + result );
          ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "### DURATION PL " + duration );
          if ( result != null && result.equals( ConstantEJB.RESULT_SQL_OK ) ) {
            responseDTO = UtilCommon.createMessageSUCCESS();
            responseDTO.setObjectResponse( duration );
          } else {
            responseDTO = UtilCommon.createMessageFAILURE();
          }
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    } finally {
      try {
        conn.close();
        cst.close();
      } catch ( SQLException e ) {
        e.printStackTrace();
      }
      
    }
    return responseDTO;
  }
  
  /**
   * </p> Find attachment files by task id. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdTask the p id task
   * @return el response dto
   * @since CetusControlEJB (11/02/2016)
   */
  public ResponseDTO findAttachmentFilesByTaskId ( long pIdTask ) {
    ResponseDTO responseDTO = null;
    List< Attach > list = null;
    List< AttachDTO > listDto = null;
    TypedQuery< Attach > query = null;
    AttachDTO dto = null;
    try {
      query = em.createNamedQuery( "Attach.findAttachmentFilesByTaskId", Attach.class );
      query.setParameter( "pIdTask", Long.valueOf( pIdTask ).intValue() );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< AttachDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new AttachDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
      }
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( listDto );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find notification gen client cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlEJB (19/02/2016)
   */
  public ResponseDTO findNotificationGenClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    List< NotificationGeneral > list = null;
    List< NotificationGeneralDTO > listDto = null;
    TypedQuery< NotificationGeneral > query = null;
    NotificationGeneralDTO dto = null;
    try {
      query = em.createNamedQuery( "NotificationGeneral.findByClientCetus", NotificationGeneral.class );
      query.setParameter( "idClientDefault", ConstantEJB.ID_CLIENT_CETUS_MASTER );
      query.setParameter( "idClientCetus", idClientCetus );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< NotificationGeneralDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new NotificationGeneralDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listDto );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find notification by group. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @return el response dto
   * @since CetusControlEJB (21/02/2016)
   */
  public ResponseDTO findNotificationByGroup ( int idGroup ) {
    ResponseDTO responseDTO = null;
    List< NotificationSetting > list = null;
    List< NotificationSettingDTO > listDto = null;
    TypedQuery< NotificationSetting > query = null;
    NotificationSettingDTO dto = null;
    try {
      query = em.createNamedQuery( "NotificationSetting.findNotificationByGroup", NotificationSetting.class );
      query.setParameter( "idGroup", idGroup );
      list = query.getResultList();
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listDto = new ArrayList< NotificationSettingDTO >();
        list = query.getResultList();
        for ( Object entity: list ) {
          dto = new NotificationSettingDTO();
          converter.convertEntityToDto( entity, dto, false );
          listDto.add( dto );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listDto );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Exists jornd in day. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdCetus the p id cetus
   * @param pDay the p day
   * @return el response dto
   * @since CetusControlEJB (29/02/2016)
   */
  public ResponseDTO existsJorndInDay ( long pIdCetus, String pDay ) {
    ResponseDTO responseDTO = null;
    TypedQuery< Workday > query = null;
    try {
      responseDTO = new ResponseDTO();
      query = em.createNamedQuery( "Workday.existsJorndInDay", Workday.class );
      query.setParameter( "idClientCetus", Long.valueOf( pIdCetus ).intValue() );
      query.setParameter( "day", pDay );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( true );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find notification by group gen. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @param idGeneral the id general
   * @return el response dto
   * @since CetusControlEJB (29/02/2016)
   */
  public ResponseDTO findNotificationByGroupGen ( int idGroup, int idGeneral ) {
    ResponseDTO responseDTO = null;
    NotificationSetting notificationSetting = null;
    TypedQuery< NotificationSetting > query = null;
    NotificationSettingDTO dto = null;
    try {
      query = em.createNamedQuery( "NotificationSetting.findNotifGenGroup", NotificationSetting.class );
      query.setParameter( "idGroup", idGroup );
      query.setParameter( "idGeneral", idGeneral );
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        notificationSetting = query.getSingleResult();
        dto = new NotificationSettingDTO();
        converter.convertEntityToDto( notificationSetting, dto, false );
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( dto );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO isTimeValid ( long pIdCetus, String pDay, Date pTime ) {
    ResponseDTO responseDTO = null;
    Query query = null;
    DateFormat formatter = null;
    Date h1 = null, h2 = null;
    try {
      formatter = new SimpleDateFormat( "HH:mm" );
      pTime = formatter.parse( formatter.format( pTime ) );
      responseDTO = new ResponseDTO();
      query = em.createNativeQuery( "SELECT min(TIME(CONCAT(TRUNCATE(START_TIME/100,0) ,':', MOD(END_TIME,100)))),max(TIME(CONCAT(TRUNCATE(END_TIME/100,0) ,':', MOD(END_TIME,100)))) FROM workday  where COL_DAY=? and ID_CLIENT_CETUS=?" );
      query.setParameter( 1, pDay );
      query.setParameter( 2, Long.valueOf( pIdCetus ).intValue() );
      Object[] results = ( Object[] ) query.getSingleResult();
      if ( results.length > 0 ) {
        h1 = ( Date ) formatter.parse( ( String ) results[0] );
        
        h2 = ( Date ) formatter.parse( ( String ) results[1] );
        if ( pTime != null ) {
          responseDTO = createMessageSUCCESS();
          if ( pTime.getTime() >= h1.getTime() && pTime.getTime() <= h2.getTime() ) {
            responseDTO.setObjectResponse( true );
          } else {
            responseDTO.setObjectResponse( false );
          }
        }
        
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Generate report view task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdTask the p id task
   * @param pFormatPattern the p format pattern
   * @since CetusControlEJB (5/03/2016)
   */
  public ResponseDTO generateReportViewTask ( long pIdTask, String pFormatPattern ) {
    ResponseDTO responseDTO = null;
    InputStream reportJasper = null;
    Map< String, Object > parameters = new HashMap< String, Object >();
    JasperReport report;
    try {
      reportJasper = this.getClass().getClassLoader().getResourceAsStream( "resource/view_task_report.jasper" );
      parameters.put( "ID_TASK", Long.valueOf( pIdTask ).intValue() );
      parameters.put( "PATTERN_DATE", String.valueOf( pFormatPattern ) );
      report = ( JasperReport ) JRLoader.loadObject( reportJasper );
      
      Connection connection = getConnection();
      
      JasperPrint jasperPrint = JasperFillManager.fillReport( report, parameters, connection );
      JRPdfExporter exporter = new JRPdfExporter();
      ByteArrayOutputStream fileReport = new ByteArrayOutputStream();
      exporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );
      exporter.setExporterOutput( new SimpleOutputStreamExporterOutput( fileReport ) );
      exporter.exportReport();
      responseDTO = createMessageSUCCESS();
      responseDTO.setObjectResponse( fileReport.toByteArray() );
    } catch ( JRException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Gets the connection. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el connection
   * @since CetusControlEJB (16/03/2016)
   */
  private Connection getConnection () {
    Connection connection = null;
    try {
      Session session = ( Session ) em.getDelegate();
      connection = ( ( SessionImplementor ) session ).connection();
    } catch ( Exception e ) {
      throw e;
    }
    return connection;
  }
  
  /**
   * </p> Find task history. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idTask the id task
   * @return el response dto
   * @since CetusControlEJB (8/04/2016)
   */
  public ResponseDTO findTaskHistory ( int idTask ) {
    ResponseDTO responseDTO = null;
    TaskHistoryDTO taskHistoryDTO = null;
    TypedQuery< TaskHistory > query = null;
    List< TaskHistoryDTO > list = null;
    try {
      query = em.createNamedQuery( "TaskHistory.findTAskHistory", TaskHistory.class );
      query.setParameter( "idTask", idTask );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        list = new ArrayList< TaskHistoryDTO >();
        for ( Object taskHistory: query.getResultList() ) {
          taskHistoryDTO = new TaskHistoryDTO();
          responseDTO = createMessageSUCCESS();
          converter.convertEntityToDto( taskHistory, taskHistoryDTO, false );
          
          list.add( taskHistoryDTO );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( list );
        
      } else {
        return createMessageNORESULT();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskTypeByArea ( int idArea ) {
    ResponseDTO responseDTO = null;
    List< AreaTypeTask > listATT = null;
    List< AreaTypeTaskDTO > listATTDTO = null;
    TypedQuery< AreaTypeTask > query = null;
    AreaTypeTaskDTO areaTypeTaskDTO = null;
    try {
      query = em.createNamedQuery( "AreaTypeTask.findTaskTypeByArea", AreaTypeTask.class );
      query.setParameter( "idArea", idArea );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listATTDTO = new ArrayList< AreaTypeTaskDTO >();
        listATT = query.getResultList();
        for ( AreaTypeTask areaTypeTask: listATT ) {
          areaTypeTaskDTO = new AreaTypeTaskDTO();
          converter.convertEntityToDto( areaTypeTask, areaTypeTaskDTO, false );
          listATTDTO.add( areaTypeTaskDTO );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listATTDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findAllTraceTaskByTaskId ( int pIdTask ) {
    ResponseDTO responseDTO = null;
    List< TraceTask > listATT = null;
    List< TraceTaskDTO > listATTDTO = null;
    TypedQuery< TraceTask > query = null;
    TraceTaskDTO dto = null;
    try {
      query = em.createNamedQuery( "TraceTask.findAllByTaskId", TraceTask.class );
      query.setParameter( "idTask", pIdTask );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listATTDTO = new ArrayList< TraceTaskDTO >();
        listATT = query.getResultList();
        for ( TraceTask obj: listATT ) {
          dto = new TraceTaskDTO();
          converter.convertEntityToDto( obj, dto, false );
          listATTDTO.add( dto );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listATTDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  public ResponseDTO findStatusById ( int idStatus ) {
    ResponseDTO responseDTO = null;
    Status obj = null;
    StatusDTO dto = null;
    TypedQuery< Status > query = null;
    try {
      query = em.createNamedQuery( "Status.findStatusById", Status.class );
      query.setParameter( "idStatus", idStatus );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        obj = query.getSingleResult();
        dto = new StatusDTO();
        converter.convertEntityToDto( obj, dto, false );
        
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( dto );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Find task by filter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClient the id client
   * @param filter the filter
   * @param inputFilter the input filter
   * @return el response dto
   * @since CetusControlEJB (14/05/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public ResponseDTO findTaskByFilter ( int idClient, String filter, String inputFilter ) {
    ResponseDTO responseDTO = null;
    List< Task > list = null;
    List< TaskDTO > listDto = null;
    TaskDTO dto = null;
    Query query = null;
    StringBuilder sql = new StringBuilder();
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "idClient=" + idClient + ", filter=" + filter + ", inputFilter=" + inputFilter );
      if ( !UtilCommon.isNullOrEmptyString( filter ) && !UtilCommon.isNullOrEmptyString( inputFilter ) ) {
        sql.append( "SELECT t " );
        sql.append( "FROM Task t " );
        sql.append( "JOIN t.personGroup pg " );
        sql.append( "JOIN pg.person p " );
        sql.append( "WHERE p.client.id = :idClient " );
        
        switch ( EFilterSearch.getFilterSearch( filter ) ) {
          case FILTER1: // Codigo de la tarea
            sql.append( "AND UPPER(t.code) LIKE :inputFilter " );
            break;
          case FILTER2: // Descripcion de la tarea
            sql.append( "AND UPPER(t.description) LIKE :inputFilter " );
            break;
          case FILTER3: // Responsable de la tarea
            sql.append( "AND ( UPPER(p.names) LIKE :inputFilter OR UPPER(p.lastNames) LIKE :inputFilter ) " );
            break;
          case FILTER4:
            break;
          case FILTER5:
            break;
          case FILTER6:
            break;
          case FILTER7:
            break;
          case FILTER8:
            break;
          case FILTER9:
            break;
          case FILTER10:
            break;
          default:
            break;
        }
        
        query = em.createQuery( sql.toString() );
        query.setParameter( "idClient", idClient );
        query.setParameter( "inputFilter", "%" + inputFilter.toUpperCase() + "%" );
        
        if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
          listDto = new ArrayList< >();
          list = query.getResultList();
          for ( Task task: list ) {
            dto = new TaskDTO();
            converter.convertEntityToDto( task, dto, false );
            listDto.add( dto );
          }
          responseDTO = createMessageSUCCESS();
          responseDTO.setObjectResponse( listDto );
        } else {
          return createMessageNORESULT();
        }
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    
    return responseDTO;
    
  }
  
  /**
   * </p> Find comment by id task. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idTask the id task
   * @return el response dto
   * @since CetusControlEJB (29/05/2016)
   */
  public ResponseDTO findCommentByIdTask ( int idTask ) {
    ResponseDTO responseDTO = null;
    List< CommentTask > listComm = null;
    List< CommentTaskDTO > listCommDTO = null;
    CommentTaskDTO dto = null;
    TypedQuery< CommentTask > query = null;
    try {
      query = em.createNamedQuery( "CommentTask.findCommentByIdTask", CommentTask.class );
      query.setParameter( "idTask", idTask );
      
      if ( query != null && query.getResultList() != null && query.getResultList().size() > 0 ) {
        listCommDTO = new ArrayList< CommentTaskDTO >();
        listComm = query.getResultList();
        for ( CommentTask obj: listComm ) {
          dto = new CommentTaskDTO();
          converter.convertEntityToDto( obj, dto, false );
          listCommDTO.add( dto );
        }
        responseDTO = createMessageSUCCESS();
        responseDTO.setObjectResponse( listCommDTO );
      } else {
        return createMessageNORESULT();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
  /**
   * </p> Send email. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param sendMailRequestDTO the send mail request dto
   * @return el response dto
   * @since CetusControlEJB (12/06/2016)
   */
  public ResponseDTO sendEmail ( SendMailRequestDTO sendMailRequestDTO ) {
    co.com.cetus.messageservice.ejb.service.ResponseWSDTO responseWSDTO = null;
    ResponseDTO responseDTO = null;
    try {
      String wsdlMessageService = getValueParameter( ConstantEJB.WSDL_CETUS_MESSAGE_SERVICE );
      CetusMessageServiceDelegate messageServiceDelegate = new CetusMessageServiceDelegate( wsdlMessageService );
      responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
      
      if ( responseWSDTO != null ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Respuesta del envio del email: " + responseWSDTO.toString() );
        if ( responseWSDTO.getCode() != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
          responseDTO = createMessageSUCCESS();
        } else {
          responseDTO = createMessageFAILURE();
        }
      } else {
        responseDTO = createMessageFAILURE();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = createMessageFAILURE();
    }
    return responseDTO;
  }
  
}
