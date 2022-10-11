package com.example.filelistner.listener;

import com.example.filelistner.manager.TestManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.util.Objects;

/**
 * @author huke
 * @date 2022/10/09/下午4:35
 */
@Slf4j
@RequiredArgsConstructor
public class TestListener extends FileAlterationListenerAdaptor {

    private final TestManager testManager;

    private static final String testConfigFile = "test.yml";

    /**
     * 文件变更重新加载配置文件爱你
     *
     * @param file
     */
    @Override
    public void onFileChange(File file) {
        if (Objects.isNull(file) || !testConfigFile.equals(file.getName())) {
            return;
        }
        log.info(String.format("file %s change , reload file content", file.getName()));
        testManager.reload();
        System.out.println("文件重新加载成功");
        log.info(String.format("reload file %s content success", file.getName()));
    }
}
