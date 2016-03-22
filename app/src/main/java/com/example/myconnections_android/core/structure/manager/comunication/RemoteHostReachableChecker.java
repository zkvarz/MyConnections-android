package com.example.myconnections_android.core.structure.manager.comunication;


import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.manager.Manager;
import com.example.myconnections_android.core.structure.manager.comunication.models.ReachableStatus;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

/**
 * Listens for remote host with limited interval;
 * <p/>
 * Might be useful for connection checking when device is connected to WI-Fi or
 * Mobile network but the host is not reachable for some reason;
 */

public class RemoteHostReachableChecker extends Manager<ReachableStatus> {
    private static final String REMOTE_HOST_FOR_REACHABLE_LISTENING = "http://www.google.com";
    private static final int REMOTE_HOST_DEFAULT_TIMEOUT = 5000; // milliseconds
    private static final int REMOTE_HOST_REACHABLE_CHECKING_INTERVAL = 5000; // milliseconds

    private static RemoteHostReachableChecker checker;

    private RemoteHostReachableChecker() {
    }

    public static synchronized RemoteHostReachableChecker get() {
        if (checker == null) {
            checker = new RemoteHostReachableChecker();
        }
        return checker;
    }

    private ReachableStatus status = ReachableStatus.HOST_REACHABLE;

    private final String remoteHost = REMOTE_HOST_FOR_REACHABLE_LISTENING;

    private CheckingHostThread checkingThread;

    /**
     * Start remote host listening
     */
    @Override
    public void obtainModel() {
        if (!isListening()) {
            Logger.warn(getClass(), "Started reachable checking!");
            checkingThread = new CheckingHostThread();
            checkingThread.start();
        }
    }

    public synchronized boolean isReachable() {
        return status == ReachableStatus.HOST_REACHABLE;
    }

    /**
     * Check if host listening started
     */
    public boolean isListening() {
        return checkingThread != null;
    }

    public void stopListening() {
        if (isListening()) {
            checkingThread.running = false;
            checkingThread = null;
        }
    }


    /**
     * Creates http params with initialized timeouts
     */
    private HttpParams createHttpParams() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = REMOTE_HOST_DEFAULT_TIMEOUT;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = REMOTE_HOST_DEFAULT_TIMEOUT;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }

    public HttpResponse getResponseFromHost() throws IOException {
        HttpGet httpGet = new HttpGet(remoteHost);
        HttpParams httpParameters = createHttpParams();

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        return httpClient.execute(httpGet);
    }

    private class CheckingHostThread extends Thread {
        private boolean running = true;

        public void run() {
            while (running) {
                HttpResponse response = null;

                try {
                    response = getResponseFromHost();
                } catch (IOException e1) {
                    Logger.warn(getClass(), "Failed to get response from host: "
                            + remoteHost);
                }

                synchronized (RemoteHostReachableChecker.this) {
                    ReachableStatus previousStatus = status;
                    if (response == null) {
                        status = ReachableStatus.HOST_NOT_REACHABLE;
                        Logger.warn(getClass(), "Host is not reachable!");
                    } else {
                        status = ReachableStatus.HOST_REACHABLE;
                        Logger.warn(getClass(), "Host is reachable!");
                    }
                    if (previousStatus != status) {
                        notifySuccess(status);
                    }
                }
                try {
                    Thread.sleep(REMOTE_HOST_REACHABLE_CHECKING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
