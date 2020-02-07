package com.mrivanplays.pagedinventory.api;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a navigation item
 */
public final class NavigationItem {

  private final ItemStack item;
  private final Action action;

  /**
   * Creates a new {@link NavigationItem}
   *
   * @param item the actual item
   * @param action the action which the navigation item should listen for
   * @return navigation item
   */
  public static NavigationItem create(@NotNull ItemStack item, @NotNull Action action) {
    Preconditions.checkNotNull(item, "item");
    Preconditions.checkNotNull(action, "action");
    return new NavigationItem(item, action);
  }

  private NavigationItem(ItemStack item, Action action) {
    this.item = item;
    this.action = action;
  }

  /**
   * Returns the actual {@link ItemStack} of the navigation item.
   *
   * @return item
   */
  @NotNull
  public ItemStack getItem() {
    return item;
  }

  /**
   * Returns the {@link Action} for which the item will trigger.
   *
   * @return action
   */
  @NotNull
  public Action getAction() {
    return action;
  }

  /**
   * Represents action
   */
  public enum Action {
    /**
     * The {@link NavigationItem} should listen to get to previous page.
     */
    PREVIOUS_PAGE,

    /**
     * The {@link NavigationItem} should listen to get to the next page.
     */
    NEXT_PAGE,

    /**
     * The {@link NavigationItem} should listen to close the inventory
     */
    CLOSE
  }
}
