package com.yao.zhihudaily.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Yao
 * @date 2020/02/19
 */
public class AssetsUtil {

    public static String getString(String fileName) {
        try {
            InputStream is = ResUtil.getResources().getAssets().open(fileName);

            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
