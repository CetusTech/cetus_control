package co.com.cetus.cetuscontrol.ejb.bean;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

/**
 * The Interface CetusControlBeanRemote.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
@Remote
public interface CetusControlBeanRemote {
  
  public ResponseDTO create ( Object pDTO );
  
  public ResponseDTO edit ( Object dto );
  
  public ResponseDTO remove ( Object dto );
  
  public ResponseDTO findAll ( String classDTO, String fieldOrder, String typeOrder );
  
  public ResponseDTO findAll ( String classDTO );
  
  public ResponseDTO find ( String classDTO, Object id );
  
  public ResponseDTO findRange ( String classDTO, int[] arrRange );
  
  public ResponseDTO count ( String classDTO );
  
  public String getValueParameter ( String nameParameter );
  
  public ResponseDTO searchUserLogged ( String userCetus );
  
  public ResponseDTO findClientByClientCetus ( int pIdClientCetus );
  
  public ResponseDTO findRolByApplication ( int idApp );
  
  public ResponseDTO findStatus ( int idclientCetus );
  
  public ResponseDTO findTaskType ( int idclientCetus );
  
  public ResponseDTO findParameterGeneralByClientCetus ( int idClientCetus );
  
  public ResponseDTO findWorkDayByClientCetus ( int idClientCetus );
  
  public ResponseDTO createUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol );
  
  public ResponseDTO deleteUserCetus ( UserPortalDTO request );
  
  public ResponseDTO findExcepWorkdayByClientCetus ( int idClientCetus );
  
  public ResponseDTO findEmailsFromPerson ( int idclientCetus );
  
  public ResponseDTO findAreaByClientCetus ( int idClientCetus );
  
  public ResponseDTO generateCodeClient ();
  
  public ResponseDTO generateCodePerson ();
  
  public ResponseDTO findUserByPerson ( int idPerson );
  
  public ResponseDTO findRolByLogin ( String request );
  
  public ResponseDTO updateUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol );
  
  public ResponseDTO findGroupByClientCetus ( int pIdClientCetus );
  
  public ResponseDTO findPersonByClient ( int pIdClient );
  
  public ResponseDTO findAllGroupTypeByClientCetus ( int pIdClientCetus );
  
  public ResponseDTO findGroupByClient ( int pIdClient );
  
  public ResponseDTO findGroupByPerson ( int pIdPerson );
  
  public ResponseDTO findTaskByClient ( int pIdClient );
  
  public ResponseDTO findGroupType ( int idclientCetus );
  
  public ResponseDTO findPersonByGroup ( int idGroup );
  
  public ResponseDTO findPriorityByClientCetus ( int idClientCetus );
  
  public ResponseDTO findPersonGroupByPerson ( int pIdPerson );
  
  public ResponseDTO findTaskByPersonGroup ( int pIdGroup, int pIdPerson );
  
  public ResponseDTO generateCodeTask ();
  
  public ResponseDTO getTemplateTaskMassive ( int idClientCetus, int idClient );
  
  public ResponseDTO findPersonGroup ( int idGroup, String personIdentity );
  
  public ResponseDTO findTaskByPerson ( int pIdPerson );
  
  public double getPercentageCurrent ( int idPerson );
  
  public ResponseDTO findProgressTaskByPerson ( int pIdPerson, int pidClientCetus );
  
  public ResponseDTO generateLengthTask ( int idTask );
  
  public ResponseDTO findTaskByPersonPriority ( int pIdPerson, String descripPriority );
  
  public ResponseDTO findTaskByPersonCompleted ( int pIdPerson, int pidClientCetus );
  
  public ResponseDTO findTaskByPersonNexOvercome ( int pIdPerson, int pidClientCetus );
  
  public ResponseDTO findTaskByPersonRun ( int pIdPerson, int pidClientCetus );
  
  public ResponseDTO findTaskByPersonExpired ( int pIdPerson, int pidClientCetus );
  
  public ResponseDTO findAttachmentFilesByTaskId ( long pIdTask );
  
  public ResponseDTO findNotificationGenClientCetus ( int idClientCetus );
  
  public ResponseDTO findNotificationByGroup ( int idGroup );
  
  public ResponseDTO existsJorndInDay ( long pIdCetus, String pDay );
  
  public ResponseDTO findNotificationByGroupGen ( int idGroup, int idGeneral );
  
  public ResponseDTO isTimeValid ( long pIdCetus, String pDay, Date pTime );
  
  public ResponseDTO generateReportViewTask ( long pIdTask, String pFormatPattern );
  
  public ResponseDTO findTaskHistory ( int idTask );
  
  public ResponseDTO findTaskTypeByArea ( int idArea );
  
  public ResponseDTO findAllTraceTaskByTaskId ( int pIdTask );
  
  public ResponseDTO findStatusById ( int idStatus );
  
  public ResponseDTO findTaskByFilter ( int idClient, String filter, String inputFilter );
  
  public ResponseDTO findCommentByIdTask ( int idTask );
  
  public ResponseDTO sendEmail ( SendMailRequestDTO sendMailRequestDTO );
}
