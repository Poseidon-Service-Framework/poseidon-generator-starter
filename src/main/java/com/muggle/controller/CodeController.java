package com.muggle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Description
 * Date 2021/8/5
 * Created by muggle
 */
@Controller
@RequestMapping("/ponseidon-ui")
public class CodeController {

    @GetMapping("/")
    public void page(){
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes()).getResponse();
        try (PrintWriter writer = resp.getWriter();
             InputStream resource = CodeController.class.getClassLoader()
                 .getResourceAsStream("html/test.html");){
            byte[] temp = new byte[1024];
            int len;
            final StringBuilder sb = new StringBuilder();
            while ((len = resource.read(temp)) != -1) {
                sb.append(new String(temp, 0, len, StandardCharsets.UTF_8));
            }
            writer.print(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/create")
    public void create(@RequestBody Map<String,Object> params){
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes()).getResponse();
        try (final PrintWriter writer = resp.getWriter()){
           writer.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
