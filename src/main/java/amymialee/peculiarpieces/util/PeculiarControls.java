package amymialee.peculiarpieces.util;

import amymialee.peculiarpieces.mixin.KeyBindingAccessor;
import amymialee.peculiarpieces.PeculiarPieces;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("SameParameterValue")
public class PeculiarControls {
    public static final KeyBinding VISIBILITY = createSafeKeyMapping("key.%s.visibility".formatted(PeculiarPieces.MOD_ID), GLFW.GLFW_KEY_B);
    private static boolean visibilityHeld = false;

    public static void keyInput(int key, int scancode) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (VISIBILITY.matchesKey(key, scancode)) {
                if (!visibilityHeld) {
                    PeculiarPieces.visible = !PeculiarPieces.visible;
                    MinecraftClient.getInstance().worldRenderer.reload();
                    visibilityHeld = true;
                }
            } else {
                visibilityHeld = false;
            }
        }
    }

    private static KeyBinding createSafeKeyMapping(String description, int keycode) {
        InputUtil.Key key = InputUtil.Type.KEYSYM.createFromCode(keycode);
        KeyBinding oldMapping = KeyBindingAccessor.getKeyToBindings().get(key);
        KeyBinding keyMapping = new KeyBinding(description, keycode, "category.%s".formatted(PeculiarPieces.MOD_ID));
        KeyBindingAccessor.getKeyToBindings().put(key, oldMapping);
        KeyBindingAccessor.getKeysByID().remove(description);
        return keyMapping;
    }
}
