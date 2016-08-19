package de.macbury.server.utils;

import com.badlogic.gdx.utils.Array;
import com.martiansoftware.jsap.*;

import java.io.File;

/**
 * Parse arguments for server
 */
public class ServerArgs {
  private static final String PARAM_IMPORT_FILE = "import-file";
  private static final String PARAM_DB_HOST = "db-host";
  private static final String PARAM_DB_PORT = "db-port";
  private static final String PARAM_DB_NAME = "db-name";
  private static final String PARAM_DB_CREATE = "db-create";
  private static final String PARAM_MAP_ZEN_API_KEY = "map-zen-api-key";
  private final JSAPResult result;
  private final Array<Parameter> pendingOptions;
  private JSAP jsap;

  public ServerArgs(String[] args) {
    this.pendingOptions = new Array<Parameter>();
    prepareParser();
    result = jsap.parse(args);
  }

  /**
   * Get current osm file to import
   * @return null if no parameter was passed
   */
  public File getImportFile() {
    if (isGoingToImportFile()) {
      return new File(result.getString(PARAM_IMPORT_FILE));
    } else {
      return null;
    }
  }

  /**
   * RethinkDB host
   * @return
   */
  public String getDatabaseHost() {
    return result.getString(PARAM_DB_HOST);
  }

  /**
   * RethinkDB db name
   * @return
   */
  public String getDatabaseName() {
    return result.getString(PARAM_DB_NAME);
  }

  /**
   * RethinkDB port
   * @return
   */
  public Integer getDatabasePort() {
    return result.getInt(PARAM_DB_PORT);
  }


  /**
   * Check if user passed import file param
   * @return
   */
  public boolean isGoingToImportFile() {
    return result.contains(PARAM_IMPORT_FILE);
  }

  /**
   * Check if user wants to create database
   * @return
   */
  public boolean isGoingToCreateDatabase() {
    return result.getBoolean(PARAM_DB_CREATE);
  }

  private void prepareParser() {
    jsap = new JSAP();
    try {
      option(PARAM_IMPORT_FILE)
              .setStringParser(JSAP.STRING_PARSER)
              .setRequired(false)
              .setUsageName("Path to file you want to import")
              .setHelp("Pass path to OSM file you want to import into RethinkDB");
      option(PARAM_DB_NAME)
              .setStringParser(JSAP.STRING_PARSER)
              .setRequired(true)
              .setHelp("Name for RethinkDB database");
      option(PARAM_DB_HOST)
              .setStringParser(JSAP.STRING_PARSER)
              .setRequired(true)
              .setUsageName("RethinkDB host")
              .setHelp("Host to RethinkDB you want connect to");
      option(PARAM_DB_PORT)
              .setStringParser(JSAP.INTEGER_PARSER)
              .setRequired(true)
              .setUsageName("RethinkDB port")
              .setHelp("Port to RethinkDB you want connect to");
      option(PARAM_MAP_ZEN_API_KEY)
              .setStringParser(JSAP.STRING_PARSER)
              .setRequired(true)
              .setUsageName("Mapzen Api key")
              .setHelp("Api key for vector.mapzen.com");

      for (int i = 0; i < pendingOptions.size; i++) {
        jsap.registerParameter(pendingOptions.get(i));
      }
    } catch (JSAPException e) {
      e.printStackTrace();
    }
  }

  /**
   * Quickly create new {@link FlaggedOption} and register it in parser
   * @param key
   * @return
   * @throws JSAPException
   */
  private FlaggedOption option(String key) throws JSAPException {
    FlaggedOption option = new FlaggedOption(key);
    option.setLongFlag(key);
    option.setShortFlag(JSAP.NO_SHORTFLAG);
    pendingOptions.add(option);
    return option;
  }

  private Switch aswitch(String key) {
    Switch option = new Switch(key);
    option.setLongFlag(key);
    option.setShortFlag(JSAP.NO_SHORTFLAG);
    pendingOptions.add(option);
    return option;
  }

  public boolean isSuccess() {
    return result.success();
  }

  /**
   * Infomation about all parameters
   * @return
   */
  public String getUsage() {
    return jsap.getUsage();
  }

  public String getHelp() {
    return jsap.getHelp();
  }

}
