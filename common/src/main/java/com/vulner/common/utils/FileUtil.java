package com.vulner.common.utils;

import com.vulner.common.bean.dto.ExcelDataDto;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.net.URLEncoder;

public class FileUtil {

    /**
     * 使用浏览器选择路径下载
     * @param response
     * @param fileName
     * @param data
     * @throws Exception
     */
    public static void exportFile(HttpServletResponse response, String fileName, ExcelDataDto data) throws Exception {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/octet-stream");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".txt", "utf-8"));
        exportWord(data, response.getOutputStream(), fileName);
    }

    private static void exportWord(ExcelDataDto data, ServletOutputStream out, String fileName) {
        BufferedOutputStream buff = null;
        StringBuffer write = new StringBuffer();
        try {
            buff = new BufferedOutputStream(out);
            if (data != null) {
                write.append(data.getContent());
            }

            buff.write(write.toString().getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buff.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
