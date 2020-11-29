package com.github.beastyboo.realestate.application;

import org.bukkit.plugin.java.JavaPlugin;

public final class RealEstatePlugin extends JavaPlugin {

    private RealEstate core;

    @Override
    public void onEnable() {
        core = new RealEstate(this);
        core.load();

    }

    @Override
    public void onDisable() {
        core.close();
        core = null;
    }
}
