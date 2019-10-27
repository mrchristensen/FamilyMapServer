package service;

import dataaccess.Database;
import exceptions.DataAccessException;
import result.ClearResult;

import java.sql.SQLException;

/**
 * Handles the clear functionality - Delete all data from the database (user accounts, auth tokens, persons, and events)
 */
public class ClearService extends Service{
    /**
     * Clears the database
     * @return Clear return response body
     */
    public ClearResult clearDatabase() throws DataAccessException, SQLException {
        Database db = new Database();
        db.openConnection();
        int result = db.clearTables();
        db.closeConnection(true);

        ClearResult clearResult = new ClearResult();
        if(result == 0) { //Successful
            clearResult.setMessage("Clear succeeded.");
        }
        else { //error
            clearResult.setMessage("Clear Failed");
        }

        return clearResult;
    }
}
