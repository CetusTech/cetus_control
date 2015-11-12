package co.com.cetus.cetuscontrol.web.bean.dialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean ( name = "dfCarsView" )
@ViewScoped
public class DFCarsView implements Serializable {
  
  /**
   * 
   */
  private static final long  serialVersionUID = 1L;
  private String             id;
  private String             name;
  private List< DFCarsView > lista;
  
  public DFCarsView () {
    lista = new ArrayList< DFCarsView >();
    DFCarsView a = new DFCarsView();
    a.setId( "1" );
    a.setName( "a" );
    lista.add( a );
  }
  
  public String getId () {
    return id;
  }
  
  public void setId ( String id ) {
    this.id = id;
  }
  
  public String getName () {
    return name;
  }
  
  public void setName ( String name ) {
    this.name = name;
  }
  
  public List< DFCarsView > getLista () {
    return lista;
  }
  
  public void setLista ( List< DFCarsView > lista ) {
    this.lista = lista;
  }
  
}
