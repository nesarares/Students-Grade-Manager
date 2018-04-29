package UI.Console.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import Utils.Utils;

public class MenuCommand implements Command {
    private String menu_name;
    private Map<String, Command> commands = new TreeMap<>();

    public MenuCommand(String menu_name) {
        this.menu_name = menu_name;
    }

    @Override
    public void execute() {
        commands.keySet().forEach((key) -> {
            System.out.printf("║ %-" + Utils.menulength + "s ║\n", key);
        });
    }

    public void addCommand(String command_name, Command command) {
        commands.put(command_name, command);
    }

    public List<Command> getCommands () {
        return new ArrayList<>(commands.values());
    }

    public String getMenuName() {
        return menu_name;
    }
}
