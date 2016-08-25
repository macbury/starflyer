package de.macbury.desktop.manager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import de.macbury.desktop.ui.DebugVisibleTileWindow;

/**
 * Configure and create main menu
 */
public class MenuBarManager extends MenuBar {
  private final DebugVisibleTileWindow debugVisibleTileWindow;

  public MenuBarManager(DebugVisibleTileWindow debugVisibleTileWindow) {
    super();
    this.debugVisibleTileWindow = debugVisibleTileWindow;
    createDebugMenu();
  }

  private void createDebugMenu() {
    Menu menu = new Menu("Debug");
    this.addMenu(menu);

    menu.addItem(createMenuItem("Visible tiles"));
    menu.addItem(createMenuItem("Go to position"));
  }

  /**
   * Create new menu item
   * @param text
   * @return
   */
  private MenuItem createMenuItem(String text) {
    final MenuItem menuItem = new MenuItem(text);
    menuItem.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        debugVisibleTileWindow.toggle();
      }
    });
    return menuItem;
  }
}