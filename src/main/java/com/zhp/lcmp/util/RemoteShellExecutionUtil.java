package com.zhp.lcmp.util;

import ch.ethz.ssh2.*;
import com.alibaba.fastjson.JSON;
import com.zhp.lcmp.entity.ServerInfoEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Vector;

/**
 * 远程Shell命令类
 *
 * @author ZhaoHP
 * @date 2020/2/16 13:57
 */
@Slf4j
public class RemoteShellExecutionUtil {

    private static Connection connection = null;
    private static final long TIME_OUT = 100000000;

    private static void getConnection(ServerInfoEntity serverInfoEntity) {
        try {

            connection = new Connection(serverInfoEntity.getIpAddress(), 22);
            connection.connect();
            boolean isAuthenticate = connection.authenticateWithPassword(serverInfoEntity.getLoginName(),serverInfoEntity.getLoginPwd());
            if (!isAuthenticate) {
                throw new Exception("连接失败，请确定用户名和密码是否正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init(ServerInfoEntity serverInfoEntity) {
        log.info("初始化Connection；服务器参数：{}", JSON.toJSONString(serverInfoEntity));
        getConnection(serverInfoEntity);
    }

    /**
     * 获得远程文件的各个属性
     *
     * @param remotePath
     */
    public static void getFileProperties(String remotePath, ServerInfoEntity serverInfoEntity) {
        try {
            init(serverInfoEntity);
            SFTPv3Client sft = new SFTPv3Client(connection);
            Vector<?> v = sft.ls(remotePath);
            for (int i = 0; i < v.size(); i++) {
                SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                s = (SFTPv3DirectoryEntry) v.get(i);
                //文件名
                String filename = s.filename;
                //文件的大小
                Long fileSize = s.attributes.size;
            }
            sft.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 上传本地文件到服务器目录下
     *
     * @param fileName   本地文件
     * @param remotePath 服务器目录
     */
    public static void putFile(String fileName, String remotePath, ServerInfoEntity serverInfoEntity) {
        init(serverInfoEntity);
        SCPClient sc = new SCPClient(connection);
        try {
            //将本地文件放到远程服务器指定目录下，默认的文件模式为 0600，即 rw，
            //如要更改模式，可调用方法 put(fileName, remotePath, mode),模式须是4位数字且以0开头
            sc.put(fileName, remotePath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }
    }


    /**
     * 下载服务器文件到本地目录
     *
     * @param fileName  服务器文件
     * @param localPath 本地目录
     */
    public static boolean copyFile(String fileName, String localPath,ServerInfoEntity serverInfoEntity){
        init(serverInfoEntity);
        SCPClient sc = new SCPClient(connection);
        boolean result = true;
        try {
            sc.get(fileName, localPath);
        } catch (IOException e) {
            result = false;
        }finally {
            connection.close();
            return result;
        }
    }

    /**
     * 在远程LINUX服务器上，在指定目录下，删除指定文件
     *
     * @return
     * @param[in] fileName 文件名
     * @param[in] remotePath 远程主机的指定目录
     */
    public void delFile(String remotePath, String fileName,ServerInfoEntity serverInfoEntity) {
        init(serverInfoEntity);
        try {
            SFTPv3Client sft = new SFTPv3Client(connection);
            //获取远程目录下文件列表
            Vector<?> v = sft.ls(remotePath);

            for (int i = 0; i < v.size(); i++) {
                SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                s = (SFTPv3DirectoryEntry) v.get(i);
                //判断列表中文件是否与指定文件名相同
                if (s.filename.equals(fileName)) {
                    //rm()方法中，须是文件绝对路径+文件名称
                    sft.rm(remotePath + s.filename);
                }
                sft.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 执行脚本
     *
     * @param cmds 要在linux上执行的指令
     */
    public static String exec(String cmds, ServerInfoEntity serverInfoEntity){
        InputStream stdOut = null;
        InputStream stdErr = null;
        int ret = -1;
        init(serverInfoEntity);
        StringBuffer buffer = new StringBuffer();
        Session session = null;
        try {
            session = connection.openSession();
            //在远程服务器上执行linux指令
            session.execCommand(cmds);
            //指令执行结束后的输出
            stdOut = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdOut));
            String line=null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            //指令执行结束后的错误
            stdErr = new StreamGobbler(session.getStderr());
            //等待指令执行结束
            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session!= null){
                log.info("关闭Session");
                session.close();
            }
            if (connection != null){
                log.info("关闭Connection");
                connection.close();
            }
        }

        return buffer.toString();
    }

}
