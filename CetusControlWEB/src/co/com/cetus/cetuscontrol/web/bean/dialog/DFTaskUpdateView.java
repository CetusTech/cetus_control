package co.com.cetus.cetuscontrol.web.bean.dialog;

import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import org.primefaces.context.RequestContext;

@ManagedBean
public class DFTaskUpdateView {
  
  public void view () {
    RequestContext.getCurrentInstance().openDialog( "update" );
  }
  
  public void viewCustomized () {
    Map< String, Object > options = new HashMap< String, Object >();
    options.put( "modal", true );
    options.put( "draggable", false );
    options.put( "resizable", false );
    options.put( "contentHeight", 320 );
    
    RequestContext.getCurrentInstance().openDialog( "update", options, null );
  }
}
