package mekanism.generators.common;

import mekanism.generators.client.GeneratorsBlockStateProvider;
import mekanism.generators.client.GeneratorsItemModelProvider;
import mekanism.generators.client.GeneratorsLangProvider;
import mekanism.generators.client.GeneratorsSoundProvider;
import mekanism.generators.common.loot.GeneratorsLootProvider;
import mekanism.patchouli.generators.GeneratorsBookProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = MekanismGenerators.MODID, bus = Bus.MOD)
public class GeneratorsDataGenerator {

    private GeneratorsDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeClient()) {
            //Client side data generators
            gen.addProvider(new GeneratorsLangProvider(gen));
            gen.addProvider(new GeneratorsSoundProvider(gen, existingFileHelper));
            //Let the blockstate provider see models generated by the item model provider
            GeneratorsItemModelProvider itemModelProvider = new GeneratorsItemModelProvider(gen, existingFileHelper);
            gen.addProvider(itemModelProvider);
            gen.addProvider(new GeneratorsBlockStateProvider(gen, itemModelProvider.existingFileHelper));
        }
        if (event.includeServer()) {
            //Server side data generators
            gen.addProvider(new GeneratorsTagProvider(gen, existingFileHelper));
            gen.addProvider(new GeneratorsLootProvider(gen));
            gen.addProvider(new GeneratorsRecipeProvider(gen, existingFileHelper));
        }
        gen.addProvider(new GeneratorsBookProvider(gen));
    }
}