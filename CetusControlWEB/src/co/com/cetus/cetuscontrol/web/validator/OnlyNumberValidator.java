package co.com.cetus.cetuscontrol.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import co.com.cetus.cetuscontrol.web.util.ConstantWEB;

/**
 * The Class OnlyNumberValidator.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (10/12/2014)
 */
@FacesValidator ( value = "onlyNumberValidator" )
public class OnlyNumberValidator implements Validator {
  
  /**
   * </p> Instancia un nuevo only number validator. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (10/12/2014)
   */
  public OnlyNumberValidator () {
  }
  
  /* (non-Javadoc)
   * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
   */
  @Override
  public void validate ( FacesContext context, UIComponent component, Object object ) throws ValidatorException {
    boolean valid = false;
    if ( object != null ) {
      valid = object.toString().matches( "(\\p{Digit})*" );
      if ( !valid ) {
        throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, ConstantWEB.VALIDATION_ONLY_NUMBER, null ) );
      }
    }
  }
}
