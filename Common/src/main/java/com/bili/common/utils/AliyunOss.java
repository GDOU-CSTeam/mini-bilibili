package com.bili.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AliyunOss {
    @Value("${oss.endpoint}")
    String endpoint;
    // 从环境变量中获取步骤1生成的RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。
    @Value("${oss.accessKeyId}")
    String accessKeyId;
    @Value("${oss.accessKeySecret}")
    String accessKeySecret;
    // 从环境变量中获取步骤3生成的RAM角色的RamRoleArn。
    @Value("${oss.roleArn}")
    String roleArn;
    // 自定义角色会话名称，用来区分不同的令牌，例如可填写为SessionTest。
    @Value("${oss.roleSessionName}")
    String roleSessionName;
    @Value("${oss.endpointForOss}")
    String endpointForOss;
    @Value("${oss.bucketName}")
    String bucketName;

    OSS ossClient = null;

    @PostConstruct
    public void init(){
        ossClient = new OSSClientBuilder().build(endpointForOss,accessKeyId,accessKeySecret);
    }



    public HashMap<String, Object> getKey(String type,String name,String fileSuffix) throws ClientException {
        // 以下Policy用于限制仅允许使用临时访问凭证向目标存储空间examplebucket下的src目录上传文件。
        // 临时访问凭证最后获得的权限是步骤4设置的角色权限和该Policy设置权限的交集，即仅允许将文件上传至目标存储空间examplebucket下的src目录。
        // 如果policy为空，则用户将获得该角色下所有权限。
        String fileName = "src/"+ type + "/" + name + "." + fileSuffix;
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:PutObject\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:"+bucketName+"/"+fileName+"\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        // 设置临时访问凭证的有效时间为900秒。
        Long durationSeconds = 900L;
        HashMap<String,Object> map = new HashMap();
        map.put("name",fileName);
        // regionId表示RAM的地域ID。以华东1（杭州）地域为例，regionID填写为cn-hangzhou。也可以保留默认值，默认值为空字符串（""）。
        String regionId = "";
        // 添加endpoint。适用于Java SDK 3.12.0及以上版本。
        DefaultProfile.addEndpoint(regionId, "Sts", endpoint);
        // 添加endpoint。适用于Java SDK 3.12.0以下版本。
        // DefaultProfile.addEndpoint("",regionId, "Sts", endpoint);
        // 构造default profile。
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        // 构造client。
        DefaultAcsClient client = new DefaultAcsClient(profile);
        final AssumeRoleRequest request = new AssumeRoleRequest();
        // 适用于Java SDK 3.12.0及以上版本。
        request.setSysMethod(MethodType.POST);
        // 适用于Java SDK 3.12.0以下版本。
        //request.setMethod(MethodType.POST);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        request.setDurationSeconds(durationSeconds);
        final AssumeRoleResponse response = client.getAcsResponse(request);
        map.put("Expiration",response.getCredentials().getExpiration());
        map.put("AccessKeyId",response.getCredentials().getAccessKeyId());
        map.put("AccessKeySecret",response.getCredentials().getAccessKeySecret());
        map.put("SecurityToken",response.getCredentials().getSecurityToken());
        map.put("RequestId",response.getRequestId());
        return map;
    }

    public boolean findFile(String objectName){
        return ossClient.doesObjectExist(bucketName, objectName);
    }
}
