package svenhjol.charm.tweaks.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import svenhjol.meson.Feature;
import svenhjol.meson.registry.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;

public class DispensersUseAnyBow extends Feature
{
    @Override
    public String getDescription()
    {
        return "Changes the default dispenser recipe to allow damaged or enchanted bows.";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        ResourceLocation res = new ResourceLocation("minecraft:dispenser");
        RecipeHandler.removeRecipeByRegistryName(res);
        RecipeHandler.addShapedRecipe(res, ProxyRegistry.newStack(Blocks.DISPENSER, 1),
            "CCC", "CBC", "CRC",
            'C', ProxyRegistry.newStack(Blocks.COBBLESTONE, 1),
            'B', ProxyRegistry.newStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE),
            'R', ProxyRegistry.newStack(Items.REDSTONE, 1)
        );
    }
}
