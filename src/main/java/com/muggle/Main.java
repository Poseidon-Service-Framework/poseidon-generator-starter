package com.muggle;

import com.muggle.poseidon.entity.ProjectMessage;
import com.muggle.poseidon.factory.CodeCommandInvoker;
import com.muggle.poseidon.genera.SimpleCodeGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Description
 * Date 2021/8/5
 * Created by muggle
 */
public class Main {
    public static void main(String[] args)  {
        Map<String, String> otherfield=new HashMap<String, String>();
        otherfield.put("parentVersion","1.0-SNAPSHOT");
        ProjectMessage build = ProjectMessage.builder().author("muggle").driver("com.mysql.jdbc.Driver").username("root")
            .swagger(true).tableName(Arrays.asList("")).parentPack("com.muggle.base")
            .jdbcUrl("jdbc:mysql:///p_oa?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC")
            .suffix("user").password("root")/*.module("muggle-generator")*/.projectPackage("com.muggle")
            .otherField(otherfield).build();
        CodeCommandInvoker invoker = new CodeCommandInvoker( new SimpleCodeGenerator(build));
//        invoker.popCommond("createCode");
        invoker.execute();
    }
}