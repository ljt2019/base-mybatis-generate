//LifeHallPlugin.java
//Create date 2016年8月29日
package com.tiger.rabbitmq.mybatis.genrator;

import com.tiger.rabbitmq.mybatis.base.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.ColumnOverride;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * LifeHallPlugin
 *
 * @author leo.lei
 */
public class LifeHallPlugin extends PluginAdapter {

    private static Log log = LogFactory.getLog(LifeHallPlugin.class);
    private Template serviceTemplate;

    private Template domainPropertiesTemplate;

    private DelUpdateWithoutAlias delUpdateWithoutAlias = new DelUpdateWithoutAlias();

    private static final String BASE_PACKAGE = Constants.BASE_PACKAGE + ".mybatis";
    private static final String BASE_PACKAGE_BASE = BASE_PACKAGE + ".base";
    private static final String BASE_PACKAGE_DAO = BASE_PACKAGE + ".dao";
    private static final String BASE_PACKAGE_DAO_MAPPER = BASE_PACKAGE_DAO + ".mapper";
    private static final String BASE_PACKAGE_QUERY = BASE_PACKAGE + ".query";

    public LifeHallPlugin() {
        Properties p = new Properties();
        // 加载classpath目录下的vm文件
        // 这里是加载模板VM文件，比如：/META-INF/template/Web_B2CPayment.vm（请参考mas_spring_integration.xml）
        p.setProperty("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        // 初始化Velocity引擎，init对引擎VelocityEngine配置了一组默认的参数
        //Velocity.init(p);
        VelocityEngine ve = new VelocityEngine();
        ve.init(p);
        //取得velocity的模版
        serviceTemplate = ve.getTemplate("serviceimpl.vm");

        domainPropertiesTemplate = ve.getTemplate("domainProperties.vm");
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> result = new ArrayList<GeneratedJavaFile>();

        JavaFormatter javaFormatter = introspectedTable.getContext().getJavaFormatter();
        String domainObjName = introspectedTable.getTableConfiguration().getDomainObjectName();
        String modelPackage = introspectedTable.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
        String daoPackage = introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
        String daoProject = introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetProject();
        String a = domainObjName.substring(0, 1).toLowerCase() + domainObjName.substring(1, domainObjName.length());

        Interface iserviceClass = new Interface(
                this.properties.getProperty("servicePackage") + "." + domainObjName + "Service");
        iserviceClass.setVisibility(JavaVisibility.PUBLIC);
        iserviceClass.addSuperInterface(
                new FullyQualifiedJavaType("IBaseService<" + domainObjName + "," + domainObjName + "Example>"));
        iserviceClass.addImportedType(new FullyQualifiedJavaType(BASE_PACKAGE_BASE + ".IBaseService"));
        iserviceClass.addImportedType(new FullyQualifiedJavaType(modelPackage + "." + domainObjName));
        iserviceClass.addImportedType(new FullyQualifiedJavaType(modelPackage + "." + domainObjName + "Example"));
        iserviceClass.addImportedType(new FullyQualifiedJavaType(modelPackage + "." + domainObjName + "Example"));
        iserviceClass.addImportedType(new FullyQualifiedJavaType(BASE_PACKAGE_QUERY + ".Query"));
        iserviceClass.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        iserviceClass.addImportedType(new FullyQualifiedJavaType(BASE_PACKAGE_BASE + ".PageParameter"));


        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType(BASE_PACKAGE_QUERY + ".Query"), "query"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(BASE_PACKAGE_BASE + ".PageParameter"), "pageParameter"));
        method.setName("query");
        method.setReturnType(new FullyQualifiedJavaType("List<" + domainObjName + ">"));
        iserviceClass.addMethod(method);

        GeneratedJavaFile serviceJavaFile = new GeneratedJavaFile(iserviceClass,
                this.properties.getProperty("targetProject"), javaFormatter);
        String iPath = this.properties.getProperty("targetProject") + "\\\\" + this.properties.getProperty("servicePackage").replaceAll("\\.", "\\\\\\\\") + "\\\\" + domainObjName + "Service.java";
        if (!new File(iPath).exists()) {
            result.add(serviceJavaFile);
        } else {
            log.info(iPath + " 已经存在,不再生成!!!");
        }

