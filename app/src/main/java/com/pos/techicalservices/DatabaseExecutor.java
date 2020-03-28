package com.pos.techicalservices;

public class DatabaseExecutor {

    private static Database database;
    private static DatabaseExecutor instance;

    private DatabaseExecutor() {

    }
    public static void setDatabase(Database db) {
        database = db;
    }

    public static DatabaseExecutor getInstance() {
        if (instance == null)
            instance = new DatabaseExecutor();
        return instance;
    }
    /**
     * Drops all data in database.
     */
    public void dropAllData() {
        execute("DELETE FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG + ";");
        execute("DELETE FROM " + DatabaseContents.TABLE_SALE + ";");
        execute("DELETE FROM " + DatabaseContents.TABLE_SALE_LINEITEM + ";");
        execute("DELETE FROM " + DatabaseContents.TABLE_STOCK + ";");
        execute("DELETE FROM " + DatabaseContents.TABLE_STOCK_SUM + ";");
        execute("VACUUM;");
    }


    private void execute(String queryString) {
        database.execute(queryString);
    }

}
