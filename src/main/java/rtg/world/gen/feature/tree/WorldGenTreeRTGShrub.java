package rtg.world.gen.feature.tree;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import rtg.config.rtg.ConfigRTG;

import java.util.Random;

import static net.minecraft.block.material.Material.plants;
import static net.minecraft.block.material.Material.vine;
import static net.minecraft.init.Blocks.*;
import static rtg.config.rtg.ConfigRTG.allowTreesToGenerateOnSand;

public class WorldGenTreeRTGShrub extends WorldGenerator {
    public boolean generate(World world, Random rand, BlockPos blockPos) {
        return this.generate(world, rand, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

	private int size;
	private Block logBlock;
	private int logMeta;
	private Block leaveBlock;
	private int leaveMeta;
	private boolean sand;

	public WorldGenTreeRTGShrub(int s, int log, int leav) {
		this(s, log, leav, false);
	}

	public WorldGenTreeRTGShrub(int s, int log, int leav, boolean sa) {
		size = s;
		sand = false;

		logBlock = log < 3 ? Blocks.log : log2;
		logMeta = log > 2 ? log - 3 : log;

		leaveBlock = leav < 4 ? Blocks.leaves : leaves2;
		leaveMeta = leav > 3 ? leav - 4 : leav;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		int width = size > 6 ? 6 : size;
		int height = size > 3 ? 2 : 1;

		for (int i = 0; i < size; i++) {
			int rX = rand.nextInt(width * 2) - width;
			int rY = rand.nextInt(height);
			int rZ = rand.nextInt(width * 2) - width;

			if (i == 0 && size > 4) {
				buildLeaves(world, x + rX, y, z + rZ, 3);
			} else if (i == 1 && size > 2) {
				buildLeaves(world, x + rX, y, z + rZ, 2);
			} else {
				buildLeaves(world, x + rX, y + rY, z + rZ, 1);
			}
		}
		return true;
	}

	public void buildLeaves(World world, int x, int y, int z, int size) {
		Block b = world.getBlockState(new BlockPos(x, y - 2, z)).getBlock();
		Block b1 = world.getBlockState(new BlockPos(x, y - 1, z)).getBlock();

		if ((b == Blocks.sand || b1 == Blocks.sand) && !allowTreesToGenerateOnSand) {
            return;
        }
		
		if(b.getMaterial() == Material.grass || b.getMaterial() == Material.ground || (sand && b.getMaterial() == Material.sand))
		{
			if (b1 != Blocks.water )
			{
                if (!ConfigRTG.allowShrubsToGenerateBelowSurface) {

                    if (b1.getMaterial() != Material.air &&
                        b1.getMaterial() != Material.vine &&
                        b1.getMaterial() != Material.plants &&
                        b1 != Blocks.snow_layer) {
                        return;
                    }
                }

				for(int i = -size; i <= size; i++)
				{
					for(int j = -1; j <= 1; j++)
					{
						for(int k = -size; k <= size; k++)
						{
							if(Math.abs(i) + Math.abs(j) + Math.abs(k) <= size)
							{
								buildBlock(world, x + i, y + j, z + k, leaveBlock, leaveMeta);
							}
						}
					}
				}
				world.setBlockState(new BlockPos(x, y - 1, z), logBlock.getStateFromMeta(logMeta), 0);
			}
		}
	}

	public void buildBlock(World world, int x, int y, int z, Block block, int meta) {
		Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();
		if (b.getMaterial() == Material.air || b.getMaterial() == vine || b.getMaterial() == plants || b == snow_layer) {
			world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(meta), 0);
		}
	}
}
