package com.lucky.androidlearn.rxjava2.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchTaskLauncher {

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    List<KeyWordsSearchRunnableTask> keywordRunnableTaskList = new ArrayList<>();

    public void executeSearchKeyWordTask(KeyWordsSearchRunnableTask runnableTask) {
        executorService.execute(runnableTask);
        keywordRunnableTaskList.add(runnableTask);
        long lastTaskCreateTime = runnableTask.getCreateTime();
        for (int index = 0; index < keywordRunnableTaskList.size() - 1; index++) {
            KeyWordsSearchRunnableTask task = keywordRunnableTaskList.get(index);
            if (lastTaskCreateTime - task.getCreateTime() < 1000) {
                task.setAbortDisplay(true);
            }
        }
    }

}
