package com.example.myconnections_android.core.structure.requests.models;

import java.net.HttpURLConnection;

/**
 * response from HttpRequest
 */
public class RemoteResponse {
    private final String mStream;
    private final int mResponseCode;

    public RemoteResponse(String mStream, int mResponseCode) {
        this.mStream = mStream;
        this.mResponseCode = mResponseCode;
    }

    /*public InputStream getInputStream() {
        return mStream;
    }*/

    public int getResponseCode() {
        return mResponseCode;
    }

    public boolean isSuccess() {
        return getResponseCode() == HttpURLConnection.HTTP_OK;
    }


    @Override
    public String toString() {
    return mStream;
    }

    /*@Override
    public String toString() {
       *//* try {
            return IOUtils.toString(mStream);
        } catch (IOException e) {
            e.printStackTrace();
        }*//*

 *//*       BufferedReader r = new BufferedReader(new InputStreamReader(mStream));
        StringBuilder total = new StringBuilder();
        String line = "";
        try {
            while ((line = r.readLine()) != null) {
                Logger.debug(this.getClass(), total.append(line).toString());
                return total.append(line).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*//*

        try {
            StringBuilder response = new StringBuilder("");

            String line = null;
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(mStream, "UTF-8"));

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            Logger.debug(this.getClass(), "AWESOME!" + response.toString());
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }*/
}
