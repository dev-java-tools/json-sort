package dev.javatools.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class has all the apis needs to sort a Map.
 *
 * How to use listKeys?
 *  If the Map has a list of objects and if you need to sort the list based on a
 *  field of an object, this feature will help.
 *
 *  Here is how it works.
 *      Take the below Map as an example
 *          {
 *              "members": [
 *                  {
 *                      "name": "John"
 *                      "age": 22
 *                  }
 *                  {
 *                      "name": "Bob"
 *                      "age": 18
 *                  }
 *              ]
 *          }
 *      If you need the sort the above Map based on the member name. You need to pass
 *          listKey.put("members[]", "name");
 *     If you need the sort the above Map based on the member age. You need to pass
 *          listKey.put("members[]", "age");
 */
public class SortMap {

    private static final String EMPTY_STRING = "";
    private static final String PERIOD = ".";
    private static final String LIST_SYMBOL = "[]";

    private Logger logger = LoggerFactory.getLogger(SortMap.class);


    /**
     *
     * Creates a new map that has all the fields from the input, sorts them and respond the newly created Map.
     *
     * @param input Map that needs to be sorted
     * @return Map that is sorted on the keys
     */
    public Map<String, Object> getSortedMap(final Map<String, Object> input) {
        Map<String, String> listKeys = new HashMap<>();
        return getSortedMap(input, listKeys);
    }


    /**
     *
     * @param input Map that needs to be sorted
     * @param listKeys List of field names that needs to be sorted, see the class documentation for more details
     * @return Map that is sorted on the keys
     */
    public Map<String, Object> getSortedMap(final Map<String, Object> input, final Map<String, String> listKeys) {
        return getSortedMap(input, listKeys, null);
    }

    /**
     * @param input Map that needs to be sorted
     * @param listKeys List of field names that needs to be sorted, see the class documentation for more details
     * @param prefix
     * @return Map that is sorted on the keys
     */
    private Map<String, Object> getSortedMap(final Map<String, Object> input, final Map<String, String> listKeys, final String prefix) {
        TreeMap<String, Object> treeMap = new TreeMap();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (entry.getValue() instanceof Map) {
                processMap(entry, treeMap, listKeys, prefix);
            } else if (entry.getValue() instanceof List) {
                processList(entry, treeMap, listKeys, prefix);
            } else {
                treeMap.put(entry.getKey(), entry.getValue());
            }
        }
        return treeMap;
    }

    /**
     * Process map inside map
     *
     * @param entry
     * @param treeMap
     * @param listKeys
     * @param prefix
     */
    private void processMap(final Map.Entry<String, Object> entry, final TreeMap<String, Object> treeMap, final Map<String, String> listKeys,
                            final String prefix) {
        String updatedPrefix = (null == prefix) ? EMPTY_STRING : prefix + PERIOD;
        Map<String, Object> innerMap = getSortedMap((Map) entry.getValue(), listKeys, updatedPrefix + entry.getKey());
        treeMap.put(entry.getKey(), innerMap);
    }

    /**
     * Process List or Set inside Map.
     *
     * @param entry
     * @param treeMap
     * @param listKeys
     * @param prefix
     */
    private void processList(final Map.Entry<String, Object> entry, final Map<String, Object> treeMap, final Map<String, String> listKeys, final String prefix) {
        String updatedPrefix = (null == prefix) ? entry.getKey() + LIST_SYMBOL : prefix + PERIOD + entry.getKey() + LIST_SYMBOL;
        List<Object> sortedSet;
        sortedSet = getSortedList(((List) entry.getValue()), listKeys, updatedPrefix);
        treeMap.put(entry.getKey(), sortedSet);
    }

    /**
     * Process List or Set inside List or Set
     *
     * @param entry
     * @param treeSet
     * @param listKeys
     * @param prefix
     */
    private void processList(final Object entry, final Set<Object> treeSet, final Map<String, String> listKeys, final String prefix) {
        String updatedPrefix = (null == prefix) ? LIST_SYMBOL : prefix + LIST_SYMBOL;
        List<Object> sortedSet;
        sortedSet = getSortedList(((List) entry), listKeys, updatedPrefix);
        treeSet.add(sortedSet);
    }

    /**
     * Process List that is inside Map
     *
     * @param input
     * @param listKeys
     * @param prefix
     * @return
     */
    private List<Object> getSortedList(final List<Object> input, final Map<String, String> listKeys, final String prefix) {
        Set<Object> response = null;
        for (Object currentItem : input) {
            logger.debug(currentItem.getClass().getName());
            if (currentItem instanceof Map) {
                if (response == null) {
                    response = new HashSet();
                }
                response.add(getSortedMap((Map<String, Object>) currentItem, listKeys, prefix));
            } else if (currentItem instanceof List) {
                if (response == null) {
                    response = new HashSet();
                }
                processList(currentItem, response, listKeys, prefix);
            } else {
                if (response == null) {
                    response = new TreeSet();
                }
                response.add(currentItem);
            }
        }
        if (listKeys.containsKey(prefix) && response instanceof HashSet) {
            return response.stream().sorted(Comparator.comparing(innerMap -> ((String) ((Map) innerMap).get(listKeys.get(prefix))))).collect(Collectors.toList());
        } else {
            return response.stream().collect(Collectors.toList());
        }
    }
}
