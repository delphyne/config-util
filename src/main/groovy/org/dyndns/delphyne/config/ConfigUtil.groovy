package org.dyndns.delphyne.config

import groovy.util.logging.Slf4j

import java.util.concurrent.Callable

import org.springframework.core.io.Resource

/**
 * A Utility class to parse groovy config files into a Bean
 */
@Slf4j
class ConfigUtil {
	/**
	 * The environment to parse out of the special "environments" closure within the config file
	 */
	String environment

	ConfigUtil(String environment = "unspecified") {
		this.environment = environment
	}

	ConfigObject getConfigObject(Properties configProperties) {
		parse(configProperties)
	}

	ConfigObject getConfigObject(String configText) {
		parse(configText)
	}

	ConfigObject getConfigObject(Class<? extends Script> configClass) {
		parse(configClass)
	}

	ConfigObject getConfigObject(Script configScript) {
		parse(configScript)
	}

	ConfigObject getConfigObject(URL configUrl) {
		parse(configUrl)
	}

	ConfigObject getConfigObject(Resource resource) {
		parse(resource.inputStream.text)
	}

	private ConfigObject parse(def configSource) {
		def co = new ConfigSlurper(environment).parse(configSource)
		logParsedConfig(co)
		return co
	}

	private void logParsedConfig(ConfigObject co) {
		log.debug "Parsed (env: $environment): ${co.toString().replaceAll(~/(, |)(.?+[pP](?:assword|asswd)):.+?(, |])/ , '$1$2:********$3')}"
	}

	public <T> T getConfigBean(Class<T> type, Properties configProperties) {
		type.newInstance(getConfigObject(configProperties))
	}

	public <T> T getConfigBean(Class<T> type, String configText) {
		type.newInstance(getConfigObject(configText))
	}

	public <T> T getConfigBean(Class<T> type, Class<? extends Script> configClass) {
		type.newInstance(getConfigObject(configClass))
	}

	public <T> T getConfigBean(Class<T> type, Script configScript) {
		type.newInstance(getConfigObject(configScript))
	}

	public <T> T getConfigBean(Class<T> type, URL configUrl) {
		type.newInstance(getConfigObject(configUrl))
	}

	public <T> T getConfigBean(Class<T> type, Resource resource) {
		type.newInstance(getConfigObject(resource))
	}
}
