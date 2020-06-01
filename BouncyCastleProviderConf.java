package com.jingantech.ngiam.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.security.Provider;
import java.security.Security;

/**
 * 添加BouncyCastleProvider
 * <p>
 * 每此新建导致内存增加, GC不回收
 *
 * @see javax.crypto.JceSecurity#verifyingProviders
 * </p>
 * Created by Lynn·Rowe on 2018/5/28.
 */
@Component
public class BouncyCastleProviderConf implements InitializingBean {

    private static final Logger logger = LogManager.getLogger(BouncyCastleProviderConf.class);

    private static final String BC_PROVIDER_FQCN = "org.bouncycastle.jce.provider.BouncyCastleProvider";

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            Class<Provider> bcProvider = (Class<Provider>) Class.forName(BC_PROVIDER_FQCN);

            for (Provider provider : Security.getProviders()) {
                if (bcProvider.isInstance(provider)) {
                    logger.debug("The BC provider has bean loaded.");
                    return;
                }
            }

            Security.addProvider(bcProvider.newInstance());
            logger.info("Load the BC provider.");
        } catch (Exception e) {
            // Not available...
            logger.error("Error loading BC provider.");
        }
    }
}
