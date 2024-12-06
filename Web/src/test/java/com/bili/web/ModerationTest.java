package com.bili.web;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.green20220302.models.ImageModerationResponseBody;
import com.aliyun.green20220302.models.TextModerationResponseBody;
import com.aliyun.green20220302.models.VideoModerationResponseBody;
import com.aliyun.green20220302.models.VideoModerationResultResponseBody;
import com.bili.common.utils.ModerationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ModerationTest {

    @Autowired
    private ModerationUtil moderationUtil;

    @Test
    public void testTextModeration() throws Exception {
        Boolean aBoolean = moderationUtil.textModerate("riskLevel：风险等级，根据系统推荐的风险等级返回，返回值包括：high：高风险medium：中风险 low：低风险");
        System.out.println(aBoolean);
    }

    @Test
    public void testImageModeration() throws Exception {
        // Arrange
        String imageUrl = "http://159.75.174.133:9000/mediafiles/2024/11/16/41687f14f22af50f8401b4dade57e0c6.png";
        Boolean responseBody = moderationUtil.imageModerate(imageUrl);
        System.out.println(responseBody);
    }

    @Test
    public void testVideoModeration() throws Exception {
        // Arrange
        String videoUrl = "http://159.75.174.133:9000/mediafiles/a/4/a44a72aa1aab0395ae9d404035f224cd/a44a72aa1aab0395ae9d404035f224cd.mp4";
        String taskId = "vi_f_OdzkdaS7xU93s9ngYRTvu1-1APxfM";
        if (taskId != null) {
            for (int i = 0; i < 20; i++) {
                //PROCESSING，SUCCESS
                String secondBody = moderationUtil.videoModerateSecond(taskId);
                if(StrUtil.isNotEmpty(secondBody) && secondBody.equals("SUCCESS")){
                    break;
                }
                System.out.println(secondBody);
                System.out.println("------------------------------------");
                Thread.sleep(2000);
            }
        }else {
            throw new RuntimeException("验证失败");
        }
    }

    @Test
    public void testUri(){

        String totalUri = UriComponentsBuilder.fromHttpUrl("http://159.75.174.133:9000/")
                .pathSegment("url")
                .toUriString();
        System.out.println(totalUri);

    }
}