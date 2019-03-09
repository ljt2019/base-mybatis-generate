package com.tiger.rabbitmq.mybatis.genrator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * LifeHallGenerator
 *
 * @author leo.lei
 *
 */
public class LifeHallGenerator {
//	[\u4e00-\u9fa5]
	public static void main(String[] args) throws Exception {
		
		String srcPath = "C:\\AAAAA\\all-code\\base-mybatis-generate\\src\\main\\resources\\base-mybatis-generator\\generatorConfig-log.xml";
		InputStream configStream = new FileInputStream(new File(srcPath));
		
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
//		InputStream configStream = LifeHallGenerator.class.getClassLoader()
//		.getResourceAsStream("mobile-mobilemap/generatorConfig.xml");
		
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configStream);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		
	}
	
}