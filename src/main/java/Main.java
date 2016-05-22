import enkan.system.command.MetricsCommandRegister;
import enkan.system.devel.DevelCommandRegister;
import enkan.system.repl.PseudoRepl;
import enkan.system.repl.ReplBoot;
import enkan.system.repl.pseudo.ReplClient;
import kotowari.system.KotowariCommandRegister;

/**
 * @author Toast kid
 */
public class Main {
    public static void main(final String[] args) throws Exception {
        final PseudoRepl repl = new PseudoRepl(SystemFactory.class.getName());
        ReplBoot.start(
                repl,
                new KotowariCommandRegister(),
                new DevelCommandRegister(),
                new MetricsCommandRegister()
                );

        final ReplClient replClient = new ReplClient();
        replClient.start(repl.getPort().get());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {replClient.close();}));
    }
}