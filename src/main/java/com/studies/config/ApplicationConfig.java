package com.studies.config;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/rest")
public class ApplicationConfig extends ResourceConfig {
	public ApplicationConfig() {
        packages("com.studies.restService");
    }
}
