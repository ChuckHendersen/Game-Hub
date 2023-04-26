package it.uniroma3.siw.GameHub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


//Logger di richieste http (utile per capire cosa accade, stampa le richieste http eseguite sul sito con la risorsa richiesta)
@Configuration
public class RequestLoggingFilterConfig {

    @Bean //Cosa è un bean? :3
    CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}