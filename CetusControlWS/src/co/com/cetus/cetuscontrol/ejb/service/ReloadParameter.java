
package co.com.cetus.cetuscontrol.ejb.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para reloadParameter complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="reloadParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reloadParameterRequestDTO" type="{http://service.ejb.cetuscontrol.cetus.com.co/}reloadParameterRequestDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reloadParameter", propOrder = {
    "reloadParameterRequestDTO"
})
public class ReloadParameter {

    protected ReloadParameterRequestDTO reloadParameterRequestDTO;

    /**
     * Obtiene el valor de la propiedad reloadParameterRequestDTO.
     * 
     * @return
     *     possible object is
     *     {@link ReloadParameterRequestDTO }
     *     
     */
    public ReloadParameterRequestDTO getReloadParameterRequestDTO() {
        return reloadParameterRequestDTO;
    }

    /**
     * Define el valor de la propiedad reloadParameterRequestDTO.
     * 
     * @param value
     *     allowed object is
     *     {@link ReloadParameterRequestDTO }
     *     
     */
    public void setReloadParameterRequestDTO(ReloadParameterRequestDTO value) {
        this.reloadParameterRequestDTO = value;
    }

}
