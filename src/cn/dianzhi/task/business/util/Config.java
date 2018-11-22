/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:Config.java Project: Framework
 * 
 * Creator:JFL 
 * Date:2012-5-16 下午04:19:05
 * 
 */
package cn.dianzhi.task.business.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 
 * 
 * 
 * @author JFL
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/10/09 02:01:29 $
 * @since 2012-5-16
 * 
 */

public class Config {
    private static final Log log = LogFactory.getLog(Config.class);
    private static final String CONFIG_FILE = "classpath:app.properties";
    private final Map<String, String> configCache = new HashMap<String, String>();
    private static final Config instance = new Config();

    public static Resource[] getResources(String locationPattern) {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[0];
        try {
            resources = patternResolver.getResources(locationPattern);
        } catch (IOException e) {
            throw new Error(e);
        }
        return resources;
    }

    private Config() {
        Resource[] resources = getResources(CONFIG_FILE);
        System.out.println("***************config*****************");
        for (Resource resource : resources) {
            InputStream in = null;
            try {
                Properties properties = new Properties();
                in = resource.getInputStream();
                properties.load(in);

                System.out.println("$$$$$$ Config File Path [" + resource.getFile().getAbsolutePath() + "] $$$$$$");
                Enumeration<?> propertyNames = properties.propertyNames();
                while (propertyNames != null && propertyNames.hasMoreElements()) {
                    String name = (String) propertyNames.nextElement();
                    if (StringUtils.isNotEmpty(name)) {
                        configCache.put(name, properties.getProperty(name, ""));
                        System.out.println(name + "=" + configCache.get(name));
                    }
                }
            } catch (Exception e) {
                log.error("解析配置文件异常(" + resource.getFilename() + ")->", e);
                throw new Error(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        System.out.println("*************************************");

    }

    public static Config getInstance() {
        return instance;
    }

    public List<String> getKeys() {
        return new ArrayList<String>(configCache.keySet());
    }

    public List<String> getKeysByRegex(String regex) {
        List<String> matchedKeys = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        for (String key : configCache.keySet()) {
            if (pattern.matcher(key).matches()) {
                matchedKeys.add(key);
            }
        }
        return matchedKeys;
    }

    public Set<String> getKeysByRegex(String regex, int group) {
        Set<String> matchedKeys = new TreeSet<String>();
        Pattern pattern = Pattern.compile(regex);
        for (String key : configCache.keySet()) {
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                matchedKeys.add(matcher.group(group));
            }
        }
        return matchedKeys;
    }

    public List<String> getKeysByPrefix(String prefix) {
        List<String> matchedKeys = new ArrayList<String>();
        for (String key : configCache.keySet()) {
            if (key.startsWith(prefix)) {
                matchedKeys.add(key);
            }
        }
        return matchedKeys;
    }

    public String getString(String key) {
        return configCache.get(key);
    }

    public String getString(String key, String defaultValue) {
        String v = getString(key);
        return v == null ? defaultValue : v;
    }

    public Boolean getBoolean(String key) {
        String v = getString(key);
        try {
            return Boolean.valueOf(v);
        } catch (Exception e) {
            return null;
        }

    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean v = getBoolean(key);
        return v == null ? defaultValue : v;
    }

    public Integer getInteger(String key) {
        String v = getString(key);
        try {
            return Integer.valueOf(v);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getInteger(String key, Integer defaultValue) {
        Integer v = getInteger(key);
        return v == null ? defaultValue : v;
    }

    public Long getLong(String key) {
        String v = getString(key);
        try {
            return Long.valueOf(v);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getLong(String key, Long defaultValue) {
        Long v = getLong(key);
        return v == null ? defaultValue : v;
    }

}
