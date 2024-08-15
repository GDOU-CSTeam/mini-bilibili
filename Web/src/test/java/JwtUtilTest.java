import com.bili.common.utils.JwtUtil;
import com.bili.web.WebApplication;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JwtUtilTest {

    @Resource
    JwtUtil jwtUtil;

    @Test
    public void testJwtPa() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzaWduVGltZSI6IjIwMjQtMDgtMDQgMDg6NDc6MzIiLCJleHAiOjE3MjI3MzMwNTd9.L55DYj-X8vVwNKwYCnHbkZWPuva0c0J9NTwk9UBFqmo";
        Claims claims = jwtUtil.parseJWT(jwt);
        System.out.println(claims); //claims = {jti=3ad1f1464ba5478d959a42c4b6d71173, sub=1, iss=bili, iat=1721488693, exp=1721575093}
        // 获取签名时间
        // 直接转换为long类型
        Integer iat = (Integer) claims.get("iat");
        System.out.println(iat);
        //Object iat = claims.get("iat");
        //System.out.println(iat.toString());
        // 直接获取Date类型
        //Object iat = claims.get("iat");
        //System.out.println(iat);

        //Date iatDate = claims.get("iat", Date.class);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String iatStr = sdf.format(iatDate);
        //System.out.println("iat: " + iatStr);
    }



}
