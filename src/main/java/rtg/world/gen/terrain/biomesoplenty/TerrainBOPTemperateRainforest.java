package rtg.world.gen.terrain.biomesoplenty;

import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.terrain.TerrainBase;

public class TerrainBOPTemperateRainforest extends TerrainBase
{
	public TerrainBOPTemperateRainforest()
	{
    
	}

	 @Override
	    public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river)
	    {
            return terrainPlains(x, y, simplex, river, 100f, 65f);
	    }
	}
