package friend.capybara.utils;

import com.google.common.eventbus.Subscribe;

import friend.capybara.event.events.EventTick;

import java.awt.*;

public class Rainbow {

    public static int rgb;
    public static float hue = 0.0F;
    public static int speed = 2;

    @Subscribe
    public void onTick(EventTick event) {
        rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
        hue += speed / 2000.0F;
    }

    public static int getInt() {
        return Rainbow.rgb;
    }
}
