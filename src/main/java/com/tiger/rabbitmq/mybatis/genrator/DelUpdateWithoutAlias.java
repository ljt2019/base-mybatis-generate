package com.tiger.rabbitmq.mybatis.genrator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.*;

import java.util.List;

/**
 * Created by leiwm on 2017/3/30.
 */
public class DelUpdateWithoutAlias {

    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        XmlElement xmlElement=document.getRootElement();
        List<Element> childElements=xmlElement.getElements();
        for (Element child:childElements) {
            XmlElement childElement=(XmlElement)child;
            if(childElement.getName().equalsIgnoreCase("sql")&&childElement.getAttributes().get(0).getValue().equals("Update_By_Example_Where_Clause")){
                childElement.getElements().clear();
                String updateByExampleWheClauserXmlElementContent="" +
                        "<where >\n" +
                        "      <foreach collection=\"example.oredCriteria\" item=\"criteria\" separator=\"or\" >\n" +
                        "        <if test=\"criteria.valid\" >\n" +
                        "          <trim prefix=\"(\" suffix=\")\" prefixOverrides=\"and\" >\n" +
                        "            <foreach collection=\"criteria.criteria\" item=\"criterion\" >\n" +
                        "              <choose >\n" +
                        "                <when test=\"criterion.noValue\" >\n" +
                        "                  and <trim prefixOverrides=\""+introspectedTable.getTableConfiguration().getAlias()+".\">${criterion.condition}</trim>\n" +
                        "                </when>\n" +
                        "                <when test=\"criterion.singleValue\" >\n" +
                        "                  and <trim prefixOverrides=\""+introspectedTable.getTableConfiguration().getAlias()+".\">${criterion.condition}</trim> #{criterion.value}\n" +
                        "                </when>\n" +
                        "                <when test=\"criterion.betweenValue\" >\n" +
                        "                  and <trim prefixOverrides=\""+introspectedTable.getTableConfiguration().getAlias()+".\">${criterion.condition}</trim> #{criterion.value} and #{criterion.secondValue}\n" +
                        "                </when>\n" +
                        "                <when test=\"criterion.listValue\" >\n" +
                        "                  and <trim prefixOverrides=\""+introspectedTable.getTableConfiguration().getAlias()+".\">${criterion.condition}</trim>\n" +
                        "                  <foreach collection=\"criterion.value\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >\n" +
                        "                    #{listItem}\n" +
                        "                  </foreach>\n" +
                        "                </when>\n" +
                        "              </choose>\n" +
                        "            </foreach>\n" +
                        "          </trim>\n" +
                        "        </if>\n" +
                        "      </foreach>\n" +
                        "    </where>";
                TextElement updateByExampleWheClauserContentElement=new TextElement(updateByExampleWheClauserXmlElementContent);
                childElement.addElement(updateByExampleWheClauserContentElement);
            }
        }
        return true;
    }

    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
                                                         IntrospectedTable introspectedTable) {
        List<Element> childElements=getChildElements(element);
        setAttributeValue(((XmlElement)getChildElements(((XmlElement)childElements.get(5))).get(0)).getAttributes().get(0),"Delete_Example_Where_Clause");
        ((XmlElement)childElements.get(5)).getAttributes().get(0);
        setContent((TextElement) childElements.get(4),"delete from "+introspectedTable.getTableConfiguration().getTableName());
        return true;
    }

    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> childElements=getChildElements(element);
        setContent((TextElement) childElements.get(4),"update "+introspectedTable.getTableConfiguration().getTableName());
        List<Element> setElements=getChildElements((XmlElement) childElements.get(5));
        setElements.clear();
        List<IntrospectedColumn> introspectedColumns=introspectedTable.getAllColumns();

        for(IntrospectedColumn introspectedColumn:introspectedColumns){
            XmlElement xmlElement=new XmlElement("if");
            xmlElement.addAttribute(new Attribute("test","record."+introspectedColumn.getJavaProperty()+" != null"));
            TextElement textElement=new TextElement(introspectedColumn.getActualColumnName()+" = #{record."+introspectedColumn.getJavaProperty()+",jdbcType="+introspectedColumn.getJdbcTypeName()+"},");
            xmlElement.addElement(textElement);
            setElements.add(xmlElement);
        }
        return true;
    }


    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> childElements=getChildElements(element);
        for(Element child:childElements){
            if(child instanceof TextElement){
                TextElement textElement=(TextElement)child;
                String content=textElement.getContent();
                if(content.indexOf((introspectedTable.getTableConfiguration().getAlias()+"."))>0){
                    content=content.replace((introspectedTable.getTableConfiguration().getAlias()+"."),"");
                }else{
                    content=content.replace(introspectedTable.getTableConfiguration().getAlias(),"");
                }
                setContent(textElement,content);
            }
        }
        return true;
    }

    private List<Element>  getChildElements(XmlElement element){
        try {
            java.lang.reflect.Field field = element.getClass().getDeclaredField("elements");
            field.setAccessible(true);
            List<Element> elements=(List<Element>) field.get(element);
            return elements;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private  void setContent(TextElement element,String content){
        try {
            java.lang.reflect.Field field = element.getClass().getDeclaredField("content");
            field.setAccessible(true);
            field.set(element,content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAttributeValue(Attribute attribute,String value){
        try {
            java.lang.reflect.Field field = attribute.getClass().getDeclaredField("value");
            field.setAccessible(true);
            field.set(attribute,value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
