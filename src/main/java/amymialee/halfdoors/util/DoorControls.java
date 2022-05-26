package amymialee.halfdoors.util;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.items.DoorFlipperItem;
import amymialee.halfdoors.mixin.KeyBindingAccessor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("SameParameterValue")
public class DoorControls {
    public static final Identifier FLIP = Halfdoors.id("flip_packet");
    public static final KeyBinding DOOR_FLIP = createSafeKeyMapping("key." + Halfdoors.MOD_ID + ".door_flip", GLFW.GLFW_MOUSE_BUTTON_MIDDLE);

    public static void keyInput(int key, int scancode) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (DOOR_FLIP.matchesKey(key, scancode)) {
                if (player.getItemCooldownManager().isCoolingDown(Halfdoors.DOOR_FLIPPER)) {
                    return;
                }
                if (!(MinecraftClient.getInstance().currentScreen == null)) {
                    System.out.println(MinecraftClient.getInstance().currentScreen);
                    return;
                }
                ItemStack flipper = DoorFlipperItem.getFlipper(player);
                if (flipper != null && DoorFlipperItem.readAmmo(flipper) > 0) {
                    ClientPlayNetworking.send(FLIP, PacketByteBufs.empty());
                }
            }
        }
    }

    private static KeyBinding createSafeKeyMapping(String description, int keycode) {
        InputUtil.Key key = InputUtil.Type.KEYSYM.createFromCode(keycode);
        KeyBinding oldMapping = KeyBindingAccessor.getKeyToBindings().get(key);
        KeyBinding keyMapping = new KeyBinding(description, keycode, "key.categories." + Halfdoors.MOD_ID);
        KeyBindingAccessor.getKeyToBindings().put(key, oldMapping);
        KeyBindingAccessor.getKeysByID().remove(description);
        return keyMapping;
    }
}
