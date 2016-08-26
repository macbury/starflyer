package de.macbury.desktop.ui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisWindow;
import de.macbury.ActionTimer;
import de.macbury.ActionTimer.TimerListener;
import de.macbury.geo.Tile;
import de.macbury.tiles.VisibleTileProvider;

/**
 * Created by macbury on 25.08.16.
 */
public class DebugVisibleTileWindow extends VisWindow implements TimerListener {
  private final VisibleTileProvider visibleTileProvider;
  private final VisLabel visibleTileCountLabel;
  private final VisList<TileListEntry> tileList;
  private final ActionTimer refreshListTimer;
  private final Pool<TileListEntry> tileEntryPool = new Pool<TileListEntry>() {
    @Override
    protected TileListEntry newObject() {
      return new TileListEntry();
    }
  };

  private Array<TileListEntry> entries = new Array<TileListEntry>();

  public DebugVisibleTileWindow(VisibleTileProvider visibleTileProvider) {
    super("Visible tiles");

    setVisible(true);
    setWidth(120);
    setHeight(320);
    setResizable(true);
    this.refreshListTimer = new ActionTimer(1f, this);
    this.visibleTileCountLabel = new VisLabel("0");
    this.tileList = new VisList<TileListEntry>();

    refreshListTimer.start();

    add(new VisLabel("Visible:")).left();
    add(visibleTileCountLabel).left().row();

    add(new VisScrollPane(tileList)).expand().colspan(2).fill();
    this.visibleTileProvider = visibleTileProvider;
  }

  public void toggle() {
    setVisible(!isVisible());
    onTimerTick(refreshListTimer);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (isVisible()) {
      visibleTileCountLabel.setText(String.valueOf(visibleTileProvider.getVisibleCount()));
      refreshListTimer.update(delta);
    }
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    tileEntryPool.freeAll(entries);
    entries.clear();
    for (Tile tile : visibleTileProvider.getVisible()) {
      TileListEntry tileListEntry = tileEntryPool.obtain();
      tileListEntry.set(tile);
      entries.add(tileListEntry);
    }
    entries.sort();
    TileListEntry selectedTile = tileList.getSelected();
    tileList.setItems(entries);
    tileList.setSelected(selectedTile);
  }

  private class TileListEntry implements Comparable<TileListEntry> {
    public String id;

    public void set(Tile tile) {
      this.id = tile.x + "x" + tile.y;
    }

    @Override
    public String toString() {
      return id;
    }

    @Override
    public int compareTo(TileListEntry o) {
      return id.compareTo(o.id);
    }

    @Override
    public boolean equals(Object obj) {
      if (TileListEntry.class.isInstance(obj)) {
        TileListEntry otherTile = (TileListEntry)obj;
        return otherTile.id.equals(id);
      }
      return super.equals(obj);
    }
  }
}
