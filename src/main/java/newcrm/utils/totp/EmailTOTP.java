package newcrm.utils.totp;

import org.testng.Assert;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.apache.commons.codec.binary.Base32;
import utils.LogUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailTOTP {

    private final String secret;
    private final TimeBasedOneTimePasswordGenerator totp;

    public EmailTOTP(String url) {
        this.secret = extractSecretFromOtpAuthUrl(url);
        this.totp = new TimeBasedOneTimePasswordGenerator();
    }

    public static String extractSecretFromOtpAuthUrl(String url) {
        // Extracts secret key
        Pattern pattern = Pattern.compile("secret=([A-Z2-7]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    public String generateTOTP() throws Exception {

        // Check empty on totp secret key
        if (secret == null) {
            LogUtils.info("TOTP secret key not found");
            Assert.fail("Fail to generate TOTP");
        }

        // Decode Base32 Secret
        Base32 base32 = new Base32();
        byte[] keyBytes = base32.decode(secret);
//        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
        SecretKey key = new SecretKeySpec(keyBytes, totp.getAlgorithm());
        LogUtils.info("TOTP Algorithm: " + totp.getAlgorithm());

        // Avoid Boundary Issue (Invalid verification code or Verification expired, please try again)
        long current = Instant.now().getEpochSecond();
        long step = totp.getTimeStep().getSeconds();
        long remaining = step - (current % step);

        // If < 3 seconds left before code refresh → wait for next cycle
        if (remaining < 3) {
            LogUtils.info(String.format("%s seconds remain until the next TOTP cycle. Wait for the next cycle...", remaining));
            Thread.sleep(remaining * 1000);
        } else {
            LogUtils.info(String.format("%s seconds remain until the next TOTP cycle", remaining));
        }

        // Generate OTP
        int otp = totp.generateOneTimePassword(key, Instant.now());
        LogUtils.info("TOTP generated: " + otp);

        return String.format("%06d", otp);
    }

}
