package com.zhp.lcmp.util;

import ch.ethz.ssh2.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 远程执行Linux的Shell脚本
 *
 * @author ZhaoHP
 * @date 2020/5/2 16:18
 */
public class RemoteExecuteCommand {
    private static final Logger logger = LoggerFactory.getLogger(RemoteExecuteCommand.class);
    private static String  DEFAULT_CHART="UTF-8";
    private Connection conn;
    private String ip;
    private String userName;
    private String userPwd;

    public RemoteExecuteCommand(String ip, String userName, String userPwd) {
        this.ip = ip;
        this.userName = userName;
        this.userPwd = userPwd;
    }

    /**
     * 远程登录linux的主机
     * 登录成功返回true，否则返回false
     */
    public Boolean login(){
        boolean loginState = false;
        try {
            conn = new Connection(ip);
            conn.connect();
            loginState = conn.authenticateWithPassword(userName, userPwd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginState;
    }

    public String executeCommand(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }
        process.destroy();
        return sb.toString();
    }

    public static void main(String[] args) {
        String ip = "101.201.70.167";
        String userName = "root";
        String userPwd = "guduke0215.";
        RemoteExecuteCommand r = new RemoteExecuteCommand(ip,userName,userPwd);
        System.out.println(r.login());
    }
}
