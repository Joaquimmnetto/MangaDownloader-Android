package com.manLoader.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by KithLenovo on 31/03/2015.
 */

public class ThreadQueue{

    private boolean looping;

    private Executor queueExecutor;
    private Executor loopExecutor;

    private int maxSimultaneousExecutions;
    private Queue<TaskEncapsuler> queue;
    private Runnable loop;
    private Semaphore loopMutex;

    private List<Object> response;




    public ThreadQueue(int maxSimultaneousExecutions){

        queueExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        loopExecutor = AsyncTask.SERIAL_EXECUTOR;
        this.maxSimultaneousExecutions = maxSimultaneousExecutions;
        queue = new ArrayDeque<TaskEncapsuler>();
        response = new ArrayList<Object>();
    }

    public synchronized void enqueue(AsyncTask task,Object... params){
        queue.add(new TaskEncapsuler(task,params));
        if(looping && !loopMutex.tryAcquire()){
            loopMutex.release();
        }

    }

    /**
     *  Executa todas as tasks enfileradas, a partir da thread principal.
     *
     * @throws ExecutionException - not going
     * @throws InterruptedException - to happen, god willing.
     */
    public void execute() throws ExecutionException, InterruptedException {
        List<TaskEncapsuler> executing = new ArrayList<TaskEncapsuler>();
        synchronized (this) {
            for (int i = 0; (i < maxSimultaneousExecutions) && !queue.isEmpty(); i++) {
                TaskEncapsuler current = queue.remove();
                current.task.executeOnExecutor(queueExecutor, current.params);
                executing.add(current);
            }

        }
        for(TaskEncapsuler t:executing){
                response.add(t.task.get());
        }
    }

    /**
     * Executa todas as tasks a medida que elas vão entrando na fila, com o limite máximo de maxSimultaneousExecutions por vez, a partir de uma thread criada.
     */
    public void executeLooping(){
        if(!looping) {
            loopMutex = new Semaphore(1);

            Runnable loop = new Runnable() {
                @Override
                public void run() {
                    while(looping){
                        try {
                            if(queue.size() <= 0){
                                loopMutex.acquire();
                            }
                            if(!looping){
                                break;
                            }
                            execute();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            looping = true;
            loopExecutor.execute(loop);
        }
        else{
            //do nothing!
        }
    }



    public boolean isLooping(){
        return looping;
    }



    public synchronized  void pauseLooping(){
        if(!loopMutex.tryAcquire()){
            loopMutex.release();
        }
        looping = false;
    }

    public synchronized void stopLooping(){
        pauseLooping();
        queue.clear();
    }



    private class TaskEncapsuler{
        public AsyncTask task;
        public Object[] params;

        public TaskEncapsuler(AsyncTask task, Object... params){
            this.task = task;
            this.params = params;
        }


    }
}
