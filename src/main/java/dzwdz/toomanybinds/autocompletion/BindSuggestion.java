package dzwdz.toomanybinds.autocompletion;

import dzwdz.toomanybinds.mixinterface.KeyBindingMixinterface;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BindSuggestion {
    public ITextComponent name;
    public ITextComponent category;
    public KeyBinding bind;
    private String searchable;
    public boolean favorite = false;

    public BindSuggestion(KeyBinding bind) {
        this.bind = bind;
        name = new TranslationTextComponent(bind.getName());
        category = new TranslationTextComponent(bind.getCategory());
        searchable = (name.getString() + " " + category.getString()).toLowerCase();
    }

    public boolean matches(String[] searchTerms) {
        for (String term : searchTerms) {
            if (!searchable.contains(term)) return false;
        }
        return true;
    }

    public void execute() {
        Minecraft mc = Minecraft.getInstance();
        GameSettings options = mc.options;

        LauncherCompletion.addToHistory(getId());

        // workarounds for keybinds that are handled in dumb, incompatible ways
        if (bind == options.keyFullscreen) {
            mc.getWindow().toggleFullScreen();
            mc.options.fullscreen = mc.getWindow().isFullscreen();
            mc.options.save();
        } else if (bind == options.keyScreenshot) {
            ScreenShotHelper.grab(mc.gameDirectory, mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight(), mc.getMainRenderTarget(), (text) -> {
                mc.execute(() -> {
                    mc.gui.getChat().addMessage(text);
                });
            });
        } else {
            ((KeyBindingMixinterface) bind).toomanybinds$press(); // todo this probably isn't compatible with like 90% of forge mods
        }
    }

    public String getId() {
        return bind.getName();
    }
}
