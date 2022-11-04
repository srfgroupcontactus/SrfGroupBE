package com.takirahal.srfgroup.utils;

import com.takirahal.srfgroup.constants.SrfGroupConstants;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-app-alert", RequestUtil.messageTranslate(message));
        try {
            headers.add("X-app-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException var5) {
            ;
        }
        return headers;
    }
}
