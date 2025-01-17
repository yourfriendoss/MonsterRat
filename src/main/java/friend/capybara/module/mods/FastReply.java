package friend.capybara.module.mods;

import com.google.common.eventbus.Subscribe;

import friend.capybara.command.Command;
import friend.capybara.event.events.EventReadPacket;
import friend.capybara.event.events.EventSendPacket;
import friend.capybara.module.Category;
import friend.capybara.module.Module;
import friend.capybara.setting.base.SettingToggle;
import friend.capybara.utils.CapyLogger;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class FastReply extends Module {

    private String name;

    public FastReply() {
        super("FastReply", KEY_UNBOUND, Category.CHAT, "Easy and fast reply to direct messages",
                new SettingToggle("Notify", true));
    }

    @Subscribe
    public void message(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameMessageS2CPacket)) return;
        String msg = ((GameMessageS2CPacket) e.getPacket()).getMessage().getString().toLowerCase();
        for (PlayerListEntry p : mc.player.networkHandler.getPlayerList()) {
            if (p.getProfile() == mc.player.getGameProfile()) continue;
            if (msg.startsWith(p.getProfile().getName().toLowerCase() + " whispers")
                    || msg.startsWith("[" + p.getProfile().getName().toLowerCase() + " ->")) {
                name = p.getProfile().getName();
                if (getSetting(0).asToggle().state) CapyLogger.infoMessage("The next message will be sent to " + p.getProfile().getName());
            }
        }
    }

    @Subscribe
    public void send(EventSendPacket e) {
        if (!(e.getPacket() instanceof ChatMessageC2SPacket)) return;
        String msg = ((ChatMessageC2SPacket) e.getPacket()).getChatMessage();
        if (msg.startsWith("/") || msg.startsWith(Command.PREFIX) || name == null) return;
        mc.player.sendChatMessage("/msg " + name + " " + msg);
        name = null;
        e.setCancelled(true);
    }
}
