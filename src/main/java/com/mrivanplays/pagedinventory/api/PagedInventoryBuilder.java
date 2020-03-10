package com.mrivanplays.pagedinventory.api;

import com.google.common.base.Preconditions;
import com.mrivanplays.pagedinventory.internal.PIEventsListener;
import com.mrivanplays.pagedinventory.internal.PagedInventoryImpl;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** Represents a builder of {@link PagedInventory} */
public class PagedInventoryBuilder {

  private final Plugin plugin;
  private PagedInventoryImpl parent;

  /**
   * Creates a new builder instance
   *
   * @param plugin plugin owner
   * @return builder
   */
  @NotNull
  public static PagedInventoryBuilder createBuilder(@NotNull Plugin plugin) {
    Preconditions.checkNotNull(plugin, "plugin");
    return new PagedInventoryBuilder(plugin);
  }

  private PagedInventoryBuilder(Plugin plugin) {
    this.plugin = plugin;
    this.parent = new PagedInventoryImpl();
  }

  /**
   * Sets the page number to the specified page
   *
   * @param page page number
   * @param inventory page
   * @return this instance for chaining
   */
  @NotNull
  public PagedInventoryBuilder page(int page, @NotNull Inventory inventory) {
    parent.setPage(page, Page.create(inventory));
    return this;
  }

  /**
   * Sets the page number to the specified page
   *
   * @param pageNum page number
   * @param page page
   * @return this instance for chaining
   */
  @NotNull
  public PagedInventoryBuilder page(int pageNum, @NotNull Page page) {
    parent.setPage(pageNum, page);
    return this;
  }

  /**
   * Sets the specified inventory slot to the specified {@link NavigationItem}
   *
   * @param slot slot
   * @param navigationItem navigation item
   * @return this instance for chaining
   */
  @NotNull
  public PagedInventoryBuilder navigationItem(int slot, @NotNull NavigationItem navigationItem) {
    parent.setNavigationItem(slot, navigationItem);
    return this;
  }

  /**
   * Adds a click function to listen for click events on the inventories.
   *
   * @param clickListener click listener
   * @return this instance for chaining
   */
  @NotNull
  public PagedInventoryBuilder clickFunction(@NotNull Consumer<PageClick> clickListener) {
    parent.addOnClickFunction(clickListener);
    return this;
  }

  /**
   * Adds a close function to listen for close events on the inventories.
   *
   * @param closeListener close listener
   * @return this instance for chaining
   */
  @NotNull
  public PagedInventoryBuilder closeFunction(@NotNull Consumer<PageClose> closeListener) {
    parent.addOnCloseFunction(closeListener);
    return this;
  }

  /**
   * Builds into a {@link PagedInventory}
   *
   * @return paged inventory
   */
  @NotNull
  public PagedInventory build() {
    Bukkit.getPluginManager().registerEvents(new PIEventsListener(parent, plugin), plugin);
    return parent;
  }
}
