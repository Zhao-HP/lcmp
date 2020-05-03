package com.zhp.lcmp.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文件工具类
 *
 * @author ZhaoHP
 * @date 2020/5/2 16:48
 */
@Slf4j
public class FileUtil {

    public static String readFileContent(String path){

        log.info("文件读取路径：{}", path);

        String result = "";
        FileInputStream fileInputStream = null;
        BufferedInputStream bis = null;
        try {
            fileInputStream = new FileInputStream(path);
            bis = new BufferedInputStream(fileInputStream);
            StringBuffer sb = new StringBuffer();
            int len = 0;
            byte[] temp = new byte[1024];
            while ((len = bis.read(temp)) != -1) {
                sb.append(new String(temp, 0, len));
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (fileInputStream!=null){
                    fileInputStream.close();
                }
                if (null != bis){
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void wirteFileContent(String filePath, String content){
        log.info("文件写入路径：{}", filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createDirs(String filePath){
        File file = new File(filePath);
        if (!file.exists()){
            log.info("创建文件夹目录：{}", filePath);
            file.mkdirs();
        }
    }

    public static void deleteFile(String filePath){
        log.info("文件删除，路径：{}",filePath);
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
    }

    public static void main(String[] args) {
        deleteFile("E:\\Code\\JAVA\\tmp\\null\\resolv.conf");
    }

}
