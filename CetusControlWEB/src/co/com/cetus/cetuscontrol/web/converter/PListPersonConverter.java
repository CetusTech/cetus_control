package co.com.cetus.cetuscontrol.web.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;

/**
 * The Class PListPersonConverter.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (18/06/2015)
 */
@FacesConverter ( forClass = PersonDTO.class, value = "pListPersonConverter" )
public class PListPersonConverter implements Converter, Serializable {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
   */
  @SuppressWarnings ( "unchecked" )
  @Override
  public Object getAsObject ( FacesContext context, UIComponent component, String value ) {
    try {
      if ( component == null || value == null ) {
        return null;
      }
      DualListModel< PersonDTO > pickListModel = ( DualListModel< PersonDTO > ) ( ( PickList ) component ).getValue();
      PersonDTO ret = null;
      for ( PersonDTO personDTO: pickListModel.getSource() ) {
        if ( personDTO.getId() == ( Long.parseLong( value ) ) ) {
          ret = personDTO;
          break;
        }
      }
      if ( ret == null ) {
        for ( PersonDTO personDTO: pickListModel.getTarget() ) {
          if ( personDTO.getId() == ( Long.parseLong( value ) ) ) {
            ret = personDTO;
            break;
          }
        }
      }
      return ret;
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
   */
  @Override
  public String getAsString ( FacesContext context, UIComponent component, Object value ) {
    String ret = "";
    try {
      if ( value != null && value instanceof PersonDTO ) {
        return String.valueOf( ( ( PersonDTO ) value ).getId() );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return ret;
  }
  
}
