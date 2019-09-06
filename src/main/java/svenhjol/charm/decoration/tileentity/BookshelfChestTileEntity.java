package svenhjol.charm.decoration.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import svenhjol.charm.decoration.block.BookshelfChestBlock;
import svenhjol.charm.decoration.container.BookshelfChestContainer;
import svenhjol.charm.base.message.MessageBookshelfInteract;
import svenhjol.charm.decoration.module.BookshelfChests;
import svenhjol.meson.handler.PacketHandler;

public class BookshelfChestTileEntity extends LockableLootTileEntity
{
    public static int SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public BookshelfChestTileEntity()
    {
        super(BookshelfChests.tile);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        loadFromNBT(tag);
    }

    public void loadFromNBT(CompoundNBT tag)
    {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag) && tag.contains("Items")) {
            ItemStackHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        return writeToNBT(tag);
    }

    public CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (!this.checkLootAndRead(tag)) {
            ItemStackHelper.saveAllItems(tag, this.items, false);
        }
        return tag;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return BookshelfChestContainer.instance(id, player, this);
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile.charm.bookshelf_chest");
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item)
    {
        super.setInventorySlotContents(slot, item);
        updateBlockState();
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        PacketHandler.sendTo(new MessageBookshelfInteract(this.pos, 0), (ServerPlayerEntity)player);
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        PacketHandler.sendTo(new MessageBookshelfInteract(this.pos, 1), (ServerPlayerEntity)player);
    }

    public void updateBlockState()
    {
        int filled = 0;

        for (int i = 0; i < SIZE; i++) {
            if (!getStackInSlot(i).isEmpty()) filled++;
        }

        if (world != null && world.getBlockState(pos).getBlock() instanceof BookshelfChestBlock) {
            world.setBlockState(pos, world.getBlockState(pos).with(BookshelfChestBlock.SLOTS, filled), 2);
        }
    }
}