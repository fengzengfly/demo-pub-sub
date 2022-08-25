package com.xiaofengstu.demopubsub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName RedisConfig
 * @Author fengzeng
 * @Date 2022/8/23 0023 16:34
 */
@Configuration
public class RedisConfig {
  /**
   * 这里我写在配置文件里了，可以自定义，只要和发布消息时定义的channel保持一致就可以
   */
  @Value("${spring.redis.topic}")
  private String topic;

  @Bean
  public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory, RedisMessageListener messageListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(factory);
    container.addMessageListener(messageListener, new ChannelTopic(topic));
    return container;
  }

}

/**
 * 消息监听类代码
 */
@Component
class RedisMessageListener implements MessageListener {

  @Resource
  private RedisTemplate redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    // 获取消息
    byte[] messageBody = message.getBody();
    // 使用值序列化器转换
    Object msg = redisTemplate.getValueSerializer().deserialize(messageBody);
    // 获取监听的频道
    byte[] channelByte = message.getChannel();
    // 使用字符串序列化器转换
    Object channel = redisTemplate.getStringSerializer().deserialize(channelByte);
    // 渠道名称转换
    String patternStr = new String(pattern);
    System.out.println(patternStr);
    System.out.println("---频道---: " + channel);
    System.out.println("---消息内容---: " + msg);
  }
}
