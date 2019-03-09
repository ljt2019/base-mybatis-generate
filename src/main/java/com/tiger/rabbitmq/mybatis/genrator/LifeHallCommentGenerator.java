//LifeHallCommentGenerator.java
//Create date 2016年8月29日
package com.tiger.rabbitmq.mybatis.genrator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 * LifeHallCommentGenerator
 * 
 * @author leo.lei
 *
 */
public class LifeHallCommentGenerator extends DefaultCommentGenerator {
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		field.addJavaDocLine("/**");
		field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
		addJavadocTag(field, false);
		field.addJavaDocLine(" */");
	}

	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		method.addJavaDocLine("/**");
		method.addJavaDocLine(" * " + introspectedColumn.getRemarks());
		addJavadocTag(method, false);
		method.addJavaDocLine(" */");
	}

	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		method.addJavaDocLine("/**");
		method.addJavaDocLine(" * " + introspectedColumn.getRemarks());
		addJavadocTag(method, false);
		method.addJavaDocLine(" */");
	}
}
