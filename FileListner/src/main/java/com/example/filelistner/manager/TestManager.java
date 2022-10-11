package com.example.filelistner.manager;

import com.example.filelistner.listener.TestListener;
import com.example.filelistner.util.YmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

/**
 * @author huke
 * @date 2022/10/10/上午11:19
 */
@Component
@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
public class TestManager {


    @Value("${Config.File.Path}")
    private String configFileRootPath;

    @Resource
    private FileListenerManager fileListenerManager;

    private static final String configFile = "test.yml";

    /**
     * 服务启动加载配置文件
     */
    @PostConstruct
    public void load() {
        String configFilePath = configFileRootPath + configFile;
        File file = new File(configFilePath);
        if(!file.exists()){
            log.warn(String.format("config file %s does not exist",configFilePath));
            return;
        }

        Map<String, String> ymlByFilePath = YmlUtil.getYmlByFileName(configFile);

        System.out.println(ymlByFilePath);

        fileListenerManager.addListener(configFilePath,new TestListener(this));

    }

    public TestManager(String configFileRootPath, FileListenerManager fileListenerManager) {
        this.configFileRootPath = configFileRootPath;
        this.fileListenerManager = fileListenerManager;
    }

    /**
     * 重新加载配置文件
     */
    public void reload() {
        load();
    }
}
