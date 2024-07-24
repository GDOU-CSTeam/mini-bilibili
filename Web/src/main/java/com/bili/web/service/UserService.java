package com.bili.web.service;

import com.bili.common.utils.Result;
import com.bili.pojo.dto.user.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Result signIn(Long userId);

    Result getUserInfo(Long userId);

    Result usernameLogin(UserNameLoginParam userNameLoginParam);

    Result emailLogin(EmailLoginParam emailLoginParam);

    Result sign(SignParam signParam);

    Result getCode(String email) throws JsonProcessingException, InterruptedException;

    Result changePassword(ChangePasswordParam changePasswordParam);

    Result forgetPassword(ForgetPassWordParam forgetPassWordParam);

    Result quit(Long userId);

    Result refreshToken(Long userId);

    Result updateNickname(String nickname, Long userId);

    Result updateAvatarImg(String avatarImgSrc, Long userId);

    Result getUploadAvatarImgSTS(String fileSuffix);
}
