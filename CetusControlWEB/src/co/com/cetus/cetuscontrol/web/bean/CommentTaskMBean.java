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

/**
 * The Class CommentTaskMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (29/05/2016)
 */
@ManagedBean
@RequestScoped
public class CommentTaskMBean extends GeneralManagedBean implements Serializable {
  
  private static final long      serialVersionUID = 1L;
                                                  
  private List< CommentTaskDTO > listComment      = null;
  
  private CommentTaskDTO commentTaskDTO = null;
                                                  
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
      if( getUserDTO() != null ){
        TaskDTO task = ( TaskDTO ) getObjectSession( "selectedObject" );
        if ( task != null ) {
          responseDTO = generalDelegate.findCommentByIdTask( task.getId() );
          if( UtilCommon.validateResponseSuccess( responseDTO ) ){
            listComment = ( List< CommentTaskDTO > ) responseDTO.getObjectResponse();
          }else{
            listComment = new ArrayList<>();
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
  
  public void validateAdd(){
    try {
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
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

  
  
}
