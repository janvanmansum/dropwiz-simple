package simple;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import simple.health.TemplateHealthCheck;
import simple.resources.HelloWorldResource;

public class DropwizSimpleApplication extends Application<DropwizSimpleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizSimpleApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizSimple";
    }

    @Override
    public void initialize(final Bootstrap<DropwizSimpleConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );

    }

    @Override
    public void run(final DropwizSimpleConfiguration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);    }

}
