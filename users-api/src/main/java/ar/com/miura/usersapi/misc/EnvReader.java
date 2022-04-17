package ar.com.miura.usersapi.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvReader {

    @Autowired
    private Environment env;

    public String getProperty(String name) {
        return this.env.getProperty(name, "");
    }

}
