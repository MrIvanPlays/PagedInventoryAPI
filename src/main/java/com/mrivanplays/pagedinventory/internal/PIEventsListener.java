package com.mrivanplays.pagedinventory.internal;

import com.mrivanplays.pagedinventory.api.NavigationItem;
import com.mrivanplays.pagedinventory.api.Page;
import com.mrivanplays.pagedinventory.api.PageClick;
import com.mrivanplays.pagedinventory.api.PageClose;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PIEventsListener implements Listener {

  private PagedInventoryImpl impl;
  private Plugin plugin;

  public PIEventsListener(PagedInventoryImpl impl, Plugin plugin) {
    this.impl = impl;
    this.plugin = plugin;
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (event.getClickedInventory() == null) {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    Optional<Page> pageOptional = impl.getPageViewed(player);
    if (!pageOptional.isPresent()) {
      return;
    }
    Page page = pageOptional.get();
    event.setCancelled(true);
    ItemStack clickedItem = event.getCurrentItem();
    boolean foundNavItem = false;
    for (NavigationItem navItem : impl.getNavigationItems().values()) {
      if (navItem.getItem().isSimilar(clickedItem)) {
        foundNavItem = true;
        switch (navItem.getAction()) {
          case PREVIOUS_PAGE:
            plugin.getServer().getScheduler().runTask(plugin, () -> impl.openPrevious(player));
            break;
          case NEXT_PAGE:
            plugin.getServer().getScheduler().runTask(plugin, () -> impl.openNext(player));
            break;
          case CLOSE:
            plugin.getServer().getScheduler().runTask(plugin, player::closeInventory);
            break;
        }
      }
    }
    if (foundNavItem) {
      return;
    }
    impl.callClickFunctions(
        new PageClick(
            impl,
            page,
            clickedItem,
            player,
            event.getClick(),
            event.getSlot(),
            event.getRawSlot(),
            event.getHotbarButton()));
  }

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    Player player = (Player) event.getPlayer();
    Optional<Page> pageViewed = impl.getPageViewed(player);
    if (!pageViewed.isPresent()) {
      return;
    }
    if (impl.getSwitchRegistrar().isSwitching(player.getUniqueId())) {
      return;
    }
    impl.callCloseFunctions(new PageClose(player, impl, pageViewed.get()));
    impl.removeViewer(player);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    impl.removeViewer(event.getPlayer());
  }
}
