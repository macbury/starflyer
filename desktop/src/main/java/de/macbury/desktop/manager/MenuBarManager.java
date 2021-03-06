package de.macbury.desktop.manager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;

/**
 * Configure and create main menu
 */
public class MenuBarManager extends MenuBar {
  public enum Action {
    DebugVisibleTiles,
    DebugGoToPosition,
    DebugTileCachePool,
    FrustumSave,
    FrustumRestore
  }

  private Array<Listener> listeners = new Array<Listener>();

  public MenuBarManager() {
    super();
    createDebugMenu();
    createFrustrumMenu();
  }

  private void createFrustrumMenu() {
    Menu menu = new Menu("Frustum");
    this.addMenu(menu);

    menu.addItem(createMenuItem("Save", Action.FrustumSave));
    menu.addItem(createMenuItem("Restore", Action.FrustumRestore));
  }

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  private void createDebugMenu() {
    Menu menu = new Menu("Debug");
    this.addMenu(menu);

    menu.addItem(createMenuItem("Visible tiles", Action.DebugVisibleTiles));
    menu.addItem(createMenuItem("Go to position", Action.DebugGoToPosition));
    menu.addItem(createMenuItem("Show tile cache pool", Action.DebugTileCachePool));
  }

  private void triggerAction(Action action) {
    for (Listener listener : listeners) {
      listener.onMenuAction(action, this);
    }
  }

  /**
   * Create new menu item
   * @param text
   * @return
   */
  private MenuItem createMenuItem(String text, final Action action) {
    final MenuItem menuItem = new MenuItem(text);
    menuItem.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        triggerAction(action);
      }
    });
    return menuItem;
  }

  public interface Listener {
    public void onMenuAction(Action action, MenuBarManager menuBarManager);
  }
}