package com.myzuji.backend.common.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 *
 * @author shine
 * @date 2020/02/24
 */
@Configuration
public class CaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        captchaSetting(properties, "kaptcha.border", "yes", "kaptcha.border.color", "229,230,231", "kaptcha.textproducer.font.color", "blue", "kaptcha.image.width", "160", "kaptcha.image.height", "60");
        // 验证码文本字符大小 默认为40
        captchaSetting(properties, "kaptcha.textproducer.font.size", "30", "kaptcha.session.key", "kaptchaCode", "kaptcha.textproducer.char.space", "3", "kaptcha.textproducer.char.length", "5", "kaptcha.textproducer.font.names", "Arial,Courier");
        // 验证码噪点颜色 默认为Color.BLACK
        properties.setProperty("kaptcha.noise.color", "white");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        captchaSetting(properties, "kaptcha.border", "yes", "kaptcha.border.color", "229,230,231", "kaptcha.textproducer.font.color", "blue", "kaptcha.image.width", "160", "kaptcha.image.height", "60");
        // 验证码文本字符大小 默认为40
        captchaSetting(properties, "kaptcha.textproducer.font.size", "35", "kaptcha.session.key", "kaptchaCodeMath", "kaptcha.textproducer.impl", "com.myzuji.zujibackend.common.config.KaptchaTextCreator", "kaptcha.textproducer.char.space", "3", "kaptcha.textproducer.char.length", "6");
        // 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        // 验证码噪点颜色 默认为Color.BLACK
        properties.setProperty("kaptcha.noise.color", "white");
        // 干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    private void captchaSetting(Properties properties, String s, String yes, String s2, String s3, String s4, String blue, String s5, String s6, String s7, String s8) {
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty(s, yes);
        // 边框颜色 默认为Color.BLACK
        properties.setProperty(s2, s3);
        // 验证码文本字符颜色 默认为Color.BLACK
        properties.setProperty(s4, blue);
        // 验证码图片宽度 默认为200
        properties.setProperty(s5, s6);
        // 验证码图片高度 默认为50
        properties.setProperty(s7, s8);
    }
}
