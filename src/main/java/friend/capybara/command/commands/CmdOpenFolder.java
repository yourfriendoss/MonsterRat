/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package friend.capybara.command.commands;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.nio.file.Paths;

import friend.capybara.command.Command;
import friend.capybara.utils.CapyLogger;
import net.minecraft.client.MinecraftClient;

public class CmdOpenFolder extends Command {
	MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public String getAlias() {
		return "openfolder";
	}

	@Override
	public String getDescription() {
		return "opens bleach folder";
	}

	@Override
	public String getSyntax() {
		return "openfolder";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		CapyLogger.infoMessage("Opening bleach folder");
		if (!GraphicsEnvironment.isHeadless()) {
			System.setProperty("java.awt.headless", "false");
		}
		Desktop.getDesktop().open(new File(String.valueOf(Paths.get(client.runDirectory.getPath(), "bleach/"))));
	}

}
