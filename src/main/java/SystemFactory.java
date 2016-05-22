import static enkan.component.ComponentRelationship.component;
import static enkan.util.BeanBuilder.builder;

import enkan.Env;
import enkan.component.ApplicationComponent;
import enkan.component.metrics.MetricsComponent;
import enkan.component.undertow.UndertowComponent;
import enkan.config.EnkanSystemFactory;
import enkan.system.EnkanSystem;

/**
 *
 * @author Toast kid
 *
 */
public class SystemFactory implements EnkanSystemFactory {

    @Override
    public EnkanSystem create() {
        return EnkanSystem.of(
                "metrics", new MetricsComponent(),
                "app",     new ApplicationComponent(AppFactory.class.getSimpleName()),
                "http",    builder(new UndertowComponent())
                            .set(UndertowComponent::setPort, Env.getInt("PORT", 3000))
                            .build()
                ).relationships(
                        component("http").using("app"),
                        component("app").using("metrics")
                );
    }

}