package helper.api;
import helper.enums.ApiFunction;
import helper.interfaces.IThreading;
import helper.io.IOHandler;
import helper.struct.JsonObject;
import helper.threading.BaseThreading;
import helper.widget.Widget;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiHandler extends BaseThreading {
    String url;
    String[] args;
    URL urlObject;
    HttpsURLConnection httpsCon;
    ApiFunction func;

    public ApiHandler(Widget w,ApiFunction func,String[] args){
        super(w);
        this.args = args;
        this.func = func;
    }

    public void heavyDuty(){
        switch(func){
            case URL_UPLOAD_HIGHSCORE:{
                urlUploadHighScore();
                break;
            }
            case URL_GET_HIGHSCORE:{
                urlGetHighScore();
                break;
            }
            default :{
                break;
            }
        }
    }

    String login(){
        url = IOHandler.getEnv("tokenAuth");
        String userName = IOHandler.getEnv("username");
        String password = IOHandler.getEnv("password");
        String parameters = "username=%s&password=%s".formatted(userName,password);
        String serverResponse = executePostRequest(parameters,url);
        if(serverResponse.length() != 0){
            JsonObject json = new JsonObject(serverResponse);
            String token = (String)json.objMap.getValue("token");
            return token;
        }
        return null;
    }

    void urlUploadHighScore(){
        String token = login();
        if(token != null){
            String url = IOHandler.getEnv("uploadNewHighScoreUrl");
            url+="&name=%s&score=%s".formatted(args[0],args[1]);
            String serverResponse = executeGetRequest(url,token);
            if(serverResponse.length()!= 0){
                JsonObject json = new JsonObject(serverResponse);
                json.getServerResponse();
            }
        }
    }

    void urlGetHighScore(){
        String token = login();
        if(token != null){
            String url = IOHandler.getEnv("getHighScoreUrl");
            String serverResponse = executeGetRequest(url,token);
            if(serverResponse.length()!= 0){
                JsonObject json = new JsonObject(serverResponse);
                json.getHighScoreValues(args);
                //IOHandler.printString(args[0]);
            }
        }
    }

    String executePostRequest(String parameters,String url){
        byte[] postData = parameters.getBytes( StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String outPutData = "";
        try{
            urlObject = new URL(url);
            SSLSocketFactory ssls = IOHandler.loadSSLCert();
            if(ssls != null){
                httpsCon = (HttpsURLConnection) urlObject.openConnection();
                httpsCon.setSSLSocketFactory(ssls);
                httpsCon.setDoOutput(true);
                httpsCon.setInstanceFollowRedirects( false );
                //httpsCon.setDoInput(true);
                httpsCon.setRequestMethod("POST");
                //httpsCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpsCon.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                httpsCon.setRequestProperty( "charset", "utf-8");
                httpsCon.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                httpsCon.setRequestProperty("Accept", "application/json");
                httpsCon.setUseCaches(false);
                /*DataOutputStream wr = new DataOutputStream(httpsCon.getOutputStream());
                wr.write(postData);*/
                IOHandler.writeOutputStream(httpsCon,postData);
                outPutData = IOHandler.readInputStream(httpsCon);
                /*String line = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));

                while((line = in.readLine())!= null){outPutData+=line;}
                in.close();*/
            }
        }
        catch(Exception e){
            IOHandler.logToFile(e.getMessage());
        }
        return outPutData;
    }

    String executeGetRequest(String url,String token){
        String outPutData = "";
        try{
            urlObject = new URL(url);
            SSLSocketFactory ssls = IOHandler.loadSSLCert();
            if(ssls != null){
                httpsCon = (HttpsURLConnection) urlObject.openConnection();
                httpsCon.setSSLSocketFactory(ssls);
                httpsCon.setDoOutput(true);
                httpsCon.setDoInput(true);
                httpsCon.setRequestMethod("GET");
                httpsCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpsCon.setRequestProperty("Accept", "application/json");
                httpsCon.setRequestProperty("Authorization", "Token " + token);
                outPutData = IOHandler.readInputStream(httpsCon);
                /*String line = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));

                while((line = in.readLine())!= null){outPutData+=line;}
                in.close();*/
            }
        }
        catch(Exception e){
            IOHandler.logToFile(e.getMessage());
        }
        return outPutData;
    }

}
