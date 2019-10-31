package service;

import dataaccess.Database;
import exceptions.DataAccessException;
import result.ClearResult;
import result.Result;

import java.sql.SQLException;

/**
 * Handles the clear functionality - Delete all data from the database (user accounts, auth tokens, persons, and events)
 */
public class ClearService extends Service{
    /**
     * Clears the database
     * @return Clear return response body
     */
    public Result clearDatabase() {
        try {
            ClearResult clearResult = new ClearResult();
            Database db = new Database();

            db.getConnection();
            int result = db.clearTables();
            db.closeConnection(true);

            if (result == 0) { //Successful
                clearResult.setMessage("Clear succeeded.");
            } else { //error
                clearResult.setMessage("Error: Clear Failed");
            }

            return clearResult;
        } catch (DataAccessException e) {
            return new Result(e.toString());
        }
    }
}
