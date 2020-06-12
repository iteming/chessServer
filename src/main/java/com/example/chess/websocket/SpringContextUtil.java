package com.example.chess.websocket;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring Context 工具类(获取Spring上下文)
 *
 * @author 江斌
 * @date 2019.3.12
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringContextUtil.applicationContext == null) {
			SpringContextUtil.applicationContext = applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		return applicationContext.getType(name);
	}


	/**
	 * 通过Bean名字获取Bean
	 *
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

	/**
	 * 通过Bean类型获取Bean
	 *
	 * @param beanClass
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> beanClass) {
		return getApplicationContext().getBean(beanClass);
	}

	/**
	 * 通过Bean名字和Bean类型获取Bean
	 *
	 * @param beanName
	 * @param beanClass
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(String beanName, Class<T> beanClass) {
		return getApplicationContext().getBean(beanName, beanClass);
	}

	/**
	 * 获取或配置 对象
	 * @return
	 */

	public static Environment getEnvironment() {
		return SpringContextUtil.getBean("environment", Environment.class);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return getEnvironment().getProperty(key);
	}

	public static <T> T getProperty(String key, Class<T> destinationClass) {
		return getEnvironment().getProperty(key, destinationClass);
	}
}
