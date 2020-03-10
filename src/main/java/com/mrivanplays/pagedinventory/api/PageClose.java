package com.mrivanplays.pagedinventory.api;

import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a object, created when {@link org.bukkit.event.inventory.InventoryCloseEvent} was
 * fired. You can make use of close events in paged inventories by using {@link
 * PagedInventory#addOnCloseFunction(Consumer)}
 */
public final class PageClose {

  private final Player closer;
  private final PagedInventory pagedInventory;
  private final Page page;

  public PageClose(Player closer, PagedInventory pagedInventory, Page page) {
    this.closer = closer;
    this.pagedInventory = pagedInventory;
    this.page = page;
  }

  /**
   * Returns the player, whom have closed the specified {@link Page}
   *
   * @return player closer
   */
  @NotNull
  public Player getCloser() {
    return closer;
  }

  /**
   * Returns the {@link PagedInventory}, where the closed {@link Page} is registered.
   *
   * @return paged inventory
   */
  @NotNull
  public PagedInventory getPagedInventory() {
    return pagedInventory;
  }

  /**
   * Returns the {@link Page} closed.
   *
   * @return closed page
   */
  @NotNull
  public Page getPage() {
    return page;
  }
}
