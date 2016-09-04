package org.tcc.relatorio.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jose Wdison
 */
@FacesConverter(value = "toLowerConverter")
@ManagedBean
@RequestScoped
public class ToLowerConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToLowerConverter.class);
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        return (submittedValue != null) ? submittedValue.toLowerCase() : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        return (modelValue != null) ? modelValue.toString() : null;
    }

}
