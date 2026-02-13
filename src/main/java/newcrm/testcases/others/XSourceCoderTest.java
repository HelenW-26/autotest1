package newcrm.testcases.others;

import newcrm.global.GlobalProperties;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.collections4.KeyValue;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import newcrm.utils.api.XSourceCoder;

class JwtXSource {
    String jwtPlatform;
    String secretKey;

    JwtXSource(String jwtPlatform, String secretKey) {
        this.jwtPlatform = jwtPlatform;
        this.secretKey = secretKey;
    }
}

public class XSourceCoderTest {

    public enum EnvPlatform {
        ALPHA_APP,
        ALPHA_PC,
        PROD_APP,
        PROD_PC
    }

    public enum JWTPLATFORM {
        PC("pc"),
        APP("app");

        private final String value;

        JWTPLATFORM(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final String APP_SECRET = "146584f90d574e5bb8aaf951ad1a8b61724121cc7a624a9a82a839c174903c09";
    public static final String PC_SECRET = "b61724121cc7a624a9a82a839c174903c146584f90d574e5bb8aaf951ad1a809";

    public static final String APP_PROD_SECRET = "503905cf15f041d68dcca2c3d0775791c933a9df72aa4be0883f6f68d3a08a46";
    public static final String PC_PROD_SECRET = "fc54d1d58189433abcba561f69d2063fb6d097d3275c4a1bb385d387c5ee9c20";

    Map<String, String> platformSecret2 = Map.of(JWTPLATFORM.APP.getValue(), APP_SECRET, JWTPLATFORM.PC.getValue(), PC_SECRET);

    Map<EnvPlatform, JwtXSource> platformSecret = Map.of(
            EnvPlatform.ALPHA_APP, new JwtXSource(JWTPLATFORM.APP.getValue(), APP_SECRET),
            EnvPlatform.ALPHA_PC, new JwtXSource(JWTPLATFORM.PC.getValue(), PC_SECRET),
            EnvPlatform.PROD_APP, new JwtXSource(JWTPLATFORM.PC.getValue(), APP_PROD_SECRET),
            EnvPlatform.PROD_PC, new JwtXSource(JWTPLATFORM.PC.getValue(), PC_PROD_SECRET)
    );

    public XSourceCoder xSourceCoder = new XSourceCoder(new XSourceCoder.ISourceConfig() {
        @Override
        public String getSecret(EnvPlatform platform) {
            return platformSecret.get(platform).secretKey;
        }

        @Override
        public String getSecret(EnvPlatform platform, String brand, String systemType) {
            return platformSecret.get(platform).secretKey;
        }

        @Override
        public long expireMillis(String platform) {
            return TimeUnit.SECONDS.toMillis(5);
        }

        @Override
        public boolean enabled(String platform) {
            return true;
        }

        @Override
        public boolean debugModeEnabled() {
            return true;
        }
    });

    @Test
    public void create4PC_Alpha(String brandValue) {
        String jwt = xSourceCoder.create(JWTPLATFORM.PC.getValue(), EnvPlatform.ALPHA_PC, brandValue);
    }

    @Test
    public void create4APP_Alpha(String brandValue) throws Exception {
        String jwt = xSourceCoder.create(JWTPLATFORM.APP.getValue(), EnvPlatform.ALPHA_APP, brandValue);
    }

//    @Test
//    void verify4Pc() throws Exception {
//        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcHAiLCJjdCI6MTc1MDM4NjE0MjE5OCwiYiI6IkFVIn0.3P2cyK_lRG2DygQfo_KPqsjYQSnObv8MVM-ezSBtKN8";
//        log.info(jwt);
//        XSourceCoderException xSourceCoderException = Assertions.assertThrows(XSourceCoderException.class, () -> {
//            JWTClaimsSet verified = xSourceCoder.verify(jwt);
//            log.info(verified.toString());
//        });
//        String errorDetail = xSourceCoderException.getErrorDetail();
//        log.info(errorDetail);
//        Assertions.assertTrue(errorDetail.contains("expired"));
//    }
//
//    @Test
//    void verify4App() throws Exception {
//        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhcHAiLCJjdCI6MTc1MDM4NjQ3MjU0NywiYiI6IkFVIn0.LhcZT4P2_7VE_VCGO9YfDfg-S0qBXQF_F3pWG8c1elg";
//        log.info(jwt);
//        XSourceCoderException xSourceCoderException = Assertions.assertThrows(XSourceCoderException.class, () -> {
//            JWTClaimsSet verified = xSourceCoder.verify(jwt);
//            log.info(verified.toString());
//        });
//        String errorDetail = xSourceCoderException.getErrorDetail();
//        log.info(errorDetail);
//        Assertions.assertTrue(errorDetail.contains("expired"));
//    }
//
//    @Test
//    void createAndVerify() throws Exception {
//        String jwt = xSourceCoder.create(JWT_PLATFORM_PC_VALUE, brandValue);
//        log.info(jwt);
//        JWTClaimsSet verified = xSourceCoder.verify(jwt);
//        log.info(verified.toString());
//    }
//
//    @Test
//    void createSecret() {
//        String secret = UUID.randomUUID().toString() + UUID.randomUUID();
//        log.info(secret.replace("-", ""));
//    }
//
//    @Test
//    void getCreatedAt() {
//        Long createdAt = xSourceCoder.getCreatedAt("123");
//        log.info(String.valueOf(createdAt));
//        createdAt = xSourceCoder.getCreatedAt(1);
//        log.info(String.valueOf(createdAt));
//        createdAt = xSourceCoder.getCreatedAt("123a");
//        log.info(String.valueOf(createdAt));
//    }
//
//    @Test
//    void verify4Loop() throws Exception {
//        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTE0LCJiIjoiQVUiLCJzY29wZSI6ImRlYnVnIn0.ifeBZo9cqbwWFDqNt8VjL-DYTIv6Df65kydj2evHdKw";
//        long start = System.currentTimeMillis();
//        log.info("start:{}", start);
//        for (int i = 0; i < 1000000; i++) {
//            xSourceCoder.verify(jwt);
//        }
//        long end = System.currentTimeMillis();
//        long elapsed = end - start;
//        log.info("end:{},elapsed:{}", end, elapsed);
//    }

}
