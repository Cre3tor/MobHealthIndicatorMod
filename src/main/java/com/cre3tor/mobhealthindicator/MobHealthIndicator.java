package com.cre3tor.mobhealthindicator;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobHealthIndicator implements ModInitializer {

    public static final String MOD_ID = "mobhealthindicator";

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("MobHealthIndicator initialized.");
    }
}
