package friend.capybara.module.mods;

import com.google.common.eventbus.Subscribe;

import friend.capybara.event.events.EventReadPacket;
import friend.capybara.mixin.IMinecraftClient;
import friend.capybara.module.Category;
import friend.capybara.module.Module;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;

public class AutoFish extends Module {

    public AutoFish() {
        super("AutoFish", KEY_UNBOUND, Category.PLAYER, "Automatically reels in fishes, just start fishing and it will handle the rest");
    }
    @Subscribe
    public void onReceivePacket(EventReadPacket event) {
        if (mc.player != null && (mc.player.getMainHandStack().getItem() == Items.FISHING_ROD || mc.player.getOffHandStack().getItem() == Items.FISHING_ROD) && event.getPacket() instanceof PlaySoundS2CPacket && SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.equals(((PlaySoundS2CPacket) event.getPacket()).getSound())) {
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((IMinecraftClient) mc).callDoItemUse();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((IMinecraftClient) mc).callDoItemUse();
            }).start();
        }
    }
}
