package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by merve on 8.01.2018.
 */

public class Business {
    private String link = "", result = "";
    private int intResult=-2;
    private final String URL = "http://localhost:51408/api/";
    private String mapURL="https://maps.googleapis.com/maps/api/directions/json";

    public String KullaniciLogin(String userName, String password) throws IOException {

        link = URL + "user/login/?kullaniciAdi=" + userName + "&sifre=" + password;
        result = Connect(link);
        link = "";
        return result;
    }

    public String UserRegister(String userName,String userPassword, String ureelName,String ureelSurName,String userMail) throws IOException {
        link= URL +"/UserRegister/?uname="+userName+"&upassword="+userPassword+"&ureelname="+ureelName+"&ureelsurname="+ureelSurName+"&umail="+userMail;
        result = Connect(link);
        link="";
        return result;
    }
    public String UserShowSpec(String userName,String userPassword) throws IOException {
        link = URL +"/UserShowSpec/?uname="+userName+"&upassword="+userPassword;
        result= Connect(link);
        link="";
        return result;
    }
    public String CafeList() throws IOException {
        link=URL +"/CafeList/";
        result=Connect(link);
        link="";
        return result;
    }
    public String CafeShowSpec(String cafeName,String cafePassword) throws IOException {
        link=URL+"/CafeShowSpec/?cname="+cafeName+"&cpassword="+cafePassword;
        result = Connect(link);
        link="";
        return result;
    }
    public String mapApi(double ilkKonum,double ikinciKonum) throws IOException {
        mapURL=URL+"?origin="+ilkKonum+"&destination"+ikinciKonum+"&key=AIzaSyC2iLa61YngDfok8eTpsjxdsd5JaVsBwVY";
        result = Connect(link);
        link="";
        return result;
    }



    private String Connect(String link) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet httpPost = new HttpGet(link);

        String jsonResult = "";

        HttpResponse response = httpClient.execute(httpPost);
        jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
        return jsonResult;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }

   /* public List<Etkinlik> returnCafeList(String result) throws JSONException {
        List<Etkinlik> ccList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        for(int i=0;i<jsonArray.length();i++ ){
            CafeClass cc = new CafeClass();
            JSONObject jo = new JSONObject(jsonArray.get(i).toString());
            cc.CafeRName = jo.getString("CafeRName");
            cc.CafeSize=jo.getInt("CafeSize");
            cc.CafeStatus=jo.getInt("CafeStatus");
            cc.CafeLocationLant=jo.getString("CafeLocationLant");
            cc.CafeLocationLong=jo.getString("CafeLocationLong");
            ccList.add(cc);
        }
        return ccList;
    }
    public String returnCafeRName(String result) throws JSONException {
        CafeClass cc = new CafeClass();
        JSONObject jsonObject=new JSONObject(result);
        cc.CafeRName=jsonObject.getString("CafeRName");
        return cc.CafeRName;
    }
    public int returnCafeSize(String result) throws JSONException {
        CafeClass cc = new CafeClass();
        JSONObject jsonObject=new JSONObject(result);
        cc.CafeSize=jsonObject.getInt("CafeSize");
        return cc.CafeSize;
    }
    public int returnCafeStatus(String result) throws JSONException {
        CafeClass cc = new CafeClass();
        JSONObject jsonObject=new JSONObject(result);
        cc.CafeStatus=jsonObject.getInt("CafeStatus");
        return cc.CafeStatus;
    }
    public String returnCafeLant(String result) throws JSONException {
        CafeClass cc = new CafeClass();
        JSONObject jsonObject = new JSONObject(result);
        cc.CafeLocationLant=jsonObject.getString("CafeLocationLant");
        return cc.CafeLocationLant;
    }
    public String returnCafeLong(String result) throws JSONException {
        CafeClass cc = new CafeClass();
        JSONObject jsonObject=new JSONObject(result);
        cc.CafeLocationLong=jsonObject.getString("CafeLocationLong");
        return cc.CafeLocationLong;
    }*/
}
