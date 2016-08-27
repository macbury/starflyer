package de.macbury.desktop.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.adapter.SimpleListAdapter;
import com.kotcrab.vis.ui.widget.*;
import de.macbury.ActionTimer;
import de.macbury.tiles.TileCachePool;
import de.macbury.tiles.TileInstance;

/**
 * Created by macbury on 26.08.16.
 */
public class DebugTileCachePoolWindow extends VisWindow implements ActionTimer.TimerListener {
  private final TileCachePool tileCachePool;
  private final ActionTimer refreshListTimer;
  private final VisLabel tileCachePoolSizeLabel;
  private final TilePoolListAdapter tileAdapter;
  private final ListView<TileInstance> tileListView;

  public DebugTileCachePoolWindow(TileCachePool tileCachePool) {
    super("Tile cache pool");
    this.tileCachePool = tileCachePool;
    this.refreshListTimer = new ActionTimer(1f, this);
    refreshListTimer.start();

    setVisible(true);
    setWidth(240);
    setHeight(320);
    setResizable(true);

    this.tileCachePoolSizeLabel = new VisLabel("0");

    add(new VisLabel("Visible:")).left();
    add(tileCachePoolSizeLabel).left().row();

    this.tileAdapter  = new TilePoolListAdapter(tileCachePool.all());
    this.tileListView = new ListView<TileInstance>(tileAdapter);
    add(tileListView.getMainTable()).expand().colspan(2).fill();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (isVisible()) {
      refreshListTimer.update(delta);
    }
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    tileCachePoolSizeLabel.setText(String.valueOf(tileCachePool.getSize()) + "/"+TileCachePool.MAX_POOL_SIZE);
    tileAdapter.itemsChanged();
  }

  public void toggle() {
    setVisible(!isVisible());
    onTimerTick(refreshListTimer);
  }

  private class TilePoolListAdapter extends SimpleListAdapter<TileInstance> {

    public TilePoolListAdapter(Array<TileInstance> array) {
      super(array);
    }

    @Override
    protected VisTable createView(TileInstance item) {
      VisTable table = new VisTable();
      table.add(new VisLabel(item.geoTile.getId())).expandX().right();
      VisLabel stateLabel = new VisLabel(item.state.toString());

      switch (item.state) {
        case Downloading:
          stateLabel.setColor(Color.FIREBRICK);
          break;
        case Assembling:
          stateLabel.setColor(Color.NAVY);
          break;
        case Error:
          stateLabel.setColor(Color.RED);
          break;
        case Ready:
          stateLabel.setColor(Color.GREEN);
          break;
      }

      table.add(stateLabel).padLeft(10).width(70).left().padRight(10);
      return table;
    }
  }
}
