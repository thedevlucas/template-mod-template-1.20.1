package com.example.mixin.client;

import com.example.Utilities;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HideToolbar {
	@Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
	private void init(CallbackInfo info) {
		if (!Utilities.shouldRender){
			info.cancel();
		}
	}
}