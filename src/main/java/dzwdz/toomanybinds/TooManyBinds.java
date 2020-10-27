package dzwdz.toomanybinds;

import dzwdz.toomanybinds.autocompletion.LauncherCompletion;
import dzwdz.toomanybinds.autocompletion.VanillaKeybindSuggestions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod(TooManyBinds.MODID)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class TooManyBinds {
    public static final String MODID = "toomanybinds";

    public static ModConfig config;

    public static KeyBinding launcherKey;
    public static KeyBinding favoriteKey;

    public TooManyBinds() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::onClientInitialize);
    }

    public void onClientInitialize() {
        //AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new); todo
        //config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        config = new ModConfig();

        launcherKey = new KeyBinding(
                "key.toomanybinds.launcher",
                GLFW.GLFW_KEY_H,
                "category.toomanybinds"
        );
        ClientRegistry.registerKeyBinding(launcherKey);
        favoriteKey = new KeyBinding(
                "key.toomanybinds.favorite",
                GLFW.GLFW_KEY_F4,
                "category.toomanybinds"
        );
        ClientRegistry.registerKeyBinding(favoriteKey);

        LauncherCompletion.loadData();

        LauncherCompletion.suggestionProviders.add(new VanillaKeybindSuggestions());
    }

    @SubscribeEvent
    public static void keyInputEvent(InputEvent.KeyInputEvent event) {
        if (launcherKey.consumeClick())
            Minecraft.getInstance().setScreen(new LauncherScreen());
    }

    @SubscribeEvent
    public static void logout(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        LauncherCompletion.saveData(); // todo, this might not be the correct event
    }
}
