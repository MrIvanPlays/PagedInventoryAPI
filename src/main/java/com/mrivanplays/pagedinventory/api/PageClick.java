package com.mrivanplays.pagedinventory.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a object, created when {@link org.bukkit.event.inventory.InventoryClickEvent} was
 * fired. You can make use of click events in paged inventories by usig {@link
 * PagedInventory#addOnClickFunction(java.util.function.Consumer)}
 */
public final class PageClick {

  private final PagedInventory pagedInventory;
  private final Page page;
  private final ItemStack clickedItem;
  private final Player clicker;
  private final ClickType clickType;
  private final int slot;
  private final int rawSlot;
  private final int hotbarButton;

  public PageClick(
      PagedInventory pagedInventory,
      Page page,
      ItemStack clickedItem,
      Player clicker,
      ClickType clickType,
      int slot,
      int rawSlot,
      int hotbarButton) {
    this.pagedInventory = pagedInventory;
    this.page = page;
    this.clickedItem = clickedItem;
    this.clicker = clicker;
    this.clickType = clickType;
    this.slot = slot;
    this.rawSlot = rawSlot;
    this.hotbarButton = hotbarButton;
  }

  /**
   * Returns the {@link PagedInventory} instance from which the click came.
   *
   * @return paged inventory
   */
  @NotNull
  public PagedInventory getPagedInventory() {
    return pagedInventory;
  }

  /**
   * Returns the clicked {@link Page}
   *
   * @return page
   */
  @NotNull
  public Page getPage() {
    return page;
  }

  /**
   * Returns the clicked {@link ItemStack}
   *
   * @return item
   */
  @NotNull
  public ItemStack getClickedItem() {
    return clickedItem;
  }

  /**
   * Returns the {@link Player}, whom have clicked on the item.
   *
   * @return player, clicker
   */
  @NotNull
  public Player getClicker() {
    return clicker;
  }

  /**
   * Returns the {@link #getClickedItem()} 's triggered click type.
   *
   * @return click type
   */
  @NotNull
  public ClickType getClickType() {
    return clickType;
  }

  /**
   * Took from {@link InventoryClickEvent}: The slot number that was clicked, ready for passing to
   * {@link Inventory#getItem(int)}. Note that there may be two slots with the same slot number,
   * since a view links two different inventories.
   *
   * @return The slot number.
   */
  public int getSlot() {
    return slot;
  }

  /**
   * Took from {@link InventoryClickEvent}: The raw slot number clicked, ready for passing to {@link
   * InventoryView #getItem(int)} This slot number is unique for the view.
   *
   * @return the slot number
   */
  public int getRawSlot() {
    return rawSlot;
  }
  /**
   * Took from {@link InventoryClickEvent}: If the ClickType is NUMBER_KEY, this method will return
   * the index of the pressed key (0-8).
   *
   * @return the number on the key minus 1 (range 0-8); or -1 if not a NUMBER_KEY action
   */
  public int getHotbarButton() {
    return hotbarButton;
  }
}
