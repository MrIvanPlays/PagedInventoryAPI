package com.mrivanplays.pagedinventory.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PageSwitchRegistrar {

  private List<UUID> switching;

  public PageSwitchRegistrar() {
    this.switching = new ArrayList<>();
  }

  public void register(UUID uuid) {
    switching.add(uuid);
  }

  public void unregister(UUID uuid) {
    switching.remove(uuid);
  }

  public boolean isSwitching(UUID uuid) {
    return switching.contains(uuid);
  }
}
