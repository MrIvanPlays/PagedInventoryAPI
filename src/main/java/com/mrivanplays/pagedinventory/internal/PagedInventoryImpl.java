package com.mrivanplays.pagedinventory.internal;

import com.google.common.base.Preconditions;
import com.mrivanplays.pagedinventory.api.NavigationItem;
import com.mrivanplays.pagedinventory.api.Page;
import com.mrivanplays.pagedinventory.api.PageClick;
import com.mrivanplays.pagedinventory.api.PageClose;
import com.mrivanplays.pagedinventory.api.PagedInventory;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PagedInventoryImpl implements PagedInventory {

  private Map<Integer, Page> pages;
  private Map<Integer, NavigationItem> navigationItems;
  private Map<UUID, UUID> viewers;
  private Set<Consumer<PageClick>> clickFunctions;
  private Set<Consumer<PageClose>> closeFunctions;
  private PageSwitchRegistrar switchRegistrar;
  private UUID pagedInentoryUUID;

  public PagedInventoryImpl() {
    this.pages = new ConcurrentHashMap<>();
    this.navigationItems = new ConcurrentHashMap<>();
    this.viewers = new ConcurrentHashMap<>();
    clickFunctions = new HashSet<>();
    closeFunctions = new HashSet<>();
    switchRegistrar = new PageSwitchRegistrar();
    this.pagedInentoryUUID = UUID.randomUUID();
  }

  @Override
  public void addOnClickFunction(@NotNull Consumer<PageClick> onClick) {
    Preconditions.checkNotNull(onClick, "onClick");
    clickFunctions.add(onClick);
  }

  @Override
  public void addOnCloseFunction(@NotNull Consumer<PageClose> onClose) {
    Preconditions.checkNotNull(onClose, "onClose");
    closeFunctions.add(onClose);
  }

  @Override
  @NotNull
  public Optional<Page> getPage(int page) {
    return Optional.ofNullable(pages.get(page));
  }

  @Override
  public @NotNull Optional<Page> getPage(@NotNull UUID page) {
    Preconditions.checkNotNull(page, "page");
    return pages.values().stream().filter(v -> v.getPageUUID().equals(page)).findFirst();
  }

  @Override
  @NotNull
  public OptionalInt getPageNumber(@NotNull Page page) {
    Preconditions.checkNotNull(page, "page");
    return pages.entrySet().stream()
        .filter(entry -> entry.getValue().getPageUUID().equals(page.getPageUUID()))
        .mapToInt(Entry::getKey)
        .findFirst();
  }

  @Override
  public @NotNull OptionalInt getPageNumber(@NotNull UUID page) {
    Preconditions.checkNotNull(page, "page");
    return pages.entrySet().stream()
        .filter(entry -> entry.getValue().getPageUUID().equals(page))
        .mapToInt(Entry::getKey)
        .findFirst();
  }

  void callClickFunctions(PageClick clickObj) {
    for (Consumer<PageClick> listener : clickFunctions) {
      listener.accept(clickObj);
    }
  }

  void callCloseFunctions(PageClose closeObj) {
    for (Consumer<PageClose> listener : closeFunctions) {
      listener.accept(closeObj);
    }
  }

  @Override
  public void addPage(@NotNull Page page) {
    Preconditions.checkNotNull(page, "page");
    Collection<Integer> pageNumbers = pages.keySet();
    if (pageNumbers.isEmpty()) {
      pages.put(1, page);
      return;
    }
    int highestPage = Collections.max(pageNumbers, Integer::compare);
    pages.put(highestPage + 1, page);
  }

  @Override
  public void setPage(int pageNum, @NotNull Page page) {
    Preconditions.checkNotNull(page, "page");
    if (pages.containsKey(pageNum)) {
      pages.replace(pageNum, page);
    } else {
      pages.put(pageNum, page);
    }
  }

  @Override
  public void removePage(int page) {
    pages.remove(page);
  }

  @Override
  @NotNull
  public Map<Integer, Page> getPages() {
    return Collections.unmodifiableMap(pages);
  }

  @Override
  public void setNavigationItem(int pos, @NotNull NavigationItem item) {
    Preconditions.checkNotNull(item, "item");
    if (navigationItems.containsKey(pos)) {
      navigationItems.replace(pos, item);
    } else {
      navigationItems.put(pos, item);
    }
    for (int i : pages.keySet()) {
      pages.get(i).getInventory().setItem(pos, item.getItem());
    }
  }

  @Override
  public void removeNavigationItem(int pos) {
    NavigationItem item = navigationItems.remove(pos);
    for (int i : pages.keySet()) {
      pages.get(i).getInventory().remove(item.getItem());
    }
  }

  @Override
  @NotNull
  public Map<Integer, NavigationItem> getNavigationItems() {
    return Collections.unmodifiableMap(navigationItems);
  }

  @Override
  public void open(@NotNull Player viewer, int page) {
    Preconditions.checkNotNull(viewer, "viewer");
    Page pageObj = pages.get(page);
    if (pageObj != null) {
      open(viewer, pageObj);
    }
  }

  private void open(Player player, Page page) {
    if (!viewers.containsKey(player.getUniqueId())) {
      viewers.put(player.getUniqueId(), page.getPageUUID());
    } else {
      viewers.replace(player.getUniqueId(), page.getPageUUID());
      switchRegistrar.register(player.getUniqueId());
    }
    try {
      player.openInventory(page.getInventory());
    } finally {
      switchRegistrar.unregister(player.getUniqueId());
    }
  }

  PageSwitchRegistrar getSwitchRegistrar() {
    return switchRegistrar;
  }

  @Override
  public void openPrevious(@NotNull Player viewer) {
    Preconditions.checkNotNull(viewer, "viewer");
    Optional<Page> current = getPageViewed(viewer);
    if (current.isPresent()) {
      Page page = current.get();
      int previousPage = getPageNumber(page).getAsInt() - 1;
      open(viewer, previousPage);
    }
  }

  @Override
  public void openNext(@NotNull Player viewer) {
    Preconditions.checkNotNull(viewer, "viewer");
    Optional<Page> current = getPageViewed(viewer);
    if (current.isPresent()) {
      Page page = current.get();
      int nextPage = getPageNumber(page).getAsInt() + 1;
      open(viewer, nextPage);
    }
  }

  void removeViewer(Player viewer) {
    if (switchRegistrar.isSwitching(viewer.getUniqueId())) {
      return;
    }
    viewers.remove(viewer.getUniqueId());
  }

  @Override
  @NotNull
  public Optional<Page> getPageViewed(@NotNull Player viewer) {
    Preconditions.checkNotNull(viewer, "viewer");
    if (viewers.containsKey(viewer.getUniqueId())) {
      return getPage(viewers.get(viewer.getUniqueId()));
    }
    return Optional.empty();
  }

  @Override
  @NotNull
  public Map<UUID, UUID> getViewers() {
    return Collections.unmodifiableMap(viewers);
  }

  @Override
  @NotNull
  public UUID getPagedInventoryUUID() {
    return pagedInentoryUUID;
  }
}