		/*TopLevelClass serviceImplClass = new TopLevelClass(
                this.properties.getProperty("servicePackage") + ".impl." + domainObjName + "ServiceImpl");
		serviceImplClass.setVisibility(JavaVisibility.PUBLIC);
		serviceImplClass.addSuperInterface(new FullyQualifiedJavaType(
				this.properties.getProperty("servicePackage") + "." + domainObjName + "Service"));
		serviceImplClass.addImportedType(new FullyQualifiedJavaType(
				this.properties.getProperty("servicePackage") + "." + domainObjName + "Service"));
		serviceImplClass.setSuperClass("BaseServiceImpl<" + domainObjName + "," + domainObjName + "Example>");
		serviceImplClass.addImportedType(new FullyQualifiedJavaType("me.healthmall.core.service.impl.BaseServiceImpl"));
		serviceImplClass.addImportedType(new FullyQualifiedJavaType(modelPackage + "." + domainObjName));
		serviceImplClass.addImportedType(new FullyQualifiedJavaType(modelPackage + "." + domainObjName + "Example"));
		serviceImplClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
		serviceImplClass.addImportedType(
				new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
		serviceImplClass.addAnnotation("@Service");
		serviceImplClass.addAnnotation("@Transactional");
		
		Field mapperField = new Field(a + "Mapper", new FullyQualifiedJavaType(domainObjName + "Mapper"));
		mapperField.setVisibility(JavaVisibility.PRIVATE);
		serviceImplClass.addImportedType(new FullyQualifiedJavaType(daoPackage + "." + domainObjName + "Mapper"));
		serviceImplClass.addField(mapperField);*/

		/*Field mapperExtendsField = new Field(a + "ExtpandsMapper",
				new FullyQualifiedJavaType(domainObjName + "ExtpandsMapper"));
		mapperExtendsField.setVisibility(JavaVisibility.PRIVATE);
		serviceImplClass
				.addImportedType(new FullyQualifiedJavaType(this.properties.getProperty("expandsPackage") + "." + domainObjName + "ExtpandsMapper"));
		serviceImplClass.addField(mapperExtendsField);*/

		/*GeneratedJavaFile serviceImplJavaFile = new GeneratedJavaFile(serviceImplClass,
				this.properties.getProperty("targetProject"), javaFormatter);
		result.add(serviceImplJavaFile);*/

		/*Interface extendsMapperClass = new Interface(this.properties.getProperty("expandsPackage") + "." + domainObjName + "ExtpandsMapper");
		extendsMapperClass.setVisibility(JavaVisibility.PUBLIC);
		GeneratedJavaFile extendsMapperJavaFile = new GeneratedJavaFile(extendsMapperClass, daoProject, javaFormatter);
		result.add(extendsMapperJavaFile);*/


        //取得velocity的上下文context
        VelocityContext context = new VelocityContext();
        //把数据填入上下文
        context.put("servicePackage", this.properties.getProperty("servicePackage"));
        context.put("daoPackage", daoPackage);
        context.put("modelPackage", modelPackage);
        context.put("domainName", domainObjName);

        context.put("basePackage", BASE_PACKAGE_BASE);
        context.put("baseQueryPackage", BASE_PACKAGE_QUERY);

        StringWriter writer = new StringWriter();
        serviceTemplate.merge(context, writer);
        String path = this.properties.getProperty("targetProject") + "\\\\" + this.properties.getProperty("servicePackage").replaceAll("\\.", "\\\\\\\\") + "\\\\impl\\\\" + domainObjName + "ServiceImpl.java";
        File serviceImplFile = new File(path);
        if (!serviceImplFile.getParentFile().exists()) {
            new File(serviceImplFile.getParent()).mkdir();
        }

