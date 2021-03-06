package rtg.config;

import java.io.File;

import rtg.config.abyssalcraft.ConfigAC;
import rtg.config.biomesoplenty.ConfigBOP;
import rtg.config.buildcraft.ConfigBC;
import rtg.config.rtg.ConfigRTG;
import rtg.config.thaumcraft.ConfigTC;
import rtg.config.vanilla.ConfigVanilla;

public class ConfigManager
{
    
    public static File rtgConfigFile;
    public static File vanillaConfigFile;
    public static File bopConfigFile;
    public static File tcConfigFile;
    public static File bcConfigFile;
    public static File acConfigFile;

    private ConfigRTG configRTG = new ConfigRTG();
    public ConfigRTG rtg() {
        return configRTG;
    }
    
    public static void init(String configpath)
    {
    
        rtgConfigFile = new File(configpath + "rtg.cfg");
        vanillaConfigFile = new File(configpath + "biomes/vanilla.cfg");
        bopConfigFile = new File(configpath + "biomes/biomesoplenty.cfg");
        tcConfigFile = new File(configpath + "biomes/thaumcraft.cfg");
        bcConfigFile = new File(configpath + "biomes/buildcraft.cfg");
        acConfigFile = new File(configpath + "biomes/abyssalcraft.cfg");
        
        ConfigRTG.init(rtgConfigFile);

        ConfigVanilla.init(vanillaConfigFile);

        ConfigBOP.init(bopConfigFile);
        ConfigTC.init(tcConfigFile);
        ConfigBC.init(bcConfigFile);
        ConfigAC.init(acConfigFile);
    }
}
