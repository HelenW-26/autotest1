package newcrm.utils.api;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import newcrm.global.GlobalProperties;
import newcrm.testcases.others.XSourceCoderTest;
import newcrm.testcases.others.XSourceCoderTest.EnvPlatform;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.Assert;

import java.util.Objects;
import java.util.Optional;

public class XSourceCoder {

    public static final String CT_TAG = "ct";
    public static final String BRAND_TAG = "b";
    public static final String SCOPE_TAG = "scope";
    public static final String SCOPE_TAG_VALUE = "debug";
    private final ISourceConfig sourceConfig;

    public XSourceCoder(ISourceConfig sourceConfig) {
        this.sourceConfig = sourceConfig;
    }

    public String create(String issuer, EnvPlatform envPlatform, String brand) {
        String jwt = "";

        try {
            jwt = create(issuer, envPlatform, brand, false);
//            System.out.println("Generated " + issuer + " JWT: " + jwt);
        } catch (Exception e) {
            Assert.fail("Failed to generate " + issuer + " JWT");
        }

        return jwt;
    }

    public String create(String issuer, EnvPlatform envPlatform, String brand, boolean isDebug) throws Exception {
        String secret = sourceConfig.getSecret(envPlatform);
        if (Objects.isNull(secret)) {
            return null;
        }

        JWTClaimsSet.Builder claimBuilder = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .claim(CT_TAG, System.currentTimeMillis())
                .claim(BRAND_TAG, brand);

        if (isDebug) {
            claimBuilder.claim(SCOPE_TAG, SCOPE_TAG_VALUE);
        }

        JWTClaimsSet claimsSet = claimBuilder.build();
        JWSSigner signer = new MACSigner(secret);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

//    public JWTClaimsSet verify(String jwt) {
//        return verify(null, jwt, true);
//    }
//
//    public JWTClaimsSet verify(String systemType, String jwt, boolean isMACVerify) {
//        if (StringUtils.isBlank(jwt)) {
//            Assert.fail("JWT not found");
//        }
//
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(jwt);
//
//            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
//            if (Objects.isNull(jwtClaimsSet)) {
//                Assert.fail("JWT not found");
//            }
//
//            String issuer = jwtClaimsSet.getIssuer();
//            if (!sourceConfig.enabled(issuer)) {
//                return jwtClaimsSet;
//            }
//
//            if (isMACVerify) {
//                String platform = getPlatform(jwtClaimsSet);
//                String brand = getBrand(jwtClaimsSet);
//                String secret = sourceConfig.getSecret(platform, brand, systemType);
//                if (Objects.isNull(secret)) {
//                    Assert.fail("JWT secret key not found");
//                }
//                JWSVerifier verifier = new MACVerifier(secret);
//                boolean verify = signedJWT.verify(verifier);
//                if (!verify) {
//                    Assert.fail("Failed to verify JWT");
//                }
//            }
//
//            if (isDebugXSource(jwtClaimsSet)) {
//                return jwtClaimsSet;
//            }
//
//            Long createdAt = getCreatedAt(jwtClaimsSet.getClaim(CT_TAG));
//            if (isExpired(issuer, createdAt)) {
//                Assert.fail("JWT expired");
//            }
//
//            return jwtClaimsSet;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("x-source is invalid! " + e);
//        }
//
//        return null;
//    }

    private boolean isDebugXSource(JWTClaimsSet jwtClaimsSet) {
        if (GlobalProperties.ENV.getENV(GlobalProperties.env) == GlobalProperties.ENV.PROD || !sourceConfig.debugModeEnabled()) {
            return false;
        }

        Object scope = jwtClaimsSet.getClaim(SCOPE_TAG);

        return SCOPE_TAG_VALUE.equals(String.valueOf(scope));
    }

    public String getBrand(JWTClaimsSet jwtClaimsSet) {
        return Optional.ofNullable(jwtClaimsSet)
                .map(claim -> claim.getClaim(BRAND_TAG))
                .map(Objects::toString).orElse(null);
    }

    public String getPlatform(JWTClaimsSet jwtClaimsSet) {
        return Optional.ofNullable(jwtClaimsSet)
                .map(JWTClaimsSet::getIssuer)
                .orElse(null);
    }

    protected Long getCreatedAt(Object createdAtObj) {
        if (createdAtObj instanceof String ct && NumberUtils.isParsable(ct)) {
            return NumberUtils.toLong(ct);
        }
        if (createdAtObj instanceof Number ct) {
            return ct.longValue();
        }

        Assert.fail("createdAt is not number, ct[%s]" + createdAtObj);

        return null;
    }

    private boolean isExpired(String platform, Long createdAt) {
        if (Objects.isNull(createdAt)) {
            return true;
        }
        long dif = System.currentTimeMillis() - createdAt;
        return dif > sourceConfig.expireMillis(platform);
    }

    public static interface ISourceConfig {

        String getSecret(EnvPlatform envPlatform);

        String getSecret(EnvPlatform envPlatform, String brand, String systemType);

        long expireMillis(String platform);

        boolean enabled(String platform);

        boolean debugModeEnabled();
    }

}
