package social.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        NativeMessageHeaderAccessor nativeAccessor =
                MessageHeaderAccessor.getAccessor(message, NativeMessageHeaderAccessor.class);


        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            Authentication user = authUser(nativeAccessor.getNativeHeader("Token").get(0)); // access authentication header(s)
            accessor.setUser(user);
        }
        return message;
    }

    private Authentication authUser(String token) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jwtConfig.getPublicKeyPath())) {    // exceptions might be thrown in creating the claims if for example the token is expired

            // 4. Validate the token

            String publicKeyString = IOUtils.toString(inputStream, "UTF-8");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            PublicKey publicKey = kf.generatePublic(X509publicKey);


            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if (username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 6. Authenticate the user
                // Now, user is authenticated
                return auth;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
