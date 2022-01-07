package com.fresh.config;

import com.fresh.utils.BloomFilterHelper;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
	
	@Value("${spring.redis.host}")
	private String addressUrl;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.port}")
	private String port;
	
	@Bean
	public RedissonClient getRedisson() throws Exception{
		RedissonClient redisson = null;
		Config config = new Config();
		String redisPort = StringUtils.isBlank(port) ? "6379" : port;
		config.useSingleServer()
			  .setAddress("//" + addressUrl + ":" + redisPort).setPassword(password);
		redisson = Redisson.create(config);
		
		System.out.println(redisson.getConfig().toJSON().toString());
		return redisson;
	}
	//初始化布隆过滤器，放入到spring容器里面
	@Bean
	public BloomFilterHelper<String> initBloomFilterHelper() {
		return new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.01);
	}

}