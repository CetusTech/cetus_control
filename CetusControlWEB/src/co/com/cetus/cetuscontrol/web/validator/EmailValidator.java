package co.com.cetus.cetuscontrol.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator ( value = "emailValidator" )
public class EmailValidator implements Validator {
  
  /** The Constant EMAIL_PATTERN. */
  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
                                              "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
                                              "(\\.[A-Za-z]{2,})$";
  
  /** The pattern. */
  private Pattern             pattern;
  
  /** The matcher. */
  private Matcher             matcher;
  
  public EmailValidator () {
    pattern = Pattern.compile( EMAIL_PATTERN );
  }
  
  /* (non-Javadoc)
   * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
   */
  @Override
  public void validate ( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
    
    matcher = pattern.matcher( value.toString() );
    if ( !matcher.matches() ) {      
      FacesMessage msg = new FacesMessage( "E-mail Fallo la Validaci\u00f3n.", "No cumple con el formato de correo." );
      msg.setSeverity( FacesMessage.SEVERITY_ERROR );
      throw new ValidatorException( msg );
    }
  }
}
