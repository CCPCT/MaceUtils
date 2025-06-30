package io.github.CCPCT.maceutils.MaceUtils.ModMenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import io.github.CCPCT.maceutils.MaceUtils.config.configScreen;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {return configScreen::getConfigScreen;}
}
