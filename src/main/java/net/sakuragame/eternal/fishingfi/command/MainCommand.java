package net.sakuragame.eternal.fishingfi.command;

import com.taylorswiftcn.justwei.commands.JustCommand;
import net.sakuragame.eternal.fishingfi.command.sub.BindCommand;
import net.sakuragame.eternal.fishingfi.command.sub.HelpCommand;

public class MainCommand extends JustCommand {

    public MainCommand() {
        super(new HelpCommand());
        this.register(new BindCommand());
    }
}
