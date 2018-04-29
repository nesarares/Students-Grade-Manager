package UI.Console.Menu;

@FunctionalInterface
public interface Command {

    /**
     * Executa comanda curenta
     */
    void execute();
}
