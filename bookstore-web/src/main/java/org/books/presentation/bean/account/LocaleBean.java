package org.books.presentation.bean.account;


import org.books.application.Country;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@SessionScoped
@Named("localeBean")
public class LocaleBean implements Serializable {

    private Locale locale;

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }
    
    public String getCountry() {
        return locale.getCountry();
    }

    /**
     * Sets the  {@code Locale} for a user
     *
     * @param language - ISO-639 language code
     */
    public void switchLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public List<Country> getCountries() {
        List<Country> result = new ArrayList<>();
        String[] countryCodeList = Locale.getISOCountries();
        
        for (String countryCode : countryCodeList) {
            Locale l = new Locale("", countryCode);
            Country country = new Country(countryCode, l.getDisplayCountry(locale));
            result.add(country);
        }
        //Sorting
        Collections.sort(result);
        return result;
    }
    
    public String getCountryDisplayName(String countryCode) {
        Locale l = new Locale("", countryCode);
        return l.getDisplayCountry(locale);
    }
    
}

