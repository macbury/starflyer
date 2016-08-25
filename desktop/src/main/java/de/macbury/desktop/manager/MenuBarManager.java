package de.macbury.desktop.manager;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;

/**
 * Configure and create main menu
 */
public class MenuBarManager extends MenuBar {
  public MenuBarManager() {
    super();
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
    return menuItem;
  }
}