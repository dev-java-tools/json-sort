package dev.javatools.json.sort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.javatools.json.maputils.CreateMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class SortJson {

    private Logger logger = LoggerFactory.getLogger(SortJson.class);
    ObjectMapper objectMapper = new ObjectMapper();
    CreateMap createMap = new CreateMap();

    public String sort(final String input, final Map<String, String> listKeys) throws JsonProcessingException {
        Map<String, String> listKeysInternal = new HashMap<>();
        if (null != listKeys) {
            listKeysInternal.putAll(listKeys);
        }
        Map<String, Object> response = sort(createMap.create(input), listKeysInternal, null);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    public String sort(final String input) throws JsonProcessingException {
        Map<String, String> listKeys = new HashMap<>();
        return sort(input, listKeys);
    }

    public Map<String, Object> sort(final Object input, final Map<String, String> listKeys) throws JsonProcessingException {
        Map<String, String> listKeysInternal = new HashMap<>();
        if (null != listKeys) {
            listKeysInternal.putAll(listKeys);
        }
        return sort(createMap.create(input), listKeysInternal, null);
    }

    public Map<String, Object> sort(Object input) throws JsonProcessingException {
        return sort(createMap.create(input), null);
    }

    private Map<String, Object> sort(final Map<String, Object> input, final Map<String, String> listKeys, final String prefix) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.debug(stackTrace[1].getMethodName() + "=>" + prefix);
        TreeMap<String, Object> treeMap = new TreeMap();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (entry.getValue() instanceof Map) {
                processMap(entry, treeMap, listKeys, prefix);
            }
            else if (entry.getValue() instanceof List) {
                processList(entry, treeMap, listKeys, prefix);
            }
            else {
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
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.debug(stackTrace[1].getMethodName() + "=>" + prefix);
        String updatedPrefix = (null == prefix) ? "" : prefix + ".";
        Map<String, Object> innerMap = sort((Map) entry.getValue(), listKeys, updatedPrefix + entry.getKey());
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
    private void processList(Map.Entry<String, Object> entry, Map<String, Object> treeMap, Map<String, String> listKeys, final String prefix) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.debug(stackTrace[1].getMethodName() + "=>" + prefix);
        String updatedPrefix = (null == prefix) ? entry.getKey() + "[]" : prefix + "." + entry.getKey() + "[]";
        List<Object> sortedSet;
        sortedSet = sortList(((List) entry.getValue()), listKeys, updatedPrefix);
        logger.debug(stackTrace[1].getMethodName() + "=>" + updatedPrefix + entry.getKey());
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
    private void processList(Object entry, Set<Object> treeSet, Map<String, String> listKeys, final String prefix) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.debug(stackTrace[1].getMethodName() + "=>" + prefix);
        String updatedPrefix = (null == prefix) ? "[]" : prefix + "[]";
        List<Object> sortedSet;
        sortedSet = sortList(((List) entry), listKeys, updatedPrefix);
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
    private List<Object> sortList(List<Object> input, Map<String, String> listKeys, String prefix) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.debug(stackTrace[1].getMethodName() + "=>" + prefix);
        Set<Object> response = null;
        for (Object currentItem : input) {
            logger.debug(currentItem.getClass().getName());
            if (currentItem instanceof Map) {
                if (response == null) {
                    response = new HashSet();
                }
                //String updatedPrefix = (null == prefix) ? "." : prefix + ".";
                response.add(sort((Map<String, Object>) currentItem, listKeys, prefix));
            }
            else if (currentItem instanceof List) {
                if (response == null) {
                    response = new HashSet();
                }
                processList(currentItem, response, listKeys, prefix);
            }
            else {
                if (response == null) {
                    response = new TreeSet();
                }
                response.add(currentItem);
            }
        }
        if (listKeys.containsKey(prefix) && response instanceof HashSet) {
            return response.stream().sorted(Comparator.comparing(innerMap -> ((String) ((Map) innerMap).get(listKeys.get(prefix))))).collect(Collectors.toList());
        }
        else {
            return response.stream().collect(Collectors.toList());
        }

    }
}
