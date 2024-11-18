package com.bili.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.bili.pojo.constant.ModerationConstants;
import com.bili.pojo.entity.MediaFiles;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@Configuration
public class ModerationUtil {

    // 阿里云访问密钥ID
    @Value("${moderation.access-key-id}")
    private String accessKeyId;

    // 阿里云访问密钥Secret
    @Value("${moderation.access-key-secret}")
    private String accessKeySecret;

    // 区域ID
    @Value("${moderation.region-id}")
    private String regionId;

    // 服务端点
    @Value("${moderation.endpoint}")
    private String endpoint;

    // 读取超时时间（毫秒）
    @Value("${moderation.read-timeout}")
    private int readTimeout;

    // 连接超时时间（毫秒）
    @Value("${moderation.connect-timeout}")
    private int connectTimeout;

    // 运行时读取超时时间（毫秒）
    @Value("${moderation.runtime-read-timeout}")
    private int runtimeReadTimeout;

    // 运行时连接超时时间（毫秒）
    @Value("${moderation.runtime-connect-timeout}")
    private int runtimeConnectTimeout;

    private Client client;

    @PostConstruct
    public void init() {
        // 配置客户端
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        config.setRegionId(regionId);
        config.setEndpoint(endpoint);
        config.setReadTimeout(readTimeout);
        config.setConnectTimeout(connectTimeout);
        try {
            client = new Client(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文本审核
     *
     * @param text 文本内容
     * @return
     */
    public Boolean textModerate(String text) {
        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = runtimeReadTimeout;
        runtime.connectTimeout = runtimeConnectTimeout;

        // 检测参数构造
        JSONObject serviceParameters = new JSONObject();
        serviceParameters.put("content", text);

        // 如果内容为空，则打印日志并返回null
        if (serviceParameters.get("content") == null || serviceParameters.getString("content").trim().length() == 0) {
            log.info("文本审核内容为空");
            return true;
        }

        TextModerationRequest textModerationRequest = new TextModerationRequest();
        /*
        文本检测服务：内容安全控制台文本增强版规则配置的serviceCode，示例：chat_detection
        */
        textModerationRequest.setService(ModerationConstants.TEXT_COMMENT_DETECTION);
        textModerationRequest.setServiceParameters(serviceParameters.toJSONString());
        try {
            // 调用方法获取检测结果
            TextModerationResponse response = client.textModerationWithOptions(textModerationRequest, runtime);

            if (response == null) {
                log.error("检测响应为空");
                return false;
            }

            // 获取响应状态码，检查是否成功
            if (response.getStatusCode() != 200) {
                log.error("响应失败，状态码: {}", response.getStatusCode());
                return false;
            }

            // 获取返回的响应体
            TextModerationResponseBody body = response.getBody();
            log.info(JSON.toJSONString(body));
            Integer code = body.getCode();
            if (code != null && code == 200) {
                // 获取
                String reason = body.getData().getReason();
                if (StrUtil.isNotEmpty(reason)) {
                    String riskLevel = (String) JSON.parseObject(reason).get("riskLevel");
                    //high：高风险,medium：中风险,low：低风险
                    if (riskLevel.equals("medium") || riskLevel.equals("low")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 图片审核
     *
     * @param imageUrl 图片url
     * @return
     * @throws Exception
     */
    public Boolean imageModerate(String imageUrl) {

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();

        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        serviceParameters.put("imageUrl", imageUrl);
        //待检测数据唯一标识
        serviceParameters.put("dataId", UUID.randomUUID().toString());

        ImageModerationRequest request = new ImageModerationRequest();
        // 图片检测service：内容安全控制台图片增强版规则配置的serviceCode，示例：baselineCheck
        // 支持service请参考：https://help.aliyun.com/document_detail/467826.html?0#p-23b-o19-gff
        request.setService(ModerationConstants.IMAGE_BASELINE_CHECK);
        request.setServiceParameters(JSON.toJSONString(serviceParameters));

        ImageModerationResponse response = null;
        try {
            response = client.imageModerationWithOptions(request, runtime);

            if (response == null) {
                log.error("检测响应为空");
                return false;
            }

            // 获取响应状态码，检查是否成功
            if (response.getStatusCode() != 200) {
                log.error("响应失败，状态码: {}", response.getStatusCode());
                return false;
            }

            // 获取返回的响应体
            ImageModerationResponseBody body = response.getBody();
            log.info(JSON.toJSONString(body));
            Integer code = body.getCode();
            if (code != null && code == 200) {
                String riskLevel = body.getData().getRiskLevel();
                //high：高风险,medium：中风险,low：低风险
                if (riskLevel.equals("medium") || riskLevel.equals("low") || riskLevel.equals("none")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 提交视频检测任务
     *
     * @param videoUrl 视频url
     * @return
     * @throws Exception
     */
    public String videoModerateFirst(String videoUrl) {

        JSONObject serviceParameters = new JSONObject();
        serviceParameters.put("url", videoUrl);

        VideoModerationRequest videoModerationRequest = new VideoModerationRequest();
        // 检测类型：videoDetection
        videoModerationRequest.setService(ModerationConstants.VIDEO_DETECTION);
        videoModerationRequest.setServiceParameters(serviceParameters.toJSONString());

        try {
            VideoModerationResponse response = client.videoModeration(videoModerationRequest);

            if (response == null) {
                log.error("检测响应为空");
                return null;
            }

            // 获取响应状态码，检查是否成功
            if (response.getStatusCode() != 200) {
                log.error("响应失败，状态码: {}", response.getStatusCode());
                return null;
            }

            // 获取返回的响应体
            VideoModerationResponseBody body = response.getBody();
            log.info(JSON.toJSONString(body));
            Integer code = body.getCode();
            if (code != null && code == 200) {
                String taskId = body.getData().getTaskId();
                //把taskId存入数据库

                //high：高风险,medium：中风险,low：低风险
                return taskId;
            } else {
                return null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取检测状态
     *
     * @param taskId
     * @return
     */
    public String videoModerateSecond(String taskId) {
        JSONObject serviceParameters = new JSONObject();
        // 提交任务时返回的taskId。
        serviceParameters.put("taskId", taskId);


        VideoModerationResultRequest videoModerationResultRequest = new VideoModerationResultRequest();
        // 检测类型：videoDetection
        videoModerationResultRequest.setService(ModerationConstants.VIDEO_DETECTION);
        videoModerationResultRequest.setServiceParameters(serviceParameters.toJSONString());

        try {
            VideoModerationResultResponse response = client.videoModerationResult(videoModerationResultRequest);


            if (response == null) {
                log.error("检测响应为空");
                return null;
            }

            // 获取响应状态码，检查是否成功
            if (response.getStatusCode() != 200) {
                log.error("响应失败，状态码: {}", response.getStatusCode());
                return null;
            }

            // 获取返回的响应体
            VideoModerationResultResponseBody body = response.getBody();
            log.info(JSON.toJSONString(body));
            //PROCESSING，SUCCESS
            return body.getMessage();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
