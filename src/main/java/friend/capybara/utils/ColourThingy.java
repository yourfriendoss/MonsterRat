package friend.capybara.utils;

import friend.capybara.module.ModuleManager;
import friend.capybara.module.mods.ColourChooser;

public class ColourThingy {
    public static int guiColour() {
        if (ModuleManager.getModule(ColourChooser.class).getSetting(0).asToggle().state) return Rainbow.getInt();
        int red;
        int green;
        int blue;
        red = (int) ModuleManager.getModule(ColourChooser.class).getSetting(1).asSlider().getValue();
        green = (int) ModuleManager.getModule(ColourChooser.class).getSetting(2).asSlider().getValue();
        blue = (int) ModuleManager.getModule(ColourChooser.class).getSetting(3).asSlider().getValue();
        return (0xff << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
    }
    public static int textColor() {
        int red;
        int green;
        int blue;
        red = (int) ModuleManager.getModule(ColourChooser.class).getSetting(4).asSlider().getValue();
        green = (int) ModuleManager.getModule(ColourChooser.class).getSetting(5).asSlider().getValue();
        blue = (int) ModuleManager.getModule(ColourChooser.class).getSetting(6).asSlider().getValue();
        return (0xff << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
    }
}
