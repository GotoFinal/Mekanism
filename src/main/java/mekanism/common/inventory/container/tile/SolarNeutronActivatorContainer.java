package mekanism.common.inventory.container.tile;

import javax.annotation.Nonnull;
import mekanism.api.gas.IGasItem;
import mekanism.common.inventory.container.MekanismContainerTypes;
import mekanism.common.inventory.slot.SlotStorageTank;
import mekanism.common.tile.TileEntitySolarNeutronActivator;
import mekanism.common.util.text.TextComponentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class SolarNeutronActivatorContainer extends MekanismTileContainer<TileEntitySolarNeutronActivator> {

    public SolarNeutronActivatorContainer(int id, PlayerInventory inv, TileEntitySolarNeutronActivator tile) {
        super(MekanismContainerTypes.SOLAR_NEUTRON_ACTIVATOR, id, inv, tile);
    }

    public SolarNeutronActivatorContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        this(id, inv, getTileFromBuf(buf, TileEntitySolarNeutronActivator.class));
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            if (slotStack.getItem() instanceof IGasItem) {
                if (slotID != 0 && slotID != 1) {
                    if (((IGasItem) slotStack.getItem()).canProvideGas(slotStack, tile.inputTank.getGas() != null ? tile.inputTank.getGas().getGas() : null)) {
                        if (!mergeItemStack(slotStack, 0, 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (((IGasItem) slotStack.getItem()).canReceiveGas(slotStack, tile.outputTank.getGas() != null ? tile.outputTank.getGas().getGas() : null)) {
                        if (!mergeItemStack(slotStack, 1, 2, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (!mergeItemStack(slotStack, 2, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID >= 2 && slotID <= 28) {
                if (!mergeItemStack(slotStack, 29, inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID > 28) {
                if (!mergeItemStack(slotStack, 2, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 2, inventorySlots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                currentSlot.putStack(ItemStack.EMPTY);
            } else {
                currentSlot.onSlotChanged();
            }
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            currentSlot.onTake(player, slotStack);
        }
        return stack;
    }

    @Override
    protected void addSlots() {
        addSlot(new SlotStorageTank(tile, 0, 5, 56));
        addSlot(new SlotStorageTank(tile, 1, 155, 56));
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return TextComponentUtil.translate("mekanism.container.solar_neutron_activator");
    }
}