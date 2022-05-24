package amymialee.halfdoors;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class HalfdoorsPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}