        try {
            if (!serviceImplFile.exists()) {
                serviceImplFile.createNewFile();
                FileWriter fw = new FileWriter(serviceImplFile);
                serviceTemplate.merge(context, fw);
                fw.close();
            } else {
                log.info(serviceImplFile + " 已经存在,不再生成!!!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map<String, String>> propertys = new ArrayList<>();
        List<ColumnOverride> columnOverrides = introspectedTable.getTableConfiguration().getColumnOverrides();
        for (ColumnOverride columnOverride : columnOverrides) {
            Map<String, String> property = new HashMap<>();
            property.put("propertyName", columnOverride.getJavaProperty());
            property.put("column", introspectedTable.getTableConfiguration().getAlias() + "." + columnOverride.getColumnName());
            propertys.add(property);
        }
        context.put("propertys", propertys);

        context.put("baseQueryPackage", BASE_PACKAGE_QUERY);
        path = this.properties.getProperty("targetProject") + "\\\\" + modelPackage.replaceAll("\\.", "\\\\\\\\") + "\\\\" + domainObjName + "_.java";
        File domainPropertiesFile = new File(path);
        if (!domainPropertiesFile.getParentFile().exists()) {
            new File(domainPropertiesFile.getParent()).mkdir();
        }

        try {
            if (!domainPropertiesFile.exists()) {
                domainPropertiesFile.createNewFile();
            }
            FileWriter fw = new FileWriter(domainPropertiesFile);
            domainPropertiesTemplate.merge(context, fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        String domainObjName = introspectedTable.getTableConfiguration().getDomainObjectName();
        interfaze.addSuperInterface(
                new FullyQualifiedJavaType("IBaseMapper<" + domainObjName + ", " + domainObjName + "Example>"));
        interfaze.addImportedType(new FullyQualifiedJavaType(BASE_PACKAGE_BASE + ".IBaseMapper"));
        interfaze.addImportedType(new FullyQualifiedJavaType(BASE_PACKAGE_BASE + ".PageParameter"));
        interfaze.addImportedType(new FullyQualifiedJavaType(BASE_PACKAGE_QUERY + ".Query"));

        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType(BASE_PACKAGE_QUERY + ".Query"), "query", "@Param(\"query\")"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(BASE_PACKAGE_BASE + ".PageParameter"), "pageParameter"));
        method.setName("query");
        method.setReturnType(new FullyQualifiedJavaType("List<" + domainObjName + ">"));
        interfaze.addMethod(method);
        interfaze.addAnnotation("@Repository");
        return true;
    }

	/*public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
		String sqlPackage = introspectedTable.getContext().getSqlMapGeneratorConfiguration().getTargetPackage();
		String sqlProject = introspectedTable.getContext().getSqlMapGeneratorConfiguration().getTargetProject();
		String domainObjName = introspectedTable.getTableConfiguration().getDomainObjectName();

		Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN",
				"http://mybatis.org/dtd/mybatis-3-mapper.dtd");

		XmlElement root = new XmlElement("mapper");
		root.addAttribute(new Attribute("namespace", this.properties.getProperty("expandsPackage") + "." + domainObjName + "ExtpandsMapper"));
		document.setRootElement(root);
		GeneratedXmlFile gxf = new GeneratedXmlFile(document, domainObjName + "ExtpandsMapper.xml",  this.properties.getProperty("expandsPackage")+".mapper",
				sqlProject, false, this.context.getXmlFormatter());

		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
		answer.add(gxf);

		return answer;
	}*/

    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap,
                                   IntrospectedTable introspectedTable) {
        try {
            java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        /*XmlElement selectByPageExampleXmlElement=new XmlElement("select");
        selectByPageExampleXmlElement.addAttribute(new Attribute("id","selectPageByExample"));
        selectByPageExampleXmlElement.addAttribute(new Attribute("resultMap","BaseResultMap"));
        String content="select\n" +
                "    <if test=\"example.distinct\">\n" +
                "      distinct\n" +
                "    </if>\n" +
                "    <include refid=\"Base_Column_List\" />\n" +
                "    from "+introspectedTable.getTableConfiguration().getTableName()+" "+introspectedTable.getTableConfiguration().getAlias()+"\n" +
                "    <if test=\"example != null\">\n" +
                "      <include refid=\"Update_By_Example_Where_Clause\"/>\n" +
                "    </if>\n" +
                "    <if test=\"example.orderByClause != null\">\n" +
                "      order by ${example.orderByClause}\n" +
                "    </if>";
        TextElement contentElement=new TextElement(content);
        selectByPageExampleXmlElement.addElement(contentElement);
        document.getRootElement().addElement(selectByPageExampleXmlElement);*/

        /* <sql id="Example_Where_Clause" >
            <where >
              <foreach collection="lifeHouseExample.oredCriteria" item="criteria" separator="or" >
                <if test="criteria.valid" >
                  <trim prefix="(" suffix=")" prefixOverrides="and" >
                    <foreach collection="criteria.criteria" item="criterion" >
                      <choose >
                        <when test="criterion.noValue" >
                        and ${criterion.condition}
                        </when>
                        <when test="criterion.singleValue" >
                        and ${criterion.condition} #{criterion.value}
                        </when>
                        <when test="criterion.betweenValue" >
                        and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}
                        </when>
                        <when test="criterion.listValue" >
                        and ${criterion.condition}
                          <foreach collection="criterion.value" item="listItem" open="(" close=")" separator="," >
                            #{listItem}
                          </foreach>
                        </when>
                      </choose>
                    </foreach>
                  </trim>
                </if>
              </foreach>
            </where>
          </sql>*/
        String domainName = introspectedTable.getTableConfiguration().getDomainObjectName();
        String firstCharLowerDomainName = domainName.replaceFirst(domainName.substring(0, 1), domainName.substring(0, 1).toLowerCase());
        XmlElement domainExampleWheClauserXmlElement = new XmlElement("sql");
        domainExampleWheClauserXmlElement.addAttribute(new Attribute("id", firstCharLowerDomainName + "ExampleWhereClauser"));
        String domainExampleWheClauserXmlElementContent = "" +
                "<trim suffixOverrides=\"AND\">\n" +
                "            AND" + " <trim prefix=\"(\" suffix=\")\">        " +
                "              <foreach collection=\"" + firstCharLowerDomainName + "Example.oredCriteria\" item=\"criteria\" separator=\"or\" >\n" +
                "                <if test=\"criteria.valid\" >\n" +
                "                  <trim prefix=\"(\" suffix=\")\" prefixOverrides=\"and\" >\n" +
                "                    <foreach collection=\"criteria.criteria\" item=\"criterion\" >\n" +
                "                      <choose >\n" +
                "                        <when test=\"criterion.noValue\" >\n" +
                "                        and ${criterion.condition}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.singleValue\" >\n" +
                "                        and ${criterion.condition} #{criterion.value}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.betweenValue\" >\n" +
                "                        and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.listValue\" >\n" +
                "                        and ${criterion.condition}\n" +
                "                          <foreach collection=\"criterion.value\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >\n" +
                "                            #{listItem}\n" +
                "                          </foreach>\n" +
                "                        </when>\n" +
                "                      </choose>\n" +
                "                    </foreach>\n" +
                "                  </trim>\n" +
                "                </if>\n" +
                "              </foreach>" +
                "             </trim> " +
                "</trim>";

        TextElement domainExampleWheClauserContentElement = new TextElement(domainExampleWheClauserXmlElementContent);
        domainExampleWheClauserXmlElement.addElement(domainExampleWheClauserContentElement);
        document.getRootElement().addElement(domainExampleWheClauserXmlElement);

        XmlElement queryXmlElement = new XmlElement("select");
        queryXmlElement.addAttribute(new Attribute("id", "query"));
        queryXmlElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        String queryXmlElementContent = " select\n" +
                "\n" +
                "    <include refid=\"Base_Column_List\" />\n" +
                "    from " + introspectedTable.getTableConfiguration().getTableName() + " " + introspectedTable.getTableConfiguration().getAlias() + "\n" +
                "    <include refid=\"" + BASE_PACKAGE_DAO_MAPPER + ".QueryGenerateMapper.QueryGenerate\" />";

        TextElement queryContentElement = new TextElement(queryXmlElementContent);
        queryXmlElement.addElement(queryContentElement);
        document.getRootElement().addElement(queryXmlElement);

        //---------------
        XmlElement deleteByExampleWheClauserXmlElement = new XmlElement("sql");
        deleteByExampleWheClauserXmlElement.addAttribute(new Attribute("id", "Delete_Example_Where_Clause"));

        String deleteByExampleWheClauserXmlElementContent = "" +
                "<where >\n" +
                "      <foreach collection=\"oredCriteria\" item=\"criteria\" separator=\"or\" >\n" +
                "        <if test=\"criteria.valid\" >\n" +
                "          <trim prefix=\"(\" suffix=\")\" prefixOverrides=\"and\" >\n" +
                "            <foreach collection=\"criteria.criteria\" item=\"criterion\" >\n" +
                "              <choose >\n" +
                "                <when test=\"criterion.noValue\" >\n" +
                "                  and <trim prefixOverrides=\"" + introspectedTable.getTableConfiguration().getAlias() + ".\">${criterion.condition}</trim>\n" +
                "                </when>\n" +
                "                <when test=\"criterion.singleValue\" >\n" +
                "                  and <trim prefixOverrides=\"" + introspectedTable.getTableConfiguration().getAlias() + ".\">${criterion.condition}</trim> #{criterion.value}\n" +
                "                </when>\n" +
                "                <when test=\"criterion.betweenValue\" >\n" +
                "                  and <trim prefixOverrides=\"" + introspectedTable.getTableConfiguration().getAlias() + ".\">${criterion.condition}</trim> #{criterion.value} and #{criterion.secondValue}\n" +
                "                </when>\n" +
                "                <when test=\"criterion.listValue\" >\n" +
                "                  and <trim prefixOverrides=\"" + introspectedTable.getTableConfiguration().getAlias() + ".\">${criterion.condition}</trim>\n" +
                "                  <foreach collection=\"criterion.value\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >\n" +
                "                    #{listItem}\n" +
                "                  </foreach>\n" +
                "                </when>\n" +
                "              </choose>\n" +
                "            </foreach>\n" +
                "          </trim>\n" +
                "        </if>\n" +
                "      </foreach>\n" +
                "</where>";

        TextElement deleteByExampleWheClauserContentElement = new TextElement(deleteByExampleWheClauserXmlElementContent);
        deleteByExampleWheClauserXmlElement.addElement(deleteByExampleWheClauserContentElement);
        document.getRootElement().addElement(deleteByExampleWheClauserXmlElement);
        //----

        return delUpdateWithoutAlias.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return super.sqlMapExampleWhereClauseElementGenerated(element, introspectedTable);
    }
    /*
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        element.getAttributes().remove(2);
        return true;
    }*/

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return delUpdateWithoutAlias.sqlMapDeleteByExampleElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return delUpdateWithoutAlias.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return delUpdateWithoutAlias.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }
}
