package com.github.yungfasty.chunkcleaner.managers;

import com.github.yungfasty.chunkcleaner.commands.GiveCCCommand;
import com.github.yungfasty.chunkcleaner.utils.CommandRegistry;

public class CommandManager {

    public CommandManager() {

        CommandRegistry.registerCommand(new GiveCCCommand());

    }

}
