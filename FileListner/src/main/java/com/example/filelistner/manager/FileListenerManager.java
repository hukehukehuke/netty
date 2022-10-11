package com.example.filelistner.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author huke
 * @date 2022/10/10/上午10:51
 */
@Component
@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
public class FileListenerManager {

    @Value("${Config.File.Path}")
    private String configFileRootPath;

    Map<String, FileAlterationListener> listenersMap = new ConcurrentHashMap<>();

    private FileAlterationObserver fileAlterationObserver;

    /**
     * 容器启动注册文件监听器
     */
    @PostConstruct
    public void registerListener() {
        File file = new File(configFileRootPath);
        if (!file.exists() || !file.isDirectory()) {
            log.error(String.format("file %s don't exist or isn't a directory !", configFileRootPath));
            return;
        }
        //创建过滤器
        IOFileFilter directories = FileFilterUtils.and(
                FileFilterUtils.directoryFileFilter(),
                HiddenFileFilter.VISIBLE);
        IOFileFilter files = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".yml"));
        IOFileFilter filter = FileFilterUtils.or(directories, files);

        //使用过滤器
        fileAlterationObserver = new FileAlterationObserver(file, filter);

        //论巡间隔1秒
        long interval = TimeUnit.SECONDS.toMillis(1);

        //创建文件变化监听器

        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, fileAlterationObserver);

        //开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            log.error("register listener of config file error");
        }
    }

    /**
     * 服务停止，清除文件监听器
     */
    @PreDestroy
    public void cleatListeners() {
        this.listenersMap.clear();
    }

    /**
     * 添加监听器
     *
     * @param configFileName 监听的文件名称
     * @param listener       文件监听器
     */
    public void addListener(String configFileName, FileAlterationListener listener) {
        if (Objects.isNull(fileAlterationObserver)) {
            log.error("FileAlterationObserver does not exist maybe config file directory config wrong!");
            return;
        }
        if (listenersMap.containsKey(configFileName)) {
            log.info(String.format("listener of %s has been added", configFileName));
            return;
        }
        fileAlterationObserver.addListener(listener);
        listenersMap.put(configFileName, listener);
        log.info(String.format("add listener of %s success", configFileName));
    }

}
