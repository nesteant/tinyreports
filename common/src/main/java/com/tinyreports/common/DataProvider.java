package com.tinyreports.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class DataProvider {
    private ConcurrentHashMap<String, Object> dataObjects = new ConcurrentHashMap<String, Object>();

    /**
     * put any quantity of parameters to DataObjects <tt>HashMap</tt>. This means
     * that each object will be wrapped as a separate <tt>Object</tt> in Array. E.g.
     * if Object is passed to this method it will be represented as Object[0].Object.
     * Method is useful when you want to pass <tt>Collection</tt> to rendering phase.
     * This method is not applicable for chart generation.
     *
     * @param objectId   unique identifier of DataObject parameter.
     * @param parameters parameters which will be returned when report engine will try to
     *                   resolve relations or parameters
     */
    public void putCollections(String objectId, Object... parameters) {
        if (dataObjects.containsKey(objectId)) {
            dataObjects.remove(objectId);
        }
        dataObjects.put(objectId, parameters);
    }

    /**
     * put collection to DataObjects <tt>HashMap</tt>. This means
     * that each object  will be passed to reportEngine as is without wrapping. This method
     * should be used in Chart Generation and for all parameters in Report Generation if you want
     * to iterate on them.
     *
     * @param objectId unique identifier of DataObject parameter.
     * @param object   collection of objects which will be transformed to array which will be returned
     *                 when report engine will try to resolve relations or parameters
     */
    public void putObject(String objectId, Object object) {
        if (dataObjects.containsKey(objectId)) {
            dataObjects.remove(objectId);
        }
        dataObjects.put(objectId, object);
    }

    public Object getDataObjectByKey(String key) {
        return dataObjects.get(key);
    }
}
