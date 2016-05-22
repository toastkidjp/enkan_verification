import java.util.Objects;

import enkan.Application;
import enkan.Env;
import enkan.application.WebApplication;
import enkan.config.ApplicationFactory;
import enkan.middleware.ContentNegotiationMiddleware;
import enkan.middleware.ContentTypeMiddleware;
import enkan.middleware.CookiesMiddleware;
import enkan.middleware.DefaultCharsetMiddleware;
import enkan.middleware.MethodOverrideMiddleware;
import enkan.middleware.MultipartParamsMiddleware;
import enkan.middleware.NestedParamsMiddleware;
import enkan.middleware.NormalizationMiddleware;
import enkan.middleware.ParamsMiddleware;
import enkan.middleware.ResourceMiddleware;
import enkan.middleware.TraceMiddleware;
import enkan.middleware.metrics.MetricsMiddleware;
import enkan.middleware.session.JCacheStore;
import enkan.middleware.session.KeyValueStore;
import enkan.middleware.session.MemoryStore;
import enkan.system.inject.ComponentInjector;
import kotowari.middleware.ControllerInvokerMiddleware;
import kotowari.middleware.FormMiddleware;
import kotowari.middleware.RenderTemplateMiddleware;
import kotowari.middleware.RoutingMiddleware;
import kotowari.middleware.ValidateFormMiddleware;
import kotowari.routing.Routes;

/**
 *
 * @author Toast kid
 *
 */
public class AppFactory implements ApplicationFactory {

    @Override
    public Application create(final ComponentInjector injector) {
        final WebApplication app = new WebApplication();

        // Routing
        final Routes routes = Routes.define(r -> {
            r.get("/").to(Controller.class, "index");
        }).compile();

        // Enkan
        app.use(new DefaultCharsetMiddleware());
        app.use(new MetricsMiddleware<>());
        //app.use(NONE, new ServiceUnavailableMiddleware<>(new ResourceEndpoint("/public/html/503.html")));
        app.use(new TraceMiddleware<>());
        app.use(new ContentTypeMiddleware());
        app.use(new ParamsMiddleware());
        app.use(new MultipartParamsMiddleware());
        app.use(new MethodOverrideMiddleware());
        app.use(new NormalizationMiddleware());
        app.use(new NestedParamsMiddleware());
        app.use(new CookiesMiddleware());

        final KeyValueStore store = Objects.equals(Env.get("ENKAN_ENV"), "jcache") ? new JCacheStore() : new MemoryStore();

        app.use(new ContentNegotiationMiddleware());
        // Kotowari
        app.use(new ResourceMiddleware());
        app.use(new RenderTemplateMiddleware());
        app.use(new RoutingMiddleware(routes));
        app.use(new FormMiddleware());
        app.use(new ValidateFormMiddleware());
        app.use(new ControllerInvokerMiddleware(injector));

        return app;
    }
}
