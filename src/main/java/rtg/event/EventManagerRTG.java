package rtg.event;

import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rtg.RTG;
import rtg.config.rtg.ConfigRTG;
import rtg.util.Logger;
import rtg.world.WorldTypeRTG;
import rtg.world.biome.WorldChunkManagerRTG;
import rtg.world.biome.realistic.RealisticBiomeBase;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBase;
import rtg.world.gen.MapGenCavesRTG;
import rtg.world.gen.MapGenRavineRTG;
import rtg.world.gen.genlayer.RiverRemover;
import rtg.world.gen.structure.MapGenScatteredFeatureRTG;
import rtg.world.gen.structure.MapGenVillageRTG;
import rtg.world.gen.structure.StructureOceanMonumentRTG;

public class EventManagerRTG
{

    public RealisticBiomeBase biome = null;
    
    public EventManagerRTG()
    {
        MapGenStructureIO.registerStructure(MapGenScatteredFeatureRTG.Start.class, "rtg_MapGenScatteredFeatureRTG");
        if (ConfigRTG.enableVillageModifications) MapGenStructureIO.registerStructure(MapGenVillageRTG.Start.class, "rtg_MapGenVillageRTG");
        MapGenStructureIO.registerStructure(StructureOceanMonumentRTG.StartMonument.class, "rtg_MapGenOceanMonumentRTG");
    }
    
	@SubscribeEvent(priority = EventPriority.LOW)
	public void eventListenerRTG(InitMapGenEvent event) {
	    
        Logger.debug("event type = %s", event.type.toString());
        Logger.debug("event originalGen = %s", event.originalGen.toString());
	    
		if (event.type == InitMapGenEvent.EventType.SCATTERED_FEATURE) {
			event.newGen = new MapGenScatteredFeatureRTG();
		}
        else if (event.type == InitMapGenEvent.EventType.VILLAGE) {
            
            if (ConfigRTG.enableVillageModifications) {
                event.newGen = new MapGenVillageRTG();
            }
        }
        else if (event.type == InitMapGenEvent.EventType.CAVE) {
            
            if (ConfigRTG.enableCaveModifications) {
                
                event.newGen = new MapGenCavesRTG();
            }
        }
        else if (event.type == InitMapGenEvent.EventType.RAVINE) {
            
            if (ConfigRTG.enableRavineModifications) {
                
                event.newGen = new MapGenRavineRTG();
            }
        }
        else if (event.type == InitMapGenEvent.EventType.OCEAN_MONUMENT) {
                event.newGen = new StructureOceanMonumentRTG();
        }
        Logger.debug("event newGen = %s", event.newGen.toString());
	}
	
    @SubscribeEvent
    public void eventListenerRTG(WorldEvent.Load event) {

        if (!(event.world.getWorldInfo().getTerrainType() instanceof WorldTypeRTG)) {
            
            MinecraftForge.TERRAIN_GEN_BUS.unregister(RTG.eventMgr);
            MinecraftForge.ORE_GEN_BUS.unregister(RTG.eventMgr);
            MinecraftForge.EVENT_BUS.unregister(RTG.eventMgr);
        }
    }

    @SubscribeEvent
    public void onGenerateMinable(OreGenEvent.GenerateMinable event) {

        switch (event.type) {
            
            case COAL:
                
                if (!ConfigRTG.generateOreCoal) {
                    event.setResult(Result.DENY);
                }
                
                break;
            
            case IRON:
                
                if (!ConfigRTG.generateOreIron) {
                    event.setResult(Result.DENY);
                }
                
                break;
            
            case GOLD:
                
                if (!ConfigRTG.generateOreGold) {
                    event.setResult(Result.DENY);
                }
                
                break;
            
            case DIAMOND:
                
                if (!ConfigRTG.generateOreDiamond) {
                    event.setResult(Result.DENY);
                }
                
                break;
            
            case REDSTONE:
                
                if (!ConfigRTG.generateOreRedstone) {
                    event.setResult(Result.DENY);
                }
                
                break;
            
            case LAPIS:
                
                if (!ConfigRTG.generateOreLapis) {
                    event.setResult(Result.DENY);
                }
                
                break;
            
            default:
                break;
        }
    }

    @SubscribeEvent
    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        
        // only handle RTG world type
        if (!event.worldType.getWorldTypeName().equalsIgnoreCase("RTG")) return;

        boolean stripRivers = true; // This used to be a config option. Hardcoding until we have a need for the option.
        
        if (stripRivers) {
            try {
                event.newBiomeGens = new RiverRemover().riverLess(event.originalBiomeGens);
            } catch (ClassCastException ex) {
                //throw ex;
                // failed attempt because the GenLayers don't end with GenLayerRiverMix
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        
        if (!event.world.getWorldInfo().getTerrainType().getWorldTypeName().equalsIgnoreCase("RTG")) {
            return;
        }
        
        if (event.world.provider.getDimensionId() == 0) {
            
            Logger.info("World Seed: %d", event.world.getSeed());
        }
    }

    @SubscribeEvent
    public void onGetVillageBlockID(BiomeEvent.GetVillageBlockID event)
    {
        RealisticBiomeBase biomeReal;
        if (!ConfigRTG.enableVillageModifications) {
            return;
        }
        if (event.biome instanceof RealisticBiomeBase) {
            biomeReal = (RealisticBiomeBase) event.biome;
        }
        else if (event.biome == null && this.biome != null) {
            biomeReal = this.biome;
        }
        else {
            return;
        }
        if (biomeReal.biomeID == RealisticBiomeVanillaBase.vanillaDesert.biomeID ||
                biomeReal.biomeID == RealisticBiomeVanillaBase.vanillaDesertHills.biomeID ||
                biomeReal.biomeID == RealisticBiomeVanillaBase.vanillaDesertM.biomeID
                )
        {
            if (event.original.getBlock() == Blocks.log || event.original.getBlock() == Blocks.log2)
            {
                event.replacement = Blocks.sandstone.getDefaultState();
            }

            if (event.original.getBlock() == Blocks.cobblestone)
            {
                event.replacement = Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
            }

            if (event.original.getBlock() == Blocks.planks)
            {
                event.replacement = Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
            }

            if (event.original.getBlock() == Blocks.oak_stairs)
            {
                event.replacement = Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, event.original.getValue(BlockStairs.FACING));
            }

            if (event.original.getBlock() == Blocks.stone_stairs)
            {
                event.replacement = Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, event.original.getValue(BlockStairs.FACING));
            }

            if (event.original.getBlock() == Blocks.gravel)
            {
                event.replacement = Blocks.sandstone.getDefaultState();
            }
        }
        // The event has to be cancelled in order to override the original block.
        if (event.replacement != null) {
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public void preBiomeDecorate(DecorateBiomeEvent.Pre event)
    {

        //Are we in an RTG world? Do we have RTG's chunk manager?
        if (event.world.getWorldInfo().getTerrainType() instanceof WorldTypeRTG && event.world.getWorldChunkManager() instanceof WorldChunkManagerRTG) {
            
            WorldChunkManagerRTG cmr = (WorldChunkManagerRTG) event.world.getWorldChunkManager();
            this.biome = cmr.getBiomeDataAt(event.pos.getX(), event.pos.getZ());
        }
    }
}