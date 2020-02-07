package com.mrivanplays.pagedinventory.api;

import com.google.common.base.Preconditions;
import java.util.UUID;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/** Represents a page */
public interface Page {

  /**
   * Creates a new page instance
   *
   * @param inventory inventory page
   * @return page object
   */
  static Page create(@NotNull Inventory inventory) {
    Preconditions.checkNotNull(inventory, "inventory");
    return new Page() {

      private UUID uuid;

      private void initializeUUID() {
        uuid = UUID.randomUUID();
      }

      @Override
      @NotNull
      public Inventory getInventory() {
        return inventory;
      }

      @Override
      @NotNull
      public UUID getPageUUID() {
        if (uuid == null) {
          initializeUUID();
        }
        return uuid;
      }
    };
  }

  /**
   * Returns the actual page as an {@link Inventory}
   *
   * @return inventory
   */
  @NotNull
  Inventory getInventory();

  /**
   * Returns the {@link UUID UniqueId} of the {@link Page} in case you want to store it.
   *
   * @return uuid
   */
  @NotNull
  UUID getPageUUID();
}
