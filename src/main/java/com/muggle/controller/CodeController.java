package com.muggle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muggle.MyUIcodeCommand;
import com.muggle.ProjectMessageVO;
import com.muggle.poseidon.entity.ProjectMessage;
import com.muggle.poseidon.factory.CodeCommandInvoker;
import com.muggle.poseidon.genera.SimpleCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.muggle.poseidon.constant.GlobalConstant.SEPARATION;
import static com.muggle.poseidon.constant.GlobalConstant.USER_DIR;

/**
 * Description
 * Date 2021/8/5
 * Created by muggle
 */
@RestController
@RequestMapping("/ponseidon")
public class CodeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeController.class);


    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String create(@RequestBody ProjectMessageVO projectMessageVO){

        final SimpleCodeGenerator simpleCodeGenerator = new SimpleCodeGenerator(convertMessage(projectMessageVO));
        final CodeCommandInvoker invoker = new CodeCommandInvoker(simpleCodeGenerator);
        invoker.popCommond("createPom");
        if (!projectMessageVO.getExcloudCommonds().contains("createUIpom")){
            invoker.addCommond(new MyUIcodeCommand());
        }
        projectMessageVO.getExcloudCommonds().forEach(name->{});
        invoker.execute();
        serializJson(projectMessageVO);
        return "{\"result\":\"success\"}";
    }

    private void serializJson(ProjectMessageVO projectMessageVO) {
        StringBuilder path = new StringBuilder();
        path.append(System.getProperty(USER_DIR)).append(SEPARATION).append("projectMessageVO.json");
        final File file = new File(path.toString());
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (final FileOutputStream fileOutputStream = new FileOutputStream(file)){
            ObjectMapper mapper = new ObjectMapper();
            fileOutputStream.write( mapper.writeValueAsString(projectMessageVO).getBytes());
            LOGGER.info("==========> [持久化提交数据]");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/message",produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public String getMessage(){
        StringBuilder path = new StringBuilder();
        path.append(System.getProperty(USER_DIR)).append(SEPARATION).append("projectMessageVO.json");
        final File file = new File(path.toString());
        if (!file.exists()){
            return "{}";
        }
        try (final FileReader fileReader = new FileReader(file)){
           BufferedReader buffer = new BufferedReader(fileReader);
            String line = null;
            final StringBuilder result = new StringBuilder();
            while((line = buffer.readLine())!=null) {
                result.append(line);
            }
            return result.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    private ProjectMessage convertMessage(ProjectMessageVO projectMessageVO) {
        final ProjectMessage build = ProjectMessage.builder().author(projectMessageVO.getAuthor())
            .driver(projectMessageVO.getDriver()).otherField(projectMessageVO.getOtherField())
            .jdbcUrl(projectMessageVO.getJdbcUrl()).parentPack(projectMessageVO.getParentPack())
            .password(projectMessageVO.getPassword()).projectPackage(projectMessageVO.getProjectPackage())
            .suffix(projectMessageVO.getSuffix()).swagger(projectMessageVO.isSwagger())
            .tableName(projectMessageVO.getTableName()).username(projectMessageVO.getUsername())
            .module(projectMessageVO.getModule()).build();
        if (CollectionUtils.isEmpty(build.getOtherField())){
            final Map<String, String> map = new HashMap<>();
            build.setOtherField(map);
        }
        if (build.getOtherField().get("parentVersion")==null){
            build.getOtherField().put("parentVersion","1.0-SNAPSHOT");
        }
        return build;
    }
}
