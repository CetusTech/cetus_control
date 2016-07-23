package co.com.cetus.cetuscontrol.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.com.cetus.cetuscontrol.dto.CommentTaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.common.validation.EmailValidator;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

/**
 * The Class CommentTaskMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (29/05/2016)
 */
@ManagedBean
@RequestScoped
public class CommentTaskMBean extends GeneralManagedBean implements Serializable {
  
  private static final long      serialVersionUID      = 1L;
                                                       
  private List< CommentTaskDTO > listComment           = null;
  private CommentTaskDTO         commentTaskDTO        = null;
  private boolean                showAddComment        = false;
  private boolean                disabledBtnAddComment = false;
                                                       
  public CommentTaskMBean () {
    commentTaskDTO = new CommentTaskDTO();
  }
  
  /**
   * </p> Initialize. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (29/05/2016)
   */
  @SuppressWarnings ( "unchecked" )
  @PostConstruct
  @Override
  public void initElement () {
    ResponseDTO responseDTO = null;
    try {
      if ( getUserDTO() != null ) {
        cleanObjectSession( "disabledBtnAddComment" );
        cleanObjectSession( "showAddComment" );
        
        TaskDTO task = ( TaskDTO ) getObjectSession( "selectedObject" );
        if ( task != null ) {
          if ( ( task.getStatus().getId() == ConstantWEB.STATUS_COMPLETED_VAL )
              || task.getStatus().getId() == ConstantWEB.STATUS_CANCELED_VAL ) {
           disabledBtnAddComment = true;
           addObjectSession( disabledBtnAddComment, "disabledBtnAddComment" );  
         }
          responseDTO = generalDelegate.findCommentByIdTask( task.getId() );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            listComment = ( List< CommentTaskDTO > ) responseDTO.getObjectResponse();
          } else {
            listComment = new ArrayList< >();
          }
          
          addObjectSession( listComment, "listComment" );
        }
      } else {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Validate add. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (12/06/2016)
   */
  public void validateAdd () {
    String[] vecEmail = null;
    String emailError = null;
    ResponseDTO responseDTO = null;
    String emailsOK = null;
    try {
      if ( commentTaskDTO != null ) {
        vecEmail = commentTaskDTO.getReceiver().split( ";" );
        if ( vecEmail != null && vecEmail.length > 0 ) {
          for ( String email: vecEmail ) {
            email = email.trim();
            if( email.length() > 0 ){
              if ( !EmailValidator.validate( email ) ) {
                if( emailError == null ){
                  emailError = email;
                }else{
                  emailError += "; " + email;
                }
              }else{
                if( emailsOK == null ){
                  emailsOK = email;
                }else{
                  emailsOK += ";"+email;
                }
              }
            }
          }
          cleanObjectSession( "showAddComment" );
          cleanObjectSession( "disabledBtnAddComment" );
          
          if ( emailError != null ) {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR + ConstantWEB.MESSAGE_ERROR_EMAIL_VALIDATOR, emailError );
            showAddComment = true;
            disabledBtnAddComment = true;
          } else {
            ConstantWEB.WEB_LOG.info( "Se procede a enviar el comentario..." );
            
            commentTaskDTO.setCreationDate( getCurrentDateTime() );
            TaskDTO task = ( TaskDTO ) getObjectSession( "selectedObject" );
            commentTaskDTO.setTask( task );
            commentTaskDTO.setEmitter( task.getPersonGroup().getPerson().getLastNames() + ", " + task.getPersonGroup().getPerson().getNames() + " ("
                                       + task.getPersonGroup().getPerson().getEmail() + ")" );
                                       
            ConstantWEB.WEB_LOG.info( commentTaskDTO.toString() );
            
            SendMailRequestDTO sendMailRequestDTO = new SendMailRequestDTO();
            sendMailRequestDTO.setUser( generalDelegate.getValueParameter( ConstantWEB.USER_WS_MESSAGE_SERVICE ) );
            sendMailRequestDTO.setPassword( generalDelegate.getValueParameter( ConstantWEB.PASSWORD_WS_MESSAGE_SERVICE ) );
            sendMailRequestDTO.setMessage( commentTaskDTO.getCommentT() );
            sendMailRequestDTO.setSubject( commentTaskDTO.getSubject() );
            sendMailRequestDTO.setRecipients( emailsOK.split( ";" ) );
            
            
            responseDTO = generalDelegate.sendEmail( sendMailRequestDTO );
            
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              commentTaskDTO.setReceiver( emailsOK );
              generalDelegate.create( commentTaskDTO );
              addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
              initElement();
              showAddComment = false;
              disabledBtnAddComment = false;
            } else {
              addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, ConstantWEB.MSG_DETAIL_ERROR );
            }
          }
          
          addObjectSession( showAddComment, "showAddComment" );
          addObjectSession( disabledBtnAddComment, "disabledBtnAddComment" );          
          
        }
      }
    } catch (Exception e ){
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  public void openComment () {
    commentTaskDTO = new CommentTaskDTO();
    showAddComment = true;
    disabledBtnAddComment = true;
    
    addObjectSession( showAddComment, "showAddComment" );
    addObjectSession( disabledBtnAddComment, "disabledBtnAddComment" );
    
  }

  
  public List< CommentTaskDTO > getListComment () {
    return listComment;
  }
  
  public void setListComment ( List< CommentTaskDTO > listComment ) {
    this.listComment = listComment;
  }
  
  @Override
  public String remove () {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String update () {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String add () {
    // TODO Auto-generated method stub
    return null;
  }
  
  public CommentTaskDTO getCommentTaskDTO () {
    return commentTaskDTO;
  }
  
  public void setCommentTaskDTO ( CommentTaskDTO commentTaskDTO ) {
    this.commentTaskDTO = commentTaskDTO;
  }
  
  public boolean isShowAddComment () {
    showAddComment = (boolean) (getObjectSession( "showAddComment" ) != null ? getObjectSession( "showAddComment" ) : false );
    return showAddComment;
  }
  
  public void setShowAddComment ( boolean showAddComment ) {
    this.showAddComment = showAddComment;
  }
  
  public boolean isDisabledBtnAddComment () {
    disabledBtnAddComment = (boolean) (getObjectSession( "disabledBtnAddComment" ) != null ? getObjectSession( "disabledBtnAddComment" ) : false );
    return disabledBtnAddComment;
  }
  
  public void setDisabledBtnAddComment ( boolean disabledBtnAddComment ) {
    this.disabledBtnAddComment = disabledBtnAddComment;
  }
  
}
