package com.mrivanplays.pagedinventory.api;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** Represents a paged inventory */
public interface PagedInventory {

  /**
   * Creates a new builder for a paged inventory. <br>
   * <i>This is just a shortcut to {@link PagedInventoryBuilder#createBuilder(Plugin)}</i>
   *
   * @param plugin plugin owning
   * @return builder
   */
  static PagedInventoryBuilder builder(@NotNull Plugin plugin) {
    return PagedInventoryBuilder.createBuilder(plugin);
  }

  /**
   * Adds a click listener, when a {@link Page} got clicked.
   *
   * @param onClick on click function
   */
  void addOnClickFunction(@NotNull Consumer<PageClick> onClick);

  /**
   * Retrieves the page specified.
   *
   * @param page the page you want to retrieve
   * @return empty optional if page wasn't found, or optional with value
   */
  @NotNull
  Optional<Page> getPage(int page);

  /**
   * Retrieves the page number of the page specified.
   *
   * @param page the page object you want to
   * @return number of the page if present
   */
  @NotNull
  OptionalInt getPageNumber(@NotNull Page page);

  /**
   * Adds a {@link Page} to this {@link PagedInventory}
   *
   * @param page the page you want to add
   */
  void addPage(@NotNull Page page);

  /**
   * Sets the specified {@link Page} number to the specified {@link Page} in this {@link
   * PagedInventory}
   *
   * @param pageNum the page number you want to override/set
   * @param page the page you want to set
   */
  void setPage(int pageNum, @NotNull Page page);

  /**
   * Removes the specified page from this {@link PagedInventory}
   *
   * @param page the page you want to remove
   */
  void removePage(int page);

  /**
   * Returns a immutable map of the {@link Page Pages} this {@link PagedInventory} has
   *
   * @return pages
   */
  @NotNull
  Map<Integer, Page> getPages();

  /**
   * Sets the specified {@link NavigationItem} on the specified {@link
   * org.bukkit.inventory.Inventory} position.
   *
   * @param pos the inventory position you want the item on
   * @param item the navigation item you want to set
   */
  void setNavigationItem(int pos, @NotNull NavigationItem item);

  /**
   * Removes the specified {@link NavigationItem} on that position.
   *
   * @param pos the position of the navigation item you want to remove
   */
  void removeNavigationItem(int pos);

  /**
   * Returns a immutable map of the {@link NavigationItem NavigationItems} added.
   *
   * @return navigation items
   */
  @NotNull
  Map<Integer, NavigationItem> getNavigationItems();

  /**
   * Opens the specified page and adds the {@link Player} to the cache.
   *
   * @param viewer viewer
   * @param page page
   */
  void open(@NotNull Player viewer, int page);

  /**
   * Opens the previous page of the page viewed by the player if present
   *
   * @param viewer player viewer
   */
  void openPrevious(@NotNull Player viewer);

  /**
   * Opens the next page of the page viewed by the player if present
   *
   * @param viewer player viewer
   */
  void openNext(@NotNull Player viewer);

  /**
   * Returns the page, currently viewed by the specified {@link Player} viewer
   *
   * @param viewer player viewer
   * @return page viewed if present
   */
  @NotNull
  Optional<Page> getPageViewed(@NotNull Player viewer);

  /**
   * Returns a immutable map of the known viewers and the viewed pages.
   *
   * @return viewers
   */
  @NotNull
  Map<UUID, Page> getViewers();

  /**
   * Returns the {@link UUID UniqueId} of this {@link PagedInventory}, in case you want to store it
   * somewhere
   *
   * @return unique id
   */
  @NotNull
  UUID getPagedInventoryUUID();
}
