package co.com.cetus.cetuscontrol.ejb.bean;

import static co.com.cetus.common.util.UtilCommon.createMessageFAILURE;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.ejb.process.CetusControlProcess;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class CetusControlBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
@Stateless
public class CetusControlBean implements CetusControlBeanRemote {
  
  /** The cetus control process. */
  @EJB
  CetusControlProcess cetusControlProcess;
  
  /**
   * </p> Instancia un nuevo cetus control bean. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlEJB (1/03/2016)
   */
  public CetusControlBean () {
  }
  
  /**
   * </p> Metodo para crear registros en una entidad mediante el Objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pDTO the p dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO create ( Object pDTO ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.create( pDTO );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para actualizar la informacion de un registro en base de datos mediante el objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dto the dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO edit ( Object dto ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.edit( dto );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para eliminar la informacion de un registro en base de datos mediante el objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dto the dto
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO remove ( Object dto ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.remove( dto );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findAll( classDTO, fieldOrder, typeOrder );
      return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findAll( classDTO );
      return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.find( classDTO, id );
      return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findRange( classDTO, arrRange );
      return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.count( classDTO );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para consultar el valor de un parametro. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameParameter the name parameter
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public String getValueParameter ( String nameParameter ) {
    String response = null;
    try {
      response = cetusControlProcess.getValueParameter( nameParameter );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return response;
  }
  
  /**
   * </p> Metodo para buscar el usuario que se encuentra logueado en la aplicacion </p>.
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param userCetus the user cetus
   * @return el response dto
   * @since CetusCSBLiquidationEJB (16/12/2014)
   */
  public ResponseDTO searchUserLogged ( String userCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.searchUserLogged( userCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findClientByClientCetus(int)
   */
  public ResponseDTO findClientByClientCetus ( int pIdClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findClientByClientCetus( pIdClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Find person by client cetus. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pIdClientCetus the p id client cetus
   * @return el response dto
   * @since CetusControlEJB (1/03/2016)
   */
  public ResponseDTO findPersonByClientCetus ( int pIdClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findPersonByClientCetus( pIdClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findRolByApplication(int)
   */
  public ResponseDTO findRolByApplication ( int idApp ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findRolByApplication( idApp );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findStatus(int)
   */
  public ResponseDTO findStatus ( int idclientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findStatus( idclientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskType(int)
   */
  public ResponseDTO findTaskType ( int idclientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskType( idclientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /**
   * </p> Metodo para obtener los parametros generales que posee un cliente cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlEJB (5/03/2015)
   */
  public ResponseDTO findParameterGeneralByClientCetus ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findParameterGeneralByClientCetus( idClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findWorkDayByClientCetus( idClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#createUserCetus(co.com.cetus.cetuscontrol.dto.UserPortalDTO, java.util.List)
   */
  @Override
  public ResponseDTO createUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.createUserCetus( pUserPortalDTO, selectedOptionsRol );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#deleteUserCetus(co.com.cetus.cetuscontrol.dto.UserPortalDTO)
   */
  @Override
  public ResponseDTO deleteUserCetus ( UserPortalDTO request ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.deleteUserCetus( request );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findExcepWorkdayByClientCetus(int)
   */
  public ResponseDTO findExcepWorkdayByClientCetus ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findExcepWorkdayByClientCetus( idClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#generateCodeClient()
   */
  @Override
  public ResponseDTO generateCodeClient () {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.generateCodeClient();
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findEmailsFromPerson(int)
   */
  public ResponseDTO findEmailsFromPerson ( int idclientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findEmailsFromPerson( idclientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findAreaByClientCetus(int)
   */
  public ResponseDTO findAreaByClientCetus ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findAreaByClientCetus( idClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#generateCodePerson()
   */
  @Override
  public ResponseDTO generateCodePerson () {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.generateCodePerson();
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findUserByPerson(int)
   */
  public ResponseDTO findUserByPerson ( int idPerson ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findUserByPerson( idPerson );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findRolByLogin(java.lang.String)
   */
  public ResponseDTO findRolByLogin ( String request ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findRolByLogin( request );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#updateUserCetus(co.com.cetus.cetuscontrol.dto.UserPortalDTO, java.util.List)
   */
  public ResponseDTO updateUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.updateUserCetus( pUserPortalDTO, selectedOptionsRol );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findGroupByClientCetus(int)
   */
  public ResponseDTO findGroupByClientCetus ( int pIdClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findGroupByClientCetus( pIdClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findPersonByClient(int)
   */
  public ResponseDTO findPersonByClient ( int pIdClient ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findPersonByClient( pIdClient );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findAllGroupTypeByClientCetus(int)
   */
  public ResponseDTO findAllGroupTypeByClientCetus ( int pIdClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findAllGroupTypeByClientCetus( pIdClientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findGroupByClient(int)
   */
  public ResponseDTO findGroupByClient ( int pIdClient ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findGroupByClient( pIdClient );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findGroupByPerson(int)
   */
  public ResponseDTO findGroupByPerson ( int pIdPerson ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findGroupByPerson( pIdPerson );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByClient(int)
   */
  public ResponseDTO findTaskByClient ( int pIdClient ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByClient( pIdClient );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findGroupType(int)
   */
  public ResponseDTO findGroupType ( int idclientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findGroupType( idclientCetus );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findPersonByGroup( idGroup );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findPriorityByClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findPersonGroupByPerson(int)
   */
  public ResponseDTO findPersonGroupByPerson ( int pIdPerson ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findPersonGroupByPerson( pIdPerson );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPersonGroup(int, int)
   */
  public ResponseDTO findTaskByPersonGroup ( int pIdGroup, int pIdPerson ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPersonGroup( pIdGroup, pIdPerson );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#generateCodeTask()
   */
  public ResponseDTO generateCodeTask () {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.generateCodeTask();
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#getTemplateTaskMassive(int, int)
   */
  public ResponseDTO getTemplateTaskMassive ( int idClientCetus, int idClient ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.getTemplateTaskMassive( idClientCetus, idClient );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findPersonGroup(int, java.lang.String)
   */
  public ResponseDTO findPersonGroup ( int idGroup, String personIdentity ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findPersonGroup( idGroup, personIdentity );
      return response;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPerson(int)
   */
  public ResponseDTO findTaskByPerson ( int pIdPerson ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPerson( pIdPerson );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#getPercentageCurrent(int)
   */
  public double getPercentageCurrent ( int idPerson ) {
    Double response = null;
    try {
      response = cetusControlProcess.getPercentageCurrent( idPerson );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findProgressTaskByPerson(int, int)
   */
  public ResponseDTO findProgressTaskByPerson ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findProgressTaskByPerson( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPersonPriority(int, java.lang.String)
   */
  public ResponseDTO findTaskByPersonPriority ( int pIdPerson, String descripPriority ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPersonPriority( pIdPerson, descripPriority );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#generateLengthTask(int)
   */
  public ResponseDTO generateLengthTask ( int idTask ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.generateLengthTask( idTask );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPersonCompleted(int, int)
   */
  @Override
  public ResponseDTO findTaskByPersonCompleted ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPersonCompleted( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPersonRun(int, int)
   */
  @Override
  public ResponseDTO findTaskByPersonRun ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPersonRun( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPersonExpired(int, int)
   */
  @Override
  public ResponseDTO findTaskByPersonExpired ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPersonExpired( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findTaskByPersonNexOvercome(int, int)
   */
  @Override
  public ResponseDTO findTaskByPersonNexOvercome ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByPersonNexOvercome( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#findAttachmentFilesByTaskId(long)
   */
  public ResponseDTO findAttachmentFilesByTaskId ( long pIdTask ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findAttachmentFilesByTaskId( pIdTask );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findNotificationGenClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findNotificationByGroup( idGroup );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#existsJorndInDay(long, java.lang.String)
   */
  public ResponseDTO existsJorndInDay ( long pIdCetus, String pDay ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.existsJorndInDay( pIdCetus, pDay );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      response = UtilCommon.createMessageFAILURE();
    }
    return response;
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
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findNotificationByGroupGen( idGroup, idGeneral );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote#isTimeValid(long, java.lang.String, java.util.Date)
   */
  public ResponseDTO isTimeValid ( long pIdCetus, String pDay, Date pTime ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.isTimeValid( pIdCetus, pDay, pTime );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  public ResponseDTO generateReportViewTask ( long pIdTask, String pFormatPattern ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.generateReportViewTask( pIdTask, pFormatPattern );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
    
  }
  
  public ResponseDTO findTaskHistory ( int idTask ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskHistory( idTask );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
    
  }
  
  public ResponseDTO findTaskTypeByArea ( int idArea ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskTypeByArea( idArea );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
    
  }
  
  @Override
  public ResponseDTO findAllTraceTaskByTaskId ( int pIdTask ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findAllTraceTaskByTaskId( pIdTask );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
    
  }
  
  @Override
  public ResponseDTO findStatusById ( int idStatus ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findStatusById( idStatus );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
    
  }

  @Override
  public ResponseDTO findTaskByFilter ( int idClient, String filter, String inputFilter ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findTaskByFilter( idClient, filter, inputFilter );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
    
  }
  
  public ResponseDTO findCommentByIdTask ( int idTask ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.findCommentByIdTask( idTask );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
  
  public ResponseDTO sendEmail ( SendMailRequestDTO sendMailRequestDTO ) {
    ResponseDTO response = null;
    try {
      response = cetusControlProcess.sendEmail( sendMailRequestDTO );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      return createMessageFAILURE();
    }
    return response;
  }
  
}
