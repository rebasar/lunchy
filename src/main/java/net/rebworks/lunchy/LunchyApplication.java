package net.rebworks.lunchy;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.rebworks.lunchy.domain.io.HttpClient;
import net.rebworks.lunchy.injection.LunchyBinder;
import net.rebworks.lunchy.resources.LunchResource;

import javax.ws.rs.client.Client;
import java.text.DateFormat;

public class LunchyApplication extends Application<LunchyConfiguration> {
    public static void main(final String[] args) throws Exception {
        new LunchyApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<LunchyConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                                                                                new EnvironmentVariableSubstitutor()));
    }

    @Override
    public void run(final LunchyConfiguration lunchyConfiguration, final Environment environment) throws Exception {
        final Client client = new JerseyClientBuilder(environment).using(lunchyConfiguration.getJerseyClient())
                                                                  .build(getName());
        environment.jersey().register(new LunchyBinder(new HttpClient(client)));
        environment.jersey().register(LunchResource.class);
        environment.getObjectMapper().setDateFormat(DateFormat.getDateInstance());
        environment.getObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Override
    public String getName() {
        return "Lunchy";
    }
}
