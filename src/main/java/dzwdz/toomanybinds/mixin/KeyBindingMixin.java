package dzwdz.toomanybinds.mixin;

import dzwdz.toomanybinds.mixinterface.KeyBindingMixinterface;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingMixinterface {
    @Shadow private int clickCount;

    @Override
    public void toomanybinds$press() {
        clickCount++;
    }
}
