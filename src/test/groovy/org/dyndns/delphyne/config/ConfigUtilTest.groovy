package org.dyndns.delphyne.config

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

class ConfigUtilTest {
	ConfigUtil util = new ConfigUtil()
	
	@Test
	void testProperties() {
		Properties props = new Properties()
		props.put("longVal", 1)
		props.put("stringVal", "Hello, World")
		props.put("listVal", [1,2,3])
		
		[util.getConfigObject(props), util.getConfigBean(ExampleConfigBean, props)].each {
			assert 1 == it.longVal
			assert "Hello, World" == it.stringVal
			assert [1,2,3] == it.listVal
		}
	}
	
	@Test
	void testString() {
		String config = """\
		longVal=Long.MAX_VALUE
		stringVal=/foo,bar,baz/
		listVal=['humpty','dumpty']
		""".stripIndent()
		[util.getConfigObject(config), util.getConfigBean(ExampleConfigBean, config)].each {
			assert Long.MAX_VALUE == it.longVal
			assert "foo,bar,baz" == it.stringVal
			assert ['humpty', 'dumpty'] == it.listVal
		}

	}
	
	@Test 
	void testClass() {
		[util.getConfigObject(ConfigScript), util.getConfigBean(ExampleConfigBean, ConfigScript)].each {
			assert 42 == it.longVal
			assert 'So long' == it.stringVal
			assert ['singleton'] == it.listVal
		}	
	}
	
	@Test
	void testScript() {
		Script script = ConfigScript.newInstance()
		[util.getConfigObject(script), util.getConfigBean(ExampleConfigBean, script)].each {
			assert 42 == it.longVal
			assert 'So long' == it.stringVal
			assert ['singleton'] == it.listVal
		}
	}
	
	@Test 
	void testUrl() {
		URL url = Thread.currentThread().contextClassLoader.getResource('test-config.groovy')
		[util.getConfigObject(url), util.getConfigBean(ExampleConfigBean, url)].each {
			assert 99 == it.longVal
			assert 'electric octopus' == it.stringVal
			assert [99, 'electric octopus'] == it.listVal
		}
	}
	
	@Test
	void testResource() {
		Resource resource = new ClassPathResource("test-config.groovy")
		[util.getConfigObject(resource), util.getConfigBean(ExampleConfigBean, resource)].each {
			assert 99 == it.longVal
			assert 'electric octopus' == it.stringVal
			assert [99, 'electric octopus'] == it.listVal
		}
	}
}

class ExampleConfigBean {
	long longVal
	String stringVal
	List listVal
}

