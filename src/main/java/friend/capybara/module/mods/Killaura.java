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
package friend.capybara.module.mods;

import com.google.common.collect.Streams;
import com.google.common.eventbus.Subscribe;

import friend.capybara.Capybara;
import friend.capybara.event.events.EventTick;
import friend.capybara.module.Category;
import friend.capybara.module.Module;
import friend.capybara.setting.base.SettingSlider;
import friend.capybara.setting.base.SettingToggle;
import friend.capybara.setting.other.SettingRotate;
import friend.capybara.utils.EntityUtils;
import friend.capybara.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;

import java.util.List;
import java.util.stream.Collectors;

public class Killaura extends Module {

    private int delay = 0;
    double y = -1;

    public Killaura() {
        super("Killaura", KEY_UNBOUND, Category.COMBAT, "Automatically attacks entities",
                new SettingToggle("Players", true),
                new SettingToggle("Mobs", false),
                new SettingToggle("Animals", false),
                new SettingToggle("Armor Stands", false),
                new SettingRotate(false),
                new SettingToggle("Thru Walls", true),
                new SettingToggle("1.9 Delay", true),
                new SettingSlider("Range", 0, 6, 4.25, 2),
                new SettingSlider("CPS", 0, 20, 8, 0),
                new SettingToggle("OnlySword", false),
                new SettingToggle("OnlyCritical", false));
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (getSetting(9).asToggle().state && !(mc.player.inventory.getMainHandStack().getItem() instanceof SwordItem)) return;
        delay++;
        int reqDelay = (int) Math.round(20 / getSetting(8).asSlider().getValue());

        List<Entity> targets = Streams.stream(mc.world.getEntities())
                .filter(e -> (e instanceof PlayerEntity && getSetting(0).asToggle().state
                        && !Capybara.friendMang.has(e.getName().asString()))
                        || (e instanceof Monster && getSetting(1).asToggle().state)
                        || (EntityUtils.isAnimal(e) && getSetting(2).asToggle().state)
                        || (e instanceof ArmorStandEntity && getSetting(3).asToggle().state))
                .sorted((a, b) -> Float.compare(a.distanceTo(mc.player), b.distanceTo(mc.player))).collect(Collectors.toList());

        for (Entity e : targets) {
            if (mc.player.distanceTo(e) > getSetting(7).asSlider().getValue()
                    || ((LivingEntity) e).getHealth() <= 0 || e.getEntityName().equals(mc.getSession().getUsername()) || e == mc.player.getVehicle()
                    || (!mc.player.canSee(e) && !getSetting(5).asToggle().state)) continue;

            if (getSetting(4).asRotate().state) {
                WorldUtils.facePosAuto(e.getX(), e.getY() + e.getHeight() / 2, e.getZ(), getSetting(4).asRotate());
            }

            if (((delay > reqDelay || reqDelay == 0) && !getSetting(6).asToggle().state) ||
                    (mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f && getSetting(6).asToggle().state)) {
                boolean wasSprinting = mc.player.isSprinting();

                if (wasSprinting)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));

                if (!getSetting(10).asToggle().state || (mc.player.getY() < y && mc.player.getY() != Math.floor(mc.player.getY()) && !mc.player.isCreative())) {
                    mc.interactionManager.attackEntity(mc.player, e);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }

                if (wasSprinting)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_SPRINTING));

                delay = 0;
            }
        }

        y = mc.player.getY();
    }
}
