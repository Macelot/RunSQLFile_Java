/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exemplorunscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author marce
 * https://www.toolbox.com/tech/programming/question/running-an-sql-file-from-java-via-jdbc-api-041307/
 * https://howtodoinjava.com/java/io/read-write-utf8-data-file/
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connection c = conecta();
        executa(c);
       
    }
    
    public static void executa(Connection c){
        String sqlFile = "usuarios.sql";
        StringBuffer sb = new StringBuffer();
        String s=new String("");
        try {
            File file = new File(sqlFile);
            // be sure to not have line starting with “–” or “/*” or any other non aplhabetical character
            FileInputStream fileIS = new FileInputStream(file);
            InputStreamReader fileISR = new InputStreamReader(fileIS, "UTF-8");//<< este aceita encoding
            BufferedReader br = new BufferedReader(fileISR);
            while((s = br.readLine()) != null){
                sb.append(s);
            }
            br.close();
            // here is our splitter ! We use “;” as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");
            Statement st = c.createStatement();
            for(int i = 0; i<inst.length; i++){
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!inst[i].trim().equals("")){
                    st.executeUpdate(inst[i]);
                    System.out.println(">> "+inst[i]);
                }
            }
        }
        catch(Exception e){
            System.out.println("*** Error : "+e.getMessage());
        }
    }
    
    public static Connection conecta(){
        Connection con=null;
        try {
            String url="jdbc:mysql://localhost:3307/test";
            String user="root";
            String pass="usbw";        
            con = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("*** Connection Error : "+e.getMessage());
        }
        return con;
    }
    
}
