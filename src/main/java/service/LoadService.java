package service;

import request.LoadRequest;
import result.LoadResult;

/**
 * Handles the load functionality
 * Clears all data from the database (just like the /clear API), and then loads the
 * posted user, person, and event data into the database.
 */
public class LoadService extends Service{
    /**
     * Calls the clear service and then loads the data with the parent classes methods (getAllUsers, getAllPersons, getAllEvents)
     * @param myRequest The info for the load request
     * @return Returns a response form body for loading service
     */
    LoadResult load(LoadRequest myRequest){
        return null;
    }
}
