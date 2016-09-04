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
@FacesConverter(value = "toUpperConverter")
@ManagedBean
@RequestScoped
public class ToUpperConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToUpperConverter.class);
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        return (submittedValue != null) ? submittedValue.toUpperCase() : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        return (modelValue != null) ? modelValue.toString() : null;
    }

}
